import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.irit.smac.libs.tooling.messaging.AgentMessaging;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;
import fr.irit.smac.libs.tooling.messaging.impl.Ref;

public class ServiceAgent extends Agent {
	// Properties
	private static final double RECOMPENSE = 0.5;
	private Pair<Boolean, ArrayList<ServiceAgent>> isConnected;

	//
	private ArrayList<ContextAgent> contextAgents;
	private int countIdContextAgents;

	private InstanceAgent instanceAgent;
	private Map<String, Pair<Integer, Double>> nbConnectEtTempMoy;
	// private Queue<MessageAgent> messagesBox;
	// action choisie et le dernier message de l'agent service sont mis à jour
	// par la methode decider
	private ArrayList<ArrayList<ContextAgentProposition>> choosenActions;

	private ServiceAgentMessage lastMessage; // TODO ?????????????
	// private Map<String, Action> contextAgentPropositions;

	// Message related attributes
	// private String id; already in agent

	// liste contenant les propositions des agents contextes
	private ArrayList<ContextAgentProposition> contextPropositions;
	Map<ServiceAgentMessage, ArrayList<ContextAgentProposition>> listProp;

	private ArrayList<ServiceAgentMessage> serviceAgentMessages;

	private IMsgBox<AbstractMessage> messageBox;
	private SAMsgBoxHistoryAgent msgBoxHAgent;

	// private HashMap<Agent, Pair<Boolean, ArrayList<Agent>>> etatsVoisins;

	// Constructor ServiceAgent
	public ServiceAgent(String id, InstanceAgent parent) {
		this.id = id;
		this.instanceAgent = parent;
		this.contextAgents = new ArrayList<ContextAgent>();
		this.nbConnectEtTempMoy = new HashMap<String, Pair<Integer, Double>>();
		// this.messagesBox = new PriorityQueue<MessageAgent>();
		choosenActions = null;
		lastMessage = null;
		// this.contextAgentPropositions = new HashMap<String, Action>();
		this.contextPropositions = new ArrayList<ContextAgentProposition>();
		listProp = new HashMap<ServiceAgentMessage, ArrayList<ContextAgentProposition>>();
		messageBox = (IMsgBox<AbstractMessage>) AgentMessaging.getMsgBox(id, AbstractMessage.class);
		msgBoxHAgent = new SAMsgBoxHistoryAgent(this);

	}

	// Acessors

	public int getCountIdContextAgents() {
		return countIdContextAgents;
	}

	public IMsgBox<AbstractMessage> getMessageBox() {
		return messageBox;
	}

	public void setCountIdContextAgents(int countIdContextAgents) {
		this.countIdContextAgents = countIdContextAgents;
	}

	// Life Cycle
	@Override
	protected void perceive() {

		ArrayList<AbstractMessage> mReceived = new ArrayList<AbstractMessage>(messageBox.getMsgs());
		Pair<ArrayList<ContextAgentProposition>, ArrayList<ServiceAgentMessage>> sortedMessages = AbstractMessage
				.sortAbstractMIntoCAPandSAM(mReceived);
		contextPropositions = sortedMessages.getFirst();
		serviceAgentMessages = sortedMessages.getSecond();

		// ArrayList<ServiceAgentMessage> mReceivedByAS

		// traiter la liste des propositions des agents contextes selon le type
		// de message au cycle precedent
		this.listProp = sortPrositionsList();
		// on peut ajouter une methode getEnvironmentProperties pour recuperer
		// les caractéristiques de l'environnement
		// envoyer les messages des agents services aux agents contextes
		// les agents contextes accèdent directement à la boite de reception de
		// l'agent service.
	}

	/**
	 * Cette méthode permet de traiter les messages
	 * 
	 * @return
	 */

	private Map<ServiceAgentMessage, ArrayList<ContextAgentProposition>> sortPrositionsList() {

		Map<ServiceAgentMessage, ArrayList<ContextAgentProposition>> propositionsList = new HashMap<ServiceAgentMessage, ArrayList<ContextAgentProposition>>();
		// trier les propositions
		for (ContextAgentProposition c : this.contextPropositions) {
			ServiceAgentMessage message = c.getServiceAgentMessage();
			if (propositionsList.containsKey(message)) {
				ArrayList<ContextAgentProposition> listContextAgentProposition = propositionsList.get(message);
				// ajouter la proposition de l'agent contexte à la liste des
				// agents contextes qui sont pour le message "message"
				listContextAgentProposition.add(c);
				propositionsList.put(message, listContextAgentProposition);
			} else {
				ArrayList<ContextAgentProposition> newList = new ArrayList<ContextAgentProposition>();
				newList.add(c);
				propositionsList.put(message, newList);
			}
		}
		return propositionsList;
	}

