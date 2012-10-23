package service.command.impl.configurer;

import model.configuration.Config;
import service.AbstractSockectService;
import service.command.impl.AuthCommand;
import service.state.impl.configurer.ReadState;


public class ConfigurerAuthCommand extends AuthCommand {

	public ConfigurerAuthCommand(AbstractSockectService owner) {
		super(owner);
		String passwd = Config.getInstance().getKeyValueConfig("configurer").get("password");
		getBundle().put("password", passwd);
	}

	@Override
	public void onLogin() {
		owner.getStateMachine().setState(new ReadState(owner));		
	}


}
