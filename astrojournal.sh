#!/bin/bash
#
# Copyright 2015 Piero Dalle Pezze
#
# This file is part of AstroJournal.
#
# AstroJournal is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA


# Note: AstroJournal also accepts -D java options (e.g. aj.raw_reports) 
# as input parameters.

application=astrojournal


# Check whether the application is not installed
if ! [ -x "$(command -v $application)" ]; 
then
   # use the local version.
   cd $(dirname $0)
   java -jar target/${application}-*-jar-with-dependencies.jar $1 $2
else
   # the symbolic link was found
   # the application is installed on this Linux machine.
   target=$(/bin/readlink -f $(which $application))
   target=$(dirname $target)
   java -jar $target/target/${application}-*-jar-with-dependencies.jar $1 $2
fi



