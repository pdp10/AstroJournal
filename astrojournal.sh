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

# the folder containing the script becomes root.
cd $(dirname $0)


# Note: AstroJournal also accepts -D java options (e.g. aj.raw_reports) 
# as input parameters.

# Run AstroJournal and generate the Latex code
java -jar target/astrojournal-*-jar-with-dependencies.jar $1 $2


# Clean the temporary and log files
rm -rf *.aux *.log *~ *.out *.toc ${output_reports_folder_by_date}/*.aux ${output_reports_folder_by_target}/*.aux ${output_reports_folder_by_target}/*.aux

