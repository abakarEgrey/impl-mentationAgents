import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class InstanceAgent extends Agent {
	// Properties
	// id
	private ArrayList<ServiceAgent> serviceAgents;
	private int countIdContextAgents;
	
	// Constructor
	public InstanceAgent(String id) {
		this.id = id;
	}

	
	
	// Accessor

	
	public int getCountIdContextAgents() {
		return countIdContextAgents;
	}



	public void setCountIdContextAgents(int countIdContextAgents) {
		this.countIdContextAgents = countIdContextAgents;
	}

	// Life Cycle
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

	public ArrayList<Pair<Boolean, ArrayList<ServiceAgent>>> getChildrenState() {
		ArrayList<Pair<Boolean, ArrayList<ServiceAgent>>> childrenConnexion = new ArrayList<Pair<Boolean, ArrayList<ServiceAgent>>>(
				serviceAgents.size());

		for (int i = 0; i < serviceAgents.size(); i++) {
			childrenConnexion.add(serviceAgents.get(i).getCurrentServiceState());
		}

		return childrenConnexion;
	}

	//TODO: to change...Or had a more efficient: we DON'T care about the bool, mb
	public ArrayList<ArrayList<Pair<Boolean, ServiceAgent>>> getActualNeighboursState(ServiceAgent askingServiceAgent) {
		
		//List containing the state of the neighbours of serviceAgentAsking
		ArrayList<ArrayList<Pair<Boolean, ServiceAgent>>> actualNeighboursState = new ArrayList<ArrayList<Pair<Boolean, ServiceAgent>>>(
				(serviceAgents.size() - 1));
		
		//To avoid multiple allocation, contain the connexions between neighbourServiceAgent and other ServiceAgents 
		Pair<Boolean, ArrayList<ServiceAgent>> servACState;
		
		//Asking Service Agent State to avoid multiple queries
		
		Pair<Boolean, ArrayList<ServiceAgent>> askingServiceAgentState = askingServiceAgent.getCurrentServiceState();
		
		for (int i = 0; i < serviceAgents.size(); i++) {
			if (!serviceAgents.get(i).equals(askingServiceAgent))
			{
				servACState = serviceAgents.get(i).getCurrentServiceState();
				ArrayList<Pair<Boolean, ServiceAgent>> thisNeighbourState = new ArrayList<Pair<Boolean, ServiceAgent>>();
				
				//Some changes before sending
				//childrenConnexion.add(serviceAgents.get(i).getCurrentServiceState());
				if (servACState.getFirst()){
					if (servACState.getSecond().isEmpty()){
						//TODO error
					}
					else{
						//Add every type with the bool "same instance"
						thisNeighbourState = new ArrayList<Pair<Boolean, ServiceAgent>>(servACState.getSecond().size());
						
						for (int j=0; j<servACState.getSecond().size();j++){
							//Check if the askingServiceAgent is connected to this serviceAgent
							if(askingServiceAgentState.getSecond().contains(servACState.getSecond().get(j))){
								thisNeighbourState.add(new Pair<Boolean,ServiceAgent>(true,servACState.getSecond().get(j)) );
							}
							else{
								thisNeighbourState.add(new Pair<Boolean,ServiceAgent>(false,servACState.getSecond().get(j)) );
							}
						}
						
						//Once every connected serviceAgents connected to the serviceAgent has been add to the sub list, 
						//add the sub list to the list Array
						actualNeighboursState.add(thisNeighbourState);
					}
				}
				else{
					if (servACState.getSecond().isEmpty()){
						//Nothing connected, add empty list
						actualNeighboursState.add(thisNeighbourState);
					}
					else{
						//TODO error
					}
					
				}	
			}
		}

		return actualNeighboursState;
	}



	
}
