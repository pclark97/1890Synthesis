*-------------------------------------------------------------------------*
*                                                                          
*                     SPSS SETUP FILE FOR ICPSR 02896
*         HISTORICAL, DEMOGRAPHIC, ECONOMIC, AND SOCIAL DATA: THE
*                         UNITED STATES, 1790-2002
*              (DATASET 0020: 1900 CENSUS (COUNTY AND STATE))
* 
*
*  SPSS setup sections are provided for the ASCII version of this data
*  collection.  These sections are listed below:
*
*  DATA LIST:  assigns the name, type, decimal specification (if any),
*  and specifies the beginning and ending column locations for each
*  variable in the data file. Users must replace the "data-filename"
*  in the DATA LIST statement with a filename specifying the directory
*  on the user's computer system in which the downloaded and unzipped
*  data file is physically located (e.g., "c:\temp\02896-0020-data.txt").
*
*  VARIABLE LABELS:  assigns descriptive labels to all variables.
*  Labels and variable names may be identical for some data files.
*
*  MISSING VALUES: declares user-defined missing values. Not all
*  variables in this data set necessarily have user-defined missing
*  values. These values can be treated specially in data transformations,
*  statistical calculations, and case selection.
*
*  VALUE LABELS: assigns descriptive labels to codes found in the data
*  file.  Not all codes necessarily have assigned value labels.
*
*  NOTE:  Users should modify this setup file to suit their specific 
*  needs. The MISSING VALUES section has been commented out (i.e., '*').
*  To include the MISSING VALUES section in the final SPSS setup, remove 
*  the comment indicators from the desired section.
*
*  CREATING A PERMANENT SPSS DATA FILE: If users wish to create and save
*  an SPSS data file for further analysis using SPSS for Windows, the
*  necessary "SAVE OUTFILE" command is provided in the last line of
*  this file.  To activate the command, users must delete the leading
*  asterisk (*) and replace "spss-filename" with a filename specifying
*  the location on the user's computer system to which the new data file
*  will be saved (e.g., SAVE OUTFILE="c:\spsswin\data\da02896-0020.sav").
*
*-------------------------------------------------------------------------.

* SPSS FILE HANDLE AND DATA LIST COMMANDS.

FILE HANDLE DATA / NAME="data-filename" LRECL=1306 .

