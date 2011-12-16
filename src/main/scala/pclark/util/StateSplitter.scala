package pclark.util

import pclark._
import pclark.data._
import pclark.data.table._
import scala.io.Source
import java.io._

/* This object reads in the usa_00054.dat.gz file and splits the output by state. */

object StateSplitter extends GZipSource {

  val baseDir = "target/scala-2.9.0.final/classes/1880-1900/Microdata"
  val extractID = "usa_00057"

  val us1900Microdata = new MicrodataVars("1900", Source.fromFile(baseDir + "/" + extractID + ".cbk"))
  val stateVar = us1900Microdata.hVars("STATEFIP")
  val desiredStateFIP = 9
    
  def areWeInTheRightState(line:String) = {
      desiredStateFIP != stateVar(line).toInt
  }
    
  def getPrintWriterForState(oldPrintWriter:PrintWriter, oldStateCode:String, stateFIP:String):PrintWriter = {
    val append = true
    if (oldStateCode != stateFIP) {
      oldPrintWriter.close
      new PrintWriter(new FileWriter(new File("data-by-state/State_" + stateFIP + ".dat"), append))
    } else {
      oldPrintWriter
    }
  }
  
  def splitPersonRecsByState(datafile: Source) {
    var done = false
    var currentState = ""
    var output = new PrintWriter(new ByteArrayOutputStream())    // this initial value will never be used.
    
    val lines = datafile getLines
    
      while (!done & lines.hasNext) {
        val line = lines.next
          line.charAt(0) match {
              case '#' => null
              case 'H' => {
                val thisState = stateVar(line)
                output = getPrintWriterForState(output, currentState, thisState)
                currentState = thisState
                // uncomment the next line if you want hierarchical data rather than just person records.
                output.println(line)   
              }
              case 'P' => output.println(line)
          }
      }
      output.close
  }
    
  def main(args: Array[String]) {
	println("Splitting " + extractID)
	val outputDir = new java.io.File("data-by-state")
	outputDir.mkdir()
    splitPersonRecsByState(getSourceForGzipFile(baseDir + "/" + extractID + ".dat.gz"))
  }
}

