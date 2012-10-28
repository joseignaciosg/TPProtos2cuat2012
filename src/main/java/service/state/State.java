package service.state;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import service.AbstractSockectService;
import service.command.ServiceCommandRecognizer;
import service.command.impl.stats.StatsService;

public abstract class State {

	protected static final Logger logger = Logger.getLogger(State.class);
	protected static final StatsService statsService = StatsService.getInstace();
	
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
		logger.debug("Entering state: " + getClass());
	}
	
	public abstract void exec(String[] params);

	public void exit() {
		logger.debug("Exiting state: " + getClass());
	}

	public Map<String, Object> getBundle() {
		return owner.getStateMachine().getBundle();
	}
}