DATA LIST FILE=DATA/
   STATE 1-3                COUNTY 4-19              NAME 20-44 (A)        
   TOTPOP 45-52             URB900 53-60             URB25 61-68           
   RUR900 69-76             NBMTOT 77-84             NBFTOT 85-92          
   FBMTOT 93-99             FBFTOT 100-106           NBWMNP 107-114        
   NBWFNP 115-122           NBWMFP 123-129           NBWFFP 130-136        
   FBWMTOT 137-143          FBWFTOT 144-150          COLMTOT 151-157       
   COLFTOT 158-164          NEGMTOT 165-171          NEGFTOT 172-178       
   M21 179-186              NWMLIT 187-194           NWMILLIT 195-200      
   NNEGMLIT 201-207         NNEGMILL 208-213         NCOLMLIT 214-218      
   NCOLMILL 219-223         FBMNALIT 224-230         FBMNAILL 231-236      
   FBMFPLIT 237-242         FBMFPILL 243-247         FBMALLIT 248-253      
   FBMALILL 254-259         FBMUNLIT 260-265         FBMUNILL 266-270      
   NBWM520 271-278          NBWF520 279-286          FBWM520 287-292       
   FBWF520 293-298          NEGM520 299-305          NEGF520 306-312       
   OCOM520 313-317          OCOF520 318-322          NBWM1844 323-330      
   FBWM1844 331-337         NEGM1844 338-344         OCOM1844 345-350      
   NWNPIL10 351-357         NWFPIL10 358-363         FBWILL10 364-370      
   COLILL10 371-377         NEGILL10 378-384         TOTPOP1 385-392       
   DWELL 393-400            FAMILIES 401-408         PRIVFAM 409-416       
   PFAMPOP 417-424          PBASIA 425-429           PBATLISL 430-434      
   PBAUSTRA 435-438         PBAUSTRI 439-444         PBBELG 445-449        
   PBBOHEM 450-455          PBCANENG 456-461         PBCANFR 462-467       
   PBCENTAM 468-471         PBCHINA 472-477          PBCUBA 478-482        
   PBDENMAR 483-488         PBENGLAN 489-494         PBEURNS 495-498       
   PBFINLAN 499-503         PBFRANCE 504-509         PBGERMAN 510-516      
   PBGREECE 517-520         PBHOLLAN 521-526         PBHUNGAR 527-532      
   PBIRELAN 533-539         PBITALY 540-545          PBJAPAN 546-550       
   PBLUXEMB 551-554         PBMEXICO 555-560         PBNORWAY 561-566      
   PBNORDEN 567-569         PBPACIFI 570-573         PBPOLAND 574-577      
   PBPOLAUS 578-582         PBPOLGER 583-588         PBPOLRUS 589-594      
   PBPOLOTH 595-597         PBPOLUNK 598-602         PBPORTUG 603-607      
   PBRUMANI 608-612         PBRUSSIA 613-618         PBSCOT 619-624        
   PBSOAMER 625-628         PBSPAIN 629-632          PBSWEDEN 633-638      
   PBSWITZ 639-644          PBTURKEY 645-648         PBWALES 649-653       
   PBWESIND 654-658         PBSEA 659-662            PBOTHFOR 663-666      
   PBASIANC 667-670         MFGESTAB 671-676         MFGCAP 677-686        
   MFGCAPLA 687-696         MFGCAPBU 697-706         MFGCAPEQ 707-716      
   MFGCAPCA 717-726         MFGPROPS 727-732         MFGOFFIC 733-738      
   MFGOFSAL 739-747         MFGAVEAR 748-754         MFGWAGES 755-764      
   MFGLBM16 765-771         MFGWGM16 772-781         MFGLBF16 782-788      
   MFGWGF16 789-797         MFGLBCH 798-803          MFGWGCH 804-811       
   MFGMISCO 812-821         MFGRENTW 822-829         MFGTAXES 830-837      
   MFGRENTO 838-846         MFGCONTR 847-855         MFGRMS 856-865        
   MFGPRMS 866-875          MFGFUEL 876-884          MFGOUT 885-895        
   FARMS 896-902            FARMSIZE 903-920         FARM12 921-925        
   FARM39 926-931           FARM1019 932-937         FARM2049 938-944      
   FARM5099 945-951         FARM100 952-958          FARM175 959-964       
   FARM260 965-970          FARM500 971-976          FARM1000 977-981      
   FARMWH 982-988           FARMWHOW 989-995         FARMWHPO 996-1001     
   FARMWHTE 1002-1006       FARMWHMA 1007-1011       FARMWHCT 1012-1017    
   FARMWHST 1018-1023       FARMCOL 1024-1029        FARMCOOW 1030-1035    
   FARMCOPO 1036-1040       FARMCOTE 1041-1044       FARMCOMA 1045-1048    
   FARMCOCT 1049-1054       FARMCOST 1055-1060       FARMS2 1061-1067      
   FARMSBUI 1068-1074       ACFARM 1075-1083         ACIMP 1084-1092       
   FARMVAL 1093-1103        FARMBUI 1104-1113        FARMEQUI 1114-1122    
   LIVSTOCK 1123-1132       FARMOUT 1133-1142        FARMLAB 1143-1151     
   AREA 1152-1158           PRIVFAM2 1159-1166       TFARMHOM 1167-1173    
   FHOWFREE 1174-1180       FHENCUMB 1181-1187       FHMTGUNK 1188-1193    
   FHRENT 1194-1200         FHUNK 1201-1205          TOTHHOME 1206-1213    
   OHOWFREE 1214-1220       OHENCUMB 1221-1227       OHMTGUNK 1228-1233    
   OHRENT 1234-1240         OHUNK 1241-1246          MTOT 1247-1254        
   FTOT 1255-1262           WMTOT 1263-1270          WFTOT 1271-1278       
   WHTOT 1279-1286          REGION1 1287             REGION2 1288          
   FIPS 1289-1305           LEVEL 1306 .          


FORMATS
  COUNTY (F17.11) /        FARMSIZE (F19.16) /      FIPS (F18.12) / .
    
