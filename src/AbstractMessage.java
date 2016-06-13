import java.util.ArrayList;

public class AbstractMessage {

	// This type is not really obligatory, it is more for readability than real
	// cast
	private MessageType type;

	public AbstractMessage(MessageType type) {
		this.type = type;
	}

	public MessageType getType() {
		return type;
	}

	private void setType(MessageType type) {
		this.type = type;
	}

	public static final Pair<ArrayList<ContextAgentProposition>, ArrayList<ServiceAgentMessage>> sortAbstractMIntoCAPandSAM(
			ArrayList<AbstractMessage> abstractMessages) {
		
		ArrayList<ContextAgentProposition> propositions = new ArrayList<ContextAgentProposition>();
		ArrayList<ServiceAgentMessage> saM = new ArrayList<ServiceAgentMessage>();
		
		for (AbstractMessage abstMessage : abstractMessages) {
			if (abstMessage.getType() == MessageType.PROPOSITIONAC) {
				if (abstMessage instanceof ContextAgentProposition) {
					propositions.add((ContextAgentProposition)abstMessage);
				} else {
					// TODO : error
					// Note, the enum was at first here for readability, may be
					// erased
				}
			} else if (abstMessage.getType() == MessageType.SAMESSAGE) {
				if (abstMessage instanceof ServiceAgentMessage) {
					saM.add((ServiceAgentMessage)abstMessage);
				} else {
					// TODO : error
					// Note, the enum was at first here for readability, may be
					// erased
				}
			} else {
				// TODO error
			}

		}
		return new Pair<ArrayList<ContextAgentProposition>, ArrayList<ServiceAgentMessage>>(propositions, saM);
	}
}
