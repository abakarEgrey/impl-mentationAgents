import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import fr.irit.smac.libs.tooling.messaging.AgentMessaging;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;

public class ServiceAgent extends Agent {
	// Properties
	private static final double RECOMPENSE = 0.5;
	private Pair<Boolean, ArrayList<ServiceAgent>> isConnected;

	private Agent connectedAgent;

	// TODO messages from other services agents and from
	private ArrayList<ContextAgent> contextAgents;
	private InstanceAgent instanceAgent;
	private Map<String, Pair<Integer, Double>> nbConnectEtTempMoy;
//	private Queue<MessageAgent> messagesBox;
	// action choisie et le dernier message de l'agent service sont mis à jour
	// par la methode decider
	private Action choosenAction;
	private ServiceAgentMessage lastMessage;
	//private Map<String, Action> contextAgentPropositions;
	// liste contenant les propositions des agents contextes
	private ArrayList<ContextAgentProposition> contextPropositions;
	Map<ServiceAgentMessage, ArrayList<Pair<ContextAgent, Action>>> listProp;

	//
	private IMsgBox<AbstractMessage> messageBox;
	
	// private HashMap<Agent, Pair<Boolean, ArrayList<Agent>>> etatsVoisins;
	// Constructor ServiceAgent
	public ServiceAgent(String id, InstanceAgent pere) {
		this.name = id;
		this.instanceAgent = pere;
		this.contextAgents = new ArrayList<ContextAgent>();
		this.nbConnectEtTempMoy = new HashMap<String, Pair<Integer, Double>>();
//		this.messagesBox = new PriorityQueue<MessageAgent>();
		choosenAction = null;
		lastMessage = null;
		//this.contextAgentPropositions = new HashMap<String, Action>();
		this.contextPropositions = new ArrayList<ContextAgentProposition>();
		listProp = new HashMap<ServiceAgentMessage, ArrayList<Pair<ContextAgent,Action>>>();
		messageBox = (IMsgBox<AbstractMessage>) AgentMessaging.getMsgBox(name, AbstractMessage.class);
		messageBox.getMsgs()
	}

	
	// Acessors
	// TODO maybe to change
	public ArrayList<ArrayList<Pair<Boolean, ServiceAgent>>> getNeighboursState() {
		return instanceAgent.getChildrenState();
		//TODO : check instance and change the list to List(same instance, Service agent)
	}

	// Life Cycle
	@Override
	protected void perceive() {
		// TODO Auto-generated method stub
		// traiter la liste des propositions des agents contextes selon le type de message au cycle precedent
		 this.listProp = treatPrositionsList();
		//on peut ajouter une methode getEnvironmentProperties pour recuperer les caractéristiques de l'environnement
		//envoyer les messages des agents services aux agents contextes
		//les agents contextes accèdent directement à la boite de reception de l'agent service.
		
	}
	/**
	 * Cette méthode permet de traiter les messages
	 * @return
	 */
	private Map<ServiceAgentMessage, ArrayList<Pair<ContextAgent, Action>>> treatPrositionsList() {
		// TODO Auto-generated method stub
		Map<ServiceAgentMessage, ArrayList<Pair<ContextAgent, Action>>> propositionsList = new HashMap<ServiceAgentMessage, ArrayList<Pair<ContextAgent, Action>>>();
		// trier les propositions
		for (ContextAgentProposition c : this.contextPropositions) {
			ServiceAgentMessage message = c.getServiceAgentMessage();
			if (propositionsList.containsKey(message)) {
				ArrayList<Pair<ContextAgent, Action>> listContextAction = propositionsList
						.get(message);
				// ajouter la proposition de l'agent contexte à la liste des
				// agents contextes qui sont pour le message "message"
				listContextAction.add(new Pair<ContextAgent, Action>(c
						.getContextAgent(), c.getAction()));
				propositionsList.put(message, listContextAction);
			} else {
				ArrayList<Pair<ContextAgent, Action>> newList = new ArrayList<Pair<ContextAgent,Action>>();
				newList.add(new Pair<ContextAgent, Action>(c.getContextAgent(), c.getAction()));
				propositionsList.put(message, newList);
			}
		}
		return propositionsList;
	}

	@Override
	protected void decide() {
		// TODO Auto-generated method stub
		// update attribute choosenAction and last message.
		// ...
		
		//this.creerFils();
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
//	private void creerFils() {
//		ContextAgent contextAgent = new ContextAgent(
//				lastMessage.getSenderType(), lastMessage.getMessageType(),
//				this.instanceAgent.getChildrenState(), this.isConnected,
//				this.choosenAction, lastMessage.getCardinality(),
//				lastMessage.getNbOfConnection(),
//				lastMessage.getAverageTOConnexion(), this, RECOMPENSE);
//
//		this.contextAgents.add(contextAgent);
//	}

	/**
	 * This fonction allows to add a new parameter to a context agent
	 * 
	 * @param typeLabel
	 *            is a array contains the type and the name of parameter
	 */
	public void addParameter(String[] typeLabel) {
		/*
		 * String type = typeLabel[0]; String label = typeLabel[1]; ArrayList<String>
		 * filePathList = new ArrayList<String>(); // get the project directory:
		 * for example, the directory // impl-mentationsAgents directory String
		 * localPath = System.getProperty("user.dir"); // get all .java
		 * FileWalker fileWalker = new FileWalker(); // this list contains all
		 * java file's absolute paths filePathList = fileWalker.walk(localPath);
		 * for (String filePath : filePathList) { if
		 * (filePath.contains("ContextAgent")) { setContextAgentFile(type,
		 * label, filePath); } }
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
	 * } else { System.out.println("File already exists.");
	 * System.out.println("chemin fichier: " + outFile.getAbsolutePath()); } }
	 * catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
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

}
