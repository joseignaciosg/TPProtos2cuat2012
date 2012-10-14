package model;

public class User {

	private String mail;
	private String password;
	
	public User(String mail, String password) {
		this.mail = mail;
		this.password = password;
	}
	
	public String getMail() {
		return mail;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getMailhost() {
		String host = mail.split("@")[1];
		int firstDot = host.indexOf("");
		return host.substring(0, firstDot);
	}
	
}
