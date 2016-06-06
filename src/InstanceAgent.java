import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class InstanceAgent extends Agent{
//Properties
	//id
	private ArrayList<ServiceAgent> ServiceAgents;
	
	
	
//Constructor
	public InstanceAgent(){
		
	}
	
//Accessor
//	@Override 
//	protected Agent getParent () 
//	{
//		//TODO use an error
//		return null;
//	}

//Life Cycle	
	@Override
	protected void perceive() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void decide() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void act() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	
	public  ArrayList<ArrayList<Pair<Boolean,ServiceAgent>>> getChildrenState() {
		// TODO Auto-generated method stub	: do
		return null;
	}


}
