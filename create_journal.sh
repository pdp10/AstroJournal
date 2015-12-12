#!/bin/bash
#
# This file is part of AstroJournal.
#
# AstroJournal is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# AstroJournal is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with AstroJournal.  If not, see <http://www.gnu.org/licenses/>.


LIB="lib";


input_report_folder="./raw_reports"
output_reports_folder_by_date="./latex_reports_by_date"
output_reports_folder_by_target="./latex_reports_by_target"
output_reports_folder_by_constellation="./latex_reports_by_constellation"
output_reports_folder_by_date_sgl="./sgl_reports_by_date"


#params="${input_report_folder} ${output_reports_folder_by_date} ${output_reports_folder_by_target} ${output_reports_folder_by_constellation} ${output_reports_folder_by_date_sgl}"
# Note: it is also possible to set up the folders as -D java options (e.g. aj.raw_reports)

for i in $LIB/*.jar; do
    CLASSPATH=$CLASSPATH:$i
done
CLASSPATH=`echo $CLASSPATH | cut -c2-`


# Run AstroJournal and generate the Latex code
java -jar astrojournal-*.jar > astrojournal_output.txt

# Clean the temporary and log files
rm -rf *.aux *.log *~ *.out *.toc ${output_reports_folder_by_date}/*.aux ${output_reports_folder_by_target}/*.aux ${output_reports_folder_by_target}/*.aux

