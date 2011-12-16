import javax.management.remote.rmi._RMIConnection_Stub
import scala.io.Source
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import pclark.data._

class AggDataRecordSpec extends FlatSpec with ShouldMatchers {
  
  val targetData = "target/scala-2.9.1.final/classes/"
  
  // static data below comes from ICPSR 02896-002-data file. Some of the code below is cribbed from the ICPSR_02896 class.
  
  val windhamCty = 
"  1             150WINDHAM                     46861   18427       0   28434   16826   17340   6401   6294   10183   10548   6403   6482   6389   6292    252    312    238    306   13933    8513   329    104    36    2    0   1278    91    62   17  1264  1223   658  356    5600    5574  1340  1374     73    103    2    2    6093   3403     96    10    284   522   3601     84     80   46861    9037   10546   10431   45161                  35   17     3   322  7693  11    11          1   694        3    64    162               0   1983   415                    3              12     1    40       6    2    0    64   261           778    16  11    7    6      44  30   456  19665686    783903   4659264   7449413   6773106   460   269   338973   9831   3425055   5657   2261443   3571  1063450   603  100162    659977   62953   91582   466389    39053   6191195   5988886   202309   12166297   2553               106   36   136   172    503    632    629   272   138    31    4   2544   1999    57   27  141   279    41     9     7    0   0   0     2     0   2553   2510   270711   102739    3325700   3584720   457780   1019108   1532753   282640    515   10431   2464   1358    675    27    379   25    7967   1234    573   103   5826   231   23227   23634   22975   23322   4629711             90151"

  val connecticut =
"  1               0CONNECTICUT                908420  543755  330730  364665  330756  339454 123538 114672  184425  188358 139111 143134 122817 114579   7941   8055   7247   7979  280340  167020  1703   3874   577   66    8  50762  2004  4406  573 23005 11676 12223 2443  110317  109742 15640 17061   1958   2314   47   22  128190  75532   3447   527   1958  1720  37723   1572   1441  908420  159677  203424  200640  871648  286   87  94  5330  209   493  7871 19174 383   569   79  2249 21569  14  442  2427  31892 121   153  5692  70994 19105   25   1    22   709     11     2085   356  7739     518  568  247 11404  6175  46 104 16164  1499 226  650  267 121 859 345  9128 314696736  19208863  47663187  80177430 167647256  9381  9981 12286050 176694  82767725 130610  69227497  42605 12873190  3479  667038  23089806 1621573 1384047 16530497  3553689 185641219 180550473  5090746  352824106  26948 85.80000305175781  377  2050  2699   6218   6943   5494  1945  1009   187   26  26839  21148  1130  348  769  2745   699   109    76    2   1   7    18     5  26948  26507  2312083  1064525   52441508  44983560  4948300  10932212  22098948  4103420   4845  200640  26496  13250   9098   321   3656  171  174144  24680  28398  1108 115438  4520  454294  454126  446353  446071  89242411             90002"

  val manchester = 
" 406850.10009765625MANCHESTER CITY              9715    9715       0       0    4539    5038     71     67    2826    3062    172    180     69     67   1543   1796   1542   1796    2400    1474    96    465   303    0    0     28     3     2    0     8     2    17    2    1024    1142     9     5    593    708    0    0    1303     28    709     0    283    10     13    952    952    9715    1807    2047    2026    9609                   1          0     4               1               51              1     17               0     17     2                                                                       13    29                   0        0            2        36    763043     51100    185139    211667    315137    39    63    56089    923    372809    781    343643    114    24475    28    4691     56641    6374    3857    43410     3000   1178131   1153486    24645    1746192      6              17.5    1     1     2      2      0      0     0     0     0    0      6      2     0    0    0     4     0     0     0    0   0   0     0     0      6      6      105       76       7200      2750      260      1420      2234       10           2026      9      1      3     0      5    0    2017    341    127     7   1481    61    4610    5105    3067    3309    637653                 1"

val fourConnecticutHouseholdMicrodata =
"""H190003000614960019000109001011
P1900030006149600010020000101010651599059900001100009009000090090003903900100000000200010110199419991000000
P1900030006149600020020000505010852599129903031100009009000090090000900900100000000200010110199419991000000
P1900030006149600030020000303010381199021700001100009009000090090000900900100000000200010110199421893062318
P1900030006149600040020000404010392199111703031100009009000090090000900900100000000200010110199421893062318
P1900030006149600050020000909010152699079900001100009009000090090000900900100000000200010110209403041000000
P1900030006149600060020000909010131699079900001100009009000090090000900900100000000200010110209403041000000
H190003000614970020000109001011
P1900030006149700010020000101010401199052100001100009009000090090000900900100000000200010110199420013060906
P1900030006149700020020000202010482199102104041100009009000090090000900900100000000200010110199419991000000
P1900030006149700030020000303010192699109900001100009009000090090000900900100000000200010110199419991000000
P1900030006149700040020000303010171699079900001100009009000090090000900900100000000200010110199420013060906
P1900030006149700050020000303010111699029900001100009009000090090000900900100000000200010110209403041000000
H190003000614980020000109001011
P1900030006149800010020000101010361199051000001100009009000090090000900900100000000200010110199421891002318
P1900030006149800020020000202010292199041005051100009009000090090000900900100000000200010110199419991000000
P1900030006149800030020000303010092699119900001100009009000090090000900900100000000000010110208009991000000
P1900030006149800040020000303010081699079900001100009009000090090000900900100000000000010110208009991000000
P1900030006149800050020000303010051699099900001100009009000090090000900900100000000000010110208009991000000
P1900030006149800060020000303010042699019900001100009009000090090000900900100000000000010110199009991000000
H190003000669260020000109005021
P1900030006692600010020000101010222699069900001100009009000250250041441400200000000200010110199419991000000
P1900030006692600020020000707010211699089900001100009009000250250041441400200000000200010110199420651002447
P1900030006692600030020000707010182699069900001100009009000250250041441400200000000200010110199429991002318
P1900030006692600040020000707010142699029900001100009009000250250041441400200000000200010110210403041000000
P1900030006692600050020000707010111699109900001100009009000250250041441400200000000200010110210403041000000
P1900030006692600060020000707010091699029900001100009009000250250041441400200000000000010110199009991000000
"""

