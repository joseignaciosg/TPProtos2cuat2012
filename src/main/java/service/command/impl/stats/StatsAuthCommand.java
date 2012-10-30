package service.command.impl.stats;

import model.configuration.Config;
import service.AbstractSockectService;
import service.command.impl.AuthCommand;
import service.state.impl.stats.StatsReadState;

public class StatsAuthCommand extends AuthCommand {
	
	public StatsAuthCommand(AbstractSockectService owner) {
		super(owner);
		String passwd = Config.getInstance().getKeyValueConfig("stats_service").get("password");
		getBundle().put("password", passwd);
	}

	@Override
	public void onLogin() {
		owner.getStateMachine().setState(new StatsReadState(owner));
	}
}
