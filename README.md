
# AstroJournal

Author: Piero Dalle Pezze

Mailing list: astrojournal AT googlegroups.com

Forum: [https://groups.google.com/forum/#!forum/astrojournal](https://groups.google.com/forum/#!forum/astrojournal)

[![Build Status](https://travis-ci.org/pdp10/astrojournal.svg?branch=master)](https://travis-ci.org/pdp10/astrojournal)

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)


### Description
This software utility aims to generate LaTeX / PDF structured documents from astronomy observation reports created as basic tables.

##### Main Features
- Generation of a PDF document containing all user observation reports collected by increasing target catalogue number. This is useful for comparing targets observed over time.
- Generation of a PDF document containing all user observation reports collected by decreasing date. This is useful for visualising one’s observations by session.
- Generation of a PDF document containing the targets observed by constellation. This is useful for checking observed and unobserved targets by constellation.
- Generation of a TXT document containing all user observation reports collected by decreasing date. This is useful for copying and pasting reports with any text editor.
- Extraction of basic statistics from the reports.

### Download and Run
The latest stable release of AstroJournal can be downloaded [here](https://github.com/pdp10/AstroJournal/releases/latest). 

A user manual is provided with the package in the doc folder.

After downloading and uncompressing the file, open the folder and run AstroJournal as follows:
```
# On GNU/Linux or Mac OS X type or click:
./astrojournal.sh
```
or 
```
# On Windows, click:
astrojournal.exe
```
This will start a simple graphical user interface to generate the journals. To run AstroJournal via command line, 
```
# On GNU/Linux or Mac OS X type:
./astrojournal.sh -c
```
Please use the option -h (--help) to see the available options.


### Requirements
To use AstroJournal you need to install:

- [Java 1.7+](https://java.com/en/download/)

- [TeX Live](http://www.tug.org/texlive/) (for GNU/Linux users only)
 
- [MikTeX](http://miktex.org/download) (for Windows users only)

- [MacTeX](https://tug.org/mactex/) (for Mac OS X users only)


#### Additional notes for installing AstroJournal dependencies:

##### GNU/Linux Debian/Ubuntu 14.04+
Users can install the LaTeX dependencies required by AstroJournal with the following command:
```
sudo apt-get --no-install-recommends install texlive-latex-base texlive-latex-recommended
```
The creation of a deb package is currently in progress.

##### Windows
Users should install MikTeX and then the LaTeX packages *url* and *mptopdf* using MikTeX Manager.

##### Mac OS X
Users should install MacTeX. I have not tested this directly. If the command *pdflatex* is not available, I think it should be possible to create a link called *pdflatex* to the corresponding program used by MacTeX to compile LaTeX code.
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
