package service.command;

import service.AbstractSockectService;


public abstract class ServiceCommand {

	protected AbstractSockectService owner;
	
	public abstract void execute(String[] params);
	
	public void setOwner(AbstractSockectService owner) {
		this.owner = owner;
	}
	
	public AbstractSockectService getOwner() {
		return owner;
	}
}