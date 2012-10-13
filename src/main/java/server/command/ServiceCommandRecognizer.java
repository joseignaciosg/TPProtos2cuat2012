package server.command;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import server.AbstractSockectService;


public class ServiceCommandRecognizer {

	private static final Logger logger = Logger.getLogger(ServiceCommandRecognizer.class);
	
	private Map<String, Class<? extends ServiceCommand>> commands;
	private AbstractSockectService owner; 
	
	public ServiceCommandRecognizer(AbstractSockectService owner) {
		this.owner = owner;
		commands = new HashMap<String, Class<? extends ServiceCommand>>();
	}
	
	public void register(String cmdName, Class<? extends ServiceCommand> clazz) {
		commands.put(cmdName.toLowerCase(), clazz);
	}
	
	public List<String> availableCommands() {
		List<String> available = new LinkedList<String>();
		for (String name : commands.keySet()) {
			available.add(name);
		}
		return available;
	}
	
	public void exec(String[] params) {
		if (params.length == 0) {
			owner.echoLine(100, "Unrecognized Command");
			return;
		}
		Class<? extends ServiceCommand> clazz = commands.get(params[0].toLowerCase());
		if (clazz == null) {
			owner.echoLine(100, "Unrecognized Command");
			logger.trace("Unrecognized command");
			return;
		}
		logger.trace("Command recognized, executing class: " + clazz);
		String[] parts = new String[params.length - 1];
		System.arraycopy(params, 1, parts, 0, params.length - 1);
		ServiceCommand command = createInstance(clazz);
		command.setOwner(owner);
		command.execute(parts);
	}
	
	private ServiceCommand createInstance(Class<? extends ServiceCommand> clazz) {
		try {
			return (ServiceCommand) clazz.getConstructors()[0].newInstance();
		} catch (Exception e) {
			logger.error("Could not instantiate class " + clazz + ". Reason: " + e.getMessage());
			throw new IllegalStateException();
		}
	}
}
