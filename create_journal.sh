#!/bin/bash


# Run AstroJournal and generate the Latex code
java -jar astrojournal-*.jar tsv_files observations


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
    printf "You need to install either pdflatex or texi2tex for generating AstroReport PDF file. Aborting.\n";
    exit 1; 
}
fi


# Clean the temporary and log files
rm -rf *.aux *.log *~ observations/*.aux

