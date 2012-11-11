package model;

import model.configuration.ConfigUtil;
import model.configuration.KeyValueConfiguration;

public class User {

	private static final KeyValueConfiguration originServerConfig = ConfigUtil.getInstance().getKeyValueConfig("origin_server");
	
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

	public String getMailServer() {
		String server = originServerConfig.get(mail);
		return server == null ? System.getProperty("defualtOriginServer") : server;
	}
}
