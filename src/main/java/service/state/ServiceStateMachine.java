package service.state;

import service.AbstractSockectService;

public class ServiceStateMachine {
	
	private AbstractSockectService owner;
	private State current;
	
	public ServiceStateMachine(AbstractSockectService owner) {
		this.owner = owner;
	}
	
	public void setState(State state) {
		if (current != null) {
			current.exit();
		}
		current = state;
		current.enter();
	}
	
	public State getCurrent() {
		return current;
	}
	
	public void exec(String[] params) {
		current.exec(params);
	}
	
	public AbstractSockectService getOwner() {
		return owner;
	}
	
	public void exit() {
		if (current != null) {
			current.exit();
		}
	}
}
