package pclark.data.cph1890

import scala.io.Source
import scala.collection.mutable

import pclark._
import pclark.data._
import pclark.data.table._

/* This singleton contains variable definitions for the ICPSR 02896 file for the US 1890 census (county and state) data file.
   The main variable list is generated from the stata dictionary for the dataset.
   Unfortunately, stata numbers columns from one, wheras scala numbers them from zero. */
   
/* Citation info for the data file:
Haines, Michael R., and Inter-university Consortium for Political and Social Research.
Historical, Demographic, Economic, and Social Data: The United States, 1790-2002 [Computer file]. ICPSR02896-v3.
Ann Arbor, MI: Inter-university Consortium for Political and Social Research [distributor], 2010-05-21. doi:10.3886/ICPSR02896
*/


object ICPSR_02896 extends AggDataFile {
  val path_to_datafile = targetData + "1890/ICPSR/02896-0018-Data.dat"
	
  val nt1 = "Total Population [NT1]"
  val nt2 = "Urban Population 2,500 and Over [NT2]"
  val nt4 = "Non-white Population by Race by Year [NT4]"
  val nt5 = "Nativity by Sex [NT5]"
  val nt6 = "Race/Nativity by Sex [NT6]"
  val nt9 = "Sex [NT9]"
  val nt10 = "Population 5 to 20 Years of Age by Race/Nativity by Sex [NT10]"
  val nt12 = "Male Population 18 to 44 Years of Age by Race/Nativity [NT12]"
  val nt14 = "Male Population 21 Years of Age and Over by Race/Nativity [NT14]"
  val nt16 = "Illiterate Population 10 Years of Age and Over by Race/Nativity [NT16]"
  val nt20 = "Total Population [NT20]"
  val nt46 = "Foreign-born Population by Country of Birth [NT46]"
  val nt60 = "Total White Population by Sex [NT60]"

  val stateICP = new AggVariable("state", "ICPSR state code", 1, 3, "", "Nontabulated")
  val countyICP = new AggVariable("county", "ICPSR county code", 4, 4, "Geographic", "Nontabulated", (x:String) => x.toDouble.round)
  val nameAggVar = new AggVariable("name", "Name of state/county", 8, 21, "", "Nontabulated")
  val geolevel = new AggVariable("level", "County=1 state=2 USA=3", 1325, 1, "", "Nontabulated")
  val stateFIP =  new AggVariable("statefips", "State FIPS code", 1308, 17, "", "Nontabulated", (x:String) => x.toDouble.round)
  val totalPopulation = new AggVariable("totpop",   "Total population 1890", 29, 8, nt1, "All")
  
