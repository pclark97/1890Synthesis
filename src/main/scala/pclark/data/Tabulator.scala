package pclark.data
import scala.io.Source
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.mutable.ParArray
import pclark.data.table._
import pclark.data.cph1890._
import java.io._

// import scala.tools.nsc.interpreter.ILoop._

class Tabulator(val aggRec:AggDataRecord, val aggDataFile: AggDataFile, val microdataVars:Map[String, Variable], val hhs:Array[Household]) {
  // groupBy is awesome. This returns a map of [Int, Array[Household]] with the input array binned by the number of persons in the household.
  // In addition, the output arrays are then converted to parallel arrays in order to have findBestReplacementsUsing run faster.
  val householdsBySize = hhs.groupBy(_.numPersons).mapValues(_.par)
  	
  /*
  TODO - evaluate substitution.
  For a given candidate household, try substituting it for each household already present, and see if the proportion gets better or worse.
  Keep track of the household for which making the substitution offers the best improvement so far
  At the end, make the substitution.
  
  TODO - change AggDataRecord's comparison function to penalize going over target for an aggvar more than being under.
  TODO - consider changing the comparison function to consider % from target rather than strict arithmetic delta from target.
  TODO - consider weighting the comparison function to weigh some stats more heavily than others.
  */
  
  // return an array of the input households of the same household size as candidate,
  // sorted by how much things improve if an instance of that household in the output were replaced by candidate.
  // Each item in the result array is a tuple of (improvement:Double, householdId:Int)
  // where householdId is the index in the original input file for the household.
  // The household in question can be retrieved by getting hhs(householdId).
  // If improvement is less than zero, then the householdId should be removed from the output and replaced by candidate.
  def findBestReplacementsUsing(candidate:Household) = {
    val initialDelta = aggRec.delta
		// get a list of candidates that match the same household size.
    val householdsToCompare = householdsBySize(candidate.numPersons)
    
    /* get the improvement for each candidate household swap, and connect with the id of the household, returning a tuple of (improvement, id)
       Note that householdsToCompare is a ParArray, so the map operation will be parallelized.
       However, the output needs to be converted to a sequential array, so that it can be sorted.
    */
    val replacementCandidates = householdsToCompare.map(thisHH => (aggRec.improvementOfDeltaWithSubstitution(thisHH, candidate), thisHH.id)).seq
				
		// println("Digesting candidates to replace with " + candidate.numPersons + "-person household:" + candidate.hrec + ", got " + replacementCandidates.length + " possibilities.")
		
		// wish I could unpack the tuples in the function parameter list, like sortWith (((x1, y1), (x2, y2)) => ...
		// sort by increasing 'improvement', since we want the ones with the best improvement first.
		val result = replacementCandidates sortWith ((a, b) => a._1 < b._1)
		/*
		val top5Candidates = result take 5
		
		println("Improvements for household: " + candidate.hrec)
		top5Candidates.foreach(item => println("Improvement: " + item._1))
		println("====")
		*/
    result toArray
  }

  /* Add records to the end of the output list, checking whether each addition helps or hurts the delta from the target tabulation.
     In addition, once we've matched total pop, it's time to stop adding.
   */
  def addAtEndWithoutSubstitution(outputHouseholds:ArrayBuffer[Int]) {
    // now that the bulk-loading is finished, start actually checking whether each hh has improved things.
    val end = hhs.size
    var index = 0
    var hhsBackedOut = 0
    
    do {
      // Get and add the next household.
      val h = hhs(index)
      val base = aggRec.delta
      aggRec.tabulateCachedHousehold(h)
    	val newDelta = aggRec.delta
    	
      // did this household help? If not, take it back out. If so, leave it there and move on.
      if (base < newDelta) {
        hhsBackedOut += 1
        aggRec.detabulateCachedHousehold(h)
      } else {
        outputHouseholds += index
      }
      
      index += 1
      
      if (0 == index % 1000 ) {
        // print some status
        println("[%1$8d] proportion complete: %2$f, %3$d households tried and backed out since last message".format(index, aggRec.proportion, hhsBackedOut))
        hhsBackedOut = 0
      }
    } while ((index < end) && (aggRec.deltaForKey(aggDataFile.totalPopulation) > 0))
  }

