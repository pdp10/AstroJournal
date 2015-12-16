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


# AstroJournal output directories
output_reports_folder_by_date="./latex_reports_by_date"
output_reports_folder_by_target="./latex_reports_by_target"
output_reports_folder_by_constellation="./latex_reports_by_constellation"

# Clean the LaTeX temporary and log files
rm -rf *.aux *.log *~ *.out *.toc ${output_reports_folder_by_date}/*.aux ${output_reports_folder_by_target}/*.aux ${output_reports_folder_by_target}/*.aux

# clean output files
rm -rf astrojournal_by* astrojournal_output.txt latex_reports* sgl_reports*
