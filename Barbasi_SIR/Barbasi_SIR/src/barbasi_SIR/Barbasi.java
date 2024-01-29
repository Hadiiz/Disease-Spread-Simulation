package barbasi_SIR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Iterator;


import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class Barbasi {
		
		private Map<Object, List<Object>> adjacencyList;
		private Map<Object, Integer> degreeList;
		private ContinuousSpace<Object> space;
		private Grid<Object> grid;
		private int total_degree;
		private int counter;
		private double infection_rate;
		private double recovery_rate;
		private int initial_graph;
	
		
		public Barbasi(Object o, ContinuousSpace<Object> space, Grid<Object> grid ,double infectedRate, double recoveryRate ) {
			this.space = space;
			this.grid = grid;
			this.adjacencyList = new HashMap<>();
			this.adjacencyList.put(o, new ArrayList<>());
			this.degreeList = new HashMap<>();
			this.degreeList.put(o, 0);
			this.total_degree = 0;
			this.infection_rate = infectedRate;
			this.recovery_rate = recoveryRate;
			this.initial_graph = 15;
		}

		
		public void addNode(Object o) {
			 adjacencyList.put(o, new ArrayList<>());
			 
			 Random random = new Random();
			 
			 int counter = 0;

		        if (degreeList.size() <= initial_graph) {
		            Set<Object> keysCopy = new HashSet<>(degreeList.keySet());
		            
		            // Select a random key from the keysCopy set
		            List<Object> keyList = new ArrayList<>(keysCopy);
		            Object randomKey = keyList.get(random.nextInt(keyList.size()));

		            addEdge(o, randomKey);
		            degreeList.put(randomKey, degreeList.get(randomKey) + 1);
		            counter++;

		            degreeList.put(o, counter);

		            return;
		        }

			    counter = 0;
			    Set<Object> keysCopy = new HashSet<>(degreeList.keySet());
			    double cumulative_probability = 0.0;
			    for (Object key : keysCopy) {
			    	cumulative_probability += degreeList.get(key).doubleValue()/total_degree;
			        double probability = RandomHelper.nextDoubleFromTo(0, 1);
			        if (probability <= cumulative_probability) {
			            degreeList.put(key, degreeList.get(key) + 1);
			            addEdge(o, key);
			            counter++;
			            break;
			        }
			    }

			    degreeList.put(o, counter);
		}
		
		public void addEdge(Object source, Object target) {
			
			adjacencyList.get(source).add(target);
	        adjacencyList.get(target).add(source);
	        total_degree += 2;
	        
		}
		
		//@ScheduledMethod(start = 1)
		public void showEdges() {
			
			for (Map.Entry<Object, List<Object>> entry : adjacencyList.entrySet()) {
		            Object key = entry.getKey();
		            List<Object> values = entry.getValue();
		            
		            
		            Context<Object> context = ContextUtils.getContext(this);
		            Network <Object> net = (Network < Object >)context.getProjection ("social network");
		            for(Object value : values) {
		            	net.addEdge(key, value);
		            }       
			}
			
		}
		
		public void replaceNode(Object oldNode, Object newNode) {
	        if (adjacencyList.containsKey(oldNode)) {
	            List<Object> neighbors = new ArrayList<>(adjacencyList.get(oldNode));

	            for (Object neighbor : neighbors) {
	                List<Object> neighborList = new ArrayList<>(adjacencyList.get(neighbor));
	                neighborList.remove(oldNode);
	                neighborList.add(newNode);
	                adjacencyList.put(neighbor, neighborList);
	            }

	            adjacencyList.put(newNode, neighbors);
	            adjacencyList.remove(oldNode);
	        }
	        if(degreeList.containsKey(oldNode)) {
	        	int value = degreeList.get(oldNode);
	        	degreeList.put(newNode, value);
	        	degreeList.remove(oldNode);
	        }
	    }
		
		
		
		
		@ScheduledMethod(start = 5, interval = 1)
		public void infect() {
		    // LOOP THROUGH INFECTED
			Set<Map.Entry<Object, List<Object>>> entrySetCopy = new HashSet<>(adjacencyList.entrySet());
		    for (Map.Entry<Object, List<Object>> entry : entrySetCopy) {
		        Object key = entry.getKey();
		        if (key instanceof Infected) {
		            List<Object> infectList = entry.getValue();
		            // GET INFECTED AND LOOP THROUGH THEIR NEIGHBORS
		            for (Object infect : infectList) {
		                	
		                    double probability = RandomHelper.nextDoubleFromTo(0, 1);
		                    if (probability < infection_rate && infect instanceof Susceptible) {
		                        // INFECT
		                        NdPoint targetPt = space.getLocation(infect);
		                        Context<Object> context = ContextUtils.getContext(infect);
		                        context.remove(infect);
		                        Object newInfected = new Infected(space, grid);
		                        context.add(newInfected);
		                        space.moveTo(newInfected, targetPt.getX(), targetPt.getY());
		                        
		                        //REPLACE IN BARBASI-ALBERT
		                        replaceNode(infect, newInfected);
		                    }
		                
		            }
		        }
		    }
		}
		
		@ScheduledMethod(start = 5, interval = 1)
		public void recover() {
			
		    // LOOP THROUGH INFECTED AND RECOVER THEM
			Set<Map.Entry<Object, List<Object>>> entrySetCopy = new HashSet<>(adjacencyList.entrySet());
		    for (Map.Entry<Object, List<Object>> entry : entrySetCopy) {
		    	Object key = entry.getKey();
		    	if(key instanceof Infected) {
					//PROBABILITY OF RECOVERY
					double probability = RandomHelper.nextDoubleFromTo(0, 1);
					if(probability > recovery_rate)
						continue;
					
					NdPoint targetPt = space.getLocation(key);
					Context<Object> context = ContextUtils.getContext(key);
					Recovered recovered = new Recovered(space, grid);
					context.add(recovered);
					space.moveTo(recovered, targetPt.getX(), targetPt.getY());
					context.remove(key);
					
                    //REPLACE IN BARBASI-ALBERT
                    replaceNode(key, recovered);
		    	}
		    }
			
			
			
			
			
			
		}


	
}