  /*
    Replace the household at ixToRemove in outputHouseholds with the household id of newHH
   */
  def substitute(ixToRemove:Int, newHH:Household, outputHouseholds:ArrayBuffer[Int]) {
    // get the household structure that's being removed, so we can detabulate it
    val removedHHId = outputHouseholds(ixToRemove)
    val removedHH = hhs(removedHHId)
    
    // do the substitution in the output array - replace the old household ID with the new one.
    outputHouseholds(ixToRemove) = newHH.id
		
    // and update the aggregate statistics
    aggRec.detabulateCachedHousehold(removedHH)
    aggRec.tabulateCachedHousehold(newHH)
  }
  
  
  // find the first occurrance of an element in candidateRemovals that occurs in the outputHouseholds.
  // it's possible that the best substitution isn't actually there anymore, if it's been removed previously.
  // TODO - consider returning a list of candidates that are both present in outputHouseholds and where
  // the improvement is significant enough to care - we could substitute this household in for more than one candidate.
  def getHouseholdForSubstitution(candidateRemovals:Array[(Double, Int)], outputHouseholds:ArrayBuffer[Int]) = {
    val parallelizedOutputHouseholds = outputHouseholds.par
    val firstValidRemoval = candidateRemovals find (ix => parallelizedOutputHouseholds.indexOf(ix._2) != -1)
    
		firstValidRemoval
  }
  
  
  // evaluate each of the households in outputHouseholds to see whether replacing it with hhToSubstitute results in a better fit.
  // If so, make the replacement. Otherwise, skip.
  def substituteIfImproved(hhToSubstitute: Household, outputHouseholds:ArrayBuffer[Int]) = {
    val candidateRemovals = findBestReplacementsUsing(hhToSubstitute)
    // candidateRemovals is an Array[(Double, Int)] of tuples (delta, household id)
    
    var result = false

    if (candidateRemovals.isEmpty) {
			println("No removals possible for household " + hhToSubstitute.id)
		} else {
			val firstValidRemoval = getHouseholdForSubstitution(candidateRemovals, outputHouseholds)
			
			firstValidRemoval match {
				case None => println("None of the households that could be removed out in favor of " + hhToSubstitute.id + " are present in the output anymore.") // no removals for this one, so keep going
				case Some(substitution) =>
  				val (subDelta, subIndex) = substitution 
				  if (subDelta < 0) {
				    // find where household id subIndex appears in the output, and replace it with hhToSubstitute.
					  substitute(outputHouseholds.indexOf(subIndex), hhToSubstitute, outputHouseholds)
					  print("+")
					  result = true
				  } else {
				    // replacing any of the existing households with this hh makes things worse, so move along.
				    hhToSubstitute.markInactive
					  print("-")
          }
		  }
    }
    
    result
  }
  
	// looks at a list of all the possible substitutions and picks the best one that's still there.
  def addWithSubstitutions(numIterations:Int, outputHouseholdsInput:ArrayBuffer[Int]) = {
    val end = hhs.size
    
    // We only replace households with other households of the same size. Given this, we can reduce the search space
    // for the replacement household in the output array by grouping the output array by household size into subarrays.
    val outputHouseholdsBySize = outputHouseholdsInput.groupBy(hhs(_).numPersons)
    var pass = 1
    var foundSubstitution = false
		
    do {
  		val now = new java.util.Date
		  foundSubstitution = false
      
  		println("\nBeginning iteration: " + pass + ", time:" + now)
  		var index = 0
  		do {
  		  // for each household in the original input, see if we should use it to replace an existing household in the output.
  			val hhToSubstitute = hhs(index)
				if (hhToSubstitute isActive) {
  			  // println("considering household " + index + " for substitutions")
  			  val outputHouseholds = outputHouseholdsBySize(hhToSubstitute.numPersons)
          val didSubstitute = substituteIfImproved(hhToSubstitute, outputHouseholds)
          if (didSubstitute) foundSubstitution = true
        } else {
          // this household failed to be useful on a previous pass, so skip it.
          print("s")
        }
        
   	    index += 1
  	    if (0 == index % 80 ) {
  	      // print a newline so that the plusses and minuses don't wrap
  	      println("")
  	    }
  	    if (0 == index % 3200 ) {
  	      // print some status about the progress towards fitting the aggregate stats
  	      println("[%1$2d, %2$8d] proportion complete: %3$f".format(pass, index, aggRec.proportion))
  	      println("Agg Data Status:\n")
  	      println(aggRec)
  	    }
  	  } while (index < end)
  	  pass += 1
  	  // keep going until we either do a full pass with no substitutions, or we've hit our iteration threshold.
  	} while (foundSubstitution && pass < numIterations)
  	
  	println("\nDone fitting! Completed " + pass + " iterations")
  	// when done, put the households all back together into one array and return that.
  	outputHouseholdsBySize.values reduceLeft (_ ++ _)
	}

