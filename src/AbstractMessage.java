
public class AbstractMessage {
	
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
