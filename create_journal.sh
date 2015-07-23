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



input_reports_folder="tsv_reports"
output_reports_folder_by_date="latex_reports_by_date"
output_reports_folder_by_target="latex_reports_by_target"
input_catalogues_folder="tsv_catalogues"
output_catalogues_folder="latex_catalogues"
aj_latex_file_by_date="astrojournal_by_date.tex"
aj_latex_file_by_target="astrojournal_by_target.tex"


# Create these folders if they do not exist
mkdir -p ${input_reports_folder}
mkdir -p ${output_reports_folder_by_date}
mkdir -p ${output_reports_folder_by_target}
mkdir -p ${input_catalogues_folder}
mkdir -p ${output_catalogues_folder}


# Remove previous aj_latex_file file 
rm -f ${aj_latex_file_by_date}
rm -f ${aj_latex_file_by_target}


# Run AstroJournal and generate the Latex code
java -jar astrojournal-*.jar ${input_reports_folder} ${output_reports_folder_by_date} ${output_reports_folder_by_target} ${input_catalogues_folder} ${output_catalogues_folder}


# Check whether astrojournal_by_date.tex is empty (has not been created correctly)
if [ ! -s ${aj_latex_file_by_date} ]; then
    rm -f ${aj_latex_file_by_date}
    printf "File ${aj_latex_file_by_date} was not created. Aborting.\n";
    exit 1;
fi

# Check whether astrojournal_by_target.tex is empty (has not been created correctly)
if [ ! -s ${aj_latex_file_by_target} ]; then
    rm -f ${aj_latex_file_by_target}
    printf "File ${aj_latex_file_by_target} was not created. Aborting.\n";
    exit 1;
fi

# Generate the PDF file from the Latex code generated previously
if command -v pdflatex 2>/dev/null >&2; then {
    printf "Generating astrojournal by date using pdflatex ... ";
    pdflatex ${aj_latex_file_by_date} >/dev/null;
    pdflatex ${aj_latex_file_by_date} >/dev/null;
    printf "DONE\n"
} elif command -v texi2pdf 2>/dev/null >&2; then {
    printf "Generating astrojournal by date using texi2tex ... ";
    texi2pdf ${aj_latex_file_by_date} >/dev/null;
    texi2pdf ${aj_latex_file_by_date} >/dev/null;
    printf "DONE\n"
} else {
    printf "You need to install either pdflatex or texi2tex for generating AstroJournal PDF file. Aborting.\n";
    exit 1; 
}
fi


# Generate the PDF file from the Latex code generated previously
if command -v pdflatex 2>/dev/null >&2; then {
    printf "Generating astrojournal by target using pdflatex ... ";
    pdflatex ${aj_latex_file_by_target} #>/dev/null;
    pdflatex ${aj_latex_file_by_target} #>/dev/null;
    printf "DONE\n"
} elif command -v texi2pdf 2>/dev/null >&2; then {
    printf "Generating astrojournal by target using texi2tex ... ";
    texi2pdf ${aj_latex_file_by_target} >/dev/null;
    texi2pdf ${aj_latex_file_by_target} >/dev/null;
    printf "DONE\n"
} else {
    printf "You need to install either pdflatex or texi2tex for generating AstroJournal PDF file. Aborting.\n";
    exit 1; 
}
fi


# Concatenate all tsv files into one file
cat ${input_reports_folder}/*.tsv > astrojournal_by_date.tsv


# Clean the temporary and log files
rm -rf *.aux *.log *~ *.out *.toc ${output_reports_folder_by_date}/*.aux ${output_reports_folder_by_target}/*.aux ${output_catalogues_folder}/*.aux

