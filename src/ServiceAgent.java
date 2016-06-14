import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.irit.smac.libs.tooling.messaging.AgentMessaging;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;
import fr.irit.smac.libs.tooling.messaging.impl.Ref;

public class ServiceAgent extends Agent {
	// Properties
	private static final double RECOMPENSE = 0.5;
	private static final double defaultAverageTime = 0;
	private static final int defaultNbOfConnection = 0;
	private Pair<Boolean, ArrayList<ServiceAgent>> isConnected;

	//
	private ArrayList<ContextAgent> contextAgents;
	private int countIdContextAgents;

	private InstanceAgent instanceAgent;
	private Map<String, Pair<Integer, Double>> nbOfConnectionAndAverageTime;
	// private Queue<MessageAgent> messagesBox;
	// action choisie et le dernier message de l'agent service sont mis à jour
	// par la methode decider
	private ArrayList<ArrayList<ContextAgentProposition>> choosenActions;
	private int cardinality;

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
	// Attribute contains context agents have proposed action (first) and agent
	// context don't have proposed (second)
	// private Map<ServiceAgentMessage,
	// Pair<ArrayList<ContextAgentProposition>,ArrayList<ContextAgentProposition>>>
	// listPropSorted;
	// The default list contains all messages with the proposition of contexts
	// agents
	private ArrayList<ArrayList<ContextAgentProposition>> listContextAgentNonSelected;
	// Attribute contains a list of messages don't have a context agent
	// proposition
	private ArrayList<ServiceAgentMessage> listOfSAMNoProposition;
	// Attribute contains a list of all actions possibles
	private Set<Action> setOfAllActions;

	// private HashMap<Agent, Pair<Boolean, ArrayList<Agent>>> etatsVoisins;

	// Constructor ServiceAgent
	public ServiceAgent(String id, InstanceAgent parent) {
		this.id = id;
		this.instanceAgent = parent;
		this.contextAgents = new ArrayList<ContextAgent>();
		this.nbOfConnectionAndAverageTime = new HashMap<String, Pair<Integer, Double>>();
		// this.messagesBox = new PriorityQueue<MessageAgent>();
		choosenActions = null;
		lastMessage = null;
		// this.contextAgentPropositions = new HashMap<String, Action>();
		this.contextPropositions = new ArrayList<ContextAgentProposition>();
		listProp = new HashMap<ServiceAgentMessage, ArrayList<ContextAgentProposition>>();
		messageBox = (IMsgBox<AbstractMessage>) AgentMessaging.getMsgBox(id,
				AbstractMessage.class);
		msgBoxHAgent = new SAMsgBoxHistoryAgent(this);
		// this.listPropSorted = new HashMap<ServiceAgentMessage,
		// Pair<ArrayList<ContextAgentProposition>,ArrayList<ContextAgentProposition>>>();
		this.listContextAgentNonSelected = new ArrayList<ArrayList<ContextAgentProposition>>();
		this.listOfSAMNoProposition = new ArrayList<ServiceAgentMessage>();
		this.setOfAllActions = new HashSet<Action>();

	}

	// Constructor ServiceAgent
	public ServiceAgent(String id, InstanceAgent parent, int cardinality) {
		this.id = id;
		this.instanceAgent = parent;
		this.contextAgents = new ArrayList<ContextAgent>();
		this.nbOfConnectionAndAverageTime = new HashMap<String, Pair<Integer, Double>>();
		// this.messagesBox = new PriorityQueue<MessageAgent>();
		choosenActions = null;
		lastMessage = null;
		// this.contextAgentPropositions = new HashMap<String, Action>();
		this.contextPropositions = new ArrayList<ContextAgentProposition>();
		listProp = new HashMap<ServiceAgentMessage, ArrayList<ContextAgentProposition>>();
		messageBox = (IMsgBox<AbstractMessage>) AgentMessaging.getMsgBox(id,
				AbstractMessage.class);
		msgBoxHAgent = new SAMsgBoxHistoryAgent(this);
		this.cardinality = cardinality;

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

		ArrayList<AbstractMessage> mReceived = new ArrayList<AbstractMessage>(
				messageBox.getMsgs());
		Pair<ArrayList<ContextAgentProposition>, ArrayList<ServiceAgentMessage>> sortedMessages = AbstractMessage
				.sortAbstractMIntoCAPandSAM(mReceived);
		contextPropositions = sortedMessages.getFirst();
		serviceAgentMessages = sortedMessages.getSecond();

		// ArrayList<ServiceAgentMessage> mReceivedByAS

		// traiter la liste des propositions des agents contextes selon le type
		// de message au cycle precedent
		this.listProp = sortPrositionsList();
		// add the SAM which have not a proposition to the
		// listOfSAMNoProposition list
		for (ServiceAgentMessage sAM : this.serviceAgentMessages) {
			if (!this.listProp.containsKey(sAM)) {
				this.listOfSAMNoProposition.add(sAM);
			}
		}
		// this.listContextAgentNonSelected = this.listProp;
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
				ArrayList<ContextAgentProposition> listContextAgentProposition = propositionsList
						.get(message);
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
		// clear choosen actions matrix
		this.choosenActions.clear();

		// recuperer la liste des messages
		if (!listProp.isEmpty()) {

			Set<ServiceAgentMessage> listMessage = this.listProp.keySet();
			// ArrayList<ContextAgentProposition> contexAgentBestConfidence =
			// new ArrayList<Pair<ContextAgent, Action>>();

			// Eliminate contradictories actions like nerienfaire and repondre:
			// have only one possible action for a SA message
			// Save the best confidence for decision
			for (ServiceAgentMessage m : listMessage) {
				ArrayList<ContextAgentProposition> subPropList = this.listProp
						.get(m);
				if (!subPropList.isEmpty()) {
					// contexAgentBestConfidence.add(eliminateContradictoryActionsAndFeedbackThem(subPropList));
					subPropList = eliminateContradictoryActionsAndFeedbackThem(
							subPropList, m); // TODO
												// :
												// create
												// list
												// that
												// will
												// be
												// feedback
												// at
												// the
												// act
												// part
					// sortALofCandAWithConfidence(subPropList);
					// Sublist with only "best actions" for each message i.e.
					// the action type with the best confidence in the set
					listProp.put(m, subPropList);
				} else {
					// TODO: error
				}
			}

			// Decision of action performed
			choosenActions = getOrderedListOfBestActions();

			// TODO: choose the limited number of action performed, now it's
			// only one action
			// TODO: positive feedback? yes?

		} else {
			// TODO: when no proposition
			decideOnlyForChoiceAction();

		}

		// this.creerFils();
	}

