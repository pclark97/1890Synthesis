package pclark.data

/* Represents a collection of agg variables and their associated values for a geographic instance (i.e. a state),
   along with nested geoinstances (i.e. counties).
   */
   
class AggDataRecord(val name:String, val stateICP:Long, val countyICP:Long, val data:Map[AggVariable, Long]) {
  
  var counties: Map[Long, AggDataRecord] = Map()
  
  // memoize the value of _tabulationKeys
  lazy val tabulationKeys = _tabulationKeys
  
  def isCounty = (countyICP != 0)
  def isState = (countyICP == 0)
  
  // answer the value for the given AggVariable.
	def apply(item: AggVariable) : Long = {
		data.getOrElse(item, 0)
	}
	
	/* Make a subset of the 'data' map, keeping only the AggVariables where the universe statement satisifes the filterFunc function.
	   Apply the same transformation to all the counties, if there are any.
	   */
	def filterUniverse(filterFunc:(String)=>Boolean) : AggDataRecord = {
	  val filteredData = data.filterKeys((x:AggVariable) => filterFunc(x.universeStatement))
	  val filteredCounties = counties.mapValues(_.filterUniverse(filterFunc))
	  val result = new AggDataRecord(name, stateICP, countyICP, filteredData)
	  result.counties = filteredCounties
	  result
	}
	
	/* ---------------------------------- */
	/* methods and instance variables dealing with tabulation */
	
	// make a new map of aggvars to longs, containing only the aggvars that have non-None universe statements, and set the inital values to be zero.
	val tab:scala.collection.mutable.Map[AggVariable, Long] = scala.collection.mutable.Map.empty ++ data.filterKeys(_.universeStatement != "None").mapValues((x:Long)=>0)
	
	// increment the value for the given key
	def increment(key:AggVariable) {
		val oldVal = tab(key)
		tab(key) = (oldVal + 1)
  }	 

	// decrement the value for the given key
	def decrement(key:AggVariable) {
		val oldVal = tab(key)
		tab(key) = (oldVal - 1)
  }
	
	// return the difference between the aggregate value and the tabulated value so far
	def deltaForKey(key:AggVariable) = {
    data.getOrElse(key, 0L) - tab.getOrElse(key, 0L)
	}
	
	// return the proportion between the aggregate value and the tabulated value so far
	// 1.0 means perfect match, 0.0 means no match.
	def proportionForKey(key:AggVariable) = {
	  val aggVal = data.getOrElse(key, 0L)
	  
	  if (aggVal == 0) {
	    0.0
	  } else {
      1.0-(((1.0*aggVal) - tab.getOrElse(key, 0L))/aggVal)
    }
	}
	
	def countFor(key: AggVariable) : Long = {
	  tab.getOrElse(key, 0)
	}
	
	// get the keys from the tabulation map that are worth comparing.
	def _tabulationKeys = {
	  tab.keys.filter((a:AggVariable) => (a.universeStatement != "None" && a.universeStatement != "Nontabulated"))
	}
	
	// compare 'tab' record with 'data' record. Returns a sum-of-squares delta between the two maps.
	def delta : Double = {
	  // for each key, fold along the list doing the following:
	  // get the value for the key from 'tab' and from 'data'.
	  // Subtract and square, and add that to the sumSoFar.
	  // the result of the foldLeft operation is the value of the function.
	  val deltaVal = tabulationKeys.foldLeft(0.0)((sumSoFar: Double, thisKey:AggVariable) => {
			val delta = deltaForKey(thisKey)
			sumSoFar + (delta * delta)
		})

    scala.math.sqrt(deltaVal)
	}
	
	/* what would happen to our delta if we swapped household 'old' for household 'new' ? 
	   Same process as the delta method above. */
	def deltaWithSubstitution(existing:Household, candidate:Household) = {
	  val deltaVal = tabulationKeys.foldLeft(0.0)((sumSoFar: Double, thisKey:AggVariable) => {
      /* Get the current tab value, back out the existing hh, add in the candidate hh, and then compute the delta */
			val currentTab = tab.getOrElse(thisKey, 0L)
			val cachedOld = existing.deltas.getOrElse(thisKey, 0L)
			val cachedNew = candidate.deltas.getOrElse(thisKey, 0L)
			val newTab = (currentTab - cachedOld) + cachedNew
      val newDelta = data.getOrElse(thisKey, 0L) - newTab
			sumSoFar + (newDelta * newDelta)
		})

    scala.math.sqrt(deltaVal)
	}

