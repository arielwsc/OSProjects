public class Memory {

    static int[] dataStorage = new int[2000]; //Array of integer for data storage
        //0-999 - User program
        //1000-199 kernel use

    public static int read(int address) throws Exception{
        if (address >= 0 && address <= 999){
            return dataStorage[address];
        }
        else{
            throw new Exception("User does not have permission to access Kernel mode");
        }
    }

    public static void write(int address, int data) throws Exception{
        if (address >= 0 && address <= 999){
            dataStorage[address] = data;
            System.out.println("Saved into memory");
        }
        else{
            throw new Exception("User does not have permission to access Kernel mode");
        }
    }
    
    public static void main(String args[]) throws Exception{
        if (args.length != 1){
            throw new IllegalArgumentException();
        }
        if (args[0] == "read"){
            System.out.println(read(Integer.parseInt(args[1])));
        }
        if(args[0] == "write"){
            write(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        }
        else{
            throw new IllegalArgumentException();
        }
    }
}
