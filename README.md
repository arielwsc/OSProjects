## Project Description
The project has as objective to show three of the six different scheduling algorithms in a uniprocessor environment. Each scheduling has a distinct algorithm to schedule the different processes the CPU has to run while achieving the maximum efficiency (the least idle time). The information for each task is passed to the program through an input file that the user provides when executing the code.

The three scheduling algorithms are shown below. All these are preemptive schedulers:

**First-come-first-served** (denoted by FCFS): The name is self-explanatory. The earliest process to arrive is given the priority to run by the CPU. This algorithm is fairly easy to implement as the process can be scheduled by its arrival time. 

**Shortest process next** (denoted by SPN): The process that has the estimated shortest service time, which is how long the CPU will be required to finish running the process, is given priority. This algorithm may cause starvation of longer processes.

**Highest Response Ration Next** (denoted by HRRN): The hardest one to be implemented out of these three. The HRRN scheduling determines which process should be given priority for execution based on a formula:
R = (w+s / s), where w is the current waiting time and s is the service time for the task.
The scheduler keeps checking each process based on this formula.

## Execution
To compile the program:
`javac Project3.java Job.Java`

To execute the program:
`java Project3 <source file>`, where the `<source file>` is the input file with
the information for each job

E.g., `java Project3 jobs.txt`
