# Developer Guide

Author: Piero Dalle Pezze

Licence: GPL v3 (2015)

Mailing list: astrojournal AT googlegroups.com

Forum: [https://groups.google.com/forum/#!forum/astrojournal](https://groups.google.com/forum/#!forum/astrojournal)


## Introduction
This guide is meant for developers of the project (AstroJournal)[https://github.com/pdp10/AstroJournal].


## Requirements
To compile AstroJournal you need to install:

- [Git](https://git-scm.com/downloads)

- [Java 1.7+](https://java.com/en/download/)

- [Maven](http://maven.apache.org/)

- [TeX Live](http://www.tug.org/texlive/) (for Linux Users) or [MikTeX](http://miktex.org/download) (for Windows Users) or [MacTeX](https://tug.org/mactex/) (pdflatex must be installed)

See README.md for installation details of LaTeX packages for GNU/Linux, Windows, and Mac OS X.


## Clone and Compile
To clone AstroJournal repository:
```
git clone https://github.com/pdp10/AstroJournal.git
```

To test the correct installation of Maven type:
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
```

To create a jar file for AstroJournal type:
```
mvn package
```

To read the source code documentation type:
```
mvn javadoc:javadoc
```

To clean the project:
```
mvn clean
```

To test AstroJournal on GNU/Linux type (run *mvn package* first):
```
./astrojournal.sh
```


## Integration Tests
A .travis.yml script is included with this project in order to perform continuous integration tests at each git push.
To use travis-ci, an account on github.com is required as now and [this guide](https://docs.travis-ci.com/user/getting-started/) 
is suggested. Information on how to fork AstroJournal can be found [here](https://help.github.com/articles/fork-a-repo/).


## Development Model
This project follows the Feature-Branching model. Briefly, there are two main branches: `master` and `develop`. The former contains the history of stable releases, the latter contains the history of development. The `master` branch only serves as checkout points for production hot-fixes or as merge point for release-x.x.x branches. The `develop` branch only serves for feature/bug-fix integration and as checkout point and is is versionless. Nobody should directly develop in here.


### Conventions
- Each new feature is developed in a separate branch called featureNUMBER, where NUMBER is the number of the issue discussing this feature. The first line of each commit message for this branch should report (Issue #NUMBER) at the end before the dot. Doing so, the commit is automatically recorded by the Issue Tracking System for that specific Issue. Note that `#` is required.  
- Same for each new bug-fix, but in this case the branch name is called bugfixNUMBER.
- Same for each new hot-fix, but in this case the branch name is called hotfixNUMBER.


### Work Flow
- Each new feature is checked out from the branch `develop` we want to add functionalities / fix bugs.
- Same for new bug fixes.
- Each new hot-fix is checked out from the `master` branch.

The procedure for checking out a new feature from the `develop` branch is: 
```
$ git checkout -b feature10 develop
```
This creates the `feature10` branch off `develop`. 
When you are ready to add and commit your work, run:
```
$ git commit -am "Summary of the changes (Issue #10). Detailed description of the changes, if any."
$ git push origin feature10       # sometimes and at the end.
```

As of June 2016, the branches `master` and `develop` are protected and a status check using Travis-CI must be performed before merging or pushing into these branches. This automatically forces a merge without fast-forward. 
In order to merge **any** new feature, bugfix or simple edits into `master` or `develop`, a developer **must** checkout a new branch and, once committed and pushed, **merge** it to `master` or `develop` using a `pull request`. To merge `feature10` to `develop`, the pull request will 
look like this:
```
base:develop  compare:feature10   Able to merge. These branches can be automatically merged.

```
A small discussion about feature10 should also be included to allow other users to understand the feature.

Finally delete the branch: 
```
$ git branch -d feature10      # delete the branch feature10 (locally)
```


### New Releases:
When the `develop` branch includes all the desired feature for a release, it is time to checkout this 
branch in a new one called `release-x.x.x`. It is at this stage that a version is established. Only bug-fixes or hot-fixes are applied to this branch. When this testing/correction phase is completed, the `master` branch will merge with the `release-x.x.x` branch, using the commands above.
To record the release add a tag:
```
git tag -a v1.3 -m "PROGRAM_NAME v1.3"
```
To transfer the tag to the remote server:
```
git push origin v1.3   # Note: it goes in a separate 'branch'
```
To see all the releases:
```
git show
```
Once a release is pushed (source code only .zip, .tar.gz), compile AstroJournal with mvn package and upload the zipped folder as executable for that specific release.



## Project Structure: 
- The AstroJournal project is built using Maven, so the main directory structure for main and test under the folder `src/` is equivalent to any other Maven project. The AstroJournal main source code is in `src/main/java/org/astrojournal`. Meta information (e.g. program name and version, website, license, etc) about the software are stored in `AJMetaInfo.java`. `AJMain.java` is the main class and initialises dependency injection using the Spring Framework. The Spring configuration file is stored in `src/main/resources/META-INF`. As of June 2016, only instances of `Configuration` and `Generator` can be injected using the Spring Framework. Injection of other classes might be added in the future. After initialising Spring `AJMain.java` executes AstroJournal via Command Line Interface (CLI) or Graphical User Interface (GUI). The code relative to the CLI is in the package `console`, while the code for the GUI is in the package `gui`. A separation between controls and logic is provided within the packages. Utility functions for AstroJournal are stored in the package `utilities`.
- AstroJournal communicates with the user using the Apache package `log4j2`. Using this package, AstroJournal sends messages regarding information, warnings and errors. Log messages are instead stored in log files and overridden at every execution of the program. In the GUI, log messages are printed with different colours to clearly distinguish them. The redirection from log4j2 to the application JTextPane is organised within the package `logging`.
- The program configuration is stored in the package `configuration`. A generic `Configuration` interface is provided, but specific configurations can be implemented and injected using the Spring Framework. The program configurations are managed using Java properties. AstroJournal configuration is stored within the application and in a configuration file within the folder `src/main/resources/`. This file is processed at program initialisation. If present, a user configuration file is processed straight after. As AstroJournal configuration is based on Java properties, the passage of program configurations using Java properties directly via command line is straight forward. To reduce coupling between the program and this configuration, no singleton pattern is adopted, but rather an instance of Configuration is passed through the classes controlling the application (e.g. GUI and Generator). 
- The management for importing, processing and converting raw observations to LaTeX files is in the package `generator`. The interface `Generator` is responsible for dynamically loading the available importers and exporters. This class is independent of the logic for importing and exporting observations, but just processes a list of `Report` objects. Each imported report is stored in an AstroJournal `Report` object. This object contains report meta data and information about the observed targets. The organisation of the logic for importing and exporting is managed with a hierarchy. The most generic classes are in the package `absgen`. As of June 2016, AstroJournal allows to process raw observations in three modes: minimal (`mini-`), basic (`basic-`) and extended (`ext-`). These modes define the amount of information to be processed from the input files and stored in LaTeX files. The package `statistics` contains the code for computing statistics from the imported reports. The package `headfoot` contains the code for generating the LaTeX header and footer. 
- Test cases are provided in `src/test/java` which uses resources from `src/test/resources`. The `resources` folders for `main` and `test` also contain the configuration for `log4j2` and the `locale` translations. 
- In the main AstroJournal directory, the folder `debian` contains files for creating a debian package for AstroJournal. The folder `latex_header_footer` contains default files for the LaTeX header and footers. The folder `raw_reports` contains examples of raw reports so that a user can see their structure.



## Miscellaneous of Useful Commands:

### Git
##### Startup
```
$ git clone https://YOURUSERNAME@server/YOURUSERNAME/SB_pipe.git   # to clone the master
$ git checkout -b develop origin/develop                               # to get the develop branch
$ for b in `git branch -r | grep -v -- '->'`; do git branch --track ${b##origin/} $b; done     # to get all the other branches
$ git fetch --all    # to update all the branches with remote
```

##### Update
```
$ git pull [--rebase] origin BRANCH  # ONLY use --rebase for private branches. Never use it for shared branches otherwise it breaks the history. --rebase moves your commits ahead. I think for shared branches, you should use `git fetch && git merge --no-ff`. **[FOR NOW, DON'T USE REBASE BEFORE AGREED]**.
```

##### File System
```
$ git rm [--cache] filename 
$ git add filename
```

##### Information
```
$ git status 
$ git log [--stat]
$ git branch       # list the branches
```

##### Maintenance
```
$ git fsck      # check errors
$ git gc        # clean up
```

##### Rename a branch locally and remotely
```
git branch -m old_branch new_branch         # Rename branch locally    
git push origin :old_branch                 # Delete the old branch    
git push --set-upstream origin new_branch   # Push the new branch, set local branch to track the new remote
```

##### Reset
```
git reset --hard HEAD    # to undo all the local uncommitted changes
```

##### Syncing a fork (assumes upstreams are set)
```
git fetch upstream
git checkout develop
git merge upstream/develop
```

### Debian package 

##### Man page
A man page for astrojournal is debian/astrojournal.1 . To uncompress it, use 
'''gunzip''', to compress it use '''gzip -9'''. Without the option '''-9''', 
Lintian throws the exception: manpage-not-compressed-with-max-compression . 

##### Changelog 
This is debian/changelog and must also be compressed before running '''mvn package'''.

##### Copyright
This is debian/copyright. If new dependencies are required, edit this file too. Keep 
synchronised the file copyright in the main directory too.

##### Control 
This is debian/control, but also src/deb/control. The two must be synchronised. 
jdeb uses src/deb, but debian-helper-maven uses debian/ .
