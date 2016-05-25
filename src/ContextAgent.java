import java.util.List;
import java.util.Set;

public class ContextAgent extends Agent {

	// TODO General TODO: make criterion variables?

	// Properties
	private double confidence;
	private ServiceAgent serviceAgent;
	// TODO id??

	// Ranges of Validity
	private String expeditorType; // List<String>?
	private Action messageType;
	private List<List<ServicesStateVR>> neightboursState;
	private Boolean serviceAgentState;
	private String typeConnectedAgent;
	// private int nbOfConnection
	// private double averageTOConnexion;
	// private Cardinality

	// Constructor
	public ContextAgent(String expeditorType, Action messageType, List<List<ServicesStateVR>> neightboursState,
			Boolean serviceAgentState, String typeConnectedAgent) {
		this.expeditorType = expeditorType;
		this.messageType = messageType;
		this.neightboursState = neightboursState;
		this.serviceAgentState = serviceAgentState;
		this.typeConnectedAgent = typeConnectedAgent;
	}

	// ***Accessor and

	// @Override
	// protected Set<Agent> getSons()
	// {
	// // TODO use errors
	// return null;
	// }

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

	// Life cycle

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

	// Methodes used during Life Cycle

	private List<List<ServicesStateVR>> getNeighboursState() {
		List<List<ServicesStateVR>> actualState = serviceAgent.getNeighboursState();
		if (actualState.size() == neightboursState.size()) {
			// The size is good: normal behavior
			return actualState;
		} else {
			// TODO send an error
		}

	}

	private Boolean isValid(String _expeditorType, Action _messageType, List<List<ServicesStateVR>> _actualState,
			Boolean _serviceAgentState, String _typeConnectedAgent) {
		// If one criteria is not valid, the context is not valid
		if (_expeditorType == null || expeditorType == null || !_expeditorType.equals(expeditorType)) {
			// TODO change if become a list
			return false;
		}
		if (_messageType == null || messageType == null || !_messageType.equals(expeditorType)) {
			return false;
		}
		if (!isNeighboursStateValid(_actualState)) {
			return false;
		}
		if (_serviceAgentState == null || serviceAgentState == null
				|| !(0 == serviceAgentState.compareTo(_serviceAgentState))) {
			return false;
		}
		if (_typeConnectedAgent == null || typeConnectedAgent == null
				|| !typeConnectedAgent.equals(_typeConnectedAgent)) {
			// TODO change if become a list
			return false;
		}

		return true;
	}

	private Boolean isNeighboursStateValid(List<List<ServicesStateVR>> actualState) {
		// TODO may require some changes if we use multiple connections for one
		// service
		Boolean isV = true;
		if (actualState.size() == neightboursState.size()) {
			// The size is good: normal behavior
			for (int i = 0; i < actualState.size(); i++) {
				if (!hasCorrelation(actualState.get(i), neightboursState.get(i))) {
					// Context agent is not valid for this criteria
					isV = false;
					break;
				}

			}
		} else {
			// TODO send an error
		}
		return isV;
	}

	private Boolean hasCorrelation(List<ServicesStateVR> list1, List<ServicesStateVR> list2) {
		Boolean isSimplyCorralated = false;
		for (int i = 0; i < list1.size(); i++) {
			if (list2.contains(list1.get(i))) // TODO need to be modify if we
												// change same instance or not
												// same instance to be possibly
												// either
			{
				isSimplyCorralated = true;
				break;
			}
		}

		return isSimplyCorralated;
	}

}
