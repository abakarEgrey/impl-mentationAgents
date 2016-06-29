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
	private static int CountContextAgents = 0;
	// parametres permettant de d'effectuer la normalisation de la valeur de
	// confiance de l'agent service
	private static final int MIN_VALUE = 0;
	private static final int MAX_VALUE = 1;
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
	private Map<ServiceAgentMessage, ArrayList<ContextAgentProposition>> listProp;

	private ArrayList<ServiceAgentMessage> serviceAgentMessages;

	private IMsgBox<AbstractMessage> messageBox;
	// private SAMsgBoxHistoryAgent msgBoxHAgent;
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
	// private Set<Action> setOfAllActions;

	// Attribute contains the number of link used. By default, nbLink = 0. If
	// service agent connect to an other agent service, nbLink is incremented
	private int nbLink;
	// Attribute contains the confidence value. By defaut confidence = 0.5. This
	// value is calculated by the service agent itself. Firstly, we look the
	// statistics of the number of messages accepted of each action
	private double confidence;
	// list of actions choosed and don't have a proposition of context agent.
	// Contexte agent is null
	private ArrayList<ArrayList<ContextAgentProposition>> actionsChoosedByItSelf;
	// may be a provided or requires interface
	private InterfaceType interfaceType;
	// parametre temporaire contenant la liste dess agents services devant
	// recevoir le message annonce
	private ArrayList<ServiceAgent> serviceAgentBrodcastList;

	// private HashMap<Agent, Pair<Boolean, ArrayList<Agent>>> etatsVoisins;

	// Constructor ServiceAgent
	public ServiceAgent(String id, InstanceAgent parent) {
		this.id = id;
		this.instanceAgent = parent;
		this.contextAgents = new ArrayList<ContextAgent>();
		this.nbOfConnectionAndAverageTime = new HashMap<String, Pair<Integer, Double>>();
		// this.messagesBox = new PriorityQueue<MessageAgent>();
		choosenActions = new ArrayList<ArrayList<ContextAgentProposition>>();
		lastMessage = null;
		// this.contextAgentPropositions = new HashMap<String, Action>();
		this.contextPropositions = new ArrayList<ContextAgentProposition>();
		listProp = new HashMap<ServiceAgentMessage, ArrayList<ContextAgentProposition>>();
		messageBox = (IMsgBox<AbstractMessage>) AgentMessaging.getMsgBox(id, AbstractMessage.class);
		// msgBoxHAgent = new SAMsgBoxHistoryAgent(this);
		// this.listPropSorted = new HashMap<ServiceAgentMessage,
		// Pair<ArrayList<ContextAgentProposition>,ArrayList<ContextAgentProposition>>>();
		this.listContextAgentNonSelected = new ArrayList<ArrayList<ContextAgentProposition>>();
		this.listOfSAMNoProposition = new ArrayList<ServiceAgentMessage>();
		// this.setOfAllActions = new HashSet<Action>();
		this.nbLink = 0;
		// we can not modify this with a global evaluation
		this.confidence = ServiceAgent.RECOMPENSE;
		this.actionsChoosedByItSelf = new ArrayList<ArrayList<ContextAgentProposition>>();
		this.cardinality = 1;
		this.serviceAgentBrodcastList = new ArrayList<ServiceAgent>();
		this.isConnected = new Pair<Boolean, ArrayList<ServiceAgent>>(false, new ArrayList<ServiceAgent>());
		this.serviceAgentMessages = new ArrayList<ServiceAgentMessage>();

		// pour test ajout d'une annonce dans le messageBox
		/*
		 * this.messageBox.send(new ServiceAgentMessage(1, null,
		 * MessageType.SAMESSAGE, this.isConnected, 0, 0.0, this,
		 * Action.ANNONCER), this.getRefBox());
		 */

	}

	// Constructor ServiceAgent
	public ServiceAgent(String id, InstanceAgent parent, int cardinality) {
		this.id = id;
		this.instanceAgent = parent;
		this.contextAgents = new ArrayList<ContextAgent>();
		this.nbOfConnectionAndAverageTime = new HashMap<String, Pair<Integer, Double>>();
		// this.messagesBox = new PriorityQueue<MessageAgent>();
		choosenActions = new ArrayList<ArrayList<ContextAgentProposition>>();
		lastMessage = null;
		// this.contextAgentPropositions = new HashMap<String, Action>();
		this.contextPropositions = new ArrayList<ContextAgentProposition>();
		listProp = new HashMap<ServiceAgentMessage, ArrayList<ContextAgentProposition>>();
		messageBox = (IMsgBox<AbstractMessage>) AgentMessaging.getMsgBox(id, AbstractMessage.class);
		// msgBoxHAgent = new SAMsgBoxHistoryAgent(this);
		// this.listPropSorted = new HashMap<ServiceAgentMessage,
		// Pair<ArrayList<ContextAgentProposition>,ArrayList<ContextAgentProposition>>>();
		this.listContextAgentNonSelected = new ArrayList<ArrayList<ContextAgentProposition>>();
		this.listOfSAMNoProposition = new ArrayList<ServiceAgentMessage>();
		// this.setOfAllActions = new HashSet<Action>();
		this.nbLink = 0;
		// we can not modify this with a global evaluation
		this.confidence = ServiceAgent.RECOMPENSE;
		this.actionsChoosedByItSelf = new ArrayList<ArrayList<ContextAgentProposition>>();
		this.cardinality = cardinality;
		this.isConnected = new Pair<Boolean, ArrayList<ServiceAgent>>(false, new ArrayList<ServiceAgent>());
		this.serviceAgentMessages = new ArrayList<ServiceAgentMessage>();

		// pour test ajout d'une annonce dans le messageBox
		/*
		 * this.messageBox.send(new ServiceAgentMessage(1, null,
		 * MessageType.SAMESSAGE, this.isConnected, 0, 0.0, this,
		 * Action.ANNONCER), this.getRefBox());
		 */

		// Pour test
		// Test.listSAM.add(this.msgBoxHAgent);

	}

	// Acessors

	public int getCountIdContextAgents() {
		return countIdContextAgents;
	}

	public ArrayList<ContextAgent> getContextAgents() {
		return contextAgents;
	}

	public ArrayList<ServiceAgent> getServiceAgentBrodcastList() {
		return serviceAgentBrodcastList;
	}

	public void setServiceAgentBrodcastList(ArrayList<ServiceAgent> serviceAgentBrodcastList) {
		this.serviceAgentBrodcastList = serviceAgentBrodcastList;
	}

	public InstanceAgent getInstanceAgent() {
		return instanceAgent;
	}

	public Double getConfidence() {
		return new Double(this.confidence);
	}

	public IMsgBox<AbstractMessage> getMessageBox() {
		return messageBox;
	}

	public void setCountIdContextAgents(int countIdContextAgents) {
		this.countIdContextAgents = countIdContextAgents;
	}

	//a supprimer apres les tests
	public void setContextAgents(ArrayList<ContextAgent> contextAgents) {
		this.contextAgents = contextAgents;
	}

	// Life Cycle
	@Override
	protected void perceive() {
		System.out.println("perceive: " + this.getId() + " Agent service: je suis en cours d'exécution");
		// serviceAgentMessages = this.msgBoxHAgent.getSaMessages();

		// sleep(1000);
		ArrayList<AbstractMessage> mReceived = new ArrayList<AbstractMessage>(messageBox.getMsgs());
		System.out.println("mReceived.size = " + mReceived.size());
		Pair<ArrayList<ContextAgentProposition>, ArrayList<ServiceAgentMessage>> sortedMessages = AbstractMessage
				.sortAbstractMIntoCAPandSAM(mReceived);
		contextPropositions = sortedMessages.getFirst(); // test si vide
		this.serviceAgentMessages = sortedMessages.getSecond();

		System.out.println("les messages envoyés par les autres agents que percoit:");
		for (ServiceAgentMessage sAM : serviceAgentMessages) {
			sAM.display();
		}
		// this.serviceAgentMessages = sortedMessages.getSecond();
		// affichage des propositions des agents contextes

		// execute context agents
		for (ContextAgent cA : this.contextAgents) {
			cA.nextStep();
		}
		// end execute
		ArrayList<AbstractMessage> mReceived2 = new ArrayList<AbstractMessage>(messageBox.getMsgs());
		System.out.println("mReceived.size = " + mReceived2.size());
		Pair<ArrayList<ContextAgentProposition>, ArrayList<ServiceAgentMessage>> sortedMessages2 = AbstractMessage
				.sortAbstractMIntoCAPandSAM(mReceived2);
		contextPropositions = sortedMessages2.getFirst();
		// this.serviceAgentMessages = sortedMessages2.getSecond(); // test si
		// vide

		for (ContextAgentProposition cAP : this.contextPropositions) {
			cAP.display();
		}

		// this.msgBoxHAgent.changePorpositionList(contextPropositions);

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
		System.out.println("perceive : " + this.getId() + " Agent service: mon execution est terminée");
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
		System.out.println("decide: " + this.getId() + " Agent service: je suis en cours d'execution");
		// clear choosen actions matrix
		this.choosenActions.clear();

		// get the list of messages
		if (!listProp.isEmpty()) {

			Set<ServiceAgentMessage> listMessage = this.listProp.keySet();
			// ArrayList<ContextAgentProposition> contexAgentBestConfidence =
			// new ArrayList<Pair<ContextAgent, Action>>();

			// Eliminate contradictories actions like nerienfaire and repondre:
			// have only one possible action for a SA message
			// Save the best confidence for decision
			for (ServiceAgentMessage m : listMessage) {
				ArrayList<ContextAgentProposition> subPropList = this.listProp.get(m);
				if (!subPropList.isEmpty()) {
					// contexAgentBestConfidence.add(eliminateContradictoryActionsAndFeedbackThem(subPropList));
					subPropList = eliminateContradictoryActionsAndFeedbackThem(subPropList, m); // TODO
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
			// lorsque l'agent service ne recoit aucun mmessage. Il doit
			// probabalement choisir l'action annoncer ou ne rien faire. A
			// si l'agent service n'est pas connecté ou le nombre des agents
			// avec lesquels il est connecté est inférieur à la cardinalité alors il frea une annonce
			if (!this.isConnected.getFirst() || this.isConnected.getSecond().size() < this.cardinality) {
				//a competer
			} else {
				//a completer
			}
		}
		// TODO: when no proposition
		/**
		 * the method decideOnlyForChoiceAction allows to choose actions when no
		 * propositions of context agents are available
		 */
		decideOnlyForChoiceAction();
		// sort the list containing the actions choosed by service agent
		// on peut supprimer cette boucle parce que chaque liste contient un
		// seul element
		for (ArrayList<ContextAgentProposition> listCANoProposition : this.actionsChoosedByItSelf) {
			Collections.sort(listCANoProposition, cANoPropositionComparator);
		}
		// sort a list of list
		Collections.sort(this.actionsChoosedByItSelf, sAListComparator);
		// allow to sort all actions (actions proposed by context agent and
		// actions choosed by itself) and all of theses actions is stored in
		// choosenActions
		this.choosenActions = sort2Matrices();

		System.out.println("decide : " + this.getId() + " Agent service: mon execution est terminée");

	}

	// private void decideOnlyForChoiceAction() {
	// // TODO Auto-generated method stub
	// // if no action is executed then create a new context agent assaciated
	// // with the action choosed
	// if (!this.listOfSAMNoProposition.isEmpty()) {
	// for (ServiceAgentMessage sAM : this.listOfSAMNoProposition) {
	// // get action
	// Action action = sAM.getMessageType();
	// // Make the sort of the confidence of service agent here in the
	// // prestep or post step.
	// ArrayList<ServiceAgent> listSA = this.isConnected.getSecond();
	// Collections.sort(listSA, sAComparator);
	// switch (action) {
	// case ANNONCER:
	// // if the service agent isn't connected
	// if (!this.isConnected.getFirst()) {
	// // view the set of action. if this set contains the
	// // action REPONDRE then create a new context agent and
	// // send negative feedback to the context agent with the
	// // action REPONDRE or ask to delete itself
	// if (!this.setOfAllActions.contains(Action.REPONDRE)) {
	// // createContextAgent(Action.REPONDRE);
	// this.setOfAllActions.add(Action.REPONDRE);
	// this.actionsChoosedByItSelf.add(Action.REPONDRE);
	// } else {
	// // it is a problem with the context agent which not
	// // propose his action. send a negative feedback.
	// // (For example, when the SA is connected but after
	// // some time, SA is deconnected)
	// //not true
	// }
	// } else {
	// // if the service agent is connected
	// // compare confidence of service agents of agent service
	// // with others
	// if (this.nbLink >= this.cardinality) {
	//
	// this.actionsChoosedByItSelf.add((listSA.get(0)
	// .getConfidence() < sAM.getServiceAgent()
	// .getConfidence()) ? Action.REPONDRE
	// : Action.NERIENFAIRE);
	//
	// } else {
	// this.actionsChoosedByItSelf.add(Action.REPONDRE);
	// // this.nbLink++;
	// }
	//
	// }
	// break;
	// case REPONDRE:
	// if (!this.isConnected.getFirst()) {
	// //if service agent is not connected then demand accept
	// this.setOfAllActions.add(Action.SECONNECTER);
	// this.actionsChoosedByItSelf.add(Action.SECONNECTER);
	// } else {
	// if (this.nbLink >= this.cardinality) {
	// //
	// this.actionsChoosedByItSelf.add((listSA.get(0)
	// .getConfidence() < sAM.getServiceAgent()
	// .getConfidence()) ? Action.SECONNECTER
	// : Action.NERIENFAIRE);
	//
	// } else {
	// this.actionsChoosedByItSelf.add(Action.REPONDRE);
	// // this.nbLink++;
	// }
	//
	// }
	// break;
	// case SECONNECTER:
	// break;
	// case SEDECONNECTER:
	// break;
	// case NERIENFAIRE:
	// break;
	// default:
	// break;
	// }
	// /*
	// * if (this.setOfAllActions.contains(action)) { // There is a
	// * context agent created with this action. If no // context
	// * agent is associated with action in the set then // remove
	// * this action from the set. Some context agents // exist but
	// * not valid
	// *
	// * createContextAgent(action);
	// *
	// * } else { createContextAgent(action);
	// * this.setOfAllActions.add(action); }
	// */
	// }
	// } else {
	// // No context agents propositions and no messages received from
	// // others. So view the state of Service agent
	// }
	// }
	/**
	 * This method allow to merge the two arrays (this.choosenActions and
	 * this.actionsChoosedByItSelf) and to order them
	 * 
	 * @return
	 */
	private ArrayList<ArrayList<ContextAgentProposition>> sort2Matrices() {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<ContextAgentProposition>> tmp = new ArrayList<ArrayList<ContextAgentProposition>>();
		int indexChoosenList = 0;
		int indexChoosedBySelf = 0;

		while ((indexChoosenList != this.choosenActions.size())
				&& (indexChoosedBySelf != this.actionsChoosedByItSelf.size())) {
			// look the array of actions choosed by itself to sort the final
			// list
			// the first element of of arrayList cap is greater or equal
			// than the element of indexChoosenList-th of the first element
			// of
			ArrayList<ContextAgentProposition> choosenActionArrayList = this.choosenActions.get(indexChoosenList);
			ArrayList<ContextAgentProposition> chooseBySelfArrayList = this.actionsChoosedByItSelf
					.get(indexChoosedBySelf);
			if (chooseBySelfArrayList.get(0).getServiceAgentMessage().getServiceAgent()
					.getConfidence() >= choosenActionArrayList.get(0).getServiceAgentMessage().getServiceAgent()
							.getConfidence()) {
				tmp.add(chooseBySelfArrayList);
				indexChoosedBySelf++;

			} else {
				tmp.add(choosenActionArrayList);
				indexChoosenList++;
			}
		}

		if (indexChoosenList == this.choosenActions.size()) {
			for (int i = indexChoosedBySelf; i < this.actionsChoosedByItSelf.size(); i++) {
				tmp.add(this.actionsChoosedByItSelf.get(i));
			}
		} else {
			for (int i = indexChoosenList; i < this.actionsChoosedByItSelf.size(); i++) {
				tmp.add(this.choosenActions.get(i));
			}
		}
		return tmp;
	}

	/**
	 * the method decideOnlyForChoiceAction allows to choose actions when no
	 * propositions of context agents are available
	 */
	private void decideOnlyForChoiceAction() {
		// TODO Auto-generated method stub
		// A faire, si l'action choisit n'est pas definitif alors il faut chager
		// l'action de l'agent contexte dans act.
		if (!this.listOfSAMNoProposition.isEmpty()) {
			for (ServiceAgentMessage sAM : this.listOfSAMNoProposition) {
				// get action
				Action action = sAM.getActionType();
				// Make the sort of the confidence of service agent here in the
				// prestep or post step.
				ContextAgentProposition cap;
				ArrayList<ContextAgentProposition> listCAP;
				switch (action) {
				case ANNONCER:
					cap = new ContextAgentProposition(null, Action.REPONDRE, sAM);
					listCAP = new ArrayList<ContextAgentProposition>();
					listCAP.add(cap);
					// cap.display();
					this.actionsChoosedByItSelf.add(listCAP);
					break;
				case REPONDRE:
					cap = new ContextAgentProposition(null, Action.SECONNECTER, sAM);
					listCAP = new ArrayList<ContextAgentProposition>();
					listCAP.add(cap);
					// cap.display();
					this.actionsChoosedByItSelf.add(listCAP);
					// this.actionsChoosedByItSelf.add(Action.SECONNECTER);
					break;
				case SECONNECTER:
					cap = new ContextAgentProposition(null, Action.SECONNECTERPHYSIQUEMENT, sAM);
					listCAP = new ArrayList<ContextAgentProposition>();
					listCAP.add(cap);
					// cap.display();
					this.actionsChoosedByItSelf.add(listCAP);
					// this.actionsChoosedByItSelf.get(0).get(0).getServiceAgentMessage().getServiceAgent().getConfidence();
					// this.actionsChoosedByItSelf.add(Action.SECONNECTERPHYSIQUEMENT);
					break;
				case SEDECONNECTER:
					// reduce the cardinality
					cap = new ContextAgentProposition(null, Action.NERIENFAIRE, sAM);
					listCAP = new ArrayList<ContextAgentProposition>();
					listCAP.add(cap);
					// cap.display();
					this.actionsChoosedByItSelf.add(listCAP);
					// this.actionsChoosedByItSelf.add(Action.NERIENFAIRE);
					break;
				case NERIENFAIRE:
					cap = new ContextAgentProposition(null, Action.NERIENFAIRE, sAM);
					listCAP = new ArrayList<ContextAgentProposition>();
					listCAP.add(cap);
					this.actionsChoosedByItSelf.add(listCAP);
					// cap.display();
					// this.actionsChoosedByItSelf.add(Action.NERIENFAIRE);
					break;
				default:
					// normalement pas d'entrée dans default aprceque toutes les
					// actions sont listées
					cap = new ContextAgentProposition(null, Action.NERIENFAIRE, sAM);
					listCAP = new ArrayList<ContextAgentProposition>();
					listCAP.add(cap);
					cap.display();
					this.actionsChoosedByItSelf.add(listCAP);
					// this.actionsChoosedByItSelf.add(Action.NERIENFAIRE);
					break;
				}
			}
		}

	}

	/**
	 * This method allows to create a context agent and add it to the list of
	 * context agents
	 * 
	 * @param action
	 * @param cAA
	 */
	private void createContextAgent(Action action, ArrayList<ContextAgentProposition> cAA) {
		// TODO Auto-generated method stub
		// ContextAgent ca = new ContextAgent(senderType, messageType,
		// neightboursState, serviceAgentState, actionPerformed, serviceAgent,
		// confidence, id);
		ContextAgent ca = new ContextAgent(this.instanceAgent.getType().toString(),
				cAA.get(0).getServiceAgentMessage().getActionType(), this.instanceAgent.getNeightboursState(this),
				this.isConnected, action, this, ServiceAgent.RECOMPENSE, this.id + ServiceAgent.CountContextAgents);
		ServiceAgent.CountContextAgents++;
		this.contextAgents.add(ca);
		ca.display();

	}

	private static Comparator<ContextAgentProposition> myComparatorOfConfidenceInSubList = new Comparator<ContextAgentProposition>() {
		@Override
		public int compare(ContextAgentProposition cap1, ContextAgentProposition cap2) {
			return cap1.getConfidenceD().compareTo(cap2.getConfidenceD());
		}
	};

	// definition of comparator of service agent confidence
	private static Comparator<ContextAgentProposition> cANoPropositionComparator = new Comparator<ContextAgentProposition>() {

		@Override
		public int compare(ContextAgentProposition cAP1, ContextAgentProposition cAP2) {
			// TODO Auto-generated method stub
			return cAP1.getServiceAgentMessage().getServiceAgent().getConfidence()
					.compareTo(cAP2.getServiceAgentMessage().getServiceAgent().getConfidence());
		}
	};

	private static Comparator<ArrayList<ContextAgentProposition>> sAListComparator = new Comparator<ArrayList<ContextAgentProposition>>() {
		@Override
		public int compare(ArrayList<ContextAgentProposition> acap1, ArrayList<ContextAgentProposition> acap2) {
			return acap2.get(acap2.size() - 1).getServiceAgentMessage().getServiceAgent().getConfidence()
					.compareTo(acap1.get(acap1.size() - 1).getServiceAgentMessage().getServiceAgent().getConfidence());
		}
	};

	private static Comparator<ArrayList<ContextAgentProposition>> myComparatorOfConfidenceList = new Comparator<ArrayList<ContextAgentProposition>>() {
		@Override
		public int compare(ArrayList<ContextAgentProposition> acap1, ArrayList<ContextAgentProposition> acap2) {
			return acap1.get(acap1.size() - 1).getConfidenceD().compareTo(acap2.get(acap2.size() - 1).getConfidenceD());
		}
	};

	// comparator service agent proposition
	private static Comparator<ServiceAgent> sAComparator = new Comparator<ServiceAgent>() {

		@Override
		public int compare(ServiceAgent sA0, ServiceAgent sA1) {
			// TODO Auto-generated method stub
			return sA0.getConfidence().compareTo(sA1.getConfidence());
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

	/**
	 * 
	 * @param subPropList
	 *            is not empty because it is tested in the method decide()
	 * @param m
	 * @return
	 */
	private ArrayList<ContextAgentProposition> eliminateContradictoryActionsAndFeedbackThem(
			ArrayList<ContextAgentProposition> subPropList, ServiceAgentMessage m) {

		// List<ContextAgentProposition> listOfNonSelectedAC = new
		// ArrayList<ContextAgentProposition>();
		List<ContextAgentProposition> subPropListTmp = subPropList;
		// Array list contains the removed elements
		ArrayList<ContextAgentProposition> listOfRemovedAC = new ArrayList<ContextAgentProposition>();
		ContextAgentProposition contextAgentBestConfidence = null;
		contextAgentBestConfidence = subPropList.get(subPropList.size() - 1);
		for (ContextAgentProposition cAP : subPropList) {
			if (cAP.getConfidence() > contextAgentBestConfidence.getConfidence()) {
				contextAgentBestConfidence = cAP;
			}
		}
		// for (Pair<ContextAgent, Action> pCA : subPropList){
		for (int i = subPropListTmp.size() - 1; i >= 0; i--) {
			if (subPropListTmp.get(i).getAction().equals(contextAgentBestConfidence.getAction())) {
				// TODO: positive feedback?: no ?
			} else {
				// listOfNonSelectedAC.add(subPropList.get(i));
				listOfRemovedAC.add(subPropList.get(i));
				subPropList.remove(i);
				// TODO: negative feedback
			}
		}

		this.listContextAgentNonSelected.add(listOfRemovedAC);
		return subPropList;
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

	private Action factorize(ArrayList<ContextAgentProposition> cAA, ServiceAgentMessage sAM, int nbOfConnection,
			double averageTOConnexion, Action actionToBeExecute, ArrayList<ServiceAgent> sAConnected) {
		Action action;

		if (cAA.get(0).getServiceAgentMessage().getServiceAgent().getConfidence() > sAConnected.get(0)
				.getConfidence()) {
			action = actionToBeExecute;
			sAM = new ServiceAgentMessage(this.cardinality, null, MessageType.SAMESSAGE, this.isConnected,
					ServiceAgent.defaultNbOfConnection, ServiceAgent.defaultAverageTime, this, action);
			// execute the action
			if (actionToBeExecute == Action.ANNONCER) {
				this.messageBox.send(sAM, this.instanceAgent.getRefInstanceAgent());
			} else {
				this.messageBox.send(sAM, cAA.get(0).getServiceAgentMessage().getRefServiceAgent());
				// no feedback for context agents and create context
				// agent
				// this.createContextAgent(action);
			}
			if (cAA.get(0).getContextAgent() != null) {
				// feedback to context agents
				// give feedbacks for all agents contexts are
				// proposed this action
				for (ContextAgentProposition cAP : cAA) {
					cAP.getContextAgent().setFeedBack(1);
				}
			}

		} else {
			action = Action.NERIENFAIRE;
			sAM = new ServiceAgentMessage(this.cardinality, null, MessageType.SAMESSAGE, this.isConnected,
					nbOfConnection, averageTOConnexion, this, action);
			// execute the action
			this.messageBox.send(sAM, cAA.get(0).getServiceAgentMessage().getRefServiceAgent());
			if (cAA.get(0).getContextAgent() == null) {
				// neagtive feedback because the action proposed by
				// context agents is not choosed
				for (ContextAgentProposition cAP : cAA) {
					cAP.getContextAgent().setFeedBack(0);

				}
			}
		}
		return action;
	}

	@Override
	protected void act() {
		System.out.println("act: " + this.getId() + " Agent service: je suis en cours d'execution");
		// TODO Auto-generated method stub

		if (!this.choosenActions.isEmpty()) {
			// number of connection to rest
			int nbConnectionRemain = this.cardinality - this.nbLink;
			ArrayList<ContextAgentProposition> cAA;
			ServiceAgentMessage sAM = null;
			// Pair<Integer, Double> connAndTime =
			// this.nbOfConnectionAndAverageTime
			// .get(this.id);
			Pair<Integer, Double> connAndTime = null;
			int nbOfConnection = 0;
			double averageTOConnexion = 0;
			// id de l'agent service qui envoyé le message
			String senderId = "";
			int index = 0;
			while (nbConnectionRemain > 0 && (index < this.choosenActions.size())) { // while
																						// the
																						// number
																						// of
																						// connection
																						// remaining
																						// is
																						// greater
																						// than
																						// 0
																						// or
																						// at
																						// the
																						// end
																						// of
																						// this.choosenActions
				// treat actions when the service agent have some connections
				// available
				cAA = this.choosenActions.get(index);
				senderId = cAA.get(0).getServiceAgentMessage().getServiceAgent().getId();
				if (this.nbOfConnectionAndAverageTime.containsKey(senderId)) {
					connAndTime = this.nbOfConnectionAndAverageTime.get(senderId);
					nbOfConnection = connAndTime.getFirst();
					averageTOConnexion = connAndTime.getSecond();
				} else {
					connAndTime = null;
					nbOfConnection = 0;
					averageTOConnexion = 0;
				}

				index++;
				Action actionToBeExecute = cAA.get(0).getAction();
				if (!cAA.isEmpty()) {
					// test if the action is the one choosed by the service
					// agent
					if ((cAA.size() == 1) && (cAA.get(0).getContextAgent() == null)) {

						nbConnectionRemain = this.sendMessage(actionToBeExecute,
								cAA.get(0).getServiceAgentMessage().getRefServiceAgent(), sAM, nbOfConnection,
								averageTOConnexion, nbConnectionRemain, true, cAA);
						// end of if when the action is choosed by the service
						// agent
					} else { // when the actions by the proposition of context
								// agent
						nbConnectionRemain = this.sendMessage(actionToBeExecute,
								cAA.get(0).getServiceAgentMessage().getRefServiceAgent(), sAM, nbOfConnection,
								averageTOConnexion, nbConnectionRemain, false, cAA);
					}

				}

			} // end while
				// il faut regarder les agents services avec les quels il est
				// connecté
			ArrayList<ServiceAgent> sAConnected = this.isConnected.getSecond();
			// sort theses services agents by their confidence
			Collections.sort(sAConnected, sAComparator);
			// We go in in this loop only where the number of connection is
			// greater or equal than the cardinality
			for (int i = index; i < this.choosenActions.size(); i++) {
				cAA = this.choosenActions.get(index);
				Action actionToBeExecute = cAA.get(0).getAction();
				Action action;
				switch (actionToBeExecute) {
				case ANNONCER:

					// if context agent propose an action then compare the
					// context agent's confidence with the service agent
					// confidence
					// else compare the service agent (itself) confidence with
					// the service agent with the smallest confidence
					if ((cAA.size() == 1) && (cAA.get(0).getContextAgent() == null)) {
						// no proposition of the context agent then compare
						// service agent confidence with the confidence of
						// connected service agents
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);
						// create context agent
						this.createContextAgent(action, cAA);

					} else {
						// proposition of context agent
						// Action action = (cAA.get(0).getConfidence() >
						// sAConnected.get(0).getConfidence()) ? Action.ANNONCER
						// : Action.NERIENFAIRE;

						// si l'action propose par l'agent contexte est bonne
						/*
						 * if (cAA.get(0).getConfidence() > sAConnected.get(0)
						 * .getConfidence()) { action = Action.ANNONCER; //
						 * response to the context agent and service agent //
						 * which send the message sAM = new
						 * ServiceAgentMessage(this.cardinality, null, action,
						 * this.isConnected, ServiceAgent.defaultNbOfConnection,
						 * ServiceAgent.defaultAverageTime, this); // execute
						 * the action this.messageBox.send(sAM,
						 * this.instanceAgent.getRefInstanceAgent()); //
						 * feedback to context agents // give feedbacks for all
						 * agents contexts are // proposed this action for
						 * (ContextAgentProposition cAP : cAA) {
						 * cAP.getContextAgent().setFeedBack(1);
						 * 
						 * }
						 * 
						 * } else { // action proposee
						 * cAA.get(0).getConfidence() a // une confiance plus
						 * faible que // sAConnected.get(0).getConfidence() où
						 * // sAConnected contient la liste des agents //
						 * services connectes a this action =
						 * Action.NERIENFAIRE; sAM = new
						 * ServiceAgentMessage(this.cardinality, null, action,
						 * this.isConnected, nbOfConnection, averageTOConnexion,
						 * this); // execute the action
						 * this.messageBox.send(sAM, cAA.get(0)
						 * .getServiceAgentMessage() .getRefServiceAgent()); //
						 * neagtive feedback because the action proposed by //
						 * context agents is not choosed for
						 * (ContextAgentProposition cAP : cAA) {
						 * cAP.getContextAgent().setFeedBack(0);
						 * 
						 * }
						 * 
						 * }
						 */
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);

					}
					// creer une fonction pour factoriser le code
					break;
				case REPONDRE:
					// L'action repondre est choisie. Il se peut que cette
					// action soit validée définitivement ou l'action ne rien
					// faire sera choisie
					if ((cAA.size() == 1) && (cAA.get(0).getContextAgent() == null)) {
						// no proposition of the context agent then compare
						// service agent confidence with the confidence of
						// connected service agents
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);
						// create context agent
						this.createContextAgent(action, cAA);

					} else {
						// proposition of context agent
						// Action action = (cAA.get(0).getConfidence() >
						// sAConnected.get(0).getConfidence()) ? Action.ANNONCER
						// : Action.NERIENFAIRE;

						// si l'action propose par l'agent contexte est bonne
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);

					}
					break;
				case SECONNECTER:
					// L'action seconnecter est choisie. Il se peut que cette
					// action soit validée définitivement ou l'action ne rien
					// faire sera choisie
					if ((cAA.size() == 1) && (cAA.get(0).getContextAgent() == null)) {
						// no proposition of the context agent then compare
						// service agent confidence with the confidence of
						// connected service agents
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);
						// create context agent
						this.createContextAgent(action, cAA);

					} else {
						// proposition of context agent
						// Action action = (cAA.get(0).getConfidence() >
						// sAConnected.get(0).getConfidence()) ? Action.ANNONCER
						// : Action.NERIENFAIRE;

						// si l'action propose par l'agent contexte est bonne
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);

					}
					break;
				case SECONNECTERPHYSIQUEMENT:
					// la connection physique fera appel à des fonction WComp
					if ((cAA.size() == 1) && (cAA.get(0).getContextAgent() == null)) {
						// no proposition of the context agent then compare
						// service agent confidence with the confidence of
						// connected service agents
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);
						// mise à jour de la map contenant le nombre de
						// connection
						connAndTime = new Pair<Integer, Double>(nbOfConnection + 1, averageTOConnexion);
						this.nbOfConnectionAndAverageTime.put(senderId, connAndTime);
						// create context agent
						this.createContextAgent(action, cAA);

					} else {
						// proposition of context agent
						// Action action = (cAA.get(0).getConfidence() >
						// sAConnected.get(0).getConfidence()) ? Action.ANNONCER
						// : Action.NERIENFAIRE;

						// si l'action propose par l'agent contexte est bonne
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);

					}
					// incremente the number of link
					this.nbLink++;
					break;
				case SEDECONNECTER:
					if ((cAA.size() == 1) && (cAA.get(0).getContextAgent() == null)) {
						// no proposition of the context agent then compare
						// service agent confidence with the confidence of
						// connected service agents
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);
						// create context agent
						this.createContextAgent(action, cAA);

					} else {
						// proposition of context agent
						// Action action = (cAA.get(0).getConfidence() >
						// sAConnected.get(0).getConfidence()) ? Action.ANNONCER
						// : Action.NERIENFAIRE;

						// si l'action propose par l'agent contexte est bonne
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);

					}
					// mise à jour du temps moyen
					// connAndTime = new Pair<Integer, Double>(nbOfConnection,
					// averageTOConnexion + temps de connection);
					// this.nbOfConnectionAndAverageTime.put(senderId,
					// connAndTime);
					// decremente the number of link
					this.nbLink--;
					break;
				case NERIENFAIRE:
					if ((cAA.size() == 1) && (cAA.get(0).getContextAgent() == null)) {
						// no proposition of the context agent then compare
						// service agent confidence with the confidence of
						// connected service agents
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);
						// create context agent
						this.createContextAgent(action, cAA);

					} else {
						// proposition of context agent
						// Action action = (cAA.get(0).getConfidence() >
						// sAConnected.get(0).getConfidence()) ? Action.ANNONCER
						// : Action.NERIENFAIRE;

						// si l'action propose par l'agent contexte est bonne
						action = this.factorize(cAA, sAM, nbOfConnection, averageTOConnexion, actionToBeExecute,
								sAConnected);

					}
					break;
				default:
					break;
				}

			}

		}

		// no cardinality is avalaible
		/*
		 * for (int i = index; i < this.choosenActions.size(); i++) { // il faut
		 * regarder les agents services avec les quels il est // connecté
		 * 
		 * for (int i = nbConnectionRemain + 1; i < this.choosenActions.size();
		 * i++) {
		 * 
		 * }
		 * 
		 * for (ArrayList<ContextAgentProposition> cAA : this.choosenActions) {
		 * // get list of context agents propositions answered for a // message
		 * or the action choosed by the agent service //
		 * ArrayList<ContextAgentProposition> contextAgentAnswered = //
		 * this.choosenActions.
		 * 
		 * if (!cAA.isEmpty()) { // test if the action is the one choosed by the
		 * service // agent if ((cAA.size() == 1) &&
		 * (cAA.get(0).getContextAgent() == null)) {
		 * 
		 * } // get action to be executed by service agent Action
		 * actionToBeExecute = cAA.get(0).getAction(); ServiceAgentMessage sAM =
		 * null; Pair<Integer, Double> connAndTime =
		 * this.nbOfConnectionAndAverageTime .get(this.id); int nbOfConnection =
		 * connAndTime.getFirst(); double averageTOConnexion =
		 * connAndTime.getSecond(); switch (actionToBeExecute) { case ANNONCER:
		 * // construct the message to be sended sAM = new
		 * ServiceAgentMessage(this.cardinality, null, actionToBeExecute,
		 * this.isConnected, ServiceAgent.defaultNbOfConnection,
		 * ServiceAgent.defaultAverageTime, this); // Execute action. Here send
		 * the message to Instance // Agent break; case REPONDRE: // build the
		 * reponse message to be sended sAM = new
		 * ServiceAgentMessage(this.cardinality,
		 * this.instanceAgent.getType().toString(), actionToBeExecute,
		 * this.isConnected, nbOfConnection, averageTOConnexion, this);
		 * 
		 * break; case SECONNECTER: // build the connection message to be sended
		 * sAM = new ServiceAgentMessage(this.cardinality,
		 * this.instanceAgent.getType().toString(), actionToBeExecute,
		 * this.isConnected, nbOfConnection, averageTOConnexion, this);
		 * 
		 * break; case SEDECONNECTER: sAM = new
		 * ServiceAgentMessage(this.cardinality,
		 * this.instanceAgent.getType().toString(), actionToBeExecute,
		 * this.isConnected, nbOfConnection, averageTOConnexion, this);
		 * 
		 * break; case NERIENFAIRE: sAM = new
		 * ServiceAgentMessage(this.cardinality,
		 * this.instanceAgent.getType().toString(), actionToBeExecute,
		 * this.isConnected, nbOfConnection, averageTOConnexion, this); // or
		 * not send message to the service agent break; default: break;
		 * 
		 * } this.messageBox.send(sAM, cAA.get(0)
		 * .getServiceAgentMessage().getRefServiceAgent());
		 * 
		 * // give feedbacks for agents contexts for (ContextAgentProposition
		 * cAP : cAA) { cAP.getContextAgent().setFeedBack(1);
		 * 
		 * }
		 * 
		 * }
		 * 
		 * } // to optimize the feedback of context where their actions // are
		 * not good if (!this.listContextAgentNonSelected.isEmpty()) { for
		 * (ArrayList<ContextAgentProposition> listCAP :
		 * this.listContextAgentNonSelected) { sendFeedBack(listCAP); } }
		 * 
		 * }
		 */
		System.out.println("taille liste agents contextes = "+this.contextAgents.size());
		for (ContextAgent cA : this.contextAgents){
			System.out.println("idContextAgent = "+cA.getId());
		}
		System.out.println("act : " + this.getId() + " Agent service: mon exécution est terminée");
	}

	private int sendMessage(Action actionToBeExecute, Ref<AbstractMessage> serviceAgentRef, ServiceAgentMessage sAM,
			int nbOfConnection, double averageTOConnexion, int nbConnectionRemain, boolean createContextAgent,
			ArrayList<ContextAgentProposition> cAA) {
		switch (actionToBeExecute) {
		case ANNONCER:
			// construct the message to be sended
			sAM = new ServiceAgentMessage(this.cardinality, null, MessageType.SAMESSAGE, this.isConnected,
					ServiceAgent.defaultNbOfConnection, ServiceAgent.defaultAverageTime, this, actionToBeExecute);
			// execute the action after and create context agent
			// createContextAgent(Action.ANNONCER);
			// commenter cette ligne temporairement le temps de l'essai
			// this.messageBox.send(sAM,
			// this.instanceAgent.getRefInstanceAgent());
			this.broadcast(serviceAgentBrodcastList, sAM);
			break;
		case REPONDRE:
			// build the reponse message to be sended
			sAM = new ServiceAgentMessage(this.cardinality, this.instanceAgent.getType().toString(),
					MessageType.SAMESSAGE, this.isConnected, nbOfConnection, averageTOConnexion, this,
					actionToBeExecute);
			// Execute action after
			this.messageBox.send(sAM, serviceAgentRef);
			break;
		case SECONNECTER:
			// build the connection message to be sended
			sAM = new ServiceAgentMessage(this.cardinality, this.instanceAgent.getType().toString(),
					MessageType.SAMESSAGE, this.isConnected, nbOfConnection, averageTOConnexion, this,
					actionToBeExecute);
			// create context agent
			nbConnectionRemain--;
			this.nbLink++;
			this.messageBox.send(sAM, serviceAgentRef);
			break;
		case SECONNECTERPHYSIQUEMENT:
			// build the connection message to be sended
			sAM = new ServiceAgentMessage(this.cardinality, this.instanceAgent.getType().toString(),
					MessageType.SAMESSAGE, this.isConnected, nbOfConnection, averageTOConnexion, this,
					actionToBeExecute);
			// incremente the number of link
			this.nbLink++;
			nbConnectionRemain--;
			this.messageBox.send(sAM, serviceAgentRef);
			break;
		case SEDECONNECTER:
			sAM = new ServiceAgentMessage(this.cardinality, this.instanceAgent.getType().toString(),
					MessageType.SAMESSAGE, this.isConnected, nbOfConnection, averageTOConnexion, this,
					actionToBeExecute);
			// decremente the number of link
			this.nbLink--;
			// a discuter s'il faut prendre en compte dans ce cycle ou au cycle
			// suivant
			nbConnectionRemain++;
			this.messageBox.send(sAM, serviceAgentRef);
			break;
		case NERIENFAIRE:
			sAM = new ServiceAgentMessage(this.cardinality, this.instanceAgent.getType().toString(),
					MessageType.SAMESSAGE, this.isConnected, nbOfConnection, averageTOConnexion, this,
					actionToBeExecute);
			// or not send message to the service agent
			this.messageBox.send(sAM, serviceAgentRef);
			break;
		default:
			break;
		}
		if (createContextAgent)
			createContextAgent(actionToBeExecute, cAA);
		return nbConnectionRemain;
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
		this.removeAllContextAgents();
		this.instanceAgent.remove(this);

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
		// return msgBoxHAgent.getSaMessages();
		return this.serviceAgentMessages;
	}

	public Ref<AbstractMessage> getRefBox() {
		return messageBox.getRef();
	}

	/**
	 * method allows to delete context agents
	 * 
	 * @param contextAgent
	 */
	public boolean remove(ContextAgent contextAgent) {
		// TODO Auto-generated method stub
		for (ContextAgent ca : this.contextAgents) {
			if (ca.getId().equals(contextAgent.getId())) {
				this.contextAgents.remove(ca);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 */
	private void removeAllContextAgents() {
		this.contextAgents.clear();
	}

	/**
	 * 
	 * @param serviceAgentList
	 * @param message
	 */
	public void broadcast(ArrayList<ServiceAgent> serviceAgentList, AbstractMessage message) {
		for (ServiceAgent sa : serviceAgentList) {
			this.messageBox.send(message, sa.getRefBox());
		}
	}

	private void calculateServiceAgentConfidence(String serviceAgentId) {
		double nbOfConnectionNormalized = 0;
		double averageTimeNormalized = 0;
		// il faut tester si l'agent service qui envoyé le message est dans sa
		// liste des agents ou pas
		if (this.nbOfConnectionAndAverageTime.containsKey(serviceAgentId)) {
			nbOfConnectionNormalized = normalize(0, Integer.MAX_VALUE,
					this.nbOfConnectionAndAverageTime.get(this.id).getFirst(), 0.0);
			averageTimeNormalized = normalize(0, Integer.MAX_VALUE, 0,
					this.nbOfConnectionAndAverageTime.get(this.id).getSecond());
		} else {
			// Il ne s'est jamais connecté avec cet agent service.
			nbOfConnectionNormalized = 0;
			averageTimeNormalized = 0;
		}

		this.confidence = (nbOfConnectionNormalized + averageTimeNormalized) / 2;

	}

	/**
	 * This method allows to normalize the values of nbConnection et averageTime
	 *
	 * @param minValue
	 *            : the min value of the variable. In our case it is always 0
	 * @param maxValue
	 *            : the max value of the variable. For the nbOfConnection, it is
	 *            equal to Integer.MAX_VALUE and for the averageTime, it is
	 *            equal to Doule.MAX_VALUE
	 * @param nbConnections
	 * @param averageTime
	 * @return
	 */
	private double normalize(int minValue, int maxValue, int nbConnections, Double averageTime) {
		// TODO Auto-generated method stub
		double normalizedValue = 0;
		double normalizationCoeff = (ServiceAgent.MIN_VALUE - ServiceAgent.MAX_VALUE) / (minValue - maxValue);
		// normalize the nbOfCoonections value
		if (nbConnections != 0) {
			normalizedValue = ServiceAgent.MIN_VALUE - normalizationCoeff * minValue
					+ normalizationCoeff * nbConnections;
			return normalizedValue;

		}
		// normalise the averageTime value
		normalizedValue = ServiceAgent.MIN_VALUE - normalizationCoeff * minValue + normalizationCoeff * averageTime;

		return normalizedValue;
	}
}
