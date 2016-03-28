AstroJournal (GNU General Public License v3)

Copyright 2015-2016 Piero Dalle Pezze

Website: http://pdp10.github.io/AstroJournal/


### RELEASE NOTES

Unplanned

- Replace Java Swing with Java FX

v3.0.0 (under development)

- Meta data fields except Date are now optional.
- Replaced Help Contents with a user manual.
- Adopt Spring Framework for dependency injection. So far Generator and Configuration.
- Created deb package for GNU/Linux Debian. This is automatically created with maven.  
- Reports by target are now sorted by date decreasing.
- Added lunar phase in the meta information for the extended generator.

v2.0.0

After the first prototype this release is more about refactoring internal components of AstroJournal than introducing new features. A summary describing the work applied for this releases is provided below.

- Re-design of the configuration package. This now has a clear pipeline of tasks to do at initialisation. The singleton pattern was replaced by a normal class. This changes the way that modules communicate with each other, and increases flux control and safety. The configuration parameters are now stored as Java properties. Configuration files are saved in xml. Modularisation of program constants.
- Re-design of the generator package. Here a generator includes a specific set of importers and exporters. Importers and Exporters now communicate with a better and simpler data structure which allows importer and exporter extensions. Two new simpler types of generators are introduced in order to import and export less data and show compact reports. These can be useful for giving a quick overview via shorter documents. 
- Importers and exporters now are much more extensible and flexible.
- Exporters and importers are dynamically loaded.
- Increased number of configuration parameters.
- Constants are now stored in enum types.
- Improved log messages and notifications at all levels.
- Increased the number of jUnit tests and adoption of Travis-CI in development.
- Fixed several bugs.

v1.0.0

- AstroJournal prototype is functional.
- Tested on GNU/Linux Kubuntu 14.04+, Windows 7 32bit, Mac OS X 10.9.
- Added unit tests to test java properties to configure AJ.
- The command pdflatex is now tested at start up and notifies users about its installation.
- XML Configuration file replaces astrojournal.conf.
- Exporters and importers are now loaded dynamically.
- All output text is now managed with log4j for both GUI and Console.
- Text output is now structured. Error messages shown in red.
- GUI refinements.
- Improved log4j levels and junit tests.
- Added Darkness as new Observation parameter. This can be used for recording sky quality reading using meters such as SQM-L.
- Added Tango icon set to AJ menu items.
- Bug fixes & use of .travis.yml
- Use of the application launch4j for generating a Windows executable file for astrojournal. In the future this could be integrated in Maven pom.xml.
- Added user configuration file.
- Added new icons
- Added internationalisation for GUI strings.
- Added menu bar, preferences and help to the GUI.
- Improved the AJ GUI to also report the program output graphically.
- Maven is now the default software project management for AJ, replacing Ant.
- AJ now supports input parameters but also Java properties.
- Added report by constellation.
- Added report of additional catalogues in astrojournal by target.
- Added exporter to SGL report files.
- Added support for csv input files.
- Added Messier and Caldwell catalogues as reference in the end
- Improved pdf layout
- Added configuration of LaTeX header and footer from Java code
- Improved table layout
- Bug fixes
