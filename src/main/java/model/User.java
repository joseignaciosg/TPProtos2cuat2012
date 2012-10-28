package model;

import java.util.HashMap;
import java.util.Map;

import model.mail.Mail;

public class User {

	private String mail;
	private String password;
	private Map<String, Mail> cachedMails;
	
	public User(String mail, String password) {
		this.mail = mail;
		this.password = password;
		cachedMails = new HashMap<String, Mail>();
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
	
	public Mail getMail(String name) {
		return cachedMails.get(name);
	}
	
	public void setMail(String name, Mail mail) {
		cachedMails.put(name, mail);
	}
	
	public void clearAllCachedData() {
		for (Mail mail : cachedMails.values()) {
			mail.delete();
		}
		cachedMails.clear();
	}
}
