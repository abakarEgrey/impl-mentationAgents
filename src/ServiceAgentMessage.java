import java.util.ArrayList;
import java.util.List;

import fr.irit.smac.libs.tooling.messaging.impl.Ref;


public class ServiceAgentMessage extends AbstractMessage {
	
	private int cardinality;
	private String senderType;
	private Action messageType;
	private Pair<Boolean, ArrayList<ServiceAgent>> serviceAgentState;
	private int nbOfConnection;
	private Double averageTOConnexion;
	//l'agent service qui envoie le message
	private ServiceAgent serviceAgent;
	//Variable permettant de compter le nombre de liaison de l'agent service
	private int nbLink;
	private Ref<AbstractMessage> refServiceAgent;
	
	
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
			Action messageType, Pair<Boolean, ArrayList<ServiceAgent>> serviceAgentState,
			int nbOfConnection, Double averageTOConnexion, ServiceAgent serviceAgent) {
		super(MessageType.SAMESSAGE, serviceAgent.getInstanceAgent());
		this.cardinality = cardinality;
		this.senderType = senderType;
		this.messageType = messageType;
		this.serviceAgentState = serviceAgentState;
		this.nbOfConnection = nbOfConnection;
		this.averageTOConnexion = averageTOConnexion;
		this.serviceAgent = serviceAgent;
		this.nbLink = 0;  
		this.refServiceAgent =  serviceAgent.getMessageBox().getRef();
	}

	public ServiceAgent getServiceAgent() {
		return this.serviceAgent;
	}

	/**
	 * 
	 * @param senderType
	 * @param messageType
	 * @param serviceAgentState
	 */
	public ServiceAgentMessage(String senderType, Action messageType,
			 Pair<Boolean, ArrayList<ServiceAgent>> serviceAgentState) {
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
	public  Pair<Boolean, ArrayList<ServiceAgent>> getServiceAgentState() {
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

	public Ref<AbstractMessage> getRefServiceAgent() {
		return refServiceAgent;
	}
	
	
	
	
}
