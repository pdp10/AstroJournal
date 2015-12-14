
rem Copyright 2015 Piero Dalle Pezze

rem This file is part of AstroJournal.


rem AstroJournal is free software; you can redistribute it and/or modify

rem it under the terms of the GNU General Public License as published by

rem the Free Software Foundation; either version 3 of the License, or

rem (at your option) any later version.


rem This program is distributed in the hope that it will be useful,

rem but WITHOUT ANY WARRANTY; without even the implied warranty of

rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

rem GNU General Public License for more details.


rem You should have received a copy of the GNU General Public License

rem along with this program; if not, write to the Free Software

rem Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA


rem echo on

rd astrojournal_by_date.pdf astrojournal_by_target.pdf astrojournal_by_constellation.pdf
  

rem Run AstroJournal and generate the Latex code
java -jar target\astrojournal-0.9-jar-with-dependencies.jar 

rem> astrojournal_output.txt


rem Clean the temporary and log files

rem rd /q /s *.aux *.log *.out *.toc %output_reports_folder_by_date%\*.aux %output_reports_folder_by_target%\*.aux %output_reports_folder_by_constellation%\*.aux


