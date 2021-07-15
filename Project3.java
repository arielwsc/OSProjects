import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

class Project3 {
    static int totalTime = 0;
    public static void main(String[] args) throws FileNotFoundException {

        ArrayList<Job> jobList = reader(args[0]);
        setTotalTime(jobList);
        FCFS(jobList);
        SPN(jobList);
        HRRN(jobList);
    }

    public static ArrayList<Job> reader(String file) throws FileNotFoundException {
        ArrayList<Job> jobList = new ArrayList<Job>();

        Scanner input = new Scanner(new FileInputStream(file));

        while (input.hasNextLine()) {
            Job job = new Job(input.next().charAt(0), input.nextInt(), input.nextInt());
            jobList.add(job);
        }
        input.close();
        return jobList;
    }

    public static void FCFS(ArrayList<Job> jobList) {
        int availableTime = 0;
        int index = 0;

        while (index < jobList.size()) {
            Job job = jobList.get(index);
            job.createTimeline(getTotalTime());

            for (int i=availableTime; i < (availableTime + job.getServiceTime()); i++) {
                job.setTimeline(i);
            }
            availableTime = availableTime + job.getServiceTime();
            index++;
        }
        /**
         * Print FCFS text-based output
         */
        System.out.println("FCFS");
        System.out.println();
        index = 0;
        while (index < jobList.size()) {
            System.out.println(jobList.get(index));
            index++;
        }
        System.out.println();
    }

    public static void SPN (ArrayList<Job> jobList) {
        char nextJob = ' ';
        int minServiceTime;
        int index;

        emptyJobList(jobList);

        for(int time=0; time<getTotalTime(); time++){
            index = 0;
            minServiceTime = getTotalTime();

            while(index < jobList.size()) {
                Job job = jobList.get(index);
                if(job.getArrivalTime() > time || job.isJobDone()) {
                    index++;
                }
                else {
                    if (job.getServiceTime() - job.getNoServiceProvided() < minServiceTime) {
                        minServiceTime = job.getServiceTime();
                        nextJob = job.getJobName();
                    }
                    index++;
                }
            }
            Iterator<Job> iterator = jobList.iterator();
            while(iterator.hasNext()){
                Job job = iterator.next();
                if (nextJob == job.getJobName()){
                    job.setTimeline(time); //Serve Job
                }
            }
        }
        /**
         * Print SPN text-based output
         */
        Iterator<Job> iterator = jobList.iterator();

        System.out.println("SPN");
        System.out.println();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println();
    }

    public static void HRRN (ArrayList<Job> jobList) {
        char nextJob = ' ';
        double maxTurnaroundTime; //Highest turnaround time
        double turnaroundTime;
        int index;
        emptyJobList(jobList);

        for(int time=0; time<getTotalTime(); time++) {
            index = 0;
            maxTurnaroundTime = 0;
            while(index < jobList.size()){
                Job job = jobList.get(index);
                if(job.getArrivalTime() > time || job.isJobDone()){ //Check if job has not already arrived
                    index++;
                }
                else {
                    job.countWaitingTime(); //Increment job's waiting time
                    turnaroundTime = (job.getWaitingTime() + job.getServiceTime()) / (double)job.getServiceTime(); //calculate Job's turnaround time for Job
                    if (turnaroundTime > maxTurnaroundTime){
                        maxTurnaroundTime = turnaroundTime;
                        nextJob = job.getJobName();
                    }
                    index++;
                }
            }
            Iterator<Job> iterator = jobList.iterator();
            while(iterator.hasNext()){
                Job job = iterator.next();
                if (nextJob == job.getJobName()){
                    job.setTimeline(time); //Serve Job
                    job.setWaitingTime(); //Reset Job's waiting time as it has just been served
                }
            }
        }
        /**
         * Print HRRN text-based output
         */
        Iterator<Job> iterator = jobList.iterator();

        System.out.println("HRRN");
        System.out.println();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }

    public static int getTotalTime() {
        return totalTime;
    }

    public static int setTotalTime(ArrayList<Job> list) {
        int index = 0;
        while (index < list.size()) {
            totalTime += list.get(index).getServiceTime();
            index++;
        }
        return totalTime;
    }

    private static void emptyJobList(ArrayList<Job> list) {
        int index = 0;
        while (index < list.size()) {
            list.get(index).createTimeline(getTotalTime());
            list.get(index).setNoServiceProvided();
            list.get(index).setJobStatus(false);
            index++;
        }
    }
}




