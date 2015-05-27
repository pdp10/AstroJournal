#!/bin/bash

# upload the last observation to my git repository
git add astrojournal.pdf
git add astrojournal.tex
git add observations/*.tex
git add tsv_files/*.tsv
git commit -m 'uploaded last observation'
git push origin master