	@Override
	protected void decide() {

		// recuperer la liste des messages
		if (!listProp.isEmpty()) {

			Set<ServiceAgentMessage> listMessage = this.listProp.keySet();
			//ArrayList<ContextAgentProposition> contexAgentBestConfidence = new ArrayList<Pair<ContextAgent, Action>>();

			// Eliminate contradictories actions like nerienfaire and repondre:
			// have only one possible action for a SA message
			// Save the best confidence for decision
			for (ServiceAgentMessage m : listMessage) {
				ArrayList<ContextAgentProposition> subPropList = this.listProp.get(m);
				if (!subPropList.isEmpty()) {
					//contexAgentBestConfidence.add(eliminateContradictoryActionsAndFeedbackThem(subPropList));
					eliminateContradictoryActionsAndFeedbackThem(subPropList);
					//sortALofCandAWithConfidence(subPropList);
					// Sublist with only "best actions" for each message i.e.
					// the action type with the best confidence in the set
					listProp.put(m, subPropList);
				} else {
					// TODO: error
				}
			}

			// Decision of action performed
			choosenActions = getOrderedListOfBestActions();

		} else {
			// TODO: when no proposition
		}

		// this.creerFils();
	}
	
	private static Comparator<ContextAgentProposition> myComparatorOfConfidenceInSubList = new Comparator<ContextAgentProposition>() {
		@Override
		public int compare(ContextAgentProposition cap1, ContextAgentProposition cap2) {
			return cap1.getConfidenceD().compareTo(cap2.getConfidenceD());
		}
	};
	
	private static Comparator<ArrayList<ContextAgentProposition>> myComparatorOfConfidenceList = new Comparator<ArrayList<ContextAgentProposition>>() {
		@Override
		public int compare(ArrayList<ContextAgentProposition> acap1, ArrayList<ContextAgentProposition> acap2) {
			return acap1.get(acap1.size()-1).getConfidenceD().compareTo(acap2.get(acap2.size()-1).getConfidenceD());
		}
	};


	private ArrayList<ArrayList<ContextAgentProposition>> getOrderedListOfBestActions() {
		ArrayList<ArrayList<ContextAgentProposition>> actionsSortedByMessage = new ArrayList<ArrayList<ContextAgentProposition>>(listProp.values());
		//ArrayList<ContextAgentProposition> actionConfidenceForM = new ArrayList<ContextAgentProposition>();
		
		//TODO: Check that each list is not empty
		
		//Sort sub list in order to have the best confidence (at the end of the sub list)
		for (ArrayList<ContextAgentProposition> aListCAP :  actionsSortedByMessage){
			Collections.sort(aListCAP , myComparatorOfConfidenceInSubList);
		}
		
		//Sort list (of sub list) with an ascending order (the sub list with the best confidence is at the end)
		Collections.sort(actionsSortedByMessage , myComparatorOfConfidenceList);
		
//		//Create an array containing 
//		for (ArrayList<ContextAgentProposition> aListCAP :  actionsSortedByMessage){
//			actionConfidenceForM.add(aListCAP.get(aListCAP.size()-1));
//		}

//		Collections.reverse(actionConfidenceForM);
		
//		return actionConfidenceForM;
		
		Collections.reverse(actionsSortedByMessage);
		return actionsSortedByMessage;
	}

