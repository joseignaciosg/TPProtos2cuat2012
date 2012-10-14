package service.command;

import java.util.Map;

import service.AbstractSockectService;


public abstract class ServiceCommand {

	protected AbstractSockectService owner;
	protected String[] originalParams;

	public ServiceCommand(AbstractSockectService owner) {
		this.owner = owner;
	}
	
	public abstract void execute(String[] params);
	
	public AbstractSockectService getOwner() {
		return owner;
	}
	
	public Map<String, Object> getBundle() {
		return owner.getStateMachine().getCurrent().getBundle();
	}
	
	public void setOriginalParams(String[] originalParams) {
		this.originalParams = originalParams;
	}
	
	public String[] getOriginalParams() {
		return originalParams;
	}
}
