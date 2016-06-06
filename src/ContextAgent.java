import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ContextAgent extends Agent {
	// TODO General TODO: make criterion variables?

	// Properties
	private ServiceAgent serviceAgent;
	// TODO id??
	private final Double LAMBDA = 0.2;

	
	private double confidence;

	// private List<String> correspondingMessages; // TODO to change for actual
	// type

	// Ranges of Validity
	// private List<List<ServicesStateVR>> neightboursState;
	// private int nbOfConnection
	// private double averageTOConnexion;
	// private Cardinality

	private List<Pair<Boolean, List<ServiceAgent>>> neightboursState; // (same
																		// instance,
																		// agent
																		// type)
	private Action actionPerformed;
	private String senderType;
	private Action messageType;
	private Pair<Boolean, List<Agent>> serviceAgentState;

	// Necessary for the perceive-decide-act cycle distinction
	private boolean isValid;
	// Necessary for the perceive-decide-act cycle distinction and because an
	// agent may respond to multiple services messages
	private List<ServiceAgentMessage> listServiceAgentMessage;

	// Constructor
	// public ContextAgent(String senderType, Action messageType,
	// List<List<ServicesStateVR>> neightboursState,
	// boolean serviceAgentState, String typeConnectedAgent) {
	// this.senderType = senderType;
	// this.messageType = messageType;
	// this.neightboursState = neightboursState;
	// this.serviceAgentState = serviceAgentState;
	// this.typeConnectedAgent = typeConnectedAgent;
	// }
	/**
	 * 
	 * @param senderType
	 * @param messageType
	 * @param neightboursState
	 * @param serviceAgentState
	 * @param actionPermed
	 * @param serviceAgent
	 * @param confidence
	 */
	public ContextAgent(String senderType, Action messageType, List<Pair<Boolean, List<ServiceAgent>>> neightboursState,
			Pair<Boolean, List<Agent>> serviceAgentState, Action actionPermed, ServiceAgent serviceAgent,
			double confidence) {
		this.senderType = senderType;

		this.messageType = messageType;
		this.neightboursState = neightboursState;
		this.serviceAgentState = serviceAgentState;
		this.actionPerformed = actionPerformed;
		this.serviceAgent = serviceAgent;
		this.confidence = confidence;
		// this.listServiceAgentMessage = new ArrayList<ServiceAgentMessage>();
		// ??
	}

	// /**
	// *
	// * @param senderType
	// * @param messageType
	// * @param neightboursState
	// * @param serviceAgentState
	// * @param actionPermed
	// * @param cardinality
	// * @param nbOfConnection
	// * @param averageTOConnexion
	// * @param serviceAgent
	// */
	// public ContextAgent(String senderType, Action messageType,
	// List<Pair<Boolean, List<ServiceAgent>>> neightboursState,
	// Pair<Boolean, List<Agent>> serviceAgentState, Action actionPermed, int
	// cardinality, int nbOfConnection,
	// Double averageTOConnexion, ServiceAgent serviceAgent, double confidence)
	// {
	// this.senderType = senderType;
	//
	// this.messageType = messageType;
	// this.neightboursState = neightboursState;
	// this.serviceAgentState = serviceAgentState;
	// this.typeConnectedAgent = typeConnectedAgent;
	// this.actionPerformed = actionPermed;
	// this.cardinality = cardinality;
	// this.nbOfConnection = nbOfConnection;
	// this.averageTOConnexion = averageTOConnexion;
	// this.serviceAgent = serviceAgent;
	// this.confidence = confidence;
	// }

	// Accessors and mutators

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

	public String getSenderType() {
		return senderType;
	}

	private void setSenderType(String senderType) {
		this.senderType = senderType;
	}

	public Action getMessageType() {
		return messageType;
	}

	private void setMessageType(Action messageType) {
		this.messageType = messageType;
	}

	public List<Pair<Boolean, List<ServiceAgent>>> getNeightboursState() {
		return neightboursState;
	}

	private void setNeightboursState(List<Pair<Boolean, List<ServiceAgent>>> neightboursState) {
		this.neightboursState = neightboursState;
	}

	public Action getActionPerformed() {
		return actionPerformed;
	}

	private void setActionPerformed(Action actionPerformed) {
		this.actionPerformed = actionPerformed;
	}

	public Pair<Boolean, List<Agent>> getServiceAgentState() {
		return serviceAgentState;
	}

	private void setServiceAgentState(Pair<Boolean, List<Agent>> serviceAgentState) {
		this.serviceAgentState = serviceAgentState;
	}

	public boolean isValid() {
		return isValid;
	}

	private void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	// Life cycle

	@Override
	protected void perceive() {
		isValid = false;
		// TODO Empty list of action
		
		if (isBasicCriterionValid(serviceAgent.getCurrentServiceState())) {
			// Neightbours
			List<Pair<Boolean, List<ServiceAgent>>> _actualNeighboursState = getNeighboursState();
			if (isNeighboursStateValid(_actualNeighboursState)) {
				// The messages

				// TODO Read every messages of its service agent until it found
				// the
				// good message
				// Try for each is valid
				// If valid for a message stock it in a list
			}

		}
	}

	@Override
	protected void decide() {
		// A contextAgent may be valid for multiple messages, for all those
		// messages he will send a proposition to its service agent
		// No real decision

	}

	@Override
	protected void act() {
		// Sends its proposition to its serviceAgent
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
	}

	// Methodes used during Life Cycle

	private List<Pair<Boolean, List<ServiceAgent>>> getNeighboursState() {
		List<Pair<Boolean, List<ServiceAgent>>> actualNeighboursState = serviceAgent.getNeighboursState();
		if (actualNeighboursState.size() == neightboursState.size()) {
			// The size is good: normal behavior
		} else {
			// TODO send an error
		}
		return actualNeighboursState;
	}

	private List<ServiceAgent> getConnectedAgents() {
		return serviceAgent.getConnectedAgents();
	}

	// /**
	// *
	// * @param _senderType
	// * : the type of the InstanceAgent of the sender (which is a
	// * ServiceAgent)
	// * @param _messageType
	// * : the type of the message (see Action)
	// * @param _actualState
	// * : the state (connected and with who) of the other
	// * ServiceAgents of the InstanceAgent
	// * @param _serviceAgentState
	// * : the state (connected) of the serviceAgent
	// * @param _typeConnectedAgent
	// * : the type of the ServiceAgent with which the serviceAgent is
	// * connected
	// * @return : if the context agent is valid i.e. if the context is
	// reproduced
	// */
	// private boolean isContextAgentValid(String _senderType, Action
	// _messageType,
	// List<List<ServicesStateVR>> _actualState, boolean _serviceAgentState,
	// String _typeConnectedAgent) {
	// // If one criteria is not valid, the context is not valid
	// if (_senderType == null || senderType == null ||
	// !_senderType.equals(senderType)) {
	// // TODO change if become a list
	// return false;
	// }
	// if (_messageType == null || messageType == null ||
	// !_messageType.equals(messageType)) {
	// return false;
	// }
	// if (!((serviceAgentState && _serviceAgentState) || ((!serviceAgentState)
	// && (!_serviceAgentState)))) {
	// // TODO may need to be changed if bool can become either
	// return false;
	// }
	// if (_typeConnectedAgent == null || typeConnectedAgent == null
	// || !typeConnectedAgent.equals(_typeConnectedAgent)) {
	// // TODO change if become a list with correlation?
	// return false;
	// }
	// if (!isNeighboursStateValid(_actualState)) {
	// return false;
	// }
	// return true;
	// }

	/**
	 * @param _serviceAgentState
	 *            : the state (connected) of the serviceAgent
	 * @param _typeConnectedAgent
	 *            : the type of the ServiceAgent with which the serviceAgent is
	 *            connected
	 * @return : if the context agent is valid for criterion about state of
	 *         ServiceAgents i.e. if the context is reproduced for these
	 *         criterion
	 */
	private boolean isBasicCriterionValid(Pair<Boolean, List<Agent>> _actualServiceAgentState) {
		//Validity of the connection range of the contexte agent
		if (!((serviceAgentState.getFirst() && _actualServiceAgentState.getFirst())
				|| ((!serviceAgentState.getFirst() && (!_actualServiceAgentState.getFirst()))))) {
			// TODO may need to be changed if bool can become either
			return false;
		}
		
		// Check if one of the connection is correlated with the type range
		// Pair<Boolean, List<Agent>> serviceAgentState;
		if ( serviceAgentState.getSecond(). )
		
		if (_typeConnectedAgents == null || typeConnectedAgent == null ||
		 * !_typeConnectedAgents.contains(typeConnectedAgent)) { return false; }
		/*
		 * if (!((serviceAgentState && _serviceAgentState) ||
		 * ((!serviceAgentState) && (!_serviceAgentState)))) { // TODO may need
		 * to be changed if bool can become either return false; }
		 * 
		 * // TODO Traitement pour obtenir les types, puis faire ce qu'il faut
		 * 
		 * if (_typeConnectedAgents == null || typeConnectedAgent == null ||
		 * !_typeConnectedAgents.contains(typeConnectedAgent)) { return false; }
		 */

		return true;
	}

	/**
	 * 
	 * @param _senderType
	 *            : the type of the InstanceAgent of the sender (which is a
	 *            ServiceAgent)
	 * @param _messageType
	 *            : the type of the message (see Action)
	 * @return : if the context agent is valid for criterion based on messages
	 *         i.e. if the context is reproduced for these criterion
	 */
	private boolean isMessageCriterionValid(String _senderType, Action _messageType) {
		if (_senderType == null || senderType == null || !_senderType.equals(senderType)) {
			// TODO change if become a list
			return false;
		}
		if (_messageType == null || messageType == null || !_messageType.equals(messageType)) {
			return false;
		}

		// vérifier le critère de même instance
		return true;
	}

	private boolean isNeighboursStateValid(List<Pair<Boolean, List<ServiceAgent>>> actualNeighboursState) {

		boolean isV = true;

		if (actualNeighboursState.size() == neightboursState.size()) {
			List<ServiceAgent> actualNState;
			boolean isNeiV = false;

			// The size is good: normal behavior
			// Look for every neightbour if the expected neightbour context
			// really is
			for (int i = 0; i < actualNeighboursState.size(); i++) {
				isNeiV = false;

				actualNState = actualNeighboursState.get(i).getSecond();
				for (int j = 0; j < actualNState.size(); j++) {
					if (actualNState.get(j).getType() == neightboursState.get(i).getSecond()) {// TODO
						isNeiV = true;
						break;
					}
				}
				if (!isNeiV) {
					isV = false;
					break;
				}

			}
		} else {
			// TODO send an error
		}
		return isV;
	}

	static <T> boolean hasSimpleCorrelation(List<T> list1, List<T> list2) {
		boolean isSimplyCorralated = false;
		T item;
		for (int i = 0; i < list1.size(); i++) {
			item = list1.get(i);
			if (list2.contains(item)) // TODO need to be modify if we
				// change same instance or not
				//same instance to be possibly
				//either
				
					isSimplyCorralated = true;
					break;
			
		}
	
	 return isSimplyCorralated;
	}

	public void useFeedback (Boolean serviceAgentReponse){
		calculateConfidence(serviceAgentReponse);
	}
	
	
	// Cette methode permet de calculer la recompense de l'agent contexte
	private void calculateConfidence(Boolean serviceAgentReponse) {
		int res = (serviceAgentReponse) ? 1 : 0;
		this.confidence = (1 - LAMBDA) * this.confidence + LAMBDA * res;
	}

}