  val stateICP = new AggVariable("state", "ICPSR state code", 1, 3, "", "Nontabulated")
  val countyICP = new AggVariable("county", "ICPSR county code", 4, 16, "", "Nontabulated")
  val nameAggVar = new AggVariable("name", "Name of state/county", 20, 25, "", "Nontabulated")
  val floatCountyICP = new AggVariable("county", "ICPSR county code", 4, 16, "", "Nontabulated", (x:String) => x.toFloat.round)
  val nt5 = "Native-Born White Population by Nativity of Parents by Sex [NT5]"

  val numericAggVars = List(
    stateICP, floatCountyICP,
    new AggVariable("totpop", "Total population, 1900", 45, 8, "", "All"),
    new AggVariable("urb900", "Population in places 2,500+, 1900", 53, 8, "", "None"),
    new AggVariable("urb25", "Population in places 25,000+, 1900", 61, 8, "", "None"),
    new AggVariable("whtot", "Total white population, 1900", 1279, 8, "", "RACE = 1"),
    new AggVariable("nbwmfp", "Native white males/for parents", 123, 7, nt5, "NATIVITY in [2, 3] and RACE = 1 and SEX = 1"),
    new AggVariable("nbwffp", "Native white females/for parents", 130, 7, nt5, "NATIVITY in [2, 3] and RACE = 1 and SEX = 2")
  )

  
  val vars:Map[String, AggVariable] = Map(numericAggVars.map(_.defaultMapElement): _*)

  // given a single line, return the name of the location (a string) and Map of Variables to numeric values (which will be ints)
  def processDataline(data:String) : AggDataRecord = {
    val name = nameAggVar(data)
    val stateID = stateICP.asNumber(data)
    val countyID = floatCountyICP.asNumber(data)
    
    val valueTuples = numericAggVars.map((item:AggVariable) => (item -> item.asNumber(data)))
    
    // can only call toMap on lists that contain Tuple2 elements, which valueTuples conveniently does.
    val resultMap = valueTuples.toMap
    
    new AggDataRecord(name, stateID, countyID, resultMap)
  }
  
  val ctHouseholds = Household.readHouseholds(Source.fromString(fourConnecticutHouseholdMicrodata))
  
  "An AggDataRecord" should "be loadable from state data" in {
    val stateRecord = processDataline(connecticut)
    
    stateRecord.name should be === "CONNECTICUT"
    stateRecord.stateICP should be === 1
    stateRecord.countyICP should be === 0
    stateRecord.isCounty should be === false
    stateRecord(vars("whtot")) should be === 892424
  }

  it should "be loadable from county data" in {
    val ctyRecord = processDataline(windhamCty)
    
    ctyRecord.name should be === "WINDHAM"
    ctyRecord.stateICP should be === 1
    ctyRecord.countyICP should be === 150
    ctyRecord.isCounty should be === true
    ctyRecord(vars("whtot")) should be === 46297
  }

