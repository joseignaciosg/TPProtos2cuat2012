package service.command.impl;

import java.util.Map;

import service.command.ServiceCommand;
import service.state.impl.configurer.ReadState;
import util.CollectionUtil;


public class AuthCommand extends ServiceCommand {

	private static final int MAX_INVALID_ATTEMPTS = 3;
	
	private String passwd;
	
	public AuthCommand() {
		passwd = "123456";
	}
	
	@Override
	public void execute(String[] params) {
		if (CollectionUtil.empty(params) || !passwd.equals(params[0])) {
			int attempts = incrementLoginAttempts();
			if (attempts == MAX_INVALID_ATTEMPTS) {
				getOwner().echoLine(103, "Too many tries! BYE!");
				getOwner().setEndOfTransmission(true);
			} else {
				getOwner().echoLine(200, "Incorrect Password - " + attempts);
			}
			return;
		}
		getOwner().echoLine(0, "Password accepted");
		getOwner().getStateMachine().setState(new ReadState(owner));
	}

	private int incrementLoginAttempts() {
		Map<String, Object> bundle = getBundle();
		Integer invalidAttempts = (Integer) bundle.get("invalidLogInAttempts");
		invalidAttempts = (invalidAttempts == null) ? 0 : invalidAttempts;
		invalidAttempts++;
		bundle.put("invalidLogInAttempts", invalidAttempts);
		return invalidAttempts;
	}
}
