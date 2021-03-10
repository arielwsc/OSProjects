###Files###
Memory.java : This file contains the memory process and its partions.
Processor.java : This file contains the processor process and all its logic including the register
instructions and the forking of the Memory process

###Execution###
Cmd compilation: "javac Processor.java"
Cmd run: "java Processor <source file> <timer>", where <source file> is the file that has processor
instructions to be fetched into memory and <timer> is the timer in int format for processing interruption.
E.g., "java Processor sample1.txt 500"
