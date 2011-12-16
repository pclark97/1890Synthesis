*-------------------------------------------------------------------------*
*                                                                          
*                     SPSS SETUP FILE FOR ICPSR 02896
*         HISTORICAL, DEMOGRAPHIC, ECONOMIC, AND SOCIAL DATA: THE
*                         UNITED STATES, 1790-2002
*              (DATASET 0018: 1890 CENSUS (COUNTY AND STATE))
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
*  data file is physically located (e.g., "c:\temp\02896-0018-data.txt").
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
*  will be saved (e.g., SAVE OUTFILE="c:\spsswin\data\da02896-0018.sav").
*
*-------------------------------------------------------------------------.

* SPSS FILE HANDLE AND DATA LIST COMMANDS.

FILE HANDLE DATA / NAME="data-filename" LRECL=1325 .

DATA LIST FILE=DATA/
   STATE 1-3                COUNTY 4-7               NAME 8-28 (A)         
   TOTPOP 29-36             URB890 37-44             URB25 45-52           
   NEGTOT 53-59             NEG1880 60-66            NEG1870 67-73         
   CHITOT 74-79             CHI1880 80-85            CHI1870 86-90         
   JAPTOT 91-94             JAP1880 95-97            JAP1870 98-99         
   INDTOT 100-104           IND1880 105-109          IND1870 110-114       
   NBMTOT 115-122           NBFTOT 123-130           FBMTOT 131-137        
   FBFTOT 138-144           NBWMNP 145-152           NBWFNP 153-160        
   NBWMFP 161-167           NBWFFP 168-174           FBWMTOT 175-181       
   FBWFTOT 182-188          COLMTOT 189-195          COLFTOT 196-202       
   MTOT 203-210             FTOT 211-218             NBWM520 219-225       
   NBWF520 226-232          FBWM520 233-238          FBWF520 239-244       
   COLM520 245-251          COLF520 252-258          NBWM1844 259-265      
   FBWM1844 266-272         COLM1844 273-279         NBWM21 280-287        
   FBWM21 288-294           COLM21 295-301           DWELL 302-309         
   FAMILIES 310-317         FARM09 318-323           FARM1019 324-329      
   FARM2049 330-335         FARM5099 336-342         FARM100 343-349       
   FARM500 350-354          FARM1000 355-359         FARMSIZE 360-364      
   FA09OW 365-369           FA1019OW 370-375         FA2049OW 376-381      
   FA5099OW 382-387         FA100OW 388-394          FA500OW 395-399       
   FA1000OW 400-404         FA09TE 405-409           FA1019TE 410-414      
   FA2049TE 415-420         FA5099TE 421-426         FA100TE 427-432       
   FA500TE 433-436          FA1000TE 437-440         FA09SC 441-445        
   FA1019SC 446-450         FA2049SC 451-456         FA5099SC 457-462      
   FA100SC 463-468          FA500SC 469-472          FA1000SC 473-476      
   FARMS 477-483            ACIMP 484-492            ACUNIMP 493-501       
   FARMVAL 502-512          EQUIPVAL 513-521         LIVSTOCK 522-531      
   FARMOUT 532-541          FARMFERT 542-549         BARLEYAC 550-556      
   BARLEY 557-564           BUCKWHAC 565-570         BUCKWHEA 571-578      
   CORNAC 579-586           CORN 587-596             OATSAC 597-604        
   OATS 605-613             RYEAC 614-620            RYE 621-628           
   WHEATAC 629-636          WHEAT 637-645            MFGESTAB 646-651      
   MFGCAP 652-661           MFGCAPLA 662-670         MFGCAPBU 671-679      
   MFGCAPEQ 680-689         MFGCAPCA 690-699         MFGMISCO 700-708      
   MFGAVEMP 709-715         MFGWAGES 716-725         MFGMOFF 726-731       
   MFGMOFWA 732-740         MFGFOFF 741-745          MFGFOFWA 746-753      
   MFGLBM16 754-760         MFGWGM16 761-770         MFGLBF15 771-776      
   MFGWGF15 777-785         MFGLBCH 786-791          MFGWGCH 792-799       
   MFGPWM16 800-805         PWWAGM16 806-814         MFGPWF15 815-820      
   PWWAGF15 821-828         MFGPWCH 829-833          PWWAGCH 834-840       
   MFGRMS 841-850           MFGOUT 851-860           CSWMTEA 861-866       
   CSWFTEA 867-872          CSCOMTEA 873-877         CSCOFTEA 878-882      
   CSWMPUP 883-889          CSWFPUP 890-896          CSCOMPUP 897-902      
   CSCOFPUP 903-908         PBAFRICA 909-912         PBASIA 913-916        
   PBATLISL 917-920         PBAUSTRA 921-924         PBAUSTRI 925-930      
   PBBELG 931-935           PBBOHEM 936-941          PBCANADA 942-947      
   PBCENTAM 948-951         PBCHINA 952-957          PBCUBAWI 958-962      
   PBDENMAR 963-968         PBENGLAN 969-974         PBEURNS 975-979       
   PBFRANCE 980-985         PBGERMAN 986-992         PBGREECE 993-996      
   PBHOLLAN 997-1001        PBHUNGAR 1002-1006       PBINDIA 1007-1010     
   PBIRELAN 1011-1017       PBITALY 1018-1023        PBJAPAN 1024-1027     
   PBLUXEMB 1028-1031       PBMEXICO 1032-1036       PBNORWAY 1037-1042    
   PBNORDEN 1043-1045       PBPACIFI 1046-1049       PBPOLAND 1050-1055    
   PBPORTUG 1056-1060       PBRUSSIA 1061-1066       PBHAWAII 1067-1070    
   PBSCOT 1071-1076         PBSOAMER 1077-1080       PBSPAIN 1081-1084     
   PBSWEDEN 1085-1090       PBSWITZ 1091-1096        PBTURKEY 1097-1100    
   PBWALES 1101-1106        PBSEA 1107-1110          PBOTHFOR 1111-1114    
   FARMFAMS 1115-1121       FARMOWN 1122-1128        FARMFREE 1129-1135    
   FARMENCU 1136-1141       FARMRENT 1142-1148       FAOWN80 1149-1155     
   FARENT80 1156-1162       HOMEFAMS 1163-1169       HOMEOWN 1170-1176     
   HOMEFREE 1177-1183       HOMEENCU 1184-1189       HOMERENT 1190-1196    
   VALTOT 1197-1206         VALMTG 1207-1216         VALFAENC 1217-1226    
   VALFAMTG 1227-1236       VALHOENC 1237-1246       VALHOMTG 1247-1256    
   INTTOT 1257-1265         INTFARM 1266-1273        INTHOME 1274-1281     
   WMTOT 1282-1289          WFTOT 1290-1297          WHTOT 1298-1305       
   REGION1 1306             REGION2 1307             FIPS 1308-1324        
   LEVEL 1325 .          


