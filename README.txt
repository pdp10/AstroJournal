
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
- Apache Ant
- Git

You can clone AstroJournal repository with the git command:
git clone https://pdp10@bitbucket.org/pdp10/astrojournal.git

To compile AstroJournal on GNU/Linux type:
ant jar

Then, you can run AstroJournal on GNU/Linux typing:
./create_journal.sh


## Samples of observation records
Samples of observation record to test AstroJournal are already inserted in the folder tsv_reports and tsv_catalogue. These can be edited with any common text editor (e.g. emacs or kate).



# Development:

## ChangeLog:

v0.7

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

- improve code for importing an observation without being dependent on AJMain.



## Open bugs:



## Close bugs:

- Latex long table can generate an empty page if the current table spans 
until the end of the page. This empty page should be removed. [SOLVED] 
Replaced \begin{centre} ... \end{centre} with \centering for longtable.
