public class ContextAgentProposition extends AbstractMessage {
	/**
	 * This class allows to get informations context agents proposals
	 */
	private ContextAgent contextAgent;
	private Action action;
	private ServiceAgentMessage serviceAgentMessage;
	private double confidence;
	private String contextAgentPropositionType = "contextAgentProposition";

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
		this.setAbstractMessageType(contextAgentPropositionType);

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

	public double getConfidence() {
		return confidence;
	}

	public Double getConfidenceD() {
		// TODO Auto-generated method stub
		return new Double(confidence);
	}

	/**
	 * redefinition of this for the genericity
	 */
	@Override
	public String getAbstractMessageType() {
		// TODO Auto-generated method stub
		return super.getAbstractMessageType();
	}

}
