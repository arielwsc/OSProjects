import java.io.*;
import java.lang.Runtime;

public class Proc 
{
   public static void main(String args[])
   {
      try
      {            
	 int x;
	 Runtime rt = Runtime.getRuntime();
    String src = "C:\\Users\\ariel\\Documents\\SE4348OS\\Project1\\src";

	 Process procComp = rt.exec("javac -cp " + src + " " + src + "\\Hello.java");
    Process procRun = rt.exec("java -cp " + src + " " + "Hello");
	 InputStream is = procRun.getInputStream();
	 OutputStream os = procRun.getOutputStream();
    System.out.println(is.available());
	 while ((x=is.read()) != -1)
	    System.out.println((char)x); 
	      
	 procRun.waitFor();
         System.out.println("procRun exited: " + procRun.exitValue());

      } 
      catch (Throwable t)
      {
	 t.printStackTrace();
      }
   }
}