  /* Compute the change in delta if candidate is substituted for existing in aggRec.
  *  Perhaps counterintuitively, negative numerical values represent improvements */
  def improvementOfDeltaWithSubstitution(existing:Household, candidate:Household) = {
    val initialDelta = delta
    val substitutedDelta = deltaWithSubstitution(existing, candidate)
    substitutedDelta - initialDelta
  }

	// compare 'tab' record with 'data' record. Returns single number between 0.0 and 1.0 representing how close we are. 1.0 represents a perfect match.
	def proportion : Double = {
	  // there are likely to be keys in 'data' that aren't tabulated and so don't show up, and they should be ignored.
	  val keysToUse = tabulationKeys
	  
	  // for each key, fold along the list doing the following:
	  // get the value for the key from 'tab' and from 'data'.
	  // add the proportionForKey to the input from the fold.
	  val sumOfProportions = keysToUse.foldLeft(0.0)((sumSoFar: Double, thisKey:AggVariable) => {
			sumSoFar + proportionForKey(thisKey)
		})
		
		sumOfProportions / keysToUse.size
	}
	
  /* here's where part of the magic happens. Assumes that deltas are precomputed and cached in the household */
	def tabulateCachedHousehold(h:Household) {
    // for each person in the household, increment each tabulable AggVariable if the person is in universe for the aggvar.
    for (aggVar <- tabulationKeys) {
  		val oldVal = tab(aggVar)
      val hhIncrementForVariable = h.deltas.getOrElse(aggVar, 0L)
      tab(aggVar) = (oldVal + hhIncrementForVariable)
	  }
	}
	
	def detabulateCachedHousehold(h:Household) {
	  // undo tabulateHousehold.
    // for each person in the household, increment each tabulable AggVariable if the person is in universe for the aggvar.
    for (aggVar <- tabulationKeys) {
  		val oldVal = tab(aggVar)
      val hhIncrementForVariable = h.deltas.getOrElse(aggVar, 0L)
      tab(aggVar) = (oldVal - hhIncrementForVariable)
    }
	}
	
	/* here's where part of the magic happens. Curryable. */
	def tabulateHousehold(vars:Map[String, Variable])(h:Household) {
	  // for each person in the household, increment each tabulable AggVariable if the person is in universe for the aggvar.
	  for (p <- h.allPersons) {
	    for (aggVar <- tabulationKeys) {
	      if (aggVar.inUniverse(vars)(p)) {
	        increment(aggVar)
	      }
	    }
	  }
	}
	
	def detabulateHousehold(vars:Map[String, Variable])(h:Household) {
	  // undo tabulateHousehold.
	  // for each person in the household, increment each tabulable AggVariable if the person is in universe for the aggvar.
	  for (p <- h.allPersons) {
	    for (aggVar <- tabulationKeys) {
	      if (aggVar.inUniverse(vars)(p)) {
	        decrement(aggVar)
	      }
	    }
	  }
	}
	
	
	/* -------------------- end of tabulation code --------------- */
	
	override def toString(): String = {
		val result = new StringBuilder("------------\n" + name + "\n------------\n")
		val keys = data.keys.toList.sortWith(_.mnemonic < _.mnemonic)
		
		result.append("delta between reported and tabulated:" + delta + "\n")
		
		for (thisKey <- keys if thisKey.isTabulated) {
			result.append("%1$10s: actual: %2$9d constructed: %3$9d delta: %4$6d (%5$s)\n".format(thisKey.mnemonic, data(thisKey),
			  countFor(thisKey), data(thisKey) - countFor(thisKey), thisKey.desc))
		}
		result.append("------------\n")
		result.toString
	}
}
