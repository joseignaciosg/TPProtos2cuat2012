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

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getMailhost() {
		return mail.split("@")[1];
	}
	
}
