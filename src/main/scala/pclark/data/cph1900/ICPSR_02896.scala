package pclark.data.cph1900

import scala.io.Source
import scala.collection.mutable

import pclark._
import pclark.data._
import pclark.data.table._

/* This singleton contains variable definitions for the ICPSR 02896 file for the US 1900 census (county and state) data file.
   The main variable list is generated from the stata dictionary for the dataset.
   Unfortunately, stata numbers columns from one, wheras scala numbers them from zero. */
   
/* Citation info for the data file:
Haines, Michael R., and Inter-university Consortium for Political and Social Research.
Historical, Demographic, Economic, and Social Data: The United States, 1790-2002 [Computer file]. ICPSR02896-v3.
Ann Arbor, MI: Inter-university Consortium for Political and Social Research [distributor], 2010-05-21. doi:10.3886/ICPSR02896
*/


object ICPSR_02896 extends AggDataFile {
  val path_to_datafile = targetData + "1900/ICPSR/02896-0020-Data.dat"
	
  val nt1 = "Total Population [NT1]"
  val nt4 = "Nativity by sex [NT4]"
  val nt5 = "Native-Born White Population by Nativity of Parents by Sex [NT5]"
  val nt6 = "Foreign-Born White Population by Sex [NT6]"
  val nt7 = "Non-White Population by Race by Sex [NT7]"
  val nt9 = "Males 21 Years of Age and Over [NT9]"
  val nt10 = "Native-Born Males 21 Years of Age and Over by Race by Literacy [NT10]"
  val nt11 = "Foreign-Born Males 21 Years of Age and Over by Citizenship Status by Literacy [NT11]"
  val nt12 = "White Population 5 to 20 Years of Age by Nativity by Sex [NT12]"
  val nt13 = "Non-White Population 5 to 20 Years of Age by Race by Sex [NT13]"
  val nt14 = "Males 18 to 44 Years of Age by Race/Nativity [NT14]"
  val nt16 = "Illiterate Population 10 Years of Age and Over by Race/Nativity [NT16]"
  val nt20 = "Total Population [NT20]"
  val nt25 = "Foreign-Born Population by Country of Birth [NT25]"
  val nt62 = "Sex [NT62]"
  val nt63 = "Total White Population by Sex [NT63]"
  val nt64 = "Total White Population [NT64]"

  val stateICP = new AggVariable("state", "ICPSR state code", 1, 3, "", "Nontabulated")
  val countyICP = new AggVariable("county", "ICPSR county code", 4, 16, "Geographic", "Nontabulated", (x:String) => x.toDouble.round)
  val nameAggVar = new AggVariable("name", "Name of state/county", 20, 25, "", "Nontabulated")
  val geolevel = new AggVariable("level", "County=1 state=2 USA=3", 1306, 1, "", "Nontabulated")
  val stateFIP =  new AggVariable("statefips", "State FIPS code", 1289, 14, "", "Nontabulated")
  val totalPopulation = new AggVariable("totpop", "Total population, 1900", 45, 8, nt1, "All")
  
