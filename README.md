
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

[https://github.com/pdp10/AstroJournal/zipball/master](https://github.com/pdp10/AstroJournal/zipball/master)

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

This will start a simple graphical user interface to generate the journals.


### Create an observation record
As currently implemented, the format of the observation tables is 
specific. 

The titles (Date, Time, Location, Altitude, Temperature, Seeing, 
Transparency, Darkness, Telescopes, Eyepieces, Filters, Target, Cons, Type, Power, 
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

2. Export your file as tsv (if using Google Spreadsheet) or csv. In the latter case, when asked, select tab as field delimiter and no quotes as text delimiter.

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

To simply test AstroJournal type:
```
mvn test

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
