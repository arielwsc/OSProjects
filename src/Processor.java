import java.io.*;
import java.util.Scanner;
import java.lang.Runtime;

public class Processor 
{
   public static void main(String args[]){

      try{
         String src = System.getProperty("user.dir");
         System.out.println("javac -cp " + src + " " + "\'Memory.java");
         Process memoryComp = Runtime.getRuntime().exec("javac -cp " + src + " " + src + "\\Memory.java");
         Scanner input = new Scanner(System.in);
         String command;
         int x;

         while(true){
            System.out.println("Enter command for memory: ");
            command = input.nextLine();

            Process memoryRun = Runtime.getRuntime().exec("java -cp " + src + " " + "Memory " + command);
            BufferedReader is = new BufferedReader(new InputStreamReader(memoryRun.getInputStream()));

            System.out.println(is.readLine());
         }
      }
      catch(Throwable t){
         t.printStackTrace();
      }
   }
      
}