	/* Precompute the deltas for each household in our dataset */
	def calcDeltasForHouseholds {
		hhs.foreach(_.computeDeltas(microdataVars, aggRec.tabulationKeys))
	}
	
	// compute the number of iterations to do before we need to start thinking about whether adding the household is a good idea
  def baseIterations(totalPop:Long, households:Array[Household]) = {
    (totalPop / Household.totalPersons(households)).toInt
  }
  
	/* here is where all the work happens:
	   Iterate over the input households and add them to the outputHouseholds buffer until we've done the best
	   we can in matching the aggregate values.
	*/
	def fitToAggregates = {
	  // outputHouseholds contains the ids of the households in hhs that represent the fitted output.
		val outputHouseholds = new ArrayBuffer[Int]
    
    val numIterations = baseIterations(aggRec(aggDataFile.totalPopulation), hhs)
    
    // start off by bulk-loading the first few iterations
    // No point in checking how well they improve things yet, as every single hh will improve it at first.
    println("Bulk-load stage - making " + numIterations + " passes.")
    for (i <- 1 to numIterations) {
      for (index <- 0 to (hhs.size-1)) {
        aggRec.tabulateCachedHousehold(hhs(index))
        outputHouseholds += index
      }
    }
  
    println("Bulk-load completed. Total delta from target stats is:" + aggRec.delta)
    println("Tabulated total pop:" + aggRec(aggDataFile.totalPopulation) + ", delta is:"+aggRec.deltaForKey(aggDataFile.totalPopulation))
  
		// now that we've got the output mostly filled out, make one more pass but only adding where it helps.
    addAtEndWithoutSubstitution(outputHouseholds)

    println("Non-substitution load completed. Total delta from target stats is:" + aggRec.delta)
    println("Tabulated total pop:" + aggRec(aggDataFile.totalPopulation) + ", delta is:"+aggRec.deltaForKey(aggDataFile.totalPopulation))
    println("Agg Data Status:\n")
    println(aggRec)

		// now that the file is as close of a match as our metric will offer just based on adding households, do a bunch of passes of substitution.
    addWithSubstitutions(60, outputHouseholds)
  }
  
  /* Write out the household records pointed to by outputIndices */
  def writeOutput(outputIndices:Array[Int], outputFile:String) {
    val out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)))
    outputIndices.foreach { index =>
      out.print(hhs(index))
    }
    out.close()
  }
}

object Tabulator extends GZipSource {
  val aggDataFile = pclark.data.cph1890.ICPSR_02896
//  val us1900Microdata = new MicrodataVars("1900", Source.fromFile("target/scala-2.9.0.final/classes/1900/Microdata/usa_00054.cbk"))
//  val pathToCtMicrodata = "target/scala-2.9.0.final/classes/1900/Microdata/Connecticut.dat.gz"

  val us1900Microdata = new MicrodataVars("1880-1900", Source.fromFile(aggDataFile.targetData + "1880-1900/Microdata/usa_00057.cbk"))
  val pathToCtMicrodata = aggDataFile.targetData + "1880-1900/Microdata/Connecticut.dat.gz"
  
  val ctAggDataRec = aggDataFile.stateData(aggDataFile.connecticutICP)
  val ctHouseholds = Household.readHouseholds(getSourceForGzipFile(pathToCtMicrodata))
  val pathToOutput = "./outputMicrodata.dat"
  
  def main(args: Array[String]) {
    println("Found " + ctHouseholds.length + " households for Connecticut, and " + ctAggDataRec.data.size + " aggregate vars to fit.")
    val t = new Tabulator(ctAggDataRec, aggDataFile, us1900Microdata.pVars, ctHouseholds)
    
    t.calcDeltasForHouseholds
    val output = t.fitToAggregates
    
    t.writeOutput(output.toArray, pathToOutput)
    println("Final status:")
    println(ctAggDataRec)
    
    
	}
}
