AstroJournal (GNU General Public License v3)

Copyright 2015-2016 Piero Dalle Pezze

Website: http://pdp10.github.io/AstroJournal/


### RELEASE NOTES

Future

- Replace Java Swing with Java FX
- Adopt Spring dependency injection for the modules which would benefit from this.

v2.0.1

- Added lunar phase in the meta information for the extended generator.

v2.0.0

After the first prototype this release is more about refactoring internal components of AstroJournal than introducing new features. This redesign allows for easier extensibility in the long term, reduces module coupling and increases flexibility. If desired, dependency injection (DI) can be introduced in the code. A new more structured developing model is now adopted. 

A summary describing the work applied for this releases is provided below.

- Re-design of the configuration package. This now has a clear pipeline of tasks to do at initialisation. The singleton pattern was replaced by a normal class. This changes the way that modules communicate with each other, and increases flux control and safety. The configuration parameters are now stored as Java properties. Configuration files are saved in xml. Modularisation of program constants.
- Re-design of the generator package. Here a generator includes a specific set of importers and exporters. Importers and Exporters now communicate with a better and simpler data structure which allows importer and exporter extensions. Two new simpler types of generators are introduced in order to import and export less data and show compact reports. These can be useful for giving a quick overview via shorter documents. Importers and exporters now are much more extensible and flexible.
- Exporters and importers are dynamically loaded.
- Increased number of configuration parameters.
- Constants are now stored in enum types.
- Improved log messages and notifications at all levels.
- Increased the number of jUnit tests and adoption of Travis-CI in development.
- Fixed several bugs.


v1.0.0

- AstroJournal prototype is functional.

v0.10.14

- Tested on GNU/Linux Kubuntu 14.04+, Windows 7 32bit, Mac OS X 10.9 (See README.md for instruction on how to 
install a more recent Java version to run AstroJournal). 
- Added unit tests to test java properties to configure AJ.
- The command pdflatex is now tested at start up and notifies users about its installation.
- XML Configuration file replaces astrojournal.conf.
- Exporters and importers are now loaded dynamically.
- All output text is now managed with log4j for both GUI and Console.
- Text output is now structured. Error messages shown in red.
- GUI refinements.
- Improved log4j levels and junit tests.
- Integrated launch4j with maven pom.xml
- Migrated from apache log4j 1.2 to 2
- Migrated from junit 4.11 to 4.12
- Added Darkness as new Observation parameter. This can be used for recording sky quality reading using meters such as SQM-L.
- Added Tango icon set to AJ menu items.
- Bug fixes & use of .travis.yml

v0.9.12

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

v0.8.8

- Input/output folders can also be passed as Java options.
- Added report by constellation.
- Added report of additional catalogues in astrojournal by target.
- Added exporter to SGL report files.
- Corrected bug in astrojournal.bat
- Added support for csv input files.
- Reorganisation of the package generator.
- Design of the main graphic user interface (currently not connected to the program).

v0.7.4

- Create file run_astrojournal.bat (for Windows Users) (not tested yet).
- Removed the field PowerExitPupilFOV from the session reports. The eyepiece information is now inserted in the file legends.tex included at header level.
- Removed catalogue tsv and latex folder. List of observed targets is the list of contents for the second generated pdf (by_target)
- Export observations by target

v0.6.2

- Imported list of objects observed by Catalogue.
- Added list of contents, sections, moved to Article class

v0.5.8

- Added Messier and Caldwell catalogues as reference in the end
- Improved pdf layout
- Extracted LaTeX header and footer from Java code
- Added additional controls
- Bug fixes
- Increased code modularity
- Renamed observations/ and tsv_files/ into latex_reports/ and tsv_reports/
- Source code cleaning

v0.4.11

- Wiki links point to the last version directly. No need to update each time 
- Simplified scripts for creating journal
- Renamed this software 'AstroJournal'
- Added controls before using pdflatex or texi2tex
- Imported LaTeX code for observations prior to the log
- Reversed the observations (now most recent first)
- Renamed .tex and .pdf to lowercase names
- Improved legends layout
- Bug fixes
- Improved table layout
- Simplified build.xml

v0.3.3

- Add latex command for showing tables on multiple pages 
( http://users.sdsc.edu/~ssmallen/latex/longtable.html )
- Draw of your own images to replace the existing ones. However, I 
decided to omit images so the pdf is lighter and faster to load on the browser
- Use tsv_files and observations outside of dist/ . Not necessary to copy 
these two folders into dist/ . Therefore, you only keep your data in one 
place. Simplified build.xml to have jar file in the current directory

v0.2.4

- Improved code in Observation.java and ObservationItem.java
- Input/output folders tsv_files/ and observations/ passed as parameters
- Simplified tsv file structure
- Corrected importation of multiple observations per day

v0.1.2

- Creation of LaTeX code for all your observation
- Reading of different tsv files sequentially and ordered by date increasing 
is supported. In this way, you can create an observation log per month or 
two (keeping the google spreadsheet short), and add the exported tsv file 
to the folder tsv_files

v0.1.0

- Project creation.
