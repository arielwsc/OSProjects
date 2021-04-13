import java.util.concurrent.Semaphore;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

public class Project2 {

    public static class EmplCall implements Runnable {
        private int num;
        private Semaphore sem = new Semaphore(0, true);

        EmplCall(int num){
            this.num = num;
        }

        public void run() {
            try{
                sem.acquire();
            }
            catch(InterruptedException e){    
            }
        }

        public void post() {
            sem.release();
        }
    }

    private static Semaphore max_capacity = new Semaphore(30, true);
    private static Semaphore reception = new Semaphore(0, true);
    private static Semaphore enterRoom = new Semaphore(0, true);
    private static Semaphore docAdv = new Semaphore(0, true);
    private static Semaphore waitingRoom = new Semaphore(1, true);
    private static Semaphore mutex1 = new Semaphore(1, true);
    private static Semaphore mutex2 = new Semaphore(1, true);
    private static Semaphore mutex3 = new Semaphore(1, true);
    private static Semaphore mutex4 = new Semaphore(1, true);
    private static int count = 0;
    private static int numDoctors;

    static PriorityQueue<Patient> queue1 = new PriorityQueue<>();
    static PriorityQueue<Patient> queue2 = new PriorityQueue<>();
    static PriorityQueue<Patient> queue3 = new PriorityQueue<>();
    static EmplCall nurseCall[] = new EmplCall[3];
    static EmplCall doctorCall[] = new EmplCall[3];

    public static class PatientThread implements Runnable {
        private Patient patient;

        public void run() {
            try{
                max_capacity.acquire();
                mutex1.acquire();
                patient = new Patient(count);
                count++;
                queue1.add(patient);
                System.out.println("Patient " + patient.getNum() + " enters waiting room, waits for receptionist");
                mutex1.release();
                reception.release();
                waitingRoom.acquire();
                System.out.println("Patient " + patient.getNum() + " leaves receptionist and sits in waiting room");
                enterRoom.acquire();
                System.out.println("Patient " + patient.getNum() + " enters doctor " + patient.getDocNum() + "'s office");
                docAdv.acquire();
                System.out.println("Patient " + patient.getNum() + " receives advice from doctor " + patient.getDocNum());
                System.out.println("Patient " + patient.getNum() + " leaves");
                max_capacity.release();
            }
            catch(InterruptedException e){
            }
        }
    }

    public static class ReceptionistThread implements Runnable {
        private Patient patient;
        Random rand = new Random();

        public void run() {
            while(true){
                try {
                    reception.acquire();
                    mutex2.acquire();
                    patient = queue1.poll();
                    patient.setDocNum(rand.nextInt(numDoctors-1)); //Randomly assigns a doctor's number to a patient
                    queue2.add(patient);
                    waitingRoom.release();
                    nurseCall[patient.getDocNum()].post();
                    mutex2.release();
                }
                catch(InterruptedException e) {
                }

            }
        }
    }

    public static class NurseThread implements Runnable {
        private Patient patient;
        private int num;

        NurseThread(int num) {
            this.num = num;
        }

        public void run() {
            while(true) {
                try {
                    nurseCall[num].run();
                    mutex3.acquire();
                    Iterator<Patient> value = queue2.iterator();
                    patient = value.next();
                    while(patient.getDocNum() != num){
                        patient = value.next();
                    }
                    System.out.println("Nurse " + num + " takes patient " + patient.getNum() + " to doctor's office");
                    queue3.add(patient);
                    doctorCall[num].post();
                    mutex3.release();
                }
                catch(InterruptedException e){
                }
            }
        }
    }

    public static class DoctorThread implements Runnable {
        private Patient patient;
        private int num;

        DoctorThread(int num) {
            this.num = num;
        }

        public void run() {
            while(true) {
                try {
                    doctorCall[num].run();
                    mutex4.acquire();
                    Iterator<Patient> value = queue3.iterator();
                    patient = value.next();
                    while(patient.getDocNum() != num){
                        patient = value.next();
                    }
                    enterRoom.release();
                    System.out.println("Doctor " + num + " listens to symptons from patient " + patient.getDocNum());
                    docAdv.release();
                    mutex4.release();
                }
                catch(InterruptedException e){
                }
            }
        }
    }


    public static void main(String args[]){
        numDoctors = Integer.parseInt(args[0]);
        int numPatients = Integer.parseInt(args[1]);

        Thread emplThread[] = new Thread[2 * numDoctors];
        Thread patientThread[] = new Thread[numPatients];

        Thread receptionistThread = new Thread(new ReceptionistThread()); //Initializing Recepcionist thread
        receptionistThread.start();

        PatientThread patient[] = new PatientThread[numPatients];
        NurseThread nurse[] = new NurseThread[numDoctors];
        DoctorThread doctor[] = new DoctorThread[numDoctors];

        for(int i=0; i<numDoctors; i++){
            nurseCall[i] = new EmplCall(i);
            doctorCall[i] = new EmplCall(i);
            
            // Create employee threads
            nurse[i] = new NurseThread(i);
            emplThread[i] = new Thread(nurse[i]); //Initializing nurse threads
            emplThread[i].start();
            doctor[i] = new DoctorThread(i);
            emplThread[2*numDoctors-(1+i)] = new Thread(doctor[i]); //Initializing Doctor threads
            emplThread[2*numDoctors-(1+i)].start();
        }

        for(int i=0; i<numPatients; i++){
            patient[i] = new PatientThread();
            patientThread[i] = new Thread(patient[i]);
            patientThread[i].setDaemon(true);
            patientThread[i].start();
        }
        
        try {
            for(int i=0; i<numDoctors; i++){
                    emplThread[i].join();
                    emplThread[2*numDoctors-(1+i)].join();
            }
            receptionistThread.join();
        }
        catch(InterruptedException e){
        }
    }

    public static class Patient implements Comparable<Patient> {
        private int patientNum;
        private int patientDocNum;

        Patient(int patientNum) {
            this.patientNum = patientNum;
        }

        public int compareTo(Patient o) {
            if (this.patientNum == o.getNum()){
                return 0;
            }
            else {
                return 1;
            }
        }

        public void setDocNum(int docNum){
            this.patientDocNum = docNum;
        }

        public int getNum() {
            return patientNum;
        }

        public int getDocNum() {
            return patientDocNum;
        }
    }
}