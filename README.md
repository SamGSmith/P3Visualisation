# P3Visualisation

This software is intended to help teach and aid study of the Parameterless Population Pyramid (P3) genetic algorithm. The algorithm was created by Goldman and Punch, and can be read about in their original paper at:
http://dl.acm.org/citation.cfm?id=2598350
This software breaks down algorithms steps, and allows you to see how the pyramid of solutions is built, along with how local search, clustering and crossover are used.
To run the software, you will need the Java Runtime Environment installed on your computer (see http://www.oracle.com/technetwork/java/javase/downloads/index.html and click download JRE). Then download and run P3Visualisation.jar from the releases tab to run the program.
Select the fitness function you would like to use, along with its solution size and other appropriate settings, then click “Run P3”.
Solutions are represented by a barcode style representation of their genotype, with the number underneath showing that solutions fitness.  A solution with a red border is the current solution which has been newly added to the pyramid, one with an orange border is the current solution which already exists in the pyramid. A solution with a yellow background is the current fittest solution in the pyramid, and one with a green background has the optimal fitness for the fitness function chosen.
Clusters are represented by a list of numbers, which are the position of the bits in the solution genotype. The crossover view shows this process, with the current cluster underlined, and the cluster highlighted in red in the donor, and the rest of the solution in blue on the current solution.