  val numericAggVars = List(    
    stateICP, countyICP, totalPopulation,
    new AggVariable("urb890",   "Urban population 1890", 37, 8, nt2, nontab), 
    new AggVariable("urb25",    "Population cities 25K+ 1890", 45, 8, "Population in Cities of 25,000 and Over (NT3)"), 
    new AggVariable("negtot",   "Total Negro population 1890", 53, 7, nt4, "RACE = 2"), 
    new AggVariable("neg1880",  "Total Negro population 1880", 60, 7, nt4, nontab), 
    new AggVariable("neg1870",  "Total Negro population 1870", 67, 7, nt4, nontab), 
    new AggVariable("chitot",   "Total Chinese population 1890", 74, 6, nt4, "RACE = 4"), 
    new AggVariable("chi1880",  "Total Chinese population 1880", 80, 6, nt4, nontab), 
    new AggVariable("chi1870",  "Total Chinese population 1870", 86, 5, nt4, nontab), 
    new AggVariable("japtot",   "Total Japanese population 1890", 91, 4, nt4, "RACE = 5"), 
    new AggVariable("jap1880",  "Total Japanese population 1880", 95, 3, nt4, nontab), 
    new AggVariable("jap1870",  "Total Japanese population 1870", 98, 2, nt4, nontab), 
    new AggVariable("indtot",   "Civilized Indian population 1890", 100, 5, nt4, "RACE = 3"), 
    new AggVariable("ind1880",  "Civilized Indian population 1880", 105, 5, nt4, nontab), 
    new AggVariable("ind1870",  "Civilized Indian population 1870", 110, 5, nt4, nontab), 
    new AggVariable("nbmtot",   "Native-born males", 115, 8, nt5, "SEX = 1 and NATIVITY in [1,2,3,4]"), 
    new AggVariable("nbftot",   "Native-born females", 123, 8, nt5, "SEX = 2 and NATIVITY in [1,2,3,4]"), 
    new AggVariable("fbmtot",   "Foreign-born males", 131, 7, nt5, "SEX = 1 AND NATIVITY = 5"), 
    new AggVariable("fbftot",   "Foreign-born females", 138, 7, nt5, "SEX = 2 AND NATIVITY = 5"), 
    new AggVariable("nbwmnp",   "Native white males/nat parents", 145, 8, nt6, "NATIVITY = 1 and RACE = 1 and SEX = 1"), 
    new AggVariable("nbwfnp",   "Native white females/nat parents", 153, 8, nt6, "NATIVITY = 1 and RACE = 1 and SEX = 2"), 
    new AggVariable("nbwmfp",   "Native white males/for parents", 161, 7, nt6, "NATIVITY in [2, 3, 4] and RACE = 1 and SEX = 1"), 
    new AggVariable("nbwffp",   "Native white females/for parents", 168, 7, nt6, "NATIVITY in [2, 3, 4] and RACE = 1 and SEX = 2"), 
    new AggVariable("fbwmtot",  "Foreign-born white males", 175, 7, nt6, "NATIVITY = 5 and RACE = 1 and SEX = 1"), 
    new AggVariable("fbwftot",  "Foreign-born white females", 182, 7, nt6, "NATIVITY = 5 and RACE = 1 and SEX = 1"), 
    new AggVariable("colmtot",  "Colored males", 189, 7, nt6,  "RACE > 1 and SEX = 1"), 
    new AggVariable("colftot",  "Colored females", 196, 7, nt6, "RACE > 1 and SEX = 2"), 
    new AggVariable("mtot",     "Total male population", 203, 8, nt9, "SEX = 1"), 
    new AggVariable("ftot",     "Total female population", 211, 8, nt9, "SEX = 2"), 
    new AggVariable("nbwm520",  "Native white males aged 5-20", 219, 7, nt10, "NATIVITY in [1,2,3,4] and RACE = 1 and SEX = 1 and AGE >= 5 and AGE <= 20"), 
    new AggVariable("nbwf520",  "Native white females aged 5-20", 226, 7, nt10, "NATIVITY in [1,2,3,4] and RACE = 1 and SEX = 2 and AGE >= 5 and AGE <= 20"), 
    new AggVariable("fbwm520",  "Foreign white males aged 5-20", 233, 6, nt10, "NATIVITY = 5 and RACE = 1 and SEX = 1 and AGE >= 5 and AGE <= 20"), 
    new AggVariable("fbwf520",  "Foreign white females aged 5-20", 239, 6, nt10, "NATIVITY = 5 and RACE = 1 and SEX = 2 and AGE >= 5 and AGE <= 20"), 
    new AggVariable("colm520",  "Colored males aged 5-20", 245, 7, nt10, "RACE = 2 and SEX = 1 and AGE >= 5 and AGE <= 20"), 
    new AggVariable("colf520",  "Colored females aged 5-20", 252, 7, nt10, "RACE = 2 and SEX = 2 and AGE >= 5 and AGE <= 20"), 
    new AggVariable("nbwm1844", "Native white males aged 18-44", 259, 7, nt12, "NATIVITY in [1,2,3,4] and RACE = 1 and SEX = 1 and AGE >= 18 and AGE <= 44"), 
    new AggVariable("fbwm1844", "Foreign white males aged 18-44", 266, 7, nt12,  "NATIVITY = 5 and RACE = 1 and SEX = 1 and AGE >= 18 and AGE <= 44"), 
    new AggVariable("colm1844", "Colored males aged 18-44", 273, 7, nt12, "RACE = 2 and SEX = 1 and AGE >= 18 and AGE <= 44"), 
    new AggVariable("nbwm21",   "Native white males aged 21+", 280, 8, nt14, "NATIVITY in [1,2,3,4] and RACE = 1 and SEX = 1 and AGE >= 21"), 
    new AggVariable("fbwm21",   "Foreign white males aged 21+", 288, 7, nt14, "NATIVITY = 5 and RACE = 1 and SEX = 1 and AGE >= 21"), 
    new AggVariable("colm21",   "Colored males aged 21+", 295, 7, nt14, "RACE > 1 and SEX = 1 and AGE >= 21"), 
    new AggVariable("dwell",    "# dwellings", 302, 8, "table", nontab), 
    new AggVariable("families", "# families", 310, 8, "table", nontab), 
    new AggVariable("farm09",   "# farms <10 acres", 318, 6, "table", nontab), 
    new AggVariable("farm1019", "# farms 10-19 acres", 324, 6, "table", nontab), 
    new AggVariable("farm2049", "# farms 20-49 acres", 330, 6, "table", nontab), 
    new AggVariable("farm5099", "# farms 50-99 acres", 336, 7, "table", nontab), 
    new AggVariable("farm100",  "# farms 100-499 acres", 343, 7, "table", nontab), 
    new AggVariable("farm500",  "# farms 500-999 acres", 350, 5, "table", nontab), 
    new AggVariable("farm1000", "# farms 1000+ acres", 355, 5, "table", nontab), 
    new AggVariable("farmsize", "Av farm size (acres)", 360, 5, "table", nontab), 
    new AggVariable("fa09ow",   "# owner farms <10 acres", 365, 5, "table", nontab), 
    new AggVariable("fa1019ow", "# owner farms 10-19 acres", 370, 6, "table", nontab), 
    new AggVariable("fa2049ow", "# owner farms 20-49 acres", 376, 6, "table", nontab), 
    new AggVariable("fa5099ow", "# owner farms 50-99 acres", 382, 6, "table", nontab), 
    new AggVariable("fa100ow",  "# owner farms 100-499 acres", 388, 7, "table", nontab), 
    new AggVariable("fa500ow",  "# owner farms 500-999 acres", 395, 5, "table", nontab), 
    new AggVariable("fa1000ow", "# owner farms 1000+ acres", 400, 5, "table", nontab), 
    new AggVariable("fa09te",   "# tenant farms <10 acres", 405, 5, "table", nontab), 
    new AggVariable("fa1019te", "# tenant farms 10-19 acres", 410, 5, "table", nontab), 
    new AggVariable("fa2049te", "# tenant farms 20-49 acres", 415, 6, "table", nontab), 
    new AggVariable("fa5099te", "# tenant farms 50-99 acres", 421, 6, "table", nontab), 
    new AggVariable("fa100te",  "# tenant farms 100-499 acres", 427, 6, "table", nontab), 
    new AggVariable("fa500te",  "# tenant farms 500-999 acres", 433, 4, "table", nontab), 
    new AggVariable("fa1000te", "# tenant farms 1000+ acres", 437, 4, "table", nontab), 
    new AggVariable("fa09sc",   "# share crop farms <10 acres", 441, 5, "table", nontab), 
    new AggVariable("fa1019sc", "# share crop farms 10-19 acres", 446, 5, "table", nontab), 
    new AggVariable("fa2049sc", "# share crop farms 20-49 acres", 451, 6, "table", nontab), 
    new AggVariable("fa5099sc", "# share crop farms 50-99 acres", 457, 6, "table", nontab), 
    new AggVariable("fa100sc",  "# share crop farms 100-499 acres", 463, 6, "table", nontab), 
    new AggVariable("fa500sc",  "# share crop farms 500-999 acres", 469, 4, "table", nontab), 
    new AggVariable("fa1000sc", "# share crop farms 1000+ acres", 473, 4, "table", nontab), 
    new AggVariable("farms",    "Total # farms", 477, 7, "table", nontab), 
    new AggVariable("acimp",    "# improved acres in farms", 484, 9, "table", nontab), 
    new AggVariable("acunimp",  "# unimproved acres in farms", 493, 9, "table", nontab), 
    new AggVariable("farmval",  "Value of farmland/improvements", 502, 11, "table", nontab), 
    new AggVariable("equipval", "Value farm implements/machinery", 513, 9, "table", nontab), 
    new AggVariable("livstock", "Value farm livestock", 522, 10, "table", nontab), 
    new AggVariable("farmout",  "Value of all farm output", 532, 10, "table", nontab), 
    new AggVariable("farmfert", "Cost of fertilizer purchased", 542, 8, "table", nontab), 
    new AggVariable("barleyac", "Acres in barley", 550, 7, "table", nontab), 
    new AggVariable("barley",   "# bushels barley", 557, 8, "table", nontab), 
    new AggVariable("buckwhac", "Acres in buckwheat", 565, 6, "table", nontab), 
    new AggVariable("buckwhea", "# bushels buckwheat", 571, 8, "table", nontab), 
    new AggVariable("cornac",   "Acres in Indian Corn", 579, 8, "table", nontab), 
    new AggVariable("corn",     "# bushels Indian corn", 587, 10, "table", nontab), 
    new AggVariable("oatsac",   "Acres in oats", 597, 8, "table", nontab), 
    new AggVariable("oats",     "# bushels oats", 605, 9, "table", nontab), 
    new AggVariable("ryeac",    "Acres in rye", 614, 7, "table", nontab), 
    new AggVariable("rye",      "# bushels rye", 621, 8, "table", nontab), 
    new AggVariable("wheatac",  "Acres in wheat", 629, 8, "table", nontab), 
    new AggVariable("wheat",    "# bushels wheat", 637, 9, "table", nontab), 
    new AggVariable("mfgestab", "# manufacturing establishments", 646, 6, "table", nontab), 
    new AggVariable("mfgcap",   "Manufacturing capital", 652, 10, "table", nontab), 
    new AggVariable("mfgcapla", "Manufacturing capital in land", 662, 9, "table", nontab), 
    new AggVariable("mfgcapbu", "Manufacturing capital/buildings", 671, 9, "table", nontab), 
    new AggVariable("mfgcapeq", "Manufacturing capital/equipment", 680, 10, "table", nontab), 
    new AggVariable("mfgcapca", "Manuf capital/live assets/cash", 690, 10, "table", nontab), 
    new AggVariable("mfgmisco", "Total misc expenses mfg", 700, 9, "table", nontab), 
    new AggVariable("mfgavemp", "Av # mfg employees", 709, 7, "table", nontab), 
    new AggVariable("mfgwages", "Total manufacturing wages", 716, 10, "table", nontab), 
    new AggVariable("mfgmoff",  "# male mfg officers, clerks 16+", 726, 6, "table", nontab), 
    new AggVariable("mfgmofwa", "Wages male mfg officers, clerks", 732, 9, "table", nontab), 
    new AggVariable("mfgfoff",  "# fem mfg officers, clerks 15+", 741, 5, "table", nontab), 
    new AggVariable("mfgfofwa", "Wages female mfg officers, clerks", 746, 8, "table", nontab), 
    new AggVariable("mfglbm16", "# males 16+ employed mfg", 754, 7, "table", nontab), 
    new AggVariable("mfgwgm16", "Total wages males 16+ mfg", 761, 10, "table", nontab), 
    new AggVariable("mfglbf15", "# females 16+ employed mfg", 771, 6, "table", nontab), 
    new AggVariable("mfgwgf15", "Total wages females 16+ mfg", 777, 9, "table", nontab), 
    new AggVariable("mfglbch",  "# children <16 employed mfg", 786, 6, "table", nontab), 
    new AggVariable("mfgwgch",  "Total wages children <16 mfg", 792, 8, "table", nontab), 
    new AggVariable("mfgpwm16", "# male piece workers 16+", 800, 6, "table", nontab), 
    new AggVariable("pwwagm16", "Wages male piece workers 16+", 806, 9, "table", nontab), 
    new AggVariable("mfgpwf15", "# female piece workers 15+", 815, 6, "table", nontab), 
    new AggVariable("pwwagf15", "Wages female piece workers 15+", 821, 8, "table", nontab), 
    new AggVariable("mfgpwch",  "# child piece workers", 829, 5, "table", nontab), 
    new AggVariable("pwwagch",  "Wages child piece workers", 834, 7, "table", nontab), 
    new AggVariable("mfgrms",   "Cost of mfg materials", 841, 10, "table", nontab), 
    new AggVariable("mfgout",   "Value manufacturing output", 851, 10, "table", nontab), 
    new AggVariable("cswmtea",  "White male teachers common school", 861, 6, "table", nontab), 
    new AggVariable("cswftea",  "White fem teachers common school", 867, 6, "table", nontab), 
    new AggVariable("cscomtea", "Colored male teachers common school", 873, 5, "table", nontab), 
    new AggVariable("cscoftea", "Colored fem teachers common school", 878, 5, "table", nontab), 
    new AggVariable("cswmpup",  "White male pupils common school", 883, 7, "table", nontab), 
    new AggVariable("cswfpup",  "White fem pupils common school", 890, 7, "table", nontab), 
    new AggVariable("cscompup", "Colored male pupils common school", 897, 6, "table", nontab), 
    new AggVariable("cscofpup", "Colored fem pupils common school", 903, 6, "table", nontab), 
    new AggVariable("pbafrica", "Persons born in Africa n.s.", 909, 4, nt46, nontab), 
    new AggVariable("pbasia",   "Persons born in Asia n.s.", 913, 4, nt46, "BPL >= 502 and BPL <= 599 and BPL != 521"), // not china, japan, india
    new AggVariable("pbatlisl", "Persons born in Atlantic Islands", 917, 4, nt46, "BPL = 160"), 
    new AggVariable("pbaustra", "Persons born in Australia", 921, 4, nt46, "BPL = 700"), 
    new AggVariable("pbaustri", "Persons born in Austria", 925, 6, nt46, "BPL = 450"), 
    new AggVariable("pbbelg",   "Persons born in Belgium", 931, 5, nt46,  "BPL = 425"), 
    new AggVariable("pbbohem",  "Persons born in Bohemia", 936, 6, nt46, "None"), // NOTE - what to do about these? Not in IPUMS.
    new AggVariable("pbcanada", "Persons born in Canada/Newfoundland", 942, 6, nt46, "BPL = 150"), 
    new AggVariable("pbcentam", "Persons born in Central America", 948, 4, nt46, "BPL = 210"), 
    new AggVariable("pbchina",  "Persons born in China", 952, 6, nt46, "BPL = 500"), 
    new AggVariable("pbcubawi", "Persons born in Cuba/West Indies", 958, 5, nt46, "BPL = 250 or BPL = 260"), 
    new AggVariable("pbdenmar", "Persons born in Denmark", 963, 6, nt46, "BPL = 400"), 
    new AggVariable("pbenglan", "Persons born in England", 969, 6, nt46, "BPL = 410"), 
    new AggVariable("pbeurns",  "Persons born in Europe n.s.", 975, 5, nt46, "BPL = 499"), 
    new AggVariable("pbfrance", "Persons born in France", 980, 6, nt46, "BPL = 421"), 
    new AggVariable("pbgerman", "Persons born in Germany", 986, 7, nt46, "BPL = 453"), 
    new AggVariable("pbgreece", "Persons born in Greece", 993, 4, nt46, "BPL = 433"), 
    new AggVariable("pbhollan", "Persons born in Holland", 997, 5, nt46, "BPL = 425"), 
    new AggVariable("pbhungar", "Persons born in Hungary", 1002, 5, nt46, "BPL = 454"), 
    new AggVariable("pbindia",  "Persons born in India", 1007, 4, nt46, "BPL = 521"), 
    new AggVariable("pbirelan", "Persons born in Ireland", 1011, 7, nt46, "BPL = 414"), 
    new AggVariable("pbitaly",  "Persons born in Italy", 1018, 6, nt46, "BPL = 434"), 
    new AggVariable("pbjapan",  "Persons born in Japan", 1024, 4, nt46, "BPL = 501"), 
    new AggVariable("pbluxemb", "Persons born in Luxemburg", 1028, 4, nt46, "BPL = 423"), 
    new AggVariable("pbmexico", "Persons born in Mexico", 1032, 5, nt46, "BPL = 200"), 
    new AggVariable("pbnorway", "Persons born in Norway", 1037, 6, nt46, "BPL = 404"), 
    new AggVariable("pbnorden", "Persons born in Norway/Denmark", 1043, 3, nt46, "BPL in [404, 400]"), 
    new AggVariable("pbpacifi", "Persons born in Pacific Islands", 1046, 4, nt46, "BPL = 710"), 
    new AggVariable("pbpoland", "Persons born in Poland", 1050, 6, nt46,  "BPL = 455"), 
    new AggVariable("pbportug", "Persons born in Portugal", 1056, 5, nt46,  "BPL = 436"), 
    new AggVariable("pbrussia", "Persons born in Russia", 1061, 6, nt46, "BPL = 465"), 
    new AggVariable("pbhawaii", "Persons born in Sandwich Islands", 1067, 4, nt46), 
    new AggVariable("pbscot",   "Persons born in Scotland", 1071, 6, nt46, "BPL = 411"), 
    new AggVariable("pbsoamer", "Persons born in South America", 1077, 4, nt46, "BPL = 300"), 
    new AggVariable("pbspain",  "Persons born in Spain", 1081, 4, nt46, "BPL = 438"), 
    new AggVariable("pbsweden", "Persons born in Sweden", 1085, 6, nt46, "BPL = 405"), 
    new AggVariable("pbswitz",  "Persons born in Switzerland", 1091, 6, nt46, "BPL = 426"), 
    new AggVariable("pbturkey", "Persons born in Turkey", 1097, 4, nt46,  "BPL = 542"), 
    new AggVariable("pbwales",  "Persons born in Wales", 1101, 6, nt46,  "BPL = 412"), 
    new AggVariable("pbsea",    "Persons born at sea", 1107, 4, nt46, "BPL = 900"), 
    new AggVariable("pbothfor", "Persons born in other countries", 1111, 4, nt46), 
    new AggVariable("farmfams", "Total farm families", 1115, 7, "table", nontab), 
    new AggVariable("farmown",  "Farm families-owners", 1122, 7, "table", nontab), 
    new AggVariable("farmfree", "Farm families-owners free/clear", 1129, 7, "table", nontab), 
    new AggVariable("farmencu", "Farm families-owners encumbered", 1136, 6, "table", nontab), 
    new AggVariable("farmrent", "Farm families-renters", 1142, 7, "table", nontab), 
    new AggVariable("faown80",  "Farms owned 1880", 1149, 7, "table", nontab), 
    new AggVariable("farent80", "Farms rented 1880", 1156, 7, "table", nontab), 
    new AggVariable("homefams", "Total nonfarm families", 1163, 7, "table", nontab), 
    new AggVariable("homeown",  "Homeowner nonfarm families", 1170, 7, "table", nontab), 
    new AggVariable("homefree", "Homeowner nonfarm families-free", 1177, 7, "table", nontab), 
    new AggVariable("homeencu", "Homeowner nonfarm families-encumb", 1184, 6, "table", nontab), 
    new AggVariable("homerent", "Renter nonfarm families", 1190, 7, "table", nontab), 
    new AggVariable("valtot",   "Value of encumbered homes/farms", 1197, 10, "table", nontab), 
    new AggVariable("valmtg",   "Mortgage value of homes/farms", 1207, 10, "table", nontab), 
    new AggVariable("valfaenc", "Value of encumbered farms", 1217, 10, "table", nontab), 
    new AggVariable("valfamtg", "Mortgage value of farms", 1227, 10, "table", nontab), 
    new AggVariable("valhoenc", "Value of encumbered homes", 1237, 10, "table", nontab), 
    new AggVariable("valhomtg", "Mortgage value of homes", 1247, 10, "table", nontab), 
    new AggVariable("inttot",   "Total interest paid homes/farms", 1257, 9, "table", nontab), 
    new AggVariable("intfarm",  "Interest paid on farms", 1266, 8, "table", nontab), 
    new AggVariable("inthome",  "Interest paid on homes", 1274, 8, "table", nontab), 
    new AggVariable("wmtot",    "Total white males", 1282, 8, nt60, "SEX = 1 and RACE = 1"), 
    new AggVariable("wftot",    "Total white females", 1290, 8, nt60, "SEX = 2 and RACE = 1"), 
    new AggVariable("whtot",    "Total white population", 1298, 8, "Total White Population (NT61)", "RACE = 1"), 
    new AggVariable("region1",  "U.S. Census Region (9)", 1306, 1, "table", nontab), 
    new AggVariable("region2",  "U.S. Census Region (5)", 1307, 1, "table", nontab),
    stateFIP, geolevel
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
  
