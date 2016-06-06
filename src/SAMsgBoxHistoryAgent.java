import java.util.ArrayList;

public class SAMsgBoxHistoryAgent extends Agent {
	private ServiceAgent myServiceAgent;

	
	public SAMsgBoxHistoryAgent(ServiceAgent _myServiceAgent) {
		this.myServiceAgent =  _myServiceAgent;
	}

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
		ArrayList<AbstractMessage> abstractMessages = myServiceAgent.getMsgs();
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		 
	}

	
	
}
