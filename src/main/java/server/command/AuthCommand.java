package server.command;

import server.state.ReadState;
import util.CollectionUtil;


public class AuthCommand extends ServiceCommand {

	private String passwd;
	
	public AuthCommand() {
		passwd = "123456";
	}
	
	@Override
	public void execute(String[] params) {
		if (CollectionUtil.empty(params)) {
			return;
		}
		if (params.length == 0 || !passwd.equals(params[0])) {
			getOwner().echoLine(200, "Incorrect Password");
			return;
		}
		getOwner().echoLine(0, "Password accepted");
		getOwner().getStateMachine().setState(new ReadState(owner));
	}

}
