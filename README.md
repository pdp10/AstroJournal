
# AstroJournal

Author: Piero Dalle Pezze

Licence: GPL v3 (2015)


### Description
This Java application imports files containing astronomy observations 
and generates an integrated journal document in PDF format using LaTeX. 
Observation files can be edited using a common Spreadsheet software 
(e.g. Google Spreadsheet, LibreOffice Calc, Office Excel) or any common 
text editor (e.g. MS Wordpad, Emacs, Kate, or GEdit) and must be saved 
either as .tsv or .csv. To be recognised, fields MUST be separated 
by a TAB delimiter. Fields can have single or double quotes.



### Features

- Support for GNU/Linux and Windows users.
- A simple graphical interface for running the program is provided. 
- Generation of a PDF document containing all user observation records collected by increasing target catalogue number. This is useful for comparing targets observed over time.
- Generation of a PDF document containing all user observation reports collected by decreasing date. This is useful for visualising one’s observations by session.
- Generation of a PDF document containing the targets observed by constellation. This is useful for checking observed and unobserved targets by constellation.
- Generation of a TXT document containing all user observation reports collected by decreasing date. This is for creating observation reports to be published in an astronomy forum (e.g. Stargazers Lounge).
- Complete lists of Messier objects and Caldwell selection of NGC targets are included at the end of the generated PDF documents.
- Although the program requires some form of structured input file, this is intentionally minimal in order to not distract the user who wants to insert his / her data rather than thinking of how to format this data. All input data is treated as a string and therefore is not parsed for controls. This leaves the freedom to the user to introduce the data content as s/he wish. For instance, although in each document header I use the Antoniadi Scale for Seeing, this can be trivially overridden with a customised one. The inserted value for the seeing is not controlled according to a specific scale. 
- Possibility to edit the document header and the footer according to one’s need. This must be done in LaTex for preserving the format controls in the final output file.
 

### Requirements
To use AstroJournal you need to install:

