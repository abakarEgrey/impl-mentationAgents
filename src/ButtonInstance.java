import java.util.ArrayList;



public class ButtonInstance extends InstanceAgent {

	/**
	 * l'id est le nom de l'instance
	 */
	private String idServiceAgent;
	/**
	 * 
	 * @param id
	 * @param routage
	 */
	public ButtonInstance(String id, Routage routage, String idServiceAgent) {
		super(id, routage);
		// TODO Auto-generated constructor stub
		this.id = id;
		this.routage = routage;
		this.type = "ButtonInstance";
		this.idServiceAgent = idServiceAgent;
		this.createButtonsAgents();
	}
	/**
	 * creation of 2  buttons: next button and previous button
	 */
	private void createButtonsAgents() {
		// TODO Auto-generated method stub
		ServiceAgent button = new ServiceAgent("@"+this.idServiceAgent, this, 1);
		this.serviceAgents.add(button);
		
	}
	/**
	 * 
	 * @return
	 */
	public ArrayList<ServiceAgent> getServiceAgentList() {
		return this.serviceAgents;
	}
	
	
}
