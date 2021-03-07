import java.io.*;
import java.util.Scanner;
import java.lang.Runtime;

public class Processor 
{
   /*
   PC = Program Counter
   SP = Stack Pointer
   IR = Instruction Register
   AC = Accumulator
   X
   Y
   */

   public static void main(String args[]) throws Exception{

      try{
         String src = System.getProperty("user.dir");
         Process memoryComp = Runtime.getRuntime().exec("javac -cp " + src + " " + src + "\\Memory.java");
         System.out.println(memoryComp.waitFor());

         Process memoryRun = Runtime.getRuntime().exec("java -cp " + src + " " + "Memory");
         
         InputStream is = memoryRun.getInputStream();
         PrintWriter pw = new PrintWriter(memoryRun.getOutputStream());
         Scanner input = new Scanner(System.in);
         Scanner outputChild = new Scanner(is);

         while(true){
            System.out.println("Print command for memory process: ");
            pw.printf(input.nextLine() + "\n");
            pw.flush();

            System.out.println(outputChild.nextLine());
         }
         
      }
      catch(Throwable t){
         t.printStackTrace();
      }
   }
      
}

