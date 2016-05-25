import java.util.List;
import java.util.Set;

public class ServiceAgent extends Agent{
//Properties
	private Boolean isConnected;
	private Agent connectedAgent;
	//TODO messages from other services agents and from 
	private List<ContextAgent> ContextAgents;
	private InstanceAgent instanceAgent;
	
//Constructor ServiceAgent
	public ServiceAgent (){
		
	}
	
//Acessors
	//TODO maybe to change
	public List<String> getNeighboursState ()
	{
		return instanceAgent.getChildrenState();
	}
	
//Life Cycle	
	@Override
	protected void perceive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void decide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void act() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

}
