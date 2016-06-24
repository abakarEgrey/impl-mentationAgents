import java.util.ArrayList;

public class ImpressInstance extends InstanceAgent {
	/**
	 * Création d'une instance du composant Impress qui requiert deux interfaces
	 * (agents services) qui sont des boutons et fournit une interface
	 * (affichage du numéro de la page actuelle)
	 */
	
	private ArrayList<ServiceAgent> serviceAgentList = new ArrayList<ServiceAgent>();
	/**
	 * 
	 * @param id
	 * @param routage
	 */
	public ImpressInstance(String id, Routage routage) {
		super(id, routage);
		// TODO Auto-generated constructor stub
		this.id = id;
		this.routage = routage;
		this.createImpressAgents();
	}
	/**
	 * 
	 */
	private void createImpressAgents() {
		// TODO Auto-generated method stub
		//on peut supprimer les variables locaux. Ils sont la juste pour la lisisbilité du code
		ServiceAgent prevSlideRequired = new ServiceAgent("@prevSlideRequired", this, 1);
		ServiceAgent nextSlideRequired = new ServiceAgent("@nextSlideRequired", this, 1);
		
		this.serviceAgentList.add(prevSlideRequired);
		this.serviceAgentList.add(nextSlideRequired);
		
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<ServiceAgent> getServiceAgentList() {
		return serviceAgentList;
	}
	
	

}