- [Java 1.7+](https://java.com/en/download/)

- [TeX Live](http://www.tug.org/texlive/) (for Linux Users) or [MikTeX](http://miktex.org/download) (for Windows Users) (pdflatex must be installed)

For windows users who installed MikTeX, the LaTeX packages url and mptopdf must be installed using the MikTeX Manager.
 

### Download
You can download the latest version of this software application here:

[https://github.com/pdp10/AstroJournal/archive/master.zip](https://github.com/pdp10/AstroJournal/archive/master.zip)

After downloading and uncompressing the file, you can run AstroJournal by typing:
```
# On GNU/Linux run AstroJournal typing (or clicking the icon):
./astrojournal.sh
```
or 
```
# On Windows, click on the icon:
astrojournal.exe
```

This will start a minimal graphical user interface to generate the journals.


### Create an observation record
As currently implemented, the format of the observation tables is 
specific. 

The titles (Date, Time, Location, Altitude, Temperature, Seeing, 
Transparency, Telescopes, Eyepieces, Filters, Target, Cons, Type, Power, 
and Notes) cannot be changed as these are used by AstroJournal to 
retrieve the data. All fields are separated by a tab character (TAB) 
explicitly shown in this example with a text when this must be included.

You can find samples of these files in the folder [raw_reports](https://github.com/pdp10/AstroJournal/tree/master/raw_reports/?at=master), which is AstroJournal input folder.

These files can be edited with any spreadsheet (e.g. Google SpreadSheet, 
MS Excel, LibreOffice SpreadSheet) or a common text editor 
(e.g. MS Wordpad, Emacs, Kate, or GEdit). 

To customise the document header and footer, please look at the 
folder latex_header_footer to find the LaTeX files for the header 
and footer. Also these files can be edited with any common text 
editor.



### Use case
Here are some guidelines for using AstroJournal:

1. Report your observations (with the structure of my tsv or csv file) using a spreadsheet program, such as MS Excel, Libreoffice Spreadsheet, or Google Spreadsheet. Alternatively you can use a common text editor (e.g. Wordpad, GNU Emacs, Kate, etc.) as long as the fields are the same as in the samples provided in the raw_report and that each field is separated using a TAB character.

2. Export your file as tsv (if using Google Spreadsheet) or csv. In the latter case, when asked, select tab as field delimiter.

3. Put this file in the folder raw_reports.

4. In the main astrojournal folder type the command above ./astrojournal.sh or astrojournal.exe .



# Develop AstroJournal

### Requirements
To compile AstroJournal you need to install:

- [Git](https://git-scm.com/downloads)

- [Java 1.7+](https://java.com/en/download/)

- [Maven](http://maven.apache.org/)

- [TeX Live](http://www.tug.org/texlive/) (for Linux Users) or [MikTeX](http://miktex.org/download) (for Windows Users) (pdflatex must be installed)

For windows users who installed MikTeX, the LaTeX packages url and mptopdf must be installed using the 
MikTeX Manager.

### Clone & Compile

To clone AstroJournal repository:
```
git clone https://github.com/pdp10/AstroJournal.git
```

To test maven type:
```
mvn --version
```

To add dependencies using Eclipse type:
```
mvn eclipse:eclipse
```

To create a jar file for AstroJournal type:
```
mvn package
```

To read the source code documentation type:
```
mvn javadoc:javadoc
```

To clean:
```
mvn clean
```

You can run AstroJournal on GNU/Linux typing:
```
./astrojournal.sh
```


### Changelog

v0.10

- Several bug fixes
- Added Tango icon set to AJ menu items.
- Added Darkness as new Observation parameter. This can be used for recording 
sky quality reading using meters such as SQM-L.
- Migrated from junit 4.11 to 4.12
- Migrated from apache log4j 1.2 to 2.5
- Integrated launch4j with maven pom.xml
- Improved log4j levels and junit tests.

v0.9

- Use of the application launch4j for generating a Windows executable file 
for astrojournal. In the future this could be integrated in Maven pom.xml.
- Added AJMainConsole to run AstroJournal via command line.
- Started support for Windows in order to avoid calling the BAT script.
- Input and output files are now placed in a configurable folder outside 
the folder containing the software AstroJournal. This folder can be edited 
using the Edit > Preferences menu.
- Added new icons
- Added internationalisation for GUI strings.
- Added menu bar, preferences and help to the GUI.
- Improved the AJ GUI to also report the program output graphically.
- Added a configuration class for managing the program parameters.
- Maven is now the default software project management for AJ, replacing 
Ant.
- Fixed a bug related to file names of galaxies. 
- AJ now supports input parameters but also Java options. 

v0.8

- Input/output folders can also be passed as Java options.
- Added report by constellation.
- Added report of additional catalogues in astrojournal by target.
- Added exporter to SGL report files.
- Corrected bug in astrojournal.bat
- Added support for csv input files.
- Reorganisation of the package generator.
- Design of the main graphic user interface (currently not connected to the program).

v0.7

- Create file run_astrojournal.bat (for Windows Users) (not tested yet).
- Removed the field PowerExitPupilFOV from the session reports. The eyepiece information is now inserted in the file legends.tex included at header level.
- Removed catalogue tsv and latex folder. List of observed targets is the list of contents for the second generated pdf (by_target)
- Export observations by target

v0.6

- Imported list of objects observed by Catalogue.
- Added list of contents, sections, moved to Article class

v0.5

- added Messier and Caldwell catalogues as reference in the end
- improved pdf layout
- extracted LaTeX header and footer from Java code
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
- imported LaTeX code for observations prior to the log
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

- creation of LaTeX code for all your observation
- reading of different tsv files sequentially and ordered by date increasing 
is supported. In this way, you can create an observation log per month or 
two (keeping the google spreadsheet short), and add the exported tsv file 
to the folder tsv_files

