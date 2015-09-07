
# AstroJournal

Author: Piero Dalle Pezze

Licence: GPL v3 (2015)

Git repository: https://pdp10@bitbucket.org/pdp10/astrojournal.git



## Description:
This Java application imports files containing astronomy observations 
and generates an integrated journal document in PDF format using Latex. 
Observation files can be edited using a common Spreadsheet software 
(e.g. Google Spreadsheet, LibreOffice Calc, Office Excel) or any common text editor (e.g. MS Wordpad, Emacs, Kate, or GEdit) and must be saved as tab-separated value (tsv) files. To be recognised, fields MUST be separated by a TAB delimiter. No quote or single quotes should be added for marking the fields.



## Requirements for using AstroJournal:
To use AstroJournal you need to install:

- Java 1.6+
- TeX Live (pdflatex or texi2tex must be installed)

Then, you can run AstroJournal on GNU/Linux typing:
./create_journal.sh



## Samples of observation records
Samples of observation record to test AstroJournal are already inserted in the folder tsv_reports. These can be edited with any common text editor (e.g. MS Wordpad, Emacs, Kate, or GEdit) in order to contain your data. 
To customise the document header and footer, please look at the folder latex_header_footer to find the Latex files for the header and footer. Also these files can be edited with any common text editor.



# Development:

## Requirements for compiling AstroJournal:
To compile AstroJournal you need to install:

- Java 1.6+
- TeX Live (pdflatex or texi2tex must be installed)
- Apache Ant
- Git

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

- Design of the main graphic user interface (currently not connected to the program)

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
