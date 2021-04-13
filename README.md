## About the Project
This project simulates a clinic’s routine where each agent: patients, receptionist, nurses, and doctors have a specific task. The sequence of task has to be strictly followed; that is: A patient can not be seen by doctor without registering with the receptionist; a doctor listens to patient’s symptoms before the patient enters the office, etc. Also, the activities performed by everyone can occur in parallel to others. e.g., While patient 0 is seeing a doctor, patient 1 is being taken by nurse to see another doctor. There can be several patients, nurses, and doctors (These values are determined by user at run time).
The idea for this simulation is to be implemented by concurrent threads: each individual in the clinic is represented by a thread. As threads’ run time can not be predicable, there must be coordination, so a thread’s run does not overlap with other. Semaphores are being used to coordinate concurrency by providing mutually exclusion for each thread’s resource.

Concurrency in programming can be challenging as we do not know exactly when a thread/process will access or modify a resource. The use of mutex and semaphores is needed so we can ensure a specific number of threads can have access to crucial parts of the code.

## Compilation:
`javac Project2.java`

## Execution:
`java Project2 <arg1> <arg2>`, where arg 1 = number of doctors & nurses, and 
arg2 = total number of patients.
