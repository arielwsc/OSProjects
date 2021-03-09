import java.util.Scanner;

public class Memory {

    static int[] dataStorage = new int[2000]; //Array of integer for data storage
        //0-999 - User program
        //1000-1999 kernel use

    public static int read(int address) throws Exception{
        if (address >= 0 && address <= 1999){
            return dataStorage[address];
        }
        else{
            throw new Exception("Error reading from memory\n");
        }
    }

    public static void write(int address, int data) throws Exception{
        if (address >= 0 && address <= 999){
            dataStorage[address] = data;
            System.out.print("Saved into " + address + " user memory: " + data + "\n");
        }
        else if(address >= 1000 && address <= dataStorage.length - 1){
            dataStorage[address] = data;
            System.out.print("Saved into " + address + " system memory: " + data + "\n");
        }
        else{
            throw new Exception("Error writing to memory\n");
        }
    }
    
    public static void main(String args[]) throws Exception{
        String[] arg = new String[3];
        Scanner is = new Scanner(System.in);
        
        while(true){
            String inputLine = is.nextLine();
            Scanner param = new Scanner(inputLine);

            for (int i = 0; i < 3; i++) {
                if (param.hasNext()) {
                    arg[i] = param.next();
                }
            }
            try{
                if (arg.length <= 1) {
                    throw new IllegalArgumentException();
                } else if (arg[0].equals("read")) {
                    System.out.print(read(Integer.parseInt(arg[1])));
                } else if (arg[0].equals("write")) {
                    write(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]));
                } else {
                    throw new IllegalArgumentException();
                }
            }
            catch(Throwable t){
                System.out.println(t.getMessage()+"\n");
            }
        }
    }   
}
