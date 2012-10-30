package service.command;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;

import service.AbstractSockectService;
import service.StatusCodes;


public class ServiceCommandRecognizer {

	private static final Logger logger = Logger.getLogger(ServiceCommandRecognizer.class);
	
	private Map<String, Class<? extends ServiceCommand>> commands;
	private Class<? extends ServiceCommand> defaultCommand;
	private AbstractSockectService owner;
	
	public ServiceCommandRecognizer(AbstractSockectService owner) {
		this.owner = owner;
		commands = new HashMap<String, Class<? extends ServiceCommand>>();
		defaultCommand = null;
	}
	
	public void register(String cmdName, Class<? extends ServiceCommand> clazz) {
		commands.put(cmdName.toLowerCase(), clazz);
	}
	
	public void registerDefault(Class<? extends ServiceCommand> clazz) {
		this.defaultCommand = clazz;
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
			owner.echoLine(StatusCodes.ERR_UNRECOGNIZED_COMMAND);
			return;
		}
		Class<? extends ServiceCommand> clazz = commands.get(params[0].toLowerCase());		
		clazz = (clazz == null) ? defaultCommand : clazz;
		if (clazz == null) {
			owner.echoLine(StatusCodes.ERR_UNRECOGNIZED_COMMAND);
			logger.info("Unrecognized command");
			return;
		}
		logger.info("Command recognized, executing class: " + clazz.getSimpleName());
		String[] parts = new String[params.length - 1];
		System.arraycopy(params, 1, parts, 0, params.length - 1);
		ServiceCommand command = createInstance(clazz);
		command.setOriginalParams(params);
		try {
			command.execute(parts);
			logger.info("Command " + clazz.getSimpleName() + " finished execution.");
		} catch (Exception e) {
			logger.error("Error executing command: " + command);
			throw new IllegalStateException(e);
		}
	}
	
	private ServiceCommand createInstance(Class<? extends ServiceCommand> clazz) {
		try {
			return (ServiceCommand) clazz.getConstructors()[0].newInstance(owner);
		} catch (Exception e) {
			logger.error("Could not instantiate class " + clazz + ". Reason: " + e.getMessage());
			throw new IllegalStateException();
		}
	}
}
