package service.command.impl.configurer;

import service.AbstractSockectService;
import service.command.impl.AuthCommand;
import service.state.impl.configurer.ReadState;
import util.Config;


public class ConfigurerAuthCommand extends AuthCommand {

	public ConfigurerAuthCommand(AbstractSockectService owner) {
		super(owner);
		getBundle().put("password", Config.getInstance().getConfig("configurer").get("password"));
	}

	@Override
	public void onLogin() {
		owner.getStateMachine().setState(new ReadState(owner));		
	}


}
