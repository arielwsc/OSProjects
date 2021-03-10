import java.io.*;
import java.util.Random;
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

   public static void loader(PrintWriter pw, InputStream is, String file) throws FileNotFoundException{
      Scanner sc = new Scanner(is);
      Scanner inFile = new Scanner(new FileInputStream(file));
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
                       System.out.println(sc.nextLine()); //Prints response from child process
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
                       System.out.println(sc.nextLine()); //Prints response from child process
                       inFile.nextLine();
                       index++;
                    }
                 }
              }
         }
         else if (inFile.hasNextInt()){ //Starts to write on user memory
           pw.print("write " + userIndex + " " + inFile.nextInt() + "\n");
           pw.flush();
           System.out.println(sc.nextLine()); //Prints response from child process
           inFile.nextLine(); //Ignores rest of line
           userIndex++;
         }
         else{
           inFile.nextLine(); //Ignores rest of line
         }
      }
   }

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

         loader(pw, is, args[0]);

         //CPU Registers:
         int OP;
         int PC = 0;
         int SP = 999;
         int address = 0;
         int IR = 0;
         int AC = 0;
         int X = 0;
         int Y = 0;

         int timer = Integer.parseInt(args[1]);
         Scanner sc = new Scanner(is);
         Random rand = new Random();

         while(IR != 50){
            if (PC == timer){
               System.out.println("PC = timer!!!");
               pw.println("write " + 1999 + " " + SP); //Save SP onto sys stack
               pw.flush();
               sc.next();
               sc.nextLine();
               pw.println("write " + 1998 + " " + PC); //Save PC onto sys stack
               pw.flush();
               sc.next();
               sc.nextLine();
               PC = 0; //Reset timer
               address = 1000;
               SP = 1997;
            }

            pw.println("read " + address);
            pw.flush();
            IR = Integer.parseInt(sc.next());

            switch(IR){
               case 1:
                  pw.println("read " + (++address));
                  pw.flush();
                  AC = Integer.parseInt(sc.next()); //Load the value into the AC
                  address++;
                  PC++;
                  break;
               case 2:
                  AC = address; //Load the value at the address into the AC
                  address++;
                  PC++;
                  break;
               case 3:
                  pw.println("read " + (++address));
                  pw.flush();
                  OP = Integer.parseInt(sc.next()); //Load the value from the address found in the given address into the AC
                  pw.println("read " + OP);
                  pw.flush();
                  OP = Integer.parseInt(sc.next());
                  pw.println("read " + OP);
                  pw.flush();
                  AC = Integer.parseInt(sc.next());
                  address++;
                  PC++;
                  break;
               case 4:
                  pw.println("read " + (++address));
                  pw.flush();
                  OP = Integer.parseInt(sc.next());
                  pw.println("read " + (OP + X));
                  pw.flush();
                  AC = Integer.parseInt(sc.next()); //Load the value at (addr + X) into AC
                  address++;
                  PC++;
                  break;
               case 5:
                  pw.println("read " + (++address));
                  pw.flush();
                  OP = Integer.parseInt(sc.next());
                  pw.println("read " + (OP + Y));
                  pw.flush();
                  AC = Integer.parseInt(sc.next()); //Load the value at (addr + X) into AC
                  address++;
                  PC++;
                  break;
               case 6:
                  pw.println("read " + (SP + X));
                  pw.flush();
                  AC = Integer.parseInt(sc.next()); //Load from (SP + X) into the AC
                  address++;
                  PC++;
                  break;
               case 7:
                  address = AC; //Store the value in the AC into the address
                  PC++;
                  break;
               case 8:
                  AC = rand.nextInt(101); //Gets a random int from 1 to 100 into the AC
                  address++;
                  PC++;
                  break;
               case 9:
                  pw.println("read " + (++address));
                  pw.flush();
                  OP = Integer.parseInt(sc.next());
                  if (OP == 1) System.out.print(AC); //If port=1, writes AC as an int to the screen
                  if (OP == 2) System.out.print((char)AC); //If port=2, writes AC as an int to the screen
                  address++;
                  PC++;
                  break;
               case 10:
                  AC += X; //Add the value in X to the AC
                  address++;
                  PC++;
                  break;
               case 11:
                  AC += Y; //Add the value in Y to the AC
                  address++;
                  PC++;
                  break;
               case 12:
                  AC -= X; //Subtract the value in X from the AC
                  address++;
                  PC++;
                  break;
               case 13:
                  AC -= Y; //Subtract the value in Y from the AC
                  address++;
                  PC++;
                  break;
               case 14:
                  X = AC; //Copy the value in the AC to X
                  address++;
                  PC++;
                  break;
               case 15:
                  AC = X; //Copy the value in X to the AC
                  address++;
                  PC++;
                  break;
               case 16:
                  Y = AC; //Copy the value in the AC to Y
                  address++;
                  PC++;
                  break;
               case 17:
                  AC = Y; //Copy the value in Y to the AC
                  address++;
                  PC++;
                  break;
               case 18:
                  SP = AC; //Copy the value in AC to the SP
                  address++;
                  PC++;
                  break;
               case 19:
                  AC = SP; //Copy the value in SP to the AC
                  address++;
                  PC++;
                  break;
               case 20:
                  pw.println("read " + (++address));
                  pw.flush();
                  address = Integer.parseInt(sc.next()); //Jump to the address
                  PC++;
                  break;
               case 21:
                  pw.println("read " + (++address));
                  pw.flush();
                  OP = Integer.parseInt(sc.next());
                  if (AC == 0){
                     address = OP; //Jump to the address only if AC = 0
                  }
                  else{
                     address++;
                  }
                  PC++;
                  break;
               case 22:
                  pw.println("read " + (++address));
                  pw.flush();
                  OP = Integer.parseInt(sc.next());
                  if (AC != 0){
                     address = OP; //Jump to the address only if AC != 0
                  }
                  else{
                     address++;
                  }
                  PC++;
                  break;
               case 23:
                  pw.println("read " + (++address));
                  pw.flush();
                  OP = Integer.parseInt(sc.next());
                  pw.print("write " + SP + " " + (++address) + "\n"); //Push return addr onto stack
                  pw.flush();
                  sc.next();
                  sc.nextLine();
                  SP--;
                  address = OP; //Jump to the addr
                  PC++;
                  break;
               case 24:
                  if (SP + 1 <= 999){
                     pw.println("read " + (SP + 1)); //Pop return addr from the stack
                     pw.flush();
                     address = Integer.parseInt(sc.next()); //Jump to the addr
                     SP++;
                  }
                  else{ throw new Exception("Cannot access system memory on user mode");
                  }
                  PC++;
                  break;
               case 25:
                  X++;
                  address++;
                  PC++;
                  break;
               case 26:
                  X--;
                  address++;
                  PC++;
                  break;
               case 27:
                  pw.print("write " + SP + " " + AC + "\n"); //Push AC onto stack
                  pw.flush();
                  sc.next();
                  sc.nextLine();
                  SP--;
                  address++;
                  PC++;
                  break;
               case 28:
                  if (SP + 1 <= 999){
                     pw.println("read " + (SP + 1)); //Pop return addr from the stack
                     pw.flush();
                     AC = Integer.parseInt(sc.next()); //Pop from stack into AC
                     SP++;
                     address++;
                  }
                  else{ throw new Exception("Cannot access system memory on user mode");
                  }
                  PC++;
                  break;
               case 29:
               case 30:
                  break;
               case 50:
                  break; //End of processing
               default:
                  throw new Exception("Error fetching to processor");
            }
         }
         pw.println("false");
         pw.flush();
         sc.close();
         memory.waitFor();
         System.out.println("Memory process ended with code: " + memory.exitValue());
         System.out.println("Processor ended with code: 0");
         System.exit(0);
      }
      catch(Throwable t){
         t.printStackTrace();
         System.out.println("Processor ended with code: 1");
         System.exit(1);
      }
   }
      
}