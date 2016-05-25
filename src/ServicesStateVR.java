

public class ServicesStateVR {
	public String cSInstanceType;
	public Boolean sameInstance; 
	
	public ServicesStateVR (String cSInstanceType, Boolean sameInstance){
		//TODO maybe protect by making a more restricted constructor asking or the agents
		this.cSInstanceType = cSInstanceType;
		this.sameInstance  = sameInstance;
	}
}
