// Source code extracted from SeqMonk and edited.
// A Java launcher which is aware of local memory and OS type
using System;
using System.Diagnostics;
using System.Globalization;
using System.Text;
using System.Threading;
using System.Windows.Forms;

// Compile command is csc /reference:Microsoft.VisualBasic.dll /win32icon:astrojournal.ico AstroJournal.cs

namespace AstroJournalLauncher
{
    class AstroJournal
    {
        static void Main()
        {
            string javaVersion = getJavaVersion();

            if (!javaVersion.Contains("Java"))
            {
                MessageBox.Show("Could not find java on your system", "Failed to launch AstroJournal", MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
                Environment.Exit(1);
            }

            int memoryCeiling = 250;

            if (javaVersion.Contains("64-Bit"))
            {
                memoryCeiling = 500;
                Console.WriteLine("Found 64-bit JVM, setting memory ceiling to 500m");
            }
            else
            {
                Console.WriteLine("Found 32-bit JVM, setting memory ceiling to 250m");
            }


            int physicalMemory = getPhysicalMemory();

            Console.WriteLine("Physical memory installed is " + physicalMemory);

            if (physicalMemory < 200)
            {
                MessageBox.Show("Not enough memory to run AstroJournal (you need at least 200MB)", "Failed to launch AstroJournal", MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
                Environment.Exit(1);
            }

            int memoryToUse = (physicalMemory * 2) / 3;

            if (memoryToUse > memoryCeiling)
            {
                memoryToUse = memoryCeiling;
            }

            if (memoryToUse == 0)
            {
                MessageBox.Show("AstroJournal process failed to start. Please, check astrojournal.exe is inside AstroJournal directory", "Failed to launch AstroJournal", MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
                Environment.Exit(1);
            }

            Console.Write("Amount of memory to use is ");
            Console.Write(memoryToUse);
            Console.WriteLine("");

            try
            {

                string path = System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().GetName().CodeBase);

                if (path.StartsWith("file:\\"))
                {
                    path = path.Substring(6);
                }

                // UNC paths will not have a drive letter so we need to prepend these with
                // a pair of slashes

                if (!path.Substring(1, 1).Equals(":"))
                {
                    path = "\\\\" + path;
                }

                string finalCommand = "java -cp \"" + path + "\" -Xss4m -Xmx" + memoryToUse + "-jar target/astrojournal-0.9-jar-with-dependencies.jar";

                Console.WriteLine("Final command is " + finalCommand);
                System.Diagnostics.ProcessStartInfo procStartInfo = new System.Diagnostics.ProcessStartInfo("java -cp \"" + path + "\" -Xss4m -Xmx" + memoryToUse + "-jar target/astrojournal-0.9-jar-with-dependencies.jar");

                procStartInfo.RedirectStandardOutput = true;
                procStartInfo.UseShellExecute = false;
                procStartInfo.CreateNoWindow = false;
                System.Diagnostics.Process proc = new System.Diagnostics.Process();
                proc.StartInfo = procStartInfo;
                proc.Start();
                string result = proc.StandardOutput.ReadToEnd();
                Console.WriteLine(result);
            }
            catch (Exception objException)
            {
                Console.WriteLine(objException.ToString());
            }

        }

        static int getPhysicalMemory()
        {
            Microsoft.VisualBasic.Devices.ComputerInfo computer = new Microsoft.VisualBasic.Devices.ComputerInfo();
            ulong rawMemory = computer.TotalPhysicalMemory;

            rawMemory /= (1024 * 1024); // Get value in MB

            return (int)rawMemory;

        }

        static string getJavaVersion()
        {
            try
            {
                string parms = @"-version";
                string output = "";
                string error = string.Empty;

                ProcessStartInfo psi = new ProcessStartInfo("java.exe", parms);

                psi.RedirectStandardOutput = true;
                psi.RedirectStandardError = true;
                psi.WindowStyle = System.Diagnostics.ProcessWindowStyle.Normal;
                psi.UseShellExecute = false;
                System.Diagnostics.Process reg;
                reg = System.Diagnostics.Process.Start(psi);
                using (System.IO.StreamReader myOutput = reg.StandardOutput)
                {
                    output = myOutput.ReadToEnd();
                }
                using (System.IO.StreamReader myError = reg.StandardError)
                {
                    error = myError.ReadToEnd();
                }

                return error;
            }
            catch (Exception objException)
            {
                Console.WriteLine(objException.ToString());
                return "";
            }
        }

    }
}