VARIABLE LABELS
   STATE 'ICPSR state code'
   COUNTY 'ICPSR county code'
   NAME 'Name of state/county'
   TOTPOP 'Total population, 1900'
   URB900 'Population in places 2,500+, 1900'
   URB25 'Population in places 25,000+, 1900'
   RUR900 'Rural population, 1900'
   NBMTOT 'Native-born males'
   NBFTOT 'Native-born females'
   FBMTOT 'Foreign-born males'
   FBFTOT 'Foreign-born females'
   NBWMNP 'Native white males/native parents'
   NBWFNP 'Native white females/native parents'
   NBWMFP 'Native white males/for parents'
   NBWFFP 'Native white females/for parents'
   FBWMTOT 'Foreign-born white males'
   FBWFTOT 'Foreign-born white females'
   COLMTOT 'Colored males'
   COLFTOT 'Colored females'
   NEGMTOT 'Negro males'
   NEGFTOT 'Negro females'
   M21 'Male population 21+'
   NWMLIT 'Literate native white males 21+'
   NWMILLIT 'Illiterate native white males 21+'
   NNEGMLIT 'Literate native Negro males 21+'
   NNEGMILL 'Illiterate native Negro males 21+'
   NCOLMLIT 'Literate other native colored males 21+'
   NCOLMILL 'Illiterate other native colored males 21+'
   FBMNALIT 'Literate naturalized foreign males 21+'
   FBMNAILL 'Illiterate naturalized foreign males 21+'
   FBMFPLIT 'Literate foreign males 21+/first papers'
   FBMFPILL 'Illiterate foreign males 21+/first papers'
   FBMALLIT 'Literate alien foreign males 21+'
   FBMALILL 'Illiterate alien foreign males 21+'
   FBMUNLIT 'Literate foreign males 21+/unknown citizenship'
   FBMUNILL 'Illiterate foreign males 21+/unknown citizenship'
   NBWM520 'Native white males aged 5-20'
   NBWF520 'Native white females aged 5-20'
   FBWM520 'Foreign white males aged 5-20'
   FBWF520 'Foreign white females aged 5-20'
   NEGM520 'Negro males aged 5-20'
   NEGF520 'Negro females aged 5-20'
   OCOM520 'Other colored males aged 5-20'
   OCOF520 'Other colored females aged 5-20'
   NBWM1844 'Native white males aged 18-44'
   FBWM1844 'Foreign white males aged 18-44'
   NEGM1844 'Negro males aged 18-44'
   OCOM1844 'Other colored males aged 18-44'
   NWNPIL10 'Illiterate native white/native parents 10+'
   NWFPIL10 'Illiterate native white/foreign parents 10+'
   FBWILL10 'Illiterate foreign white 10+'
   COLILL10 'Illiterate total colored 10+'
   NEGILL10 'Illiterate Negroes 10+'
   DWELL '# dwellings'
   FAMILIES '# families'
   PRIVFAM '# private families'
   PFAMPOP 'Persons in private families'
   PBASIA 'Persons born in Asia n.s.'
   PBATLISL 'Persons born in Atlantic Islands'
   PBAUSTRA 'Persons born in Australia'
   PBAUSTRI 'Persons born in Austria'
   PBBELG 'Persons born in Belgium'
   PBBOHEM 'Persons born in Bohemia'
   PBCANENG 'Persons born in English Canada'
   PBCANFR 'Persons born in French Canada'
   PBCENTAM 'Persons born in Central America'
   PBCHINA 'Persons born in China'
   PBCUBA 'Persons born in Cuba'
   PBDENMAR 'Persons born in Denmark'
   PBENGLAN 'Persons born in England'
   PBEURNS 'Persons born in Europe n.s.'
   PBFINLAN 'Persons born in Finland'
   PBFRANCE 'Persons born in France'
   PBGERMAN 'Persons born in Germany'
   PBGREECE 'Persons born in Greece'
   PBHOLLAN 'Persons born in Holland'
   PBHUNGAR 'Persons born in Hungary'
   PBIRELAN 'Persons born in Ireland'
   PBITALY 'Persons born in Italy'
   PBJAPAN 'Persons born in Japan'
   PBLUXEMB 'Persons born in Luxemburg'
   PBMEXICO 'Persons born in Mexico'
   PBNORWAY 'Persons born in Norway'
   PBNORDEN 'Persons born in Norway/Denmark'
   PBPACIFI 'Persons born in Pacific Islands'
   PBPOLAND 'Persons born in Poland'
   PBPOLAUS 'Persons born in Austrian Poland'
   PBPOLGER 'Persons born in German Poland'
   PBPOLRUS 'Persons born in Russian Poland'
   PBPOLOTH 'Persons born in other Poland'
   PBPOLUNK 'Persons born in Poland (unk)'
   PBPORTUG 'Persons born in Portugal'
   PBRUMANI 'Persons born in Rumania'
   PBRUSSIA 'Persons born in Russia'
   PBSCOT 'Persons born in Scotland'
   PBSOAMER 'Persons born in South America'
   PBSPAIN 'Persons born in Spain'
   PBSWEDEN 'Persons born in Sweden'
   PBSWITZ 'Persons born in Switzerland'
   PBTURKEY 'Persons born in Turkey'
   PBWALES 'Persons born in Wales'
   PBWESIND 'Persons born in West Indies'
   PBSEA 'Persons born at sea'
   PBOTHFOR 'Persons born in other countries'
   PBASIANC 'Persons born in Asia exc China'
   MFGESTAB '# manufacturing establishments'
   MFGCAP 'Manufacturing capital'
   MFGCAPLA 'Manufacturing capital in land'
   MFGCAPBU 'Manufacturing capital/buildings'
   MFGCAPEQ 'Manufacturing capital/equipment'
   MFGCAPCA 'Manufacturing capital/cash'
   MFGPROPS '# Manufacturing proprietors'
   MFGOFFIC '# manufacturing salaried officials, clerks'
   MFGOFSAL 'Salaries of manufacturing officials, clerks'
   MFGAVEAR 'Av # manufacturing wage earners'
   MFGWAGES 'Total manufacturing wages (workers)'
   MFGLBM16 'Av # males 16+ employed manufacturing'
   MFGWGM16 'Total wages males 16+ manufacturing'
   MFGLBF16 'Av # females 16+ employed manufacturing'
   MFGWGF16 'Total wages females 16+ manufacturing'
   MFGLBCH 'Av # children <16 employed manufacturing'
   MFGWGCH 'Total wages children <16 manufacturing'
   MFGMISCO 'Total misc expenses manufacturing'
   MFGRENTW 'Total works rental manufacturing'
   MFGTAXES 'Total taxes (excluding internal revenue) manufacturing'
   MFGRENTO 'Total other rental/interest manufacturing'
   MFGCONTR 'Cost of contract work manufacturing'
   MFGRMS 'Cost of materials manufacturing'
   MFGPRMS 'Cost of principal materials manufacturing'
   MFGFUEL 'Cost of fuel/power/heat manufacturing'
   MFGOUT 'Value manufacturing output'
   FARMS 'Total # of farms'
   FARMSIZE 'Av farm size (acres)'
   FARM12 '# farms 1-2 acres'
   FARM39 '# farms 3-9 acres'
   FARM1019 '# farms 10-19 acres'
   FARM2049 '# farms 20-49 acres'
   FARM5099 '# farms 50-99 acres'
   FARM100 '# farms 100-174 acres'
   FARM175 '# farms 175-259 acres'
   FARM260 '# farms 260-499 acres'
   FARM500 '# farms 500-999 acres'
   FARM1000 '# farms 1000+ acres'
   FARMWH '# farms white farmers'
   FARMWHOW '# farms white farm owners'
   FARMWHPO '# farms white part owners'
   FARMWHTE '# farms white owners/tenants'
   FARMWHMA '# farms white farm managers'
   FARMWHCT '# farms white cash tenants'
   FARMWHST '# farms white share tenants'
   FARMCOL '# farms colored farmers'
   FARMCOOW '# farms colored farm owners'
   FARMCOPO '# farms colored part owners'
   FARMCOTE '# farms colored owners/tenants'
   FARMCOMA '# farms colored farm managers'
   FARMCOCT '# farms colored cash tenants'
   FARMCOST '# farms colored share tenants'
   FARMS2 'Total # of farms'
   FARMSBUI 'Total # of farms w/ buildings'
   ACFARM 'Total acres farmland'
   ACIMP '# improved acres farmland'
   FARMVAL 'Value of farmland/improvements (excluding buildings)'
   FARMBUI 'Value of farm buildings'
   FARMEQUI 'Value of farm implements/machinery'
   LIVSTOCK 'Value of farm livestock'
   FARMOUT 'Value of farm output not fed to livestock'
   FARMLAB 'Expenditure for farm labor'
   AREA 'Area (sq mi)'
   PRIVFAM2 '# private families'
   TFARMHOM 'Total farm homes'
   FHOWFREE 'Farm homes owned free'
   FHENCUMB 'Farm homes owned mortgaged'
   FHMTGUNK 'Farm homes encumbrance unknown'
   FHRENT 'Farm homes rented'
   FHUNK 'Farm homes tenure unknown'
   TOTHHOME 'Total nonfarm homes'
   OHOWFREE 'Nonfarm homes owned free'
   OHENCUMB 'Nonfarm homes owned mortgaged'
   OHMTGUNK 'Nonfarm homes encumbrance unknown'
   OHRENT 'Nonfarm homes rented'
   OHUNK 'Nonfarm homes tenure unknown'
   MTOT 'Total male population, 1900'
   FTOT 'Total female population, 1900'
   WMTOT 'Total white male population, 1900'
   WFTOT 'Total white female population, 1900'
   WHTOT 'Total white population, 1900'
   REGION1 'U.S. Census Region (9)'
   REGION2 'U.S. Census Region (5)'
   FIPS 'State/county FIPS code'
   LEVEL 'County=1 state=2 USA=3' .

EXECUTE.

* Create SPSS system file.

*SAVE OUTFILE="spss-filename.sav".
