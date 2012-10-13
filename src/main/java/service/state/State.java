package service.state;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import service.AbstractSockectService;
import service.command.ServiceCommandRecognizer;

public abstract class State {

	protected static final Logger logger = Logger.getLogger(State.class);
	
	protected ServiceCommandRecognizer commandRecognizer;
	protected AbstractSockectService owner;
	protected Map<String, Object> bundle;
	
	public State(AbstractSockectService owner) {
		this.owner = owner;
		commandRecognizer = new ServiceCommandRecognizer(owner);
		bundle = new HashMap<String, Object>();
	}
	
	public List<String> getAvailableCommands() {
		return commandRecognizer.availableCommands();
	}
	
	public void enter() {
		logger.trace("Entering state: " + getClass().getSimpleName());
	}
	
	public abstract void exec(String[] params);

	public void exit() {
		logger.trace("Exiting state: " + getClass().getSimpleName());
	}

	public Map<String, Object> getBundle() {
		return bundle;
	}
}