	private void decideOnlyForChoiceAction() {
		// TODO Auto-generated method stub
		// if no action is executed then create a new context agent assaciated
		// with the action choosed
		if (!this.listOfSAMNoProposition.isEmpty()) {
			for (ServiceAgentMessage sAM : this.listOfSAMNoProposition) {
				// get action
				Action action = sAM.getMessageType();
				switch (action) {
				case ANNONCER:
					// if the service agent isn't connected
					if (!this.isConnected.getFirst()) {
						// view the set of action. if this set contains the
						// action REPONDRE then create a new context agent and
						// send negative feedback to the context agent with the
						// action REPONDRE or ask to delete itself
						if (!this.setOfAllActions.contains(Action.REPONDRE)) {
							createContextAgent(Action.REPONDRE);
							this.setOfAllActions.add(Action.REPONDRE);
						} else {
							// it is a problem with the context agent which not
							// propose his action. send a negative feedback. (For example, when the SA is connected but after some time, SA is deconnected)
						}
					} else {
						//if the service agent is connected
						
					}
					break;
				}
				/*if (this.setOfAllActions.contains(action)) {
					// There is a context agent created with this action. If no
					// context agent is associated with action in the set then
					// remove this action from the set. Some context agents
					// exist but not valid

					createContextAgent(action);

				} else {
					createContextAgent(action);
					this.setOfAllActions.add(action);
				}*/
			}
		} else {
			// No context agents propositions and no messages received from
			// others. So view the state of Service agent
		}
	}

	private void createContextAgent(Action action) {
		// TODO Auto-generated method stub

	}

	private static Comparator<ContextAgentProposition> myComparatorOfConfidenceInSubList = new Comparator<ContextAgentProposition>() {
		@Override
		public int compare(ContextAgentProposition cap1,
				ContextAgentProposition cap2) {
			return cap1.getConfidenceD().compareTo(cap2.getConfidenceD());
		}
	};

	private static Comparator<ArrayList<ContextAgentProposition>> myComparatorOfConfidenceList = new Comparator<ArrayList<ContextAgentProposition>>() {
		@Override
		public int compare(ArrayList<ContextAgentProposition> acap1,
				ArrayList<ContextAgentProposition> acap2) {
			return acap1.get(acap1.size() - 1).getConfidenceD()
					.compareTo(acap2.get(acap2.size() - 1).getConfidenceD());
		}
	};

	private ArrayList<ArrayList<ContextAgentProposition>> getOrderedListOfBestActions() {
		ArrayList<ArrayList<ContextAgentProposition>> actionsSortedByMessage = new ArrayList<ArrayList<ContextAgentProposition>>(
				listProp.values());
		// ArrayList<ContextAgentProposition> actionConfidenceForM = new
		// ArrayList<ContextAgentProposition>();

		// TODO: Check that each list is not empty

		// Sort sub list in order to have the best confidence (at the end of the
		// sub list)
		for (ArrayList<ContextAgentProposition> aListCAP : actionsSortedByMessage) {
			Collections.sort(aListCAP, myComparatorOfConfidenceInSubList);
		}

		// Sort list (of sub list) with an ascending order (the sub list with
		// the best confidence is at the end)
		Collections.sort(actionsSortedByMessage, myComparatorOfConfidenceList);

		// //Create an array containing
		// for (ArrayList<ContextAgentProposition> aListCAP :
		// actionsSortedByMessage){
		// actionConfidenceForM.add(aListCAP.get(aListCAP.size()-1));
		// }

		// Collections.reverse(actionConfidenceForM);

		// return actionConfidenceForM;

		Collections.reverse(actionsSortedByMessage);
		return actionsSortedByMessage;
	}

