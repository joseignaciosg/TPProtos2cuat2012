package service.command.impl;

import java.util.Map;

import model.util.CollectionUtil;

import service.AbstractSockectService;
import service.StatusCodes;
import service.command.ServiceCommand;

public abstract class AuthCommand extends ServiceCommand {

	private static final int MAX_INVALID_ATTEMPTS = 3;

	public AuthCommand(AbstractSockectService owner) {
		super(owner);
	}
	
	@Override
	public void execute(String[] params) {
		String passwd = (String) getBundle().get("password");
		if (CollectionUtil.empty(params) || !passwd.equals(params[0])) {
			int attempts = incrementLoginAttempts();
			if (attempts == MAX_INVALID_ATTEMPTS) {
				getOwner().echoLine(StatusCodes.ERR_TOO_MANY_ATTEMPTS);
				getOwner().setEndOfTransmission(true);
			} else {
				getOwner().echoLine(StatusCodes.ERR_INVALID_PASSWORD, "Attempts: " + attempts);
			}
			return;
		}
		getOwner().echoLine(StatusCodes.OK_PASSWORD_ACCEPTED);
		onLogin();
	}

	private int incrementLoginAttempts() {
		Map<String, Object> bundle = getBundle();
		Integer invalidAttempts = (Integer) bundle.get("invalidLogInAttempts");
		invalidAttempts = (invalidAttempts == null) ? 0 : invalidAttempts;
		invalidAttempts++;
		bundle.put("invalidLogInAttempts", invalidAttempts);
		return invalidAttempts;
	}
	
	public abstract void onLogin();
}