	private ContextAgentProposition eliminateContradictoryActionsAndFeedbackThem(
			ArrayList<ContextAgentProposition> subPropList) {
		ContextAgentProposition contextAgentBestConfidence = new ContextAgentProposition(null, null, null);
		if (subPropList.isEmpty()) {
			contextAgentBestConfidence = subPropList.get(subPropList.size() - 1);
			for (ContextAgentProposition cAP : subPropList) {
				if (cAP.getConfidence() > contextAgentBestConfidence.getConfidence()) {
					contextAgentBestConfidence = cAP;
				}
			}
			// for (Pair<ContextAgent, Action> pCA : subPropList){
			for (int i = subPropList.size() - 1; i >= 0; i--) {
				if (subPropList.get(i).getAction().equals(contextAgentBestConfidence.getAction())) {
					// TODO: positive feedback?: no ?
				} else {
					subPropList.remove(i);
					// TODO: negative feedback
				}
			}
		} else {

			// TODO:Error
		}
		return contextAgentBestConfidence;
	}

	private void sortALofCandAWithConfidence(ArrayList<ContextAgentProposition> subPropList) {
		Collections.sort(subPropList, new Comparator<ContextAgentProposition>() {
			@Override
			public int compare(ContextAgentProposition cap1, ContextAgentProposition cap2) {
				return cap1.getConfidenceD().compareTo(cap2.getConfidenceD());
			}
		});
	}

	private Action chooseBestAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void act() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/**
	 * This fonction allows to create a new contextAgent
	 */
	// private void creerFils() {
	// ContextAgent contextAgent = new ContextAgent(
	// lastMessage.getSenderType(), lastMessage.getMessageType(),
	// this.instanceAgent.getChildrenState(), this.isConnected,
	// this.choosenAction, lastMessage.getCardinality(),
	// lastMessage.getNbOfConnection(),
	// lastMessage.getAverageTOConnexion(), this, RECOMPENSE);
	//
	// this.contextAgents.add(contextAgent);
	// }

	/**
	 * This fonction allows to add a new parameter to a context agent
	 * 
	 * @param typeLabel
	 *            is a array contains the type and the name of parameter
	 */
	public void addParameter(String[] typeLabel) {
		/*
		 * String type = typeLabel[0]; String label = typeLabel[1];
		 * ArrayList<String> filePathList = new ArrayList<String>(); // get the
		 * project directory: for example, the directory //
		 * impl-mentationsAgents directory String localPath =
		 * System.getProperty("user.dir"); // get all .java FileWalker
		 * fileWalker = new FileWalker(); // this list contains all java file's
		 * absolute paths filePathList = fileWalker.walk(localPath); for (String
		 * filePath : filePathList) { if (filePath.contains("ContextAgent")) {
		 * setContextAgentFile(type, label, filePath); } }
		 */

	}

	/*
	 * private void setContextAgentFile(String type, String label, String
	 * filePath) { // TODO Auto-generated method stub // read of context agent
	 * file Path path = Paths.get(filePath); Charset charset =
	 * Charset.forName("ISO-8859-1");
	 * 
	 * List<String> lines = null; try { lines = Files.readAllLines(path,
	 * charset); } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * for (String line : lines) { System.out.println(line); } // creation du
	 * fichier copie Path parentDirectory =
	 * Paths.get(path.getParent().toString(), "NewContextAgent.java"); File
	 * outFile = new File(parentDirectory.toString()); try { if
	 * (outFile.createNewFile()) { System.out.println("File is created!");
	 * 
	 * } else { System.out.println("File already exists."); System.out.println(
	 * "chemin fichier: " + outFile.getAbsolutePath()); } } catch (IOException
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * }
	 */

	public boolean isConnected() {
		return isConnected.getFirst();
	}

	public ArrayList<ServiceAgent> getConnectedAgents() {
		return isConnected.getSecond();
	}

	public Pair<Boolean, ArrayList<ServiceAgent>> getCurrentServiceState() {
		return isConnected;
	}

	public ArrayList<ArrayList<Pair<Boolean, ServiceAgent>>> getActualNeighboursState() {
		return instanceAgent.getActualNeighboursState(this);
	}

	public ArrayList<Pair<Boolean, ArrayList<ServiceAgent>>> getActualServicesState() {
		return instanceAgent.getChildrenState();
		// TODO : check instance and change the list to List(same instance,
		// Service agent)
	}

	public ArrayList<AbstractMessage> getMessages() {
		return new ArrayList<AbstractMessage>(messageBox.getMsgs());
	}

	public ArrayList<ServiceAgentMessage> getServiceMessages() {
		return msgBoxHAgent.getSaMessages();
	}

	public Ref<AbstractMessage> getRefBox() {
		return messageBox.getRef();
	}

}
