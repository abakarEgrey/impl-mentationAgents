import java.util.Set;

import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy;

abstract public class Agent implements IAgentStrategy {
// Properties
	
	protected String name; //TODO peut être un id
	
//Accessors	
	protected String getName ()
	{
		return name;
	}
	
	
//Life Cycle
	
	/*
	 * 
	 */
	protected abstract void perceive();
	
	/*
	 * 
	 */
	protected abstract void decide ();
	
	/*
	 * 
	 */
	protected abstract void act ();

	/*
	 * 
	 */
	
	
	public abstract void delete();
	
//IAgentStrategy implementation
	public void nextStep(){
		perceive();
		decide();
		act();
	}
	

}
