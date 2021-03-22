## About the Project
The project has been implemented in a higher language where simulate how a CPU would operate with its many register instructions. The processor is running as the parent process in which constantly talks via PIPE with the memory process that is forked from the processor. The memory has two sections in which the processor uses system call, stack insertion/remove, and interruption to switch between all the memory segments. Once processing is done, processor process terminates the memory and then exits with code 0 is execution went ok or 1 in case of error.

## Concepts and Objectives

**Interprocess communication:** Processes requires a communication method to transfer data between each other. This project demonstrates the use of pipe for receiving and sending data from one process to the other.

**Multiprocessing:** Most modern processors work with multiprocessing. This project demonstrates the use of parent and child processes (fork).

**Process-memory communication:** The memory is responsible to store both instruction and data set where the processor constantly access and modify.

**Stack manipulation:** The use of stack to read/write data to upon executing several CPU instructions.

**System calls:** As the memory is divided into two section: kernel and user mode, the system calls are required to interrupt the normal execution of the processes and enter in system mode.

![alt text](https://github.com/arielwsc/OSProjects/blob/master/Proc_Instr_Set.JPG?raw=true)

# Files
Memory.java : This file contains the memory process and its partions.
Processor.java : This file contains the processor process and all its logic including the register
instructions and the forking of the Memory process

# Execution
Cmd compilation: "javac Processor.java"
Cmd run: `java Processor <source file> <timer>`, where `<source file>` is the file that has the processor
instructions to be fetched into memory and `<timer>` is the timer in int format for processing interruption.
E.g., `java Processor sample1.txt 500`