  it should "occasionally handle things that aren't ints" in {    
    val manchesterStateICP = stateICP.asNumber(manchester)
    val manchesterCountyICP = floatCountyICP.asNumber(manchester)
    
    manchesterStateICP should be === 40
    manchesterCountyICP should be === 6850
  }
  
  it should "handle counties that are subordinate to states" in {
    val stateRecord = processDataline(connecticut)
    val ctyRecord1 = processDataline(windhamCty)
    val ctyRecord2 = processDataline(manchester)
    
    val counties = Map(ctyRecord1.countyICP -> ctyRecord1, ctyRecord2.countyICP -> ctyRecord2)
    
    stateRecord.counties = counties
    
    stateRecord.counties.size should be === 2
  }

  it should "handle filtering by universe" in {
    val stateRecord = processDataline(connecticut)
    val ctyRecord1 = processDataline(windhamCty)
    val ctyRecord2 = processDataline(manchester)
    
    val counties = Map(ctyRecord1.countyICP -> ctyRecord1, ctyRecord2.countyICP -> ctyRecord2)
    val nonNoneCount = (numericAggVars.length-2) // there are two aggvars where the universe statement is None.
    
    stateRecord.counties = counties
    
    stateRecord.counties.size should be === 2
    
    stateRecord.data.keys.size should be === numericAggVars.length
    stateRecord.tab.keys.size should be === nonNoneCount
    
    val filteredStateRecord = stateRecord.filterUniverse(_ != "None")
    filteredStateRecord.data.keys.size should be === nonNoneCount    
  }

  it should "tabulate a household" in {
    val us1900Microdata = new MicrodataVars("1900", Source.fromFile(targetData + "1900/Microdata/usa_00054.cbk"))
    
    val stateRecord = processDataline(connecticut)
    
    stateRecord.proportion should be === 0.0
    
    stateRecord.tabulateHousehold(us1900Microdata.pVars)(ctHouseholds head)

    stateRecord.proportion should be > 0.0
    stateRecord.proportion should be < 0.01
    // we've added one household, not the whole state of connecticut, so the proportion should be larger than zero, but still pretty small.
  }

  it should "get the same results tabulating cached households as non-cached households" in {
    val us1900Microdata = new MicrodataVars("1900", Source.fromFile(targetData + "1900/Microdata/usa_00054.cbk"))

    val stateRecord = processDataline(connecticut)
    val cachedStateRecord = processDataline(connecticut)

    ctHouseholds.foreach(_.computeDeltas(us1900Microdata.pVars, stateRecord.tabulationKeys))

    // load up a bunch of households to start with.
    for (i <- 1 to 1000) {
      ctHouseholds.foreach(stateRecord.tabulateHousehold(us1900Microdata.pVars)(_))
      ctHouseholds.foreach(cachedStateRecord.tabulateCachedHousehold(_))
    }

    stateRecord.delta should be === cachedStateRecord.delta
  }
  
  it should "support substitution for tabulation" in {
    val us1900Microdata = new MicrodataVars("1900", Source.fromFile(targetData + "1900/Microdata/usa_00054.cbk"))

    val stateRecord = processDataline(connecticut)

    stateRecord.proportion should be === 0.0

    ctHouseholds.foreach(_.computeDeltas(us1900Microdata.pVars, stateRecord.tabulationKeys))

    val firstHH = ctHouseholds head
    val lastHH = ctHouseholds.last

    // load up a bunch of households to start with.
    for (i <- 1 to 38800) {
      ctHouseholds.foreach(stateRecord.tabulateHousehold(us1900Microdata.pVars)(_))
    }

    println("Pre-substution state record:")
    println(stateRecord)
    val populatedDelta = stateRecord.delta
    populatedDelta should be < 45000.0

    val predictedDeltaOfDelta = stateRecord.improvementOfDeltaWithSubstitution(lastHH, firstHH)
    val predictedSubstitutionDelta = stateRecord.deltaWithSubstitution(lastHH, firstHH)
    stateRecord.detabulateCachedHousehold(lastHH)
    stateRecord.tabulateCachedHousehold(firstHH)
    val actualSubstitutionDelta = stateRecord.delta

    val actualDeltaOfDelta = actualSubstitutionDelta - populatedDelta

    println("Post-substution state record:")
    println(stateRecord)

    println("Original delta:" + populatedDelta)
    println("Predicted delta delta:" + predictedDeltaOfDelta)
    println("   Actual delta delta:" + actualDeltaOfDelta)
    println("Predicted substitution delta:" + predictedSubstitutionDelta)
    println("   Actual substitution delta:" + actualSubstitutionDelta)

    scala.math.abs(predictedSubstitutionDelta) - scala.math.abs(actualSubstitutionDelta) should be < 2.0
  }
}