FORMATS
  FIPS (F18.12) / .
    
VARIABLE LABELS
   STATE 'ICPSR state code'
   COUNTY 'ICPSR county code'
   NAME 'Name of state/county'
   TOTPOP 'Total population 1890'
   URB890 'Urban population 1890'
   URB25 'Population cities 25K+ 1890'
   NEGTOT 'Total Negro population 1890'
   NEG1880 'Total Negro population 1880'
   NEG1870 'Total Negro population 1870'
   CHITOT 'Total Chinese population 1890'
   CHI1880 'Total Chinese population 1880'
   CHI1870 'Total Chinese population 1870'
   JAPTOT 'Total Japanese population 1890'
   JAP1880 'Total Japanese population 1880'
   JAP1870 'Total Japanese population 1870'
   INDTOT 'Civilized Indian population 1890'
   IND1880 'Civilized Indian popuation 1880'
   IND1870 'Civilized Indian popuation 1870'
   NBMTOT 'Native-born males'
   NBFTOT 'Native-born females'
   FBMTOT 'Foreign-born males'
   FBFTOT 'Foreign-born females'
   NBWMNP 'Native white males/nat parents'
   NBWFNP 'Native white females/nat parents'
   NBWMFP 'Native white males/for parents'
   NBWFFP 'Native white females/for parents'
   FBWMTOT 'Foreign-born white males'
   FBWFTOT 'Foreign-born white females'
   COLMTOT 'Colored males'
   COLFTOT 'Colored females'
   MTOT 'Total male population'
   FTOT 'Total female population'
   NBWM520 'Native white males aged 5-20'
   NBWF520 'Native white females aged 5-20'
   FBWM520 'Foreign white males aged 5-20'
   FBWF520 'Foreign white females aged 5-20'
   COLM520 'Colored males aged 5-20'
   COLF520 'Colored females aged 5-20'
   NBWM1844 'Native white males aged 18-44'
   FBWM1844 'Foreign white males aged 18-44'
   COLM1844 'Colored males aged 18-44'
   NBWM21 'Native white males aged 21+'
   FBWM21 'Foreign white males aged 21+'
   COLM21 'Colored males aged 21+'
   DWELL '# dwellings'
   FAMILIES '# families'
   FARM09 '# farms <10 acres'
   FARM1019 '# farms 10-19 acres'
   FARM2049 '# farms 20-49 acres'
   FARM5099 '# farms 50-99 acres'
   FARM100 '# farms 100-499 acres'
   FARM500 '# farms 500-999 acres'
   FARM1000 '# farms 1000+ acres'
   FARMSIZE 'Av farm size (acres)'
   FA09OW '# owner farms <10 acres'
   FA1019OW '# owner farms 10-19 acres'
   FA2049OW '# owner farms 20-49 acres'
   FA5099OW '# owner farms 50-99 acres'
   FA100OW '# owner farms 100-499 acres'
   FA500OW '# owner farms 500-999 acres'
   FA1000OW '# owner farms 1000+ acres'
   FA09TE '# tenant farms <10 acres'
   FA1019TE '# tenant farms 10-19 acres'
   FA2049TE '# tenant farms 20-49 acres'
   FA5099TE '# tenant farms 50-99 acres'
   FA100TE '# tenant farms 100-499 acres'
   FA500TE '# tenant farms 500-999 acres'
   FA1000TE '# tenant farms 1000+ acres'
   FA09SC '# share crop farms <10 acres'
   FA1019SC '# share crop farms 10-19 acres'
   FA2049SC '# share crop farms 20-49 acres'
   FA5099SC '# share crop farms 50-99 acres'
   FA100SC '# share crop farms 100-499 acres'
   FA500SC '# share crop farms 500-999 acres'
   FA1000SC '# share crop farms 1000+ acres'
   FARMS 'Total # farms'
   ACIMP '# improved acres in farms'
   ACUNIMP '# unimproved acres in farms'
   FARMVAL 'Value of farmland/improvements'
   EQUIPVAL 'Value farm implements/machinery'
   LIVSTOCK 'Value farm livestock'
   FARMOUT 'Value of all farm output'
   FARMFERT 'Cost of fertilizer purchased'
   BARLEYAC 'Acres in barley'
   BARLEY '# bushels barley'
   BUCKWHAC 'Acres in buckwheat'
   BUCKWHEA '# bushels buckwheat'
   CORNAC 'Acres in Indian Corn'
   CORN '# bushels Indian corn'
   OATSAC 'Acres in oats'
   OATS '# bushels oats'
   RYEAC 'Acres in rye'
   RYE '# bushels rye'
   WHEATAC 'Acres in wheat'
   WHEAT '# bushels wheat'
   MFGESTAB '# manufacturing establishments'
   MFGCAP 'Manufacturing capital'
   MFGCAPLA 'Manufacturing capital in land'
   MFGCAPBU 'Manufacturing capital/buildings'
   MFGCAPEQ 'Manufacturing capital/equipment'
   MFGCAPCA 'Manuf capital/live assets/cash'
   MFGMISCO 'Total misc expenses mfg'
   MFGAVEMP 'Av # mfg employees'
   MFGWAGES 'Total manufacturing wages'
   MFGMOFF '# male mfg officers, clerks 16+'
   MFGMOFWA 'Wages male mfg officers, clerks'
   MFGFOFF '# fem mfg officers, clerks 15+'
   MFGFOFWA 'Wages female mfg officers, clerks'
   MFGLBM16 '# males 16+ employed mfg'
   MFGWGM16 'Total wages males 16+ mfg'
   MFGLBF15 '# females 16+ employed mfg'
   MFGWGF15 'Total wages females 16+ mfg'
   MFGLBCH '# children <16 employed mfg'
   MFGWGCH 'Total wages children <16 mfg'
   MFGPWM16 '# male piece workers 16+'
   PWWAGM16 'Wages male piece workers 16+'
   MFGPWF15 '# female piece workers 15+'
   PWWAGF15 'Wages female piece workers 15+'
   MFGPWCH '# child piece workers'
   PWWAGCH 'Wages child piece workers'
   MFGRMS 'Cost of mfg materials'
   MFGOUT 'Value manufacturing output'
   CSWMTEA 'White male teachers common school'
   CSWFTEA 'White fem teachers common school'
   CSCOMTEA 'Colored male teachers common school'
   CSCOFTEA 'Colored fem teachers common school'
   CSWMPUP 'White male pupils common school'
   CSWFPUP 'White fem pupils common school'
   CSCOMPUP 'Colored male pupils common school'
   CSCOFPUP 'Colored fem pupils common school'
   PBAFRICA 'Persons born in Africa n.s.'
   PBASIA 'Persons born in Asia n.s.'
   PBATLISL 'Persons born in Atlantic Islands'
   PBAUSTRA 'Persons born in Australia'
   PBAUSTRI 'Persons born in Austria'
   PBBELG 'Persons born in Belgium'
   PBBOHEM 'Persons born in Bohemia'
   PBCANADA 'Persons born in Canada/Newfoundland'
   PBCENTAM 'Persons born in Central America'
   PBCHINA 'Persons born in China'
   PBCUBAWI 'Persons born in Cuba/West Indies'
   PBDENMAR 'Persons born in Denmark'
   PBENGLAN 'Persons born in England'
   PBEURNS 'Persons born in Europe n.s.'
   PBFRANCE 'Persons born in France'
   PBGERMAN 'Persons born in Germany'
   PBGREECE 'Persons born in Greece'
   PBHOLLAN 'Persons born in Holland'
   PBHUNGAR 'Persons born in Hungary'
   PBINDIA 'Persons born in India'
   PBIRELAN 'Persons born in Ireland'
   PBITALY 'Persons born in Italy'
   PBJAPAN 'Persons born in Japan'
   PBLUXEMB 'Persons born in Luxemburg'
   PBMEXICO 'Persons born in Mexico'
   PBNORWAY 'Persons born in Norway'
   PBNORDEN 'Persons born in Norway/Denmark'
   PBPACIFI 'Persons born in Pacific Islands'
   PBPOLAND 'Persons born in Poland'
   PBPORTUG 'Persons born in Portugal'
   PBRUSSIA 'Persons born in Russia'
   PBHAWAII 'Persons born in Sandwich Islands'
   PBSCOT 'Persons born in Scotland'
   PBSOAMER 'Persons born in South America'
   PBSPAIN 'Persons born in Spain'
   PBSWEDEN 'Persons born in Sweden'
   PBSWITZ 'Persons born in Switzerland'
   PBTURKEY 'Persons born in Turkey'
   PBWALES 'Persons born in Wales'
   PBSEA 'Persons born at sea'
   PBOTHFOR 'Persons born in other countries'
   FARMFAMS 'Total farm families'
   FARMOWN 'Farm families-owners'
   FARMFREE 'Farm families-owners free/clear'
   FARMENCU 'Farm families-owners encumbered'
   FARMRENT 'Farm families-renters'
   FAOWN80 'Farms owned 1880'
   FARENT80 'Farms rented 1880'
   HOMEFAMS 'Total nonfarm families'
   HOMEOWN 'Homeowner nonfarm families'
   HOMEFREE 'Homeowner nonfarm families-free'
   HOMEENCU 'Homeowner nonfarm families-encumb'
   HOMERENT 'Renter nonfarm families'
   VALTOT 'Value of encumbered homes/farms'
   VALMTG 'Mortgage value of homes/farms'
   VALFAENC 'Value of encumbered farms'
   VALFAMTG 'Mortgage value of farms'
   VALHOENC 'Value of encumbered homes'
   VALHOMTG 'Mortgage value of homes'
   INTTOT 'Total interest paid homes/farms'
   INTFARM 'Interest paid on farms'
   INTHOME 'Interest paid on homes'
   WMTOT 'Total white males'
   WFTOT 'Total white females'
   WHTOT 'Total white population'
   REGION1 'U.S. Census Region (9)'
   REGION2 'U.S. Census Region (5)'
   FIPS 'State/county FIPS code'
   LEVEL 'county=1 state=2 USA=3' .

EXECUTE.

* Create SPSS system file.

*SAVE OUTFILE="spss-filename.sav".
