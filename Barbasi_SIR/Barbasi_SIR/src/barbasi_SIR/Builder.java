package barbasi_SIR;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.util.ContextUtils;

public class Builder implements ContextBuilder<Object> {
	

	@Override
	public Context build(Context<Object> context) {
		
		context.setId("Barbasi_SIR");
		
		
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context, 
				new RandomCartesianAdder<Object>(), 
				new repast.simphony.space.continuous.WrapAroundBorders(),
				50, 50);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(),
						true, 50, 50));
		
		
		//NETWORK
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("social network", context , true );
		netBuilder.buildNetwork();
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		int populationCount = params.getInteger("population_count");
		
		int infectedCount = params.getInteger("infected_count");
		double infectedRate = params.getDouble("infected_rate");
		double recoveryRate = params.getDouble("recovery_rate");
		
		Susceptible a = new Susceptible(space, grid);
		context.add(a);
		Barbasi b = new Barbasi(a, space, grid, infectedRate, recoveryRate);
		for(int i=0; i< populationCount; i++) {
			Susceptible s = new Susceptible(space, grid);
			context.add(s);
			b.addNode(s);
			
		}
		for(int j=0; j<infectedCount; j++) {
			Infected i = new Infected(space, grid);
			b.addNode(i);
			context.add(i);
		}
		
		context.add(b);
		
		/**
		//PARAMS
		Parameters params = RunEnvironment.getInstance().getParameters();
		int populationCount = params.getInteger("population_count");
 
		
		int infectedCount = params.getInteger("infected_count");;
		for(int i=0; i<infectedCount; i++) {
			context.add(new Infected(space, grid));
		}
		
		int susceptibleCount = populationCount - infectedCount;
		for(int i=0; i<susceptibleCount; i++) {
			context.add(new Susceptible(space, grid));
		}
		
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int)pt.getX(), (int)pt.getY());
		}**/
		
		return context;
	}

}
