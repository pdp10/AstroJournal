
# AstroJournal

Author: Piero Dalle Pezze

Licence: GPL v3 (2015)



## Description:
This Java application imports files containing astronomy observations 
and generates an integrated journal document in PDF format using Latex. 
Observation files can be edited using a common Spreadsheet software 
(e.g. Google Spreadsheet, LibreOffice Calc, Office Excel) or any common 
text editor (e.g. MS Wordpad, Emacs, Kate, or GEdit) and must be saved 
either as .tsv or .csv. To be recognised, fields MUST be separated 
by a TAB delimiter. Fields can have single or double quotes.



## Requirements for using AstroJournal:
To use AstroJournal you need to install:

- Java 1.6+
- TeX Live (for Linux Users) or MkTeX (for Windows Users) (pdflatex must be installed)

For windows users who installed MkTeX, the Latex packages url and mptopdf must be installed using the 
MkTeX Manager.

Then, if you are using GNU/Linux or MAC OS X, you can run AstroJournal on GNU/Linux typing (or clicking the icon):
./create_journal.sh

or if you are using Windows, click on:
create_journal.bat

This will start a very basic window with 1 button to generate the journals.


## How to create an observation record:
As currently implemented, the format of the observation tables is 
specific. A sample of an observation table is provides below. The 
titles (Date, Time, Location, Altitude, Temperature, Seeing, 
Transparency, Telescopes, Eyepieces, Filters, Target, Cons, Type, Power, 
and Notes) cannot be changed as these are used by AstroJournal to 
retrieve the data. All fields are separated by a tab character (\t) 
explicitly shown in this example with a text when this must be included.
An example of observation is as follows:

Date	03/06/2015
Time	21:40-23:30
Location	Cambridge, UK
Altitude	12m
Temperature	12C (wind: 0km/h)
Seeing	1 - Perfect seeing
Transparency	5 - Clear
Telescopes	Tele Vue 60 F6
Eyepieces	TV Panoptic 24mm, Nagler 3.5mm
Filters	Single Polarising Filter
Target	Cons	Type	Power	Notes
Jupiter	Cnc	Planet	103x +/- SPF	Write description here.
Moon	Sgr	Satellite	103x	Write description here.

Examples of observations can also be found in the folder tsv_folder/ .
These files can be edited with any spreadsheet (e.g. Google SpreadSheet, 
MS Excel, LibreOffice SpreadSheet) or a common text editor 
(e.g. MS Wordpad, Emacs, Kate, or GEdit). 
To customise the document header and footer, please look at the 
folder latex_header_footer to find the Latex files for the header 
and footer. Also these files can be edited with any common text 
editor.



## Use case
Here are some guidelines for using AstroJournal:
1. Report your observations (with the structure of my tsv file) using a spreadsheet program, such as MS Excel, Libreoffice Spreadsheet, or Google Spreadsheet. Alternatively you can use a common text editor (e.g. Wordpad, GNU Emacs, Kate, etc.) as long as the fields are the same as in the samples provided in the raw_report and that each field is separated using a TAB character.
2. Export your file as tsv (if using Google Spreadsheet) or csv. In the latter case, when asked, select tab as field delimiter.
3. Put this file in the folder raw_reports.
4. In the main astrojournal folder type the command above ./create_journal.sh or ./create_journal.bat .



# Development:

## Requirements for compiling AstroJournal:
To compile AstroJournal you need to install:

- Java 1.6+
- TeX Live (for Linux Users) or MkTeX (for Windows Users) (pdflatex must be installed)
- Apache Ant
- Git

For windows users who installed MkTeX, the Latex packages url and mptopdf must be installed using the 
MkTeX Manager.

You can clone AstroJournal repository with the git command:
git clone https://pdp10@bitbucket.org/pdp10/astrojournal.git

To compile AstroJournal on GNU/Linux type:
ant jar

To read the source code documentation type:
ant javadoc

You can run AstroJournal on GNU/Linux typing:
./create_journal.sh



## ChangeLog:

v0.8

- Corrected bug in astrojournal.bat
- Added support for csv input files.
- Improvement for the package generator.
- Design of the main graphic user interface (currently not connected to the program).

v0.7

- Create file create_journal.bat (for Windows Users) (not tested yet).
- Removed the field PowerExitPupilFOV from the session reports. The eyepiece information is now inserted in the file legends.tex included at header level.
- Removed catalogue tsv and latex folder. List of observed targets is the list of contents for the second generated pdf (by_target)
- Export observations by target

v0.6

- Imported list of objects observed by Catalogue.
- Added list of contents, sections, moved to Article class

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

- Insert a new observation session using a GUI. All observations are stored in a csv (or tsv) file.
- Have a GUI view where the previous observations can be visualised and edited.



## Open bugs:



## Close bugs:

- Latex long table can generate an empty page if the current table spans 
until the end of the page. This empty page should be removed. [SOLVED] 
Replaced \begin{centre} ... \end{centre} with \centering for longtable.
