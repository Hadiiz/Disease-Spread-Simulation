package sI_SIR;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class Infected {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private double infectionRate = 0.1;
	private double recoveryRate = -1;
	
	public Infected(ContinuousSpace<Object> space, Grid<Object> grid, double infectionRate, double recoveryRate ) {
		this.space = space;
		this.grid = grid;
		this.infectionRate = infectionRate;
		this.recoveryRate = recoveryRate;
	}
	
	
	
	@ScheduledMethod(start = 1, interval = 1)
	public void infect() {
		//PROBABILITY OF INFECTION
		double probability = RandomHelper.nextDoubleFromTo(0, 1);
		if(probability > infectionRate)
			return;
		
		//INFECT
		GridPoint pt = grid.getLocation(this);
		List<Object> susceptibles = new ArrayList<Object>();
		for (Object obj : grid.getObjects()) {
			if (obj instanceof Susceptible) {
				susceptibles.add(obj);
			}
		}
		if(susceptibles.size()>0) {
			int index = RandomHelper.nextIntFromTo(0, susceptibles.size() - 1);
			Object target = susceptibles.get(index);
			NdPoint targetPt = space.getLocation(target);
			Context<Object> context = ContextUtils.getContext(target);
			context.remove(target);
			Infected infected = new Infected(space, grid, infectionRate, recoveryRate);
			context.add(infected);
			space.moveTo(infected, targetPt.getX(), targetPt.getY());
			grid.moveTo(infected, pt.getX(), pt.getY());
			
		}
	}
	@ScheduledMethod(start = 10, interval = 1)
	public void recover() {
		//PROBABILITY OF RECOVERY
		double probability = RandomHelper.nextDoubleFromTo(0, 1);
		if(probability > recoveryRate)
			return;
		
		GridPoint pt = grid.getLocation(this);
		NdPoint targetPt = space.getLocation(this);
		Context<Object> context = ContextUtils.getContext(this);
		
		Recovered recovered = new Recovered(space, grid);
		context.add(recovered);
		space.moveTo(recovered, targetPt.getX(), targetPt.getY());
		grid.moveTo(recovered, pt.getX(), pt.getY());
		
		context.remove(this);
	}
}
