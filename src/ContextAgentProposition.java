
public class ContextAgentProposition extends AbstractMessage{
	/**
	 * This class allows to get informations context agents proposals
	 */
	private ContextAgent contextAgent;
	private Action action;
	private ServiceAgentMessage serviceAgentMessage;
	private double confidence;
	
	/**
	 * 
	 * @param contextAgent
	 * @param action
	 * @param serviceAgentMessage
	 */
	public ContextAgentProposition(ContextAgent contextAgent, Action action,
			ServiceAgentMessage serviceAgentMessage) {
		super(MessageType.PROPOSITIONAC);
		this.contextAgent = contextAgent;
		this.action = action;
		this.serviceAgentMessage = serviceAgentMessage;
		this.confidence = contextAgent.getConfidence();
	}

	public ContextAgent getContextAgent() {
		return contextAgent;
	}

	public Action getAction() {
		return action;
	}

	public ServiceAgentMessage getServiceAgentMessage() {
		return serviceAgentMessage;
	}
	
	public double getConfidence(){
		return confidence;
	}

	
}