	private ArrayList<ContextAgentProposition> eliminateContradictoryActionsAndFeedbackThem(
			ArrayList<ContextAgentProposition> subPropList,
			ServiceAgentMessage m) {

		List<ContextAgentProposition> listOfNonSelectedAC = new ArrayList<ContextAgentProposition>();
		List<ContextAgentProposition> subPropListTmp = subPropList;
		// Array list contains the removed elements
		ArrayList<ContextAgentProposition> listOfRemovedAC = new ArrayList<ContextAgentProposition>();
		ContextAgentProposition contextAgentBestConfidence = null;
		if (subPropList.isEmpty()) {
			contextAgentBestConfidence = subPropList
					.get(subPropList.size() - 1);
			for (ContextAgentProposition cAP : subPropList) {
				if (cAP.getConfidence() > contextAgentBestConfidence
						.getConfidence()) {
					contextAgentBestConfidence = cAP;
				}
			}
			// for (Pair<ContextAgent, Action> pCA : subPropList){
			for (int i = subPropListTmp.size() - 1; i >= 0; i--) {
				if (subPropListTmp.get(i).getAction()
						.equals(contextAgentBestConfidence.getAction())) {
					// TODO: positive feedback?: no ?
				} else {
					listOfNonSelectedAC.add(subPropList.get(i));
					listOfRemovedAC.add(subPropList.get(i));
					subPropList.remove(i);
					// TODO: negative feedback
				}
			}
		} else {

			// TODO:Error
		}
		this.listContextAgentNonSelected.add(listOfRemovedAC);
		return subPropList;
	}

	private void sortALofCandAWithConfidence(
			ArrayList<ContextAgentProposition> subPropList) {
		Collections.sort(subPropList,
				new Comparator<ContextAgentProposition>() {
					@Override
					public int compare(ContextAgentProposition cap1,
							ContextAgentProposition cap2) {
						return cap1.getConfidenceD().compareTo(
								cap2.getConfidenceD());
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
		if (!this.choosenActions.isEmpty()) {
			for (ArrayList<ContextAgentProposition> cAA : this.choosenActions) {
				// get list of context agents propositions answered for a
				// message
				// ArrayList<ContextAgentProposition> contextAgentAnswered =
				// this.choosenActions.

				if (!cAA.isEmpty()) {
					// get action to be executed by service agent
					Action actionToBeExecute = cAA.get(0).getAction();
					ServiceAgentMessage sAM = null;
					Pair<Integer, Double> connAndTime = this.nbOfConnectionAndAverageTime
							.get(this.id);
					int nbOfConnection = connAndTime.getFirst();
					double averageTOConnexion = connAndTime.getSecond();
					switch (actionToBeExecute) {
					case ANNONCER:
						// construct the message to be sended
						sAM = new ServiceAgentMessage(this.cardinality, null,
								actionToBeExecute, this.isConnected,
								ServiceAgent.defaultNbOfConnection,
								ServiceAgent.defaultAverageTime, this);
						// Execute action. Here send the message to Instance
						// Agent
						break;
					case REPONDRE:
						// build the reponse message to be sended
						sAM = new ServiceAgentMessage(this.cardinality,
								this.instanceAgent.getType().toString(),
								actionToBeExecute, this.isConnected,
								nbOfConnection, averageTOConnexion, this);

						break;
					case SECONNECTER:
						// build the connection message to be sended
						sAM = new ServiceAgentMessage(this.cardinality,
								this.instanceAgent.getType().toString(),
								actionToBeExecute, this.isConnected,
								nbOfConnection, averageTOConnexion, this);

						break;
					case SEDECONNECTER:
						sAM = new ServiceAgentMessage(this.cardinality,
								this.instanceAgent.getType().toString(),
								actionToBeExecute, this.isConnected,
								nbOfConnection, averageTOConnexion, this);

						break;
					case NERIENFAIRE:
						sAM = new ServiceAgentMessage(this.cardinality,
								this.instanceAgent.getType().toString(),
								actionToBeExecute, this.isConnected,
								nbOfConnection, averageTOConnexion, this);
						// or not send message to the service agent
						break;
					default:
						break;

					}
					this.messageBox.send(sAM, cAA.get(0)
							.getServiceAgentMessage().getRefServiceAgent());

					// give feedbacks for agents contexts
					for (ContextAgentProposition cAP : cAA) {
						cAP.getContextAgent().setFeedBack(1);

					}

				}

			}
			// to optimize the feedback of context where their actions
			// are not good
			if (!this.listContextAgentNonSelected.isEmpty()) {
				for (ArrayList<ContextAgentProposition> listCAP : this.listContextAgentNonSelected) {
					sendFeedBack(listCAP);
				}
			}

		}

	}

	private void sendFeedBack(ArrayList<ContextAgentProposition> listCAP) {
		if (!listCAP.isEmpty()) {
			for (ContextAgentProposition cAP : listCAP) {
				cAP.getContextAgent().setFeedBack(0);
			}
		}
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
