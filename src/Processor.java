import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;
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
   public static void classComp(String command) throws Exception{
      Process proc = Runtime.getRuntime().exec(command);
      proc.waitFor();
      if (proc.exitValue() != 0){
         throw new Exception("Process failed to compile");
      }
   }

   public static Process procRun(String command) throws Exception{
       Process proc = Runtime.getRuntime().exec(command);
       return proc;
   }

   private static void skipBlankLines(Scanner in){
      while(in.hasNextLine()){
         String line = in.nextLine();
         if(line.length() == 0)
            continue;
      }
   }

   // public static void loader(PrintWriter pw, Scanner sc, String file) throws FileNotFoundException{
   //    Scanner inFile = new Scanner(new FileInputStream(file));
   //    int index = 0;

   //    while(inFile.hasNext()){
   //        if(inFile.next().contains(".")){
   //             pw.printf("sys.write " + index + " " + inFile.nextInt() + "\n");
   //             pw.flush();
   //             System.out.println(sc.nextLine()); //Prints response from child process
   //             inFile.nextLine(); //Ignores rest of line
   //             index++;
   //        }
   //        else{
   //          pw.printf("write " + index + " " + inFile.nextInt() + "\n");
   //          pw.flush();
   //          System.out.println(sc.nextLine()); //Prints response from child process
   //          inFile.nextLine(); //Ignores rest of line
   //          index++;
   //        }
   //    }
   // }

   public static void main(String args[]) throws Exception{

      try{
         String src = System.getProperty("user.dir");
         classComp("javac -cp " + src + " " + src + "\\Memory.java");

         Process memory = procRun("java -cp " + src + " " + "Memory");
         InputStream is = memory.getInputStream();
         PrintWriter pw = new PrintWriter(memory.getOutputStream());

         if (args.length < 1){
            throw new FileNotFoundException("No file to be fed into memory");
         }

         Scanner outputChild = new Scanner(is);
         Scanner inFile = new Scanner(new FileInputStream(args[0]));
      
         int index = 0; //Index for both user and kernel mode
         int userIndex = 0; //Index for only user mode
         boolean sysMode, userMode; //Flag to indicate for write/read on sys or user mode
         String pattern = "\\..|\\...|\\....|\\.....";

      while(inFile.hasNext()){
          if(inFile.hasNext(pattern)){ //Causes the loader to change the load address
               index = Integer.parseInt(inFile.next().substring(1));
               inFile.nextLine(); //Ignores rest of line

               if(index >= 0 && index <= 999){ //User mode
                  userMode = true;
                  while(userMode){
                     if(inFile.hasNext(pattern) || !inFile.hasNext()){ //Check for user/kernel switch mode or eof
                        userMode = false;
                     }
                     else{
                        pw.print("write " + index + " " + inFile.nextInt() + "\n");
                        pw.flush();
                        System.out.println(outputChild.nextLine()); //Prints response from child process
                        inFile.nextLine();
                        index++;
                     }
                  }
               }

               if(index >= 1000 && index <= 1999){ //Kernel mode
                  sysMode = true;
                  while(sysMode){
                     if(inFile.hasNext(pattern) || !inFile.hasNext()){ //Check for user/kernel switch mode or eof
                        sysMode = false;
                     }
                     else{
                        pw.print("write " + index + " " + inFile.nextInt() + "\n");
                        pw.flush();
                        System.out.println(outputChild.nextLine()); //Prints response from child process
                        inFile.nextLine();
                        index++;
                     }
                  }
               }
          }
          else if (inFile.hasNextInt()){ //Starts to write on user memory
            pw.print("write " + userIndex + " " + inFile.nextInt() + "\n");
            pw.flush();
            System.out.println(outputChild.nextLine()); //Prints response from child process
            inFile.nextLine(); //Ignores rest of line
            userIndex++;
          }
          else{
            System.out.print(inFile.nextLine()); //Ignores rest of line
          }
      }
         //memory.waitFor();
      }
      catch(Throwable t){
         t.printStackTrace();
      }
   }
      
}

