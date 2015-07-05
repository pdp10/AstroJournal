
# AstroJournal

Author: Piero Dalle Pezze

Licence: GPL v3 (2015)

Git repository: https://pdp10@bitbucket.org/pdp10/astrojournal.git



## Description:
This Java application imports files containing astronomy observations 
and generates an integrated journal document in PDF format using Latex. 
Observation files can be edited using a common Spreadsheet software 
(e.g. Google Spreadsheet, LibreOffice Calc, Office Excel) and must be 
saved as tab-separated value (tsv) files.



## Requirements:
To use AstroJournal you need to install:

- Java 1.6+
- TeX Live (pdflatex or texi2tex must be installed)

After downloading and uncompressing the file, you can execute AstroJournal 
on GNU/Linux typing: ./create_journal.sh.



## How to create an observation record:
As currently implemented, the format of the observation tables is 
specific. A sample of an observation table is provides below. The 
titles (e.g. Date, Time, Target, Cons, ..) cannot be changed as these 
are used by AstroJournal to retrieve the data. All fields are separated 
by a tab character (\t) explicitly shown in this example with a TAB text 
when this must be included.  

Date TAB 03/06/2015			
Time TAB 21:40-23:30			
Location TAB Cambridge, UK			
Altitude TAB 12m			
Temperature TAB 12C (wind: 0km/h)			
Seeing TAB 1 - Perfect seeing			
Transparency TAB 5 - Clear			
Telescopes TAB Tele Vue 60 F6			
Eyepieces TAB TV Panoptic 24mm, Nagler 3.5mm			
Power, EP, FOV TAB 15x, 4mm, 4.30deg; 103x, 0.6mm, 0.77deg		
Filters TAB Single Polarising Filter			
Target TAB Cons TAB Type TAB Power TAB Notes
Jupiter TAB Cnc TAB Planet TAB 103x +/- SPF TAB Write description here.
Moon TAB Sgr TAB Satellite TAB 103x TAB Write description here.

Examples of observations can also be found in the folder tsv_folder/ and 
can be opened using a text editor or a spreadsheet software.



## Use case

1. edit your observation using a SpreadSheet (e.g. Google Drive) (see above);
2. save your observations as .tsv (tab separated values) and put this .tsv file in the 
folder tsv_reports/;
3. run the following command for creating or updating the journal: ./create_journal.sh
4. run the following command for versioning the new files (just me): ./push_obs_git.sh



Thanks for using AstroJournal!
Piero





# Additional information:

## ChangeLog:

v0.6

- Imported list of objects observed by Catalogue.

v0.5

- added Messier and Caldwell catalogues as reference in the end
- improved pdf layout
- extracted Latex header and footer from Java code
- added additional controls
- bug fixes
- increased code modularity
- renamed observations/ and tsv_files/ into latex_reports/ and tsv_reports/
- source code cleaning

v0.4

- wiki links point to the last version directly. No need to update each time 
- simplified scripts for creating journal
- renamed this software 'AstroJournal'
- added controls before using pdflatex or texi2tex
- imported Latex code for observations prior to the log
- reversed the observations (now most recent first)
- renamed .tex and .pdf to lowercase names
- improved legends layout
- bug correction
- improved table layout
- simplified build.xml

v0.3

- add latex command for showing tables on multiple pages 
( http://users.sdsc.edu/~ssmallen/latex/longtable.html )
- Draw of your own images to replace the existing ones. However, I 
decided to omit images so the pdf is lighter and faster to load on the browser
- use tsv_files and observations outside of dist/ . Not necessary to copy 
these two folders into dist/ . Therefore, you only keep your data in one 
place. Simplified build.xml to have jar file in the current directory

v0.2

- improved code in Observation.java and ObservationItem.java
- input/output folders tsv_files/ and observations/ passed as parameters
- simplified tsv file structure
- corrected importation of multiple observations per day

v0.1

- creation of Latex code for all your observation
- reading of different tsv files sequentially and ordered by date increasing 
is supported. In this way, you can create an observation log per month or 
two (keeping the google spreadsheet short), and add the exported tsv file 
to the folder tsv_files



## Iced features:

- improve code for importing an observation without being dependent on AJMain.



## Open bugs:



## Close bugs:

- Latex long table can generate an empty page if the current table spans 
until the end of the page. This empty page should be removed. [SOLVED] 
Replaced \begin{centre} ... \end{centre} with \centering for longtable.