/* to read the output of this program into R, use the following syntax:
 Useful R stuff to be found here:
 http://www.statmethods.net/input/valuelabels.html

widths <- c(-1, -4, -2, 8, 4, 6, 2, -4, 3, 1, 1, -2, -2, -2, -2, -2, 1, 3, 3, -5, 3, -5, 3, -5, 1, 1, -4, -2, -1, -1, -3, -1, -1, 1, 2, 1, -2, 1, -1, 3, -1, -2, -2, 2)
names <- c(#"RECTYPEP",
  #"YEARP",
  #"DATANUMP",
  "SERIALP",
  "PERNUM",
  "PERWT",
  "RELATE",
  #"RELATED",
  "AGE",
  "SEX",
  "MARST",
  #"AGEMONTH",
  #"BIRTHMO",
  #"DURMARR",
  #"CHBORN",
  #"CHSURV",
  "RACE",
  "RACED",
  "BPL",
  #"BPLD",
  "MBPL",
  #"MBPLD",
  "FBPL",
  #"FBPLD",
  "NATIVITY",
  "CITIZEN",
  #"YRIMMIG",
  #"YRSUSA1",
  #"YRSUSA2",
  #"SPEAKENG",
  #"HISPAND",
  #"SPANNAME",
  #"HISPRULE",
  "RACESING",
  "RACESINGD",
  "SCHOOL",
  #"SCHLMNTH",
  "LIT",
  #"LABFORCE",
  "OCC",
  #"QTRUNEMP",
  #"MOUNEMP",
  #"OCCSCORE",
  "SEI")


#small <- read.fwf("/Users/pclark/Projects/1900data.dat", widths=widths, col.names=names, colClasses="numeric", buffersize = 1000, nrows=300)
#small <- read.fwf("/Users/pclark/Projects/PlanB2/data-by-state/State_04.dat", widths=widths, col.names=names, colClasses="numeric", buffersize = 1000)

# wonder if this will work?
compressed <- gzfile("/Users/pclark/Projects/PlanB2/data-by-state/State_01.dat.gz", "r")
small <- read.fwf(compressed, widths=widths, col.names=names, colClasses="numeric", buffersize = 4000)

small$RACE <- ordered(small$RACE, levels = c(1:9),
  labels = c("White", "Black", "American Indian or Alaska", "Chinese", "Japanese", "Other Asian", "Other race, nec", "Two major races", "Three or more major races"))

small$LIT <- ordered(small$LIT, levels = c(0:4),
  labels = c("N/A", "Illiterate", "Cannot read, can write", "Cannot write, can read", "Literate"))

small$RELATE <- ordered(small$RELATE, levels = c(1:13),
  labels = c("Head/Householder", "Spouse", "Child", "Child-in-law", "Parent", "Parent-in-Law", "Sibling", "Sibling-in-Law", "Grandchild", "Other relatives", "Partner, friend, visitor", "Other non-relatives", "Institutional inmates"))

small$SEX <- ordered(small$SEX, levels = c(1, 2),
 labels = c("Male", "Female"))
 
small$MARST <- ordered(small$MARST, levels = c(1:6),
  labels = c("Married, spouse present", "Married, spouse absent", "Separated", "Divorced", "Widowed", "Never married/single"))

bpl.levels = c(001, 002, 004, 005, 006, 008, 009, 010, 011, 012, 013, 015, 016, 017, 018, 019, 020, 021, 022, 023, 024, 025, 026, 027, 028, 029, 030, 031, 032, 033, 034, 035, 036, 037, 038, 039, 040, 041, 042, 044, 045, 046, 047, 048, 049, 050, 051, 053, 054, 055, 056, 090, 099, 100, 105, 110, 115, 120, 150, 155, 160, 199, 200, 210, 250, 260, 299, 300, 400, 401, 402, 403, 404, 405, 410, 411, 412, 413, 414, 419, 420, 421, 422, 423, 424, 425, 426, 429, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 450, 451, 452, 453, 454, 455, 456, 457, 458, 459, 460, 461, 462, 463, 465, 499, 500, 501, 502, 509, 510, 511, 512, 513, 514, 515, 516, 517, 518, 519, 520, 521, 522, 523, 524, 530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547, 548, 549, 550, 599, 600, 700, 710, 800, 900, 950, 999)

bpl.labels = c( "Alabama",  "Alaska", "Arizona",  "Arkansas", "California", "Colorado", "Connecticut",  "Delaware", "District of Columbia", "Florida",  "Georgia",  "Hawaii", "Idaho",  "Illinois", "Indiana",  "Iowa", "Kansas", "Kentucky", "Louisiana",  "Maine",  "Maryland", "Massachusetts",  "Michigan", "Minnesota",  "Mississippi",  "Missouri", "Montana",  "Nebraska", "Nevada", "New Hampshire",  "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee",  "Texas",  "Utah", "Vermont",  "Virginia", "Washington", "West Virginia",  "Wisconsin",  "Wyoming",  "Native American",  "United States, ns",  "American Samoa", "Guam", "Puerto Rico",  "U.S. Virgin Islands",  "Other US Possessions", "Canada", "St. Pierre and Miquelon",  "Atlantic Islands", "North America, ns",  "Mexico", "Central America",  "Cuba", "West Indies",  "Americas, n.s.", "SOUTH AMERICA",  "Denmark",  "Finland",  "Iceland",  "Lapland, n.s.",  "Norway", "Sweden", "England",  "Scotland", "Wales",  "United Kingdom, ns", "Ireland",  "Northern Europe, ns",  "Belgium",  "France", "Liechtenstein",  "Luxembourg", "Monaco", "Netherlands",  "Switerland", "Western Europe, ns", "Albania",  "Andorra",  "Gibraltar",  "Greece", "Italy",  "Malta",  "Portugal", "San Marino", "Spain",  "Vatican City", "Southern Europe, ns",  "Austria",  "Bulgaria", "Czechoslovakia", "Germany",  "Hungary",  "Poland", "Romania",  "Yugoslavia", "Central Europe, ns", "Eastern Europe, ns", "Estonia",  "Latvia", "Lithuania",  "Baltic States, ns",  "Other USSR/Russia",  "Europe, ns", "China",  "Japan",  "Korea",  "East Asia, ns",  "Brunei", "Cambodia (Kampuchea)", "Indonesia",  "Laos", "Malaysia", "Philippines",  "Singapore",  "Thailand", "Vietnam",  "Southeast Asia, ns", "Afghanistan",  "India",  "Iran", "Maldives", "Nepal",  "Bahrain",  "Cyprus", "Iraq", "Iraq/Saudi Arabia",  "Israel/Palestine", "Jordan", "Kuwait", "Lebanon",  "Oman", "Qatar",  "Saudi Arabia", "Syria",  "Turkey", "United Arab Emirates", "Yemen Arab Republic (North)",  "Yemen, PDR (South)", "Persian Gulf States, n.s.",  "Middle East, ns",  "Southwest Asia, nec/ns", "Asia Minor, ns", "South Asia, nec",  "Asia, nec/ns", "AFRICA", "Australia and New Zealand",  "Pacific Islands",  "Antarctica, ns/nec", "Abroad (unknown) or at sea", "Other, nec", "Missing/blank")

small$BPL <- ordered(small$BPL, levels = bpl.levels, labels = bpl.labels)
small$MBPL <- ordered(small$MBPL, levels = bpl.levels, labels = bpl.labels)
small$FBPL <- ordered(small$FBPL, levels = bpl.levels, labels = bpl.labels)

small$NATIVITY <- ordered(small$NATIVITY, levels = c(0:5),
  labels = c("N/A", "Native born, and both parents native born", "Native born, and father foreign, mother native", "Native born, and mother foreign, father native", "Native born, and both parents foreign", "Foreign born"))

small$CITIZEN <- ordered(small$CITIZEN, levels = c(0:5),
  labels = c("N/A", "Born abroad of American parents", "Naturalized citizen", "Not a citizen", "Not a citizen, but has received first papers", "Foreign born, citizenship status not reported"))


To get a frequency count of race, use the following:
race.freq <- table(small$RACE)
race.freq

To get a frequency count of race by literacy, use the following:
race.lit <- table(small$RACE, small$LIT)
race.lit



*/

