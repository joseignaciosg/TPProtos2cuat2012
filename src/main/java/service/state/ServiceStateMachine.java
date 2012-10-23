package service.state;

import java.util.HashMap;
import java.util.Map;

import service.AbstractSockectService;

public class ServiceStateMachine {
	
	private AbstractSockectService owner;
	private Map<String, Object> bundle;
	private State current;
	
	public ServiceStateMachine(AbstractSockectService owner) {
		this.owner = owner;
		bundle = new HashMap<String, Object>();
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
	
	public Map<String, Object> getBundle() {
		return bundle;
	}
	
	public void exit() {
		if (current != null) {
			current.exit();
		}
	}
}
