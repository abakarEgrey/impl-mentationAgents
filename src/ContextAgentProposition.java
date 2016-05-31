
public class ContextAgentProposition {
	/**
	 * This class allows to get informations context agents proposals
	 */
	private ContextAgent contextAgent;
	private Action action;
	private ServiceAgentMessage serviceAgentMessage;
	
	/**
	 * 
	 * @param contextAgent
	 * @param action
	 * @param serviceAgentMessage
	 */
	public ContextAgentProposition(ContextAgent contextAgent, Action action,
			ServiceAgentMessage serviceAgentMessage) {
		super();
		this.contextAgent = contextAgent;
		this.action = action;
		this.serviceAgentMessage = serviceAgentMessage;
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
	

	
}
