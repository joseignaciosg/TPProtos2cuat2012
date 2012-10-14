package service.state.impl.mail;

public enum Params {
	MAIL_ADDRESS("address"), MAIL_SOCKET("socket");
	
	public final String key;
	
	private Params(String key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		return key;
	}
}
