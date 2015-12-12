rem This file is part of AstroJournal.

rem

rem AstroJournal is free software: you can redistribute it and/or modify

rem it under the terms of the GNU General Public License as published by

rem the Free Software Foundation, either version 3 of the License, or

rem (at your option) any later version.

rem

rem AstroJournal is distributed in the hope that it will be useful,

rem but WITHOUT ANY WARRANTY; without even the implied warranty of

rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

rem GNU General Public License for more details.

rem

rem You should have received a copy of the GNU General Public License

rem along with AstroJournal.  If not, see <http://www.gnu.org/licenses/>.


rem echo on

rd astrojournal_by_date.pdf astrojournal_by_target.pdf astrojournal_by_constellation.pdf
  

rem Run AstroJournal and generate the Latex code
java -jar astrojournal-0.8.jar 

rem> astrojournal_output.txt


rem Clean the temporary and log files

rem rd /q /s *.aux *.log *.out *.toc %output_reports_folder_by_date%\*.aux %output_reports_folder_by_target%\*.aux %output_reports_folder_by_constellation%\*.aux


