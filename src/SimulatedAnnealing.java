import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.moeaframework.problem.tsplib.NodeCoordinates;
import org.moeaframework.problem.tsplib.TSPInstance;

public class SimulatedAnnealing {
	
    public static void main(String[] args) throws IOException {
        // Create and add our cities        
        TSPInstance problem = new TSPInstance(new File("./TSPLIB95/tsp/eil51.tsp"));
        NodeCoordinates nodes = (NodeCoordinates)problem.getDistanceTable();
        
        // Optimal
        problem.addTour(new File("./TSPLIB95/tsp/eil51.opt.tour"));
        System.out.println("Optimal Distance : "+ problem.getTours().get(0).distance(problem));
        
        // add cities
        for (int i = 1; i <= nodes.size(); i++) {
            City city = new City (Integer.toString(nodes.get(i).getId()),(int)nodes.get(i).getPosition()[0],(int)nodes.get(i).getPosition()[1]);
            TourManager.addCity(city);
        }
        
        // start
        ArrayList<Integer> results = new ArrayList<Integer>();
        ArrayList<Double> durations = new ArrayList<Double>();
        ArrayList<String> runs = new ArrayList<String>();
        
        for (int j = 0; j < 10; j++)
        {
        	long startTime = System.nanoTime();
        	
        	//Set initial temp
            double temp = 1000000000; //8

            //Cooling rate
            double coolingRate = 0.00003; //4

            //create random intial solution
            Tour currentSolution = new Tour();
            currentSolution.generateIndividual();
            
            // System.out.println("Total distance of initial solution: " + currentSolution.getTotalDistance());
            // System.out.println("Tour: " + currentSolution);

            // We would like to keep track if the best solution
            // Assume best solution is the current solution
            Tour best = new Tour(currentSolution.getTour());
            
            // Loop until system has cooled
            while (temp > 0.001) 
            {
            	int i =1;
            	// loop for iterations
    			while (i <= 10000) //6
    			{
                // Create new neighbour tour
                Tour newSolution = new Tour(currentSolution.getTour());

                // Get random positions in the tour
                int tourPos1 = Utility.randomInt(0 , newSolution.tourSize());
                int tourPos2 = Utility.randomInt(0 , newSolution.tourSize());
                
                //to make sure that tourPos1 and tourPos2 are different
        		while(tourPos1 == tourPos2) {tourPos2 = Utility.randomInt(0 , newSolution.tourSize());}

                // Get the cities at selected positions in the tour
                City citySwap1 = newSolution.getCity(tourPos1);
                City citySwap2 = newSolution.getCity(tourPos2);

                // Swap them
                newSolution.setCity(tourPos2, citySwap1);
                newSolution.setCity(tourPos1, citySwap2);
                
                // Get energy of solutions
                int currentDistance   = currentSolution.getTotalDistance();
                int neighbourDistance = newSolution.getTotalDistance();

                // Decide if we should accept the neighbour
                double rand = Utility.randomDouble();
                if (Utility.acceptanceProbability(currentDistance, neighbourDistance, temp) > rand) {
                    currentSolution = new Tour(newSolution.getTour());
                }

                // Keep track of the best solution found
                if (currentSolution.getTotalDistance() < best.getTotalDistance()) {
                    best = new Tour(currentSolution.getTour());
                }
                
                // Cool system
                i++;
                temp *= 1 - coolingRate;
    			}
            }
            
            long duration = System.nanoTime() - startTime;
            durations.add((double)duration / 1000000000);
            results.add(best.getTotalDistance());
            System.out.println("Run #" + (j + 1) + " done.");
            runs.add("Run " + (j + 1));
        }
        
        // end
        
        int min = Integer.MAX_VALUE;
        int max = 0;
        int sum = 0;
        
        for (int result : results)
        {
        	if (result < min)
        		min = result;
        	
        	if (result > max)
        		max = result;
        	
        	sum += result;
        }
        
        double avg = sum / results.size();
        
        double durations_sum = 0;
        for (double duration : durations)
        {
        	durations_sum += duration;
        }
        
        double avgTime = (double)durations_sum / durations.size();
        
        System.out.println(String.format("Min: %d, Avg: %f, Max: %d, AvgTime: %f", min, avg, max, avgTime));
        System.out.println(results);
        System.out.println(runs );
        // System.out.println("Final solution distance: " + best.getTotalDistance());
        // System.out.println("Tour: " + best);
        // final long duration = System.nanoTime() - startTime;
        // System.out.println((double)duration/1000000000 + " sec");
    }
}
