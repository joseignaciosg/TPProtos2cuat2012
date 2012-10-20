package service.state.impl.stats;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Timer;

import model.StatusCodes;
import model.util.Config;
import service.AbstractSockectService;
import service.command.impl.ListCommand;
import service.command.impl.configurer.LogOutCommand;
import service.command.impl.stats.HistogramCommand;
import service.command.impl.stats.ShowCommand;
import service.state.State;

public class ReadState extends State {
	
	private static Config statsConfig = Config.getInstance().getConfig("stats_conf");
	
	private Timer taskTimer;
	
	public ReadState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("EXIT", LogOutCommand.class);
		commandRecognizer.register("HIST", HistogramCommand.class);
		commandRecognizer.register("LIST", ListCommand.class);
		commandRecognizer.register("SHOW", ShowCommand.class);
		commandRecognizer.register("SHOW_ALL", ShowCommand.class);
//		taskTimer = new Timer();
//		long timerDelay = statsConfig.getInt("refresh_rate_ms");
		try {
			DataOutputStream out = new DataOutputStream(owner.getSocket().getOutputStream());
//			taskTimer.schedule(new StatsTimedTask("monitorTask", out), 0, timerDelay);
		} catch (IOException e) {
			logger.error("Could not start Monitor Task!");
			owner.echoLine(StatusCodes.ERR_INTERNAL_SERVER_ERROR);
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
//		taskTimer.cancel();
	}

}
