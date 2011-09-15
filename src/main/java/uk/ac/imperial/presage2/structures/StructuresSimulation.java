package uk.ac.imperial.presage2.structures;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.AbstractModule;

import uk.ac.imperial.presage2.core.Time;
import uk.ac.imperial.presage2.core.simulator.InjectedSimulation;
import uk.ac.imperial.presage2.core.simulator.Parameter;
import uk.ac.imperial.presage2.core.simulator.Scenario;
import uk.ac.imperial.presage2.util.location.area.Area;

/**
 * @author Sam Macbeth
 * 
 */
class StructuresSimulation extends InjectedSimulation {

	@Parameter(name = "finishTime")
	public int finishTime;

	@Parameter(name = "xSize")
	public int xSize;

	@Parameter(name = "ySize")
	public int ySize;

	@Parameter(name = "zSize")
	public int zSize;

	@Parameter(name = "cellCount")
	public int cellCount;

	@Parameter(name = "seedCount")
	public int seedCount;

	StructuresSimulation(Set<AbstractModule> modules) {
		super(modules);
	}

	@Override
	protected Set<AbstractModule> getModules() {
		Set<AbstractModule> modules = new HashSet<AbstractModule>();
		modules.add(new StructuresModule());
		modules.add(Time.Bind.integerTime(finishTime));
		modules.add(new Area.Bind(xSize, ySize, zSize));
		return modules;
	}

	@Override
	protected void addToScenario(Scenario s) {
		// TODO Auto-generated method stub

	}

}
