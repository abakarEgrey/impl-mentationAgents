import java.util.ArrayList;
import java.util.List;


public class ServiceAgentMessage extends AbstractMessage {
	
	private int cardinality;
	private String senderType;
	private Action messageType;
	private Pair<Boolean, List<Agent>> serviceAgentState;
	private int nbOfConnection;
	private Double averageTOConnexion;
	//l'agent service qui envoie le message
	private ServiceAgent serviceAgent;
	
	/**
	 * 
	 * @param cardinality
	 * @param senderType
	 * @param messageType
	 * @param serviceAgentState
	 * @param nbOfConnection
	 * @param averageTOConnexion
	 */
	public ServiceAgentMessage(int cardinality, String senderType,
			Action messageType, Pair<Boolean, List<Agent>> serviceAgentState,
			int nbOfConnection, Double averageTOConnexion, ServiceAgent serviceAgent) {
		super(MessageType.SAMESSAGE);
		this.cardinality = cardinality;
		this.senderType = senderType;
		this.messageType = messageType;
		this.serviceAgentState = serviceAgentState;
		this.nbOfConnection = nbOfConnection;
		this.averageTOConnexion = averageTOConnexion;
		this.serviceAgent = serviceAgent;
	}

	public ServiceAgent getServiceAgent() {
		return serviceAgent;
	}

	/**
	 * 
	 * @param senderType
	 * @param messageType
	 * @param serviceAgentState
	 */
	public ServiceAgentMessage(String senderType, Action messageType,
			Pair<Boolean, List<Agent>> serviceAgentState) {
		super(MessageType.SAMESSAGE);
		this.senderType = senderType;
		this.messageType = messageType;
		this.serviceAgentState = serviceAgentState;
	}
	/**
	 * 
	 * @return
	 */
	public int getCardinality() {
		return cardinality;
	}

	/**
	 * 
	 * @return
	 */
	public String getSenderType() {
		return senderType;
	}
	/**
	 * 
	 * @return
	 */
	public Action getMessageType() {
		return messageType;
	}
	/**
	 * 
	 * @return
	 */
	public Pair<Boolean, List<Agent>> getServiceAgentState() {
		return serviceAgentState;
	}
	/**
	 * 
	 * @return
	 */
	public int getNbOfConnection() {
		return nbOfConnection;
	}
	/**
	 * 
	 * @return
	 */
	public Double getAverageTOConnexion() {
		return averageTOConnexion;
	}
	
	
	
}
