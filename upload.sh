#!/bin/bash

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
 


# upload the last observation to my git repository
git add astrojournal.pdf
git add astrojournal.tex
git add astrojournal.tsv
git add latex_header_footer/*.tex
git add latex_reports/*.tex
git add latex_catalogues/*.tex
git add tsv_reports/*.tsv
git add tsv_catalogues/*.tsv
git commit -m 'uploaded last observation and catalogue records'
git push origin master


# upload the pdf to the website

