
public class MessageAgent<T> extends Ref<T> implements IMsgBox<T>{

	public MessageAgent(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Ref<T> getRef() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	IMsgSink<T> getMsgSink() {
		// TODO Auto-generated method stub
		return null;
	}

}
