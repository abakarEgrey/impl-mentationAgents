import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class ServiceAgent extends Agent{
//Properties

	private Pair<Boolean, List<Agent>> isConnected;

	private Pair<Boolean, List<ServiceAgent>> isConnected;
	private Agent connectedAgent;

	//TODO messages from other services agents and from 
	private List<ContextAgent> contextAgents;
	private InstanceAgent instanceAgent;
	private Map<String, Pair<Integer, Double>> nbConnectEtTempMoy;
	private Queue<MessageAgent> messages;
	//action choisie par l'agent service et elle est mis à jour par la methode decider
	private Action actionChoisie = null;  
	//private  HashMap<Agent, Pair<Boolean, List<Agent>>> etatsVoisins; 
//Constructor ServiceAgent
	public ServiceAgent (String id, InstanceAgent pere){
		this.name = id;
		this.instanceAgent = pere;
		this.contextAgents = new ArrayList<ContextAgent>();
		this.nbConnectEtTempMoy = new HashMap<String, Pair<Integer,Double>>();
		this.messages = new PriorityQueue<MessageAgent>();
	}
	
//Acessors
	//TODO maybe to change
	public List<Pair<Boolean, List<ServiceAgent>>> getNeighboursState()
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
	
	public void creerFils() {
		
	}


	public boolean isConnected() {
		return isConnected.getFirst();
	}

	public List<ServiceAgent> getConnectedAgents() {
		return isConnected.getSecond();
	}

}
