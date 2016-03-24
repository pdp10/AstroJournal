# Packaging for GNU/Linux Debian:


### Install: 
$ sudo apt-get install maven-debian-helper apt-file devscripts


### Add to ~/.bashrc:
 # maven-debian-helper
export DEBFULLNAME="Piero Dalle Pezze"
export DEBEMAIL="piero.dallepezze@gmail.com"


### Run: 
$ source ~/.bashrc
$ apt-file update
$ cd path/to/project/folder
$ mh_make

