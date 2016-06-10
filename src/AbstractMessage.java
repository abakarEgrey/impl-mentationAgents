
public class AbstractMessage {
	
	//This type is not really obligatory, it is more for readability than real cast 
	private MessageType type;
	
	public AbstractMessage (MessageType type)
	{
		this.type = type;
	}

	public MessageType getType() {
		return type;
	}

	private void setType(MessageType type) {
		this.type = type;
	}
}
