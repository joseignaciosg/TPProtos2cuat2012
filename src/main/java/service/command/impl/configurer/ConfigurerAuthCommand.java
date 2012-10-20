package service.command.impl.configurer;

import model.util.Config;
import service.AbstractSockectService;
import service.command.impl.AuthCommand;
import service.state.impl.configurer.ReadState;


public class ConfigurerAuthCommand extends AuthCommand {

	public ConfigurerAuthCommand(AbstractSockectService owner) {
		super(owner);
		getBundle().put("password", Config.getInstance().getConfig("configurer_conf").get("password"));
	}

	@Override
	public void onLogin() {
		owner.getStateMachine().setState(new ReadState(owner));		
	}


}
