import java.util.List;
import java.util.Set;

public class ContextAgent extends Agent{

//TODO General TODO: make criterion variables? 	
	
//Properties
	private double confidence;
	private ServiceAgent serviceAgent;
	//TODO id??
	
	//TODO Plages de valeurs???
	
	//Ranges of Validity 
	private String expeditorType; //List<String>?
	private Action messageType;
	private List<List<ServicesStateVR>> neightboursState;
	private Boolean serviceAgentState;
	private String typeConnectedAgent;
	//private int nbOfConnection
	//private double averageTOConnexion;
	//private Cardinality
	
//Constructor	
public 	ContextAgent(String expeditorType, Action messageType, List<List<ServicesStateVR>> neightboursState, Boolean serviceAgentState, String typeConnectedAgent){
	this.expeditorType = expeditorType; 
	this.messageType = messageType;
	this.neightboursState = neightboursState;
	this.serviceAgentState = serviceAgentState;
	this.typeConnectedAgent = typeConnectedAgent;
}
	
//***Accessor and  

//	@Override
//	protected  Set<Agent> getSons()
//	{
//		// TODO use errors
//		return null;
//	}


public double getConfidence() {
	return confidence;
}

private void setConfidence(double confidence) {
	this.confidence = confidence;
}

public ServiceAgent getServiceAgent() {
	return serviceAgent;
}

private void setServiceAgent(ServiceAgent serviceAgent) {
	this.serviceAgent = serviceAgent;
}

public String getExpeditorType() {
	return expeditorType;
}

private void setExpeditorType(String expeditorType) {
	this.expeditorType = expeditorType;
}

public Action getMessageType() {
	return messageType;
}

private void setMessageType(Action messageType) {
	this.messageType = messageType;
}

public List<List<ServicesStateVR>> getNeightboursState() {
	return neightboursState;
}

private void setNeightboursState(List<List<ServicesStateVR>> neightboursState) {
	this.neightboursState = neightboursState;
}

public Boolean getServiceAgentState() {
	return serviceAgentState;
}

private void setServiceAgentState(Boolean serviceAgentState) {
	this.serviceAgentState = serviceAgentState;
}

public String getTypeConnectedAgent() {
	return typeConnectedAgent;
}

private void setTypeConnectedAgent(String typeConnectedAgent) {
	this.typeConnectedAgent = typeConnectedAgent;
}
	
//Life cycle

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
	
//Methodes used during Life Cycle
	
private List<List<ServicesStateVR>> getNeighboursState()
{
	List<List<ServicesStateVR>> actualState = serviceAgent.getNeighboursState();
	if(actualState.size() == neightboursState.size())
	{
		//The size is good: normal behavior
		return actualState;
	}	
	else 
	{
		//TODO send an error
	}
						
}	

private Boolean isValid (List<ServicesStateVR> actualState){
	//TODO change to actual function
	return true;
}

private Boolean isNeighboursStateValid (List<List<ServicesStateVR>> actualState){
	//TODO may require some changes if we use multiple connections for one service
	if(actualState.size() == neightboursState.size())
	{
		//The size is good: normal behavior
		Boolean isV = true;
		for (int i=0; i<actualState.size(); i++)
		{
			if( hasSimpleCorrelation(actualState.get(i), neightboursState.get(i)))
			{
				//TODO
				//Context agent is not valid for this criteria
			break;
			}
			
		}
		return isV;
	}	
	else 
	{
		//TODO send an error
	}
}

private Boolean hasSimpleCorrelation(List<ServicesStateVR> list1, List<ServicesStateVR> liste2){
	
	//TODO
	return true;
}

	
}
