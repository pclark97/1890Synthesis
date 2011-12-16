Abstract:
On January 10, 1921, the original United States 1890 census schedules—the household-by-household enumeration forms—were severely damaged in a fire. Most of the surviving records were destroyed in the mid-1930s. The loss represents irreplaceable information about the condition of the country at the end of the nineteenth century. However, census microdata for the surrounding enumerations do survive, as do tabulations from 1890. We propose a method for generating a synthetic microdata dataset with similar statistical properties to 1890 that, while of limited use, would nevertheless fill a hole in the historical census records. This method constructs a new dataset by sampling with replacement of entire households from the 1880 and 1900 census microdata records to match selected tabulations from 1890. We show that the dataset produced by this method has error properties not dissimilar to those found when comparing tabulations produced from the IPUMS 1880 and 1900 microdata samples to the actual tabulations from 1880 and 1900.

Code notes:
Code is based on Scala 2.9.1, and uses sbt 0.10.1 to build and compile.

To build and run:
[10:14:39] SnowWhite2:PlanB-git2$ sbt compile
[10:15:12] SnowWhite2:PlanB-git2$ sbt test
[10:16:39] SnowWhite2:PlanB-git2$ sbt run

On a mid-2008-era Mac Pro, expect things to run for about 12-15 minutes.
The code uses the scala parallel collections, so more cores means faster runtime. If you're running this on a laptop, it'll get hot and noisy.

The main class is pclark.data.Tabulator.

My master's thesis, describing what this code does and why it's interesting, is in the docs folder.
