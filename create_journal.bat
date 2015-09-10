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

rem WARNING! THIS SCRIPT HAS NOT BEEN TESTED! 

echo on

set input_reports_folder="tsv_reports"
set output_reports_folder_by_date="latex_reports_by_date"
set output_reports_folder_by_target="latex_reports_by_target"
set aj_latex_file_by_date="astrojournal_by_date.tex"
set aj_latex_file_by_target="astrojournal_by_target.tex"


rem Create these folders if they do not exist
mkdir %input_reports_folder%
mkdir %output_reports_folder_by_date%
mkdir %output_reports_folder_by_target%


rem  rem Remove previous aj_latex_file file 
rem  rm -f %aj_latex_file_by_date%
rem  rm -f %aj_latex_file_by_target%
rem  
rem  
rem  rem Run AstroJournal and generate the Latex code
rem  java -jar astrojournal-*.jar %input_reports_folder% %output_reports_folder_by_date% %output_reports_folder_by_target%
rem  
rem  
rem  rem Check whether astrojournal_by_date.tex is empty (has not been created correctly)
rem  if not exist %aj_latex_file_by_date% (
rem      rm -f %aj_latex_file_by_date%
rem      echo "WARNING: File %aj_latex_file_by_date% was not created."
rem  ) else (
rem      rem Generate the PDF file from the Latex code generated previously
rem      where pdflatex >nul
rem      if %ERRORLEVEL% EQ 0 (
rem  	echo "Generating astrojournal by date using pdflatex ... "
rem  	pdflatex %aj_latex_file_by_date% 
rem  	pdflatex %aj_latex_file_by_date% 
rem  	echo "DONE"
rem      ) else (
rem          where texi2pdf >nul
rem          if %ERRORLEVEL% EQ 0 (
rem              echo "Generating astrojournal by date using texi2tex ... "
rem              texi2pdf %aj_latex_file_by_date% 
rem              texi2pdf %aj_latex_file_by_date% 
rem              echo "DONE"
rem          ) else (
rem              echo "You need to install either pdflatex or texi2tex for generating AstroJournal PDF file."
rem          )
rem      )
rem  )
rem  
rem  
rem  
rem  rem Check whether astrojournal_by_target.tex is empty (has not been created correctly)
rem  if not exist %aj_latex_file_by_target% (
rem      rm -f %aj_latex_file_by_target%
rem      echo "WARNING: File %aj_latex_file_by_target% was not created. "
rem  ) else (
rem      rem Generate the PDF file from the Latex code generated previously
rem      where pdflatex >nul
rem      if %ERRORLEVEL% EQ 0 (
rem          echo "Generating astrojournal by target using pdflatex ... "
rem          pdflatex %aj_latex_file_by_target% 
rem          pdflatex %aj_latex_file_by_target% 
rem          echo "DONE"
rem      ) else (
rem          where texi2pdf >nul
rem          if %ERRORLEVEL% EQ 0 (
rem              echo "Generating astrojournal by target using texi2tex ... "
rem              texi2pdf %aj_latex_file_by_target% 
rem              texi2pdf %aj_latex_file_by_target% 
rem              echo "DONE"
rem          ) else (
rem              echo "You need to install either pdflatex or texi2tex for generating AstroJournal PDF file."
rem          )
rem      )
rem  )


java -jar astrojournal-*.jar


rem Concatenate all tsv files into one file
type %input_reports_folder%\*.tsv > astrojournal_by_date.tsv

rem Clean the temporary and log files
rd /q /s *.aux *.log *.out *.toc %output_reports_folder_by_date%\*.aux %output_reports_folder_by_target%\*.aux
