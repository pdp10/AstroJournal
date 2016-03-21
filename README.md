
# AstroJournal

Author: Piero Dalle Pezze

Licence: GPL v3 (2015-16)

Mailing list: astrojournal AT googlegroups.com


### Description
This Java application imports files containing astronomy observations 
and generates an integrated journal document in PDF format using LaTeX. 
Observation files can be edited using a common Spreadsheet software 
(e.g. Google Spreadsheet, LibreOffice Calc, Office Excel) or any common 
text editor (e.g. MS Wordpad, Emacs, Kate, or GEdit) and must be saved 
either as .tsv or .csv.


### Features
- Support for GNU/Linux and Windows users.
- A simple graphical interface for running the program is provided. 
- Generation of a PDF document containing all user observation records collected by increasing target catalogue number. This is useful for comparing targets observed over time.
- Generation of a PDF document containing all user observation reports collected by decreasing date. This is useful for visualising one’s observations by session.
- Generation of a PDF document containing the targets observed by constellation. This is useful for checking observed and unobserved targets by constellation.
- Generation of a TXT document containing all user observation reports collected by decreasing date. This is for creating observation reports to be published in an astronomy forum (e.g. Stargazers Lounge).
- Although the program requires some form of structured input file, this is intentionally minimal in order to not distract the user who wants to insert his / her data rather than thinking of how to format this data.
- Summary reports are also available by choosing a different generator in the Preferences Menu.
- Possibility to edit the document header and the footer according to one’s need. This must be done in LaTeX to maintain the format controls in the final output file.
- Complete lists of Messier objects and Caldwell selection of NGC targets are included at the end of the generated PDF documents.


### Requirements
To use AstroJournal you need to install:

- [Java 1.7+](https://java.com/en/download/)

- [TeX Live](http://www.tug.org/texlive/) (for Linux Users) or [MikTeX](http://miktex.org/download) (for Windows Users) or [MacTeX](https://tug.org/mactex/) (pdflatex must be installed)

Notes:

On GNU/Linux Debian/Ubuntu 14.04+, users can install the LaTeX dependencies required by AstroJournal with the following command:
```
sudo apt-get --no-install-recommends install texlive-latex-base texlive-latex-recommended
```
On Windows, users should install MikTeX and then the LaTeX packages *url* and *mptopdf* using MikTeX Manager.

On Mac OS X, users should install MacTeX. I have not tested this directly. If the command *pdflatex* is not available, I think it should be possible to create a link called *pdflatex* to the corresponding program used by MacTeX to compile LaTeX code.
In addition, to run AstroJournal on a MAC OS X platform, some steps are required since Mac OS X still uses Java 1.6 while AstroJournal requires Java 1.7+: 
- Download the latest Java from [https://java.com/en/download/mac_download.jsp](https://java.com/en/download/mac_download.jsp);
- Follow the procedure for installing the package.
- Unfortunately, Mac OS X installs this version of Java as Plugin, and this is not in the $PATH environment variable.
To correct this, 1) open the application Terminal; 2) type *nano ~/.bash_profile* ; 3) write at the beginning of the file the following instruction: *export PATH=/Library/Internet\ Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin/:$PATH* (there is a SPACE after *Internet\*) ; 4) hold the button *Control* while pressing the button *x* ; 5) press the button *y* (Yes) ; Press the button *Enter* / *Return* ; 5) close Terminal. To test: start Terminal and type *java -version*. It should report a version above 1.6. As of the time this README was written, it prints *java version "1.8.0_66"*. This procedure is required for the first time only. 
- Download AstroJournal, unzip the file, and enter the application folder.
- Enter the folder *target*. 
- Click *astrojournal-x.x.x-jar-with-dependencies.jar*
- Mac OS X will ask for permissions to execute the file. Answer *Yes*. This may require the user to disable special controls in Mac OS X in *System Preference > Security & Privacy*. In particular at least the radio box *Mac App Store and identified developers* should be selected. 
- AstroJournal should start correctly now. 


### Download
The latest stable version of this software application can be downloaded here:

[https://github.com/pdp10/AstroJournal/zipball/master](https://github.com/pdp10/AstroJournal/zipball/master)

After downloading and uncompressing the file, run AstroJournal by typing:
```
# On GNU/Linux or Mac OS X run AstroJournal typing (or clicking):
./astrojournal.sh
```
or 
```
# On Windows, click:
astrojournal.exe
```
This will start a simple graphical user interface to generate the journals.

The user manual for the software astroJournal can be downloaded [here](https://github.com/pdp10/AstroJournal/blob/develop/doc/user_manual.pdf)



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
