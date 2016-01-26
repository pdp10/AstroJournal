<sup><sub>Author: Piero Dalle Pezze</sup></sub>

<sup><sub>Version: 1.0</sup></sub>

<sup><sub>Since: 1.0</sup></sub>

<sup><sub>Date: 25/01/2016</sup></sub>



# Developer Guide


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
This project follows the Feature-Branching model. Briefly, there are two main branches: `master` and `devel`. The former contains the history of stable releases, the latter contains the history of development. The `master` branch only serves as checkout points for production hot-fixes or as merge point for release-x.x.x branches. The `devel` branch only serves for feature/bug-fix integration and as checkout point and is is versionless. Nobody should directly develop in here.


### Conventions
- Each new feature is developed in a separate branch called featureNUMBER, where NUMBER is the number of the issue discussing this feature. The first line of each commit message for this branch should report (Issue #NUMBER) at the end before the dot. Doing so, the commit is automatically recorded by the Issue Tracking System for that specific Issue. Note that `#` is required.  
- Same for each new bug-fix, but in this case the branch name is called bugfixNUMBER.
- Same for each new hot-fix, but in this case the branch name is called hotfixNUMBER.


### Work Flow
- Each new feature is checked out from the branch `devel` we want to add functionalities / fix bugs.
- Same for new bug fixes.
- Each new hot-fix is checked out from the `master` branch.

The procedure for checking out a new feature from the `devel` branch is: 
```
$ git checkout -b feature10 devel
```
This creates the `feature10` branch off `devel`. 
When you are ready to add and commit your work, run:
```
$ git commit -am "Summary of the changes (Issue #10). Detailed description of the changes, if any."
$ git push origin feature10       # sometimes and at the end.
```

When `feature10` is completed and tested, merge this branch to `devel` WITHOUT a fast-forward, so that the history of `feature10` is also recorded (= we know that there was a branch, which is very useful for debugging). 
```
$ git pull origin devel         # update the branch devel in the local repository. Don't do this on master.
$ git checkout devel            # switch to devel
$ git merge --no-ff feature10  
```

Alternatively, use a pull request to open a discussion. 

When the integration tests are successful, then: 
```
$ git branch -d feature10      # delete the branch feature10 (locally)
```

Finally, push everything to the server:
```
$ git push origin devel
$ git push origin feature10   # if not done before
```

### New Releases:
When the `devel` branch includes all the desired feature for a release, it is time to checkout this 
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



## Project Structure: 
Not written yet! 



## Miscellaneous of Useful Commands:

### Git
##### Startup
```
$ git clone https://YOURUSERNAME@server/YOURUSERNAME/SB_pipe.git   # to clone the master
$ git checkout -b devel origin/devel                               # to get the devel branch
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

##### Reset
```
git reset --hard HEAD    # to undo all the local uncommitted changes
```
