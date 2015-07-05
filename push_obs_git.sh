#!/bin/bash

# upload the last observation to my git repository
git add astrojournal.pdf
git add astrojournal.tex
git add latex_reports/*.tex
git add tsv_reports/*.tsv
git add latex_catalogues/*.tex
git add tsv_catalogues/*.tsv
git commit -m 'uploaded last observation and catalogue records'
git push origin master
