package service.state.impl.monitor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Timer;

import service.AbstractSockectService;
import service.command.impl.LogOutCommand;
import service.state.State;
import util.Config;
import worker.MonitorTask;

public class ReadState extends State {
	
	private static Config monitorConfig = Config.getInstance().getConfig("monitor_conf");
	
	private Timer taskTimer;
	
	public ReadState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("EXIT", LogOutCommand.class);
		taskTimer = new Timer();
		long timerDelay = monitorConfig.getInt("refresh_rate_ms");
		try {
			DataOutputStream out = new DataOutputStream(owner.getSocket().getOutputStream());
			taskTimer.schedule(new MonitorTask("monitorTask", out), 0, timerDelay);
		} catch (IOException e) {
			logger.error("Could not start Monitor Task!");
			owner.echoLine(666, "Internal Error!");
			owner.setEndOfTransmission(true);
		}
	}
	
	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}
	
	@Override
	public void exit() {
		super.exit();
		taskTimer.cancel();
	}

}
