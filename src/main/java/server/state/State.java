package server.state;

import java.util.List;

import org.apache.log4j.Logger;

import server.AbstractSockectService;
import server.command.ServiceCommandRecognizer;

public abstract class State {

	private static final Logger logger = Logger.getLogger(State.class);
	
	protected ServiceCommandRecognizer commandRecognizer;
	protected AbstractSockectService owner;
	
	public State(AbstractSockectService owner) {
		this.owner = owner;
		commandRecognizer = new ServiceCommandRecognizer(owner);
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

}