  val numericAggVars = List(
    stateICP, countyICP, totalPopulation,
    new AggVariable("urb900", "Population in places 2,500+, 1900", 53, 8),
    new AggVariable("urb25", "Population in places 25,000+, 1900", 61, 8),
    new AggVariable("rur900", "Rural population, 1900", 69, 8),
    new AggVariable("nbmtot", "Native-born males", 77, 8, nt4, "SEX = 1 and NATIVITY in [1,2,3,4]"),
    new AggVariable("nbftot", "Native-born females", 85, 8, nt4, "SEX = 2 and NATIVITY in [1,2,3,4]"),
    new AggVariable("fbmtot", "Foreign-born males", 93, 7, nt4, "SEX = 1 AND NATIVITY = 5"),
    new AggVariable("fbftot", "Foreign-born females", 100, 7, nt4, "SEX = 2 AND NATIVITY = 5"),
    new AggVariable("nbwmnp", "Native white males/native parents", 107, 8, nt5, "NATIVITY = 1 and RACE = 1 and SEX = 1"),
    new AggVariable("nbwfnp", "Native white females/native parents", 115, 8, nt5, "NATIVITY = 1 and RACE = 1 and SEX = 2"),
    new AggVariable("nbwmfp", "Native white males/for parents", 123, 7, nt5, "NATIVITY in [2, 3, 4] and RACE = 1 and SEX = 1"),
    new AggVariable("nbwffp", "Native white females/for parents", 130, 7, nt5, "NATIVITY in [2, 3, 4] and RACE = 1 and SEX = 2"),
    new AggVariable("fbwmtot", "Foreign-born white males", 137, 7, nt6, "NATIVITY = 5 and RACE = 1 and SEX = 1"),
    new AggVariable("fbwftot", "Foreign-born white females", 144, 7, nt6, "NATIVITY = 5 and RACE = 1 and SEX = 1"),
    new AggVariable("colmtot", "Colored males", 151, 7, nt7, "RACE > 1 and SEX = 1"),
    new AggVariable("colftot", "Colored females", 158, 7, nt7, "RACE > 1 and SEX = 2"),
    new AggVariable("negmtot", "Negro males", 165, 7, nt7, "RACE = 2 and SEX = 1"),
    new AggVariable("negftot", "Negro females", 172, 7, nt7, "RACE = 2 and SEX = 1"),
    new AggVariable("m21", "Male population 21+", 179, 8, nt9, "SEX = 1 and AGE >= 21"),
    new AggVariable("nwmlit", "Literate native white males 21+", 187, 8, nt10, "LIT = 4 and NATIVITY in [1,2,3,4] and RACE = 1 and SEX = 1 and AGE >= 21"),
    new AggVariable("nwmillit", "Illiterate native white males 21+", 195, 6, nt10, "LIT in [1,2,3] and NATIVITY in [1,2,3,4] and RACE = 1 and SEX = 1 and AGE >= 21"),
    new AggVariable("nnegmlit", "Literate native Negro males 21+", 201, 7, nt10, "LIT = 4 and NATIVITY in [1,2,3,4] and RACE = 2 and SEX = 1 and AGE >= 21"),
    new AggVariable("nnegmill", "Illiterate native Negro males 21+", 208, 6, nt10, "LIT in [1,2,3] and NATIVITY in [1,2,3,4] and RACE = 2 and SEX = 1 and AGE >= 21"),
    new AggVariable("ncolmlit", "Literate other native colored males 21+", 214, 5, nt10, "LIT = 4 and NATIVITY in [1,2,3,4] and RACE > 2 and SEX = 1 and AGE >= 21"),
    new AggVariable("ncolmill", "Illiterate other native colored males 21+", 219, 5, nt10, "LIT in [1,2,3] and NATIVITY in [1,2,3,4] and RACE > 2 and SEX = 1 and AGE >= 21"),
    new AggVariable("fbmnalit", "Literate naturalized foreign males 21+", 224, 7, nt11, "LIT = 4 and CITIZEN = 2 and SEX = 1 and AGE >= 21"),
    new AggVariable("fbmnaill", "Illiterate naturalized foreign males 21+", 231, 6, nt11, "LIT in [1,2,3] and CITIZEN = 2 and SEX = 1 and AGE >= 21"),
    new AggVariable("fbmfplit", "Literate foreign males 21+/first papers", 237, 6, nt11, "LIT = 4 and CITIZEN = 4 and SEX = 1 and AGE >= 21"),
    new AggVariable("fbmfpill", "Illiterate foreign males 21+/first papers", 243, 5, nt11, "LIT in [1,2,3] and CITIZEN = 4 and SEX = 1 and AGE >= 21"),
    new AggVariable("fbmallit", "Literate alien foreign males 21+", 248, 6, nt11, "LIT = 4 and CITIZEN = 3 and SEX = 1 and AGE >= 21"),
    new AggVariable("fbmalill", "Illiterate alien foreign males 21+", 254, 6, nt11, "LIT in [1,2,3] and CITIZEN = 3 and SEX = 1 and AGE >= 21"),
    new AggVariable("fbmunlit", "Literate foreign males 21+/unknown citizenship", 260, 6, nt11, "LIT = 4 and CITIZEN = 5 and SEX = 1 and AGE >= 21"),
    new AggVariable("fbmunill", "Illiterate foreign males 21+/unknown citizenship", 266, 5, nt11, "LIT in [1,2,3] and CITIZEN = 5 and SEX = 1 and AGE >= 21"),
    new AggVariable("nbwm520", "Native white males aged 5-20", 271, 8, nt12, "NATIVITY in [1,2,3,4] and RACE = 1 and SEX = 1 and AGE >= 5 and AGE <= 20"),
    new AggVariable("nbwf520", "Native white females aged 5-20", 279, 8, nt12, "NATIVITY in [1,2,3,4] and RACE = 1 and SEX = 2 and AGE >= 5 and AGE <= 20"),
    new AggVariable("fbwm520", "Foreign white males aged 5-20", 287, 6, nt12, "NATIVITY = 5 and RACE = 1 and SEX = 1 and AGE >= 5 and AGE <= 20"),
    new AggVariable("fbwf520", "Foreign white females aged 5-20", 293, 6, nt12, "NATIVITY = 5 and RACE = 1 and SEX = 2 and AGE >= 5 and AGE <= 20"),
    new AggVariable("negm520", "Negro males aged 5-20", 299, 7, nt13, "RACE = 2 and SEX = 1 and AGE >= 5 and AGE <= 20"),
    new AggVariable("negf520", "Negro females aged 5-20", 306, 7, nt13, "RACE = 2 and SEX = 2 and AGE >= 5 and AGE <= 20"),
    new AggVariable("ocom520", "Other colored males aged 5-20", 313, 5, nt13, "RACE > 2 and SEX = 1 and AGE >= 5 and AGE <= 20"),
    new AggVariable("ocof520", "Other colored females aged 5-20", 318, 5, nt13, "RACE > 2 and SEX = 2 and AGE >= 5 and AGE <= 20"),
    new AggVariable("nbwm1844", "Native white males aged 18-44", 323, 8, nt14, "NATIVITY in [1,2,3,4] and RACE = 1 and SEX = 1 and AGE >= 18 and AGE <= 44"),
    new AggVariable("fbwm1844", "Foreign white males aged 18-44", 331, 7, nt14, "NATIVITY = 5 and RACE = 1 and SEX = 1 and AGE >= 18 and AGE <= 44"),
    new AggVariable("negm1844", "Negro males aged 18-44", 338, 7, nt14, "RACE = 2 and SEX = 1 and AGE >= 18 and AGE <= 44"),
    new AggVariable("ocom1844", "Other colored males aged 18-44", 345, 6, nt14, "RACE > 2 and SEX = 1 and AGE >= 18 and AGE <= 44"),
    new AggVariable("nwnpil10", "Illiterate native white/native parents 10+", 351, 7, nt16, "LIT in [1,2,3] and NATIVITY = 1 and RACE = 1 and AGE >= 10"),
    new AggVariable("nwfpil10", "Illiterate native white/foreign parents 10+", 358, 6, nt16, "LIT in [1,2,3] and NATIVITY in [2,3,4] and RACE = 1 and AGE >= 10"),
    new AggVariable("fbwill10", "Illiterate foreign white 10+", 364, 7, nt16, "LIT in [1,2,3] and NATIVITY = 5 and RACE = 1 and AGE >= 10"),
    new AggVariable("colill10", "Illiterate total colored 10+", 371, 7, nt16, "LIT in [1,2,3] and RACE > 1 and AGE >= 10"),
    new AggVariable("negill10", "Illiterate Negroes 10+", 378, 7, nt16,  "LIT in [1,2,3] and RACE =2 and AGE >= 10"),
    new AggVariable("totpop1", "Total Population", 385, 8, nt20, "All"),
    new AggVariable("dwell", "# dwellings", 393, 8),
    new AggVariable("families", "# families", 401, 8),
    new AggVariable("privfam", "# private families", 409, 8),
    new AggVariable("pfampop", "Persons in private families", 417, 8),
    new AggVariable("pbasia", "Persons born in Asia n.s.", 425, 5, nt25, "BPL >= 502 and BPL <= 599 and BPL != 521"), // not china, japan, india
    new AggVariable("pbatlisl", "Persons born in Atlantic Islands", 430, 5, nt25, "BPL = 160"),
    new AggVariable("pbaustra", "Persons born in Australia", 435, 4, nt25, "BPL = 700"),
    new AggVariable("pbaustri", "Persons born in Austria", 439, 6, nt25, "BPL = 450"),
    new AggVariable("pbbelg", "Persons born in Belgium", 445, 5, nt25, "BPL = 425"),
    new AggVariable("pbbohem", "Persons born in Bohemia", 450, 6, nt25, "None"),  // NOTE - what to do about these? Kinda present in detailed BPL.
    new AggVariable("pbcaneng", "Persons born in English Canada", 456, 6, nt25),
    new AggVariable("pbcanfr", "Persons born in French Canada", 462, 6, nt25),
    new AggVariable("pbcentam", "Persons born in Central America", 468, 4, nt25, "BPL = 210"),
    new AggVariable("pbchina", "Persons born in China", 472, 6, nt25, "BPL = 500"),
    new AggVariable("pbcuba", "Persons born in Cuba", 478, 5, nt25, "BPL = 250"),
    new AggVariable("pbdenmar", "Persons born in Denmark", 483, 6, nt25, "BPL = 400"),
    new AggVariable("pbenglan", "Persons born in England", 489, 6, nt25, "BPL = 410"),
    new AggVariable("pbeurns", "Persons born in Europe n.s.", 495, 4, nt25, "BPL = 499"),
    new AggVariable("pbfinlan", "Persons born in Finland", 499, 5, nt25, "BPL = 401"),
    new AggVariable("pbfrance", "Persons born in France", 504, 6, nt25, "BPL = 421"),
    new AggVariable("pbgerman", "Persons born in Germany", 510, 7, nt25, "BPL = 453"),
    new AggVariable("pbgreece", "Persons born in Greece", 517, 4, nt25, "BPL = 433"),
    new AggVariable("pbhollan", "Persons born in Holland", 521, 6, nt25, "BPL = 425"),
    new AggVariable("pbhungar", "Persons born in Hungary", 527, 6, nt25, "BPL = 454"),
    new AggVariable("pbirelan", "Persons born in Ireland", 533, 7, nt25, "BPL = 414"),
    new AggVariable("pbitaly", "Persons born in Italy", 540, 6, nt25, "BPL = 434"),
    new AggVariable("pbjapan", "Persons born in Japan", 546, 5, nt25, "BPL = 501"),
    new AggVariable("pbluxemb", "Persons born in Luxemburg", 551, 4, nt25, "BPL = 423"),
    new AggVariable("pbmexico", "Persons born in Mexico", 555, 6, nt25, "BPL = 200"),
    new AggVariable("pbnorway", "Persons born in Norway", 561, 6, nt25, "BPL = 404"),
    new AggVariable("pbnorden", "Persons born in Norway/Denmark", 567, 3, nt25, "BPL in [404, 400]"),
    new AggVariable("pbpacifi", "Persons born in Pacific Islands", 570, 4, nt25, "BPL = 710"),
    new AggVariable("pbpoland", "Persons born in Poland", 574, 4, nt25, "BPL = 455"),
    new AggVariable("pbpolaus", "Persons born in Austrian Poland", 578, 5, nt25),
    new AggVariable("pbpolger", "Persons born in German Poland", 583, 6, nt25),
    new AggVariable("pbpolrus", "Persons born in Russian Poland", 589, 6, nt25),
    new AggVariable("pbpoloth", "Persons born in other Poland", 595, 3, nt25),
    new AggVariable("pbpolunk", "Persons born in Poland (unk)", 598, 5, nt25),
    new AggVariable("pbportug", "Persons born in Portugal", 603, 5, nt25, "BPL = 436"),
    new AggVariable("pbrumani", "Persons born in Rumania", 608, 5, nt25, "BPL = 456"),
    new AggVariable("pbrussia", "Persons born in Russia", 613, 6, nt25, "BPL = 465"),
    new AggVariable("pbscot", "Persons born in Scotland", 619, 6, nt25, "BPL = 411"),
    new AggVariable("pbsoamer", "Persons born in South America", 625, 4, nt25, "BPL = 300"),
    new AggVariable("pbspain", "Persons born in Spain", 629, 4, nt25, "BPL = 438"),
    new AggVariable("pbsweden", "Persons born in Sweden", 633, 6, nt25, "BPL = 405"),
    new AggVariable("pbswitz", "Persons born in Switzerland", 639, 6, nt25, "BPL = 426"),
    new AggVariable("pbturkey", "Persons born in Turkey", 645, 4, nt25, "BPL = 542"),
    new AggVariable("pbwales", "Persons born in Wales", 649, 5, nt25, "BPL = 412"),
    new AggVariable("pbwesind", "Persons born in West Indies", 654, 5, nt25, "BPL = 260"),
    new AggVariable("pbsea", "Persons born at sea", 659, 4, nt25, "BPL = 900"),
    new AggVariable("pbothfor", "Persons born in other countries", 663, 4, nt25),
    new AggVariable("pbasianc", "Persons born in Asia exc China", 667, 4, nt25, "BPL in [501, 502, 509, 510, 511, 512, 513, 514, 515, 516, 516, 518, 519, 548, 549, 550, 599]"),
    new AggVariable("mfgestab", "# manufacturing establishments", 671, 6),
    new AggVariable("mfgcap", "Manufacturing capital", 677, 10),
    new AggVariable("mfgcapla", "Manufacturing capital in land", 687, 10),
    new AggVariable("mfgcapbu", "Manufacturing capital/buildings", 697, 10),
    new AggVariable("mfgcapeq", "Manufacturing capital/equipment", 707, 10),
    new AggVariable("mfgcapca", "Manufacturing capital/cash", 717, 10),
    new AggVariable("mfgprops", "# Manufacturing proprietors", 727, 6),
    new AggVariable("mfgoffic", "# manufacturing salaried officials, clerks", 733, 6),
    new AggVariable("mfgofsal", "Salaries of manufacturing officials, clerks", 739, 9),
    new AggVariable("mfgavear", "Av # manufacturing wage earners", 748, 7),
    new AggVariable("mfgwages", "Total manufacturing wages (workers)", 755, 10),
    new AggVariable("mfglbm16", "Av # males 16+ employed manufacturing", 765, 7),
    new AggVariable("mfgwgm16", "Total wages males 16+ manufacturing", 772, 10),
    new AggVariable("mfglbf16", "Av # females 16+ employed manufacturing", 782, 7),
    new AggVariable("mfgwgf16", "Total wages females 16+ manufacturing", 789, 9),
    new AggVariable("mfglbch", "Av # children <16 employed manufacturing", 798, 6),
    new AggVariable("mfgwgch", "Total wages children <16 manufacturing", 804, 8),
    new AggVariable("mfgmisco", "Total misc expenses manufacturing", 812, 10),
    new AggVariable("mfgrentw", "Total works rental manufacturing", 822, 8),
    new AggVariable("mfgtaxes", "Total taxes (excluding internal revenue) manufacturing", 830, 8),
    new AggVariable("mfgrento", "Total other rental/interest manufacturing", 838, 9),
    new AggVariable("mfgcontr", "Cost of contract work manufacturing", 847, 9),
    new AggVariable("mfgrms", "Cost of materials manufacturing", 856, 10),
    new AggVariable("mfgprms", "Cost of principal materials manufacturing", 866, 10),
    new AggVariable("mfgfuel", "Cost of fuel/power/heat manufacturing", 876, 9),
    new AggVariable("mfgout", "Value manufacturing output", 885, 11),   // blows up for new york when treated as an int. This drove refactoring variable values to be Longs.
    new AggVariable("farms", "Total # of farms", 896, 7),
    new AggVariable("farmsize", "Av farm size (acres)", 903, 18, "",  (x:String) => x.toDouble.round),
    new AggVariable("farm12", "# farms 1-2 acres", 921, 5),
    new AggVariable("farm39", "# farms 3-9 acres", 926, 6),
    new AggVariable("farm1019", "# farms 10-19 acres", 932, 6),
    new AggVariable("farm2049", "# farms 20-49 acres", 938, 7),
    new AggVariable("farm5099", "# farms 50-99 acres", 945, 7),
    new AggVariable("farm100", "# farms 100-174 acres", 952, 7),
    new AggVariable("farm175", "# farms 175-259 acres", 959, 6),
    new AggVariable("farm260", "# farms 260-499 acres", 965, 6),
    new AggVariable("farm500", "# farms 500-999 acres", 971, 6),
    new AggVariable("farm1000", "# farms 1000+ acres", 977, 5),
    new AggVariable("farmwh", "# farms white farmers", 982, 7),
    new AggVariable("farmwhow", "# farms white farm owners", 989, 7),
    new AggVariable("farmwhpo", "# farms white part owners", 996, 6),
    new AggVariable("farmwhte", "# farms white owners/tenants", 1002, 5),
    new AggVariable("farmwhma", "# farms white farm managers", 1007, 5),
    new AggVariable("farmwhct", "# farms white cash tenants", 1012, 6),
    new AggVariable("farmwhst", "# farms white share tenants", 1018, 6),
    new AggVariable("farmcol", "# farms colored farmers", 1024, 6),
    new AggVariable("farmcoow", "# farms colored farm owners", 1030, 6),
    new AggVariable("farmcopo", "# farms colored part owners", 1036, 5),
    new AggVariable("farmcote", "# farms colored owners/tenants", 1041, 4),
    new AggVariable("farmcoma", "# farms colored farm managers", 1045, 4),
    new AggVariable("farmcoct", "# farms colored cash tenants", 1049, 6),
    new AggVariable("farmcost", "# farms colored share tenants", 1055, 6),
    new AggVariable("farms2", "Total # of farms", 1061, 7),
    new AggVariable("farmsbui", "Total # of farms w/ buildings", 1068, 7),
    new AggVariable("acfarm", "Total acres farmland", 1075, 9),
    new AggVariable("acimp", "# improved acres farmland", 1084, 9),
    new AggVariable("farmval", "Value of farmland/improvements (excluding buildings)", 1093, 11),
    new AggVariable("farmbui", "Value of farm buildings", 1104, 10),
    new AggVariable("farmequi", "Value of farm implements/machinery", 1114, 9),
    new AggVariable("livstock", "Value of farm livestock", 1123, 10),
    new AggVariable("farmout", "Value of farm output not fed to livestock", 1133, 10),
    new AggVariable("farmlab", "Expenditure for farm labor", 1143, 9),
    new AggVariable("area", "Area (sq mi)", 1152, 7),
    new AggVariable("privfam2", "# private families", 1159, 8),
    new AggVariable("tfarmhom", "Total farm homes", 1167, 7),
    new AggVariable("fhowfree", "Farm homes owned free", 1174, 7),
    new AggVariable("fhencumb", "Farm homes owned mortgaged", 1181, 7),
    new AggVariable("fhmtgunk", "Farm homes encumbrance unknown", 1188, 6),
    new AggVariable("fhrent", "Farm homes rented", 1194, 7),
    new AggVariable("fhunk", "Farm homes tenure unknown", 1201, 5),
    new AggVariable("tothhome", "Total nonfarm homes", 1206, 8),
    new AggVariable("ohowfree", "Nonfarm homes owned free", 1214, 7),
    new AggVariable("ohencumb", "Nonfarm homes owned mortgaged", 1221, 7),
    new AggVariable("ohmtgunk", "Nonfarm homes encumbrance unknown", 1228, 6),
    new AggVariable("ohrent", "Nonfarm homes rented", 1234, 7),
    new AggVariable("ohunk", "Nonfarm homes tenure unknown", 1241, 6),
    new AggVariable("mtot", "Total male population, 1900", 1247, 8, nt62, "SEX = 1"),
    new AggVariable("ftot", "Total female population, 1900", 1255, 8, nt62, "SEX = 2"),
    new AggVariable("wmtot", "Total white male population, 1900", 1263, 8, nt63, "SEX = 1 and RACE = 1"),
    new AggVariable("wftot", "Total white female population, 1900", 1271, 8, nt63, "SEX = 2 and RACE = 1"),
    new AggVariable("whtot", "Total white population, 1900", 1279, 8, nt64, "RACE = 1"),
    new AggVariable("region1", "U.S. Census Region (9)", 1287, 1, "", "Nontabulated"),
    new AggVariable("region2", "U.S. Census Region (5)", 1288, 1, "", "Nontabulated"),
    new AggVariable("fips", "State/county FIPS code", 1289, 17, "", "Nontabulated", (x:String) => x.toDouble.round),
    geolevel
  )
    
