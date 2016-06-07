
public class PropositionAC extends AbstractMessage {
		private String message ;
	
	public PropositionAC (String message){
		super(MessageType.PROPOSITIONAC);
		this.message = message;
	}
}
