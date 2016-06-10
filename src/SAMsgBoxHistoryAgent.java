import java.util.ArrayList;



public class SAMsgBoxHistoryAgent extends Agent {
	private ServiceAgent myServiceAgent;
	private ArrayList<ContextAgentProposition> propositionsAC;
	private ArrayList<ServiceAgentMessage> saMessages;

	
	public SAMsgBoxHistoryAgent(ServiceAgent _myServiceAgent) {
		this.myServiceAgent =  _myServiceAgent;
		this.propositionsAC = new ArrayList<ContextAgentProposition>();
		this.saMessages = new ArrayList<ServiceAgentMessage>();
	}
	

	public ServiceAgent getMyServiceAgent() {
		return myServiceAgent;
	}

	private void setMyServiceAgent(ServiceAgent myServiceAgent) {
		this.myServiceAgent = myServiceAgent;
	}

	public ArrayList<ContextAgentProposition> getPropositionsAC() {
		return propositionsAC;
	}

	private void setPropositionsAC(ArrayList<ContextAgentProposition> propositionsAC) {
		this.propositionsAC = propositionsAC;
	}

	public ArrayList<ServiceAgentMessage> getSaMessages() {
		return saMessages;
	}

	private void setSaMessages(ArrayList<ServiceAgentMessage> saMessages) {
		this.saMessages = saMessages;
	}

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
		ArrayList<AbstractMessage> abstractMessages = myServiceAgent.getMessages();
		propositionsAC.clear();
		saMessages.clear();
		for (AbstractMessage abstMessage : abstractMessages) {
			if(abstMessage.getType() == MessageType.PROPOSITIONAC){
				 if (abstMessage instanceof ContextAgentProposition){
					 propositionsAC.add((ContextAgentProposition)abstMessage); 
				 }
				 else{
					 //TODO : error
					 //Note, the enum was at first here for readability, may be erased
				 }
			 }
			 else if(abstMessage.getType() == MessageType.SAMESSAGE){
				 if (abstMessage instanceof ServiceAgentMessage){
					 saMessages.add((ServiceAgentMessage)abstMessage);
				 }
				 else{
					 //TODO : error
					 //Note, the enum was at first here for readability, may be erased
				 }
			 }
			 else {
				 //TODO error
			 }
			
        }
		
				
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		 
	}

	
	

	
	
}
