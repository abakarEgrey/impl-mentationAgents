import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ServiceAgent extends Agent{
//Properties
	private Pair<Boolean, List<Agent>> isConnected;
	private Agent connectedAgent;
	//TODO messages from other services agents and from 
	private List<ContextAgent> ContextAgents;
	private InstanceAgent instanceAgent;
	private HashMap<String, Pair<Integer, Double>> nbConnectEtTempMoy;
	//private  HashMap<Agent, Pair<Boolean, List<Agent>>> etatsVoisins; 
//Constructor ServiceAgent
	public ServiceAgent (){
		
	}
	
//Acessors
	//TODO maybe to change
	public List<Pair<Boolean, List<Agent>>> getNeighboursState()
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
