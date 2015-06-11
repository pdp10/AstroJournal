#!/bin/bash

input_folder="tsv_reports"
output_folder="latex_reports"
aj_latex_file="astrojournal.tex"


# Check if input_folder exists
if [ ! -d ${input_folder} ]; then
    printf "Folder ${input_folder} does not exist. Aborting.\n";
    exit 1;
fi

# Check if output_folder_exists
if [ ! -d ${output_folder} ]; then
    printf "Folder ${output_folder} does not exist. Aborting.\n";
    exit 1;
fi


# Remove previous aj_latex_file file 
rm -f ${aj_latex_file}




# Run AstroJournal and generate the Latex code
java -jar astrojournal-*.jar ${input_folder} ${output_folder}


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
    printf "DONE\n"
} elif command -v texi2pdf 2>/dev/null >&2; then {
    printf "Generating astrojournal.pdf using texi2tex ... ";
    texi2pdf astrojournal.tex >/dev/null;
    printf "DONE\n"
} else {
    printf "You need to install either pdflatex or texi2tex for generating AstroJournal PDF file. Aborting.\n";
    exit 1; 
}
fi


# Clean the temporary and log files
rm -rf *.aux *.log *~ *.out ${output_folder}/*.aux 

