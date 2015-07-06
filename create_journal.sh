#!/bin/bash

input_reports_folder="tsv_reports"
output_reports_folder="latex_reports"
input_catalogues_folder="tsv_catalogues"
output_catalogues_folder="latex_catalogues"
aj_latex_file="astrojournal.tex"


# Check if input_reports_folder exists
if [ ! -d ${input_reports_folder} ]; then
    printf "Folder ${input_reports_folder} does not exist. Aborting.\n";
    exit 1;
fi

# Check if output_reports_folder exists
if [ ! -d ${output_reports_folder} ]; then
    printf "Folder ${output_reports_folder} does not exist. Aborting.\n";
    exit 1;
fi

# Check if input_catalogues_folder exists
if [ ! -d ${input_catalogues_folder} ]; then
    printf "Folder ${input_catalogues_folder} does not exist. Aborting.\n";
    exit 1;
fi

# Check if output_catalogues_folder exists
if [ ! -d ${output_catalogues_folder} ]; then
    printf "Folder ${output_catalogues_folder} does not exist. Aborting.\n";
    exit 1;
fi




# Remove previous aj_latex_file file 
rm -f ${aj_latex_file}




# Run AstroJournal and generate the Latex code
java -jar astrojournal-*.jar ${input_reports_folder} ${output_reports_folder} ${input_catalogues_folder} ${output_catalogues_folder}


# Check whether astrojournal.tex is empty (has not been created correctly)
if [ ! -s ${aj_latex_file} ]; then
    rm -f ${aj_latex_file}
    printf "File ${aj_latex_file} was not created. Aborting.\n";
    exit 1;
fi



# Generate the PDF file from the Latex code generated previously
if command -v pdflatex 2>/dev/null >&2; then {
    printf "Generating astrojournal.pdf using pdflatex ... ";
    pdflatex astrojournal.tex >/dev/null;
    pdflatex astrojournal.tex >/dev/null;
    printf "DONE\n"
} elif command -v texi2pdf 2>/dev/null >&2; then {
    printf "Generating astrojournal.pdf using texi2tex ... ";
    texi2pdf astrojournal.tex >/dev/null;
    texi2pdf astrojournal.tex >/dev/null;
    printf "DONE\n"
} else {
    printf "You need to install either pdflatex or texi2tex for generating AstroJournal PDF file. Aborting.\n";
    exit 1; 
}
fi


# Clean the temporary and log files
rm -rf *.aux *.log *~ *.out *.toc ${output_reports_folder}/*.aux ${output_catalogues_folder}/*.aux

