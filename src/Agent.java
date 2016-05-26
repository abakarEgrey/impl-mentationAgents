import java.util.Set;

import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy;

abstract public class Agent implements IAgentStrategy {
// Properties
<<<<<<< HEAD
	
=======
>>>>>>> 2ebe6b34b90b50548e8f8323b286c02fa29c15f3
	protected String name; //TODO peut être un id
	
//Accessors	
	protected String getName ()
	{
		return name;
	}
	
<<<<<<< HEAD
	
=======
>>>>>>> 2ebe6b34b90b50548e8f8323b286c02fa29c15f3
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
