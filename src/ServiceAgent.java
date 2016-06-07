import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class ServiceAgent extends Agent {
	// Properties
	private static final double RECOMPENSE = 0.5;
	private Pair<Boolean, List<Agent>> isConnected;

	// TODO messages from other services agents and from
	private List<ContextAgent> contextAgents;
	private InstanceAgent instanceAgent;
	private Map<String, Pair<Integer, Double>> nbConnectEtTempMoy;
	private Queue<MessageAgent> messagesBox;
	// action choisie et le dernier message de l'agent service sont mis à jour
	// par la methode decider
	private Action choosenAction;
	private ServiceAgentMessage lastMessage;
	// private Map<String, Action> contextAgentPropositions;
	// liste contenant les propositions des agents contextes
	private List<ContextAgentProposition> contextPropositions;
	private Map<ServiceAgentMessage, List<Pair<ContextAgent, Action>>> listProp;
	// une liste contenant l'ensemble des actions durant un cycle
	private List<Action> listAction;

	// private HashMap<Agent, Pair<Boolean, List<Agent>>> etatsVoisins;
	// Constructor ServiceAgent
	public ServiceAgent(String id, InstanceAgent pere) {
		this.name = id;
		this.instanceAgent = pere;
		this.contextAgents = new ArrayList<ContextAgent>();
		this.nbConnectEtTempMoy = new HashMap<String, Pair<Integer, Double>>();
		this.messagesBox = new PriorityQueue<MessageAgent>();
		choosenAction = null;
		lastMessage = null;
		// this.contextAgentPropositions = new HashMap<String, Action>();
		this.contextPropositions = new ArrayList<ContextAgentProposition>();
		listProp = new HashMap<ServiceAgentMessage, List<Pair<ContextAgent, Action>>>();
		this.listAction = new ArrayList<Action>();
	}

	// Acessors
	// TODO maybe to change
	public List<Pair<Boolean, List<ServiceAgent>>> getNeighboursState() {
		return instanceAgent.getChildrenState();
	}

	// Life Cycle
	@Override
	protected void perceive() {
		// TODO Auto-generated method stub
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
	private Map<ServiceAgentMessage, List<Pair<ContextAgent, Action>>> sortPrositionsList() {
		// TODO Auto-generated method stub
		Map<ServiceAgentMessage, List<Pair<ContextAgent, Action>>> propositionsList = new HashMap<ServiceAgentMessage, List<Pair<ContextAgent, Action>>>();
		// trier les propositions
		for (ContextAgentProposition c : this.contextPropositions) {
			ServiceAgentMessage message = c.getServiceAgentMessage();
			if (propositionsList.containsKey(message)) {
				List<Pair<ContextAgent, Action>> listContextAction = propositionsList
						.get(message);
				// ajouter la proposition de l'agent contexte à la liste des
				// agents contextes qui sont pour le message "message"
				listContextAction.add(new Pair<ContextAgent, Action>(c
						.getContextAgent(), c.getAction()));
				propositionsList.put(message, listContextAction);
			} else {
				List<Pair<ContextAgent, Action>> newList = new ArrayList<Pair<ContextAgent, Action>>();
				newList.add(new Pair<ContextAgent, Action>(c.getContextAgent(),
						c.getAction()));
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
		// recuperer la liste des messages
		Set<ServiceAgentMessage> listMessage = this.listProp.keySet();
		for (ServiceAgentMessage m : listMessage) {
			if (!this.listProp.get(m).isEmpty()) {
				Action action = chooseBestAction();
				this.listAction.add(action);
			} else {
				
			}
		}
		this.creerFils();
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
	private void creerFils() {
		ContextAgent contextAgent = new ContextAgent(
				lastMessage.getSenderType(), lastMessage.getMessageType(),
				this.instanceAgent.getChildrenState(), this.isConnected,
				this.choosenAction, lastMessage.getCardinality(),
				lastMessage.getNbOfConnection(),
				lastMessage.getAverageTOConnexion(), this, RECOMPENSE);

		this.contextAgents.add(contextAgent);
	}

	/**
	 * This fonction allows to add a new parameter to a context agent
	 * 
	 * @param typeLabel
	 *            is a array contains the type and the name of parameter
	 */
	public void addParameter(String[] typeLabel) {
		/*
		 * String type = typeLabel[0]; String label = typeLabel[1]; List<String>
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

	public List<ServiceAgent> getConnectedAgents() {
		return isConnected.getSecond();
	}

}
