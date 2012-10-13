package service.command.impl.monitor;

import service.AbstractSockectService;
import service.command.impl.AuthCommand;
import service.state.impl.monitor.ReadState;
import util.Config;

public class MonitorAuthCommand extends AuthCommand {
	
	public MonitorAuthCommand(AbstractSockectService owner) {
		super(owner);
		getBundle().put("password", Config.getInstance().getConfig("monitor_conf").get("password"));
	}

	@Override
	public void onLogin() {
		owner.getStateMachine().setState(new ReadState(owner));
	}
}
