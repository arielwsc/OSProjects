public class Job {
    private char jobName;
    private int arrivalTime;
    private double waitingTime;
    private int serviceTime, noServiceProvided;
    private char[] timeline;
    private boolean isDone;

    public Job(char jobName, int arrivalTime, int serviceTime) {
        this.jobName = jobName;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.isDone = false;
        this.waitingTime = 0;
        this.noServiceProvided = 0;
    }

    public char getJobName() {
        return this.jobName;
    }

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    public int getServiceTime() {
        return this.serviceTime;
    }

    public int getNoServiceProvided() {
        return this.noServiceProvided;
    }

    public double getWaitingTime() {
        return this.waitingTime;
    }

    public boolean isJobDone() {
        return this.isDone;
    }

    public void setWaitingTime() {
        this.waitingTime = 0;
    }

    public void setNoServiceProvided() {
        this.noServiceProvided = 0;
    }

    public void setJobStatus(boolean status) {
        this.isDone = status;
    }

    public void countWaitingTime() {
        this.waitingTime += 1;
    }

    public void createTimeline(int totalTime) {
        timeline = new char[totalTime];
        for (int i=0; i<totalTime; i++) {
            timeline[i] = ' ';
        }
    }

    public void setTimeline(int time) {
        timeline[time] = 'X';
        noServiceProvided++;
        if (noServiceProvided == serviceTime){
            isDone = true;
        }
    }

    public String toString() {
        String timeLine = this.jobName + " ";
        for (int i=0; i<timeline.length; i++) {
            timeLine += timeline[i];
        }
        return timeLine;
    }
}