  val vars:Map[String, AggVariable] = Map(numericAggVars.map(_.defaultMapElement): _*)
  
  lazy val stateData = readData
  
  // Returns an Map of Maps:
  // the inner map maps Variables to their values - the outer Map has keys of state ICP code and values of the inner map.
  def readData : Map[Long, AggDataRecord] = {
    
    // define some useful constants for the match block below.
    // Note that they need to be capitalized, due to case sensitivity in the pattern matching in the match expression:
    // They're mentioned in section 8.1.13 of the Scala Language Specification, and page 276 of Programming in Scala (2nd ed)
    // If they're lower-case, then they're treated as patterns to be filled in and you'll get non-obvious "unreachable code" errors.
    val County = "1"
    val State = "2"
    val Nation = "3"
    var lineNum = 0
    
    // println("Reading data for 1900 from:" + path_to_datafile)
		
    val states = mutable.Map[Long, AggDataRecord]()
    var counties = mutable.Map[Long, AggDataRecord]()
    
		val datafile = Source.fromFile(path_to_datafile)
		
		for (line <- datafile.getLines) {
		  lineNum += 1
      val name = nameAggVar(line)
      val level = geolevel(line)
      
      // println("[Line: " + lineNum + "] Processing: " + name + " at level " + level)
      
      val record = processDataline(line)
      
		  level match {
        case County => counties += (record.countyICP -> record)
        case State => {
          // state records always show up after their constituent counties.
          // When we see a state, add the current county data to the state (as an immutable map),
          // add the state to the result map,
          // and then get ready to process a new set of counties.
          record.counties = Map.empty ++ counties // make an immutable collection
          states += (record.stateICP -> record)
          counties = mutable.Map[Long, AggDataRecord]()
        }
        case Nation => null    // skip lines that we don't care about, which, for the moment, is the nation-level data
        case _ => null    // should never happen
		  }
	  }
    
    // answer an immutable Map of just the state data for now
    Map.empty ++ states
  }
  
  // given a single line, return the name of the location (a string) and Map of Variables to numeric values (which will be ints)
  def processDataline(data:String) : AggDataRecord = {
    val name = nameAggVar(data)
    val stateID = stateICP.asNumber(data)
    val countyID = countyICP.asNumber(data)
    
    val valueTuples = numericAggVars.map((item:AggVariable) => (item -> item.asNumber(data)))
    
    // can only call toMap on lists that contain Tuple2 elements, which valueTuples conveniently does.      
    new AggDataRecord(name, stateID, countyID, valueTuples.toMap)
  }
  
  // for a given state, get the variables that make up a specific table
  // should be callable like getTableForState(nt1, 33)
  // returns a BasicCount instance.
  def getTableForState(tableName:String, stateID:Long):BasicCount = {
    val stateRecord = stateData(stateID)
		val table = new BasicCount("Table: " + tableName + " data for State ID:" + stateID.toString)
		
		// get just the variables that comprise the table of interest.
    val tableContents = stateRecord.data filterKeys {(a:AggVariable) => a.tablename == tableName}

    // and load it into the result table
    table.setValuesForKeys(tableContents)
    
    table

  }    
}

