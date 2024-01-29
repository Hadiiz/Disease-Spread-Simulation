package sI_SIR;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Susceptible {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	public Susceptible(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
	}
}
