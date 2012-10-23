package service.command.impl.stats;

import java.io.DataOutputStream;
import java.util.Timer;

import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.util.CollectionUtil;
import service.AbstractSockectService;
import service.StatusCodes;
import service.command.ServiceCommand;
import service.worker.StatsTimedTask;

public class AutoUpdatesCommand extends ServiceCommand {

	private static KeyValueConfiguration statsConfig = Config.getInstance().getKeyValueConfig("stats_service");
	
	public AutoUpdatesCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		if (CollectionUtil.empty(params)) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_ARGUMENTS);
			return;
		}
		StatsTimer statsTimer = getTimer();
		if ("start".equalsIgnoreCase(params[0])) {
			if (statsTimer.started) {
				owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS, "Already started");
				return;
			}
			long timerDelay = statsConfig.getInt("refresh_rate_ms");
			DataOutputStream out = new DataOutputStream(owner.getSocket().getOutputStream());
			owner.echoLine(StatusCodes.OK_SUCCESS);
			statsTimer.timer.schedule(new StatsTimedTask("monitorTask", out), 0, timerDelay);
			statsTimer.started = true;
		} else if ("stop".equalsIgnoreCase(params[0])) {
			if (!statsTimer.started) {
				owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS, "Not started");
				return;
			}
			statsTimer.timer.cancel();
			statsTimer.started = false;
			owner.echoLine(StatusCodes.OK_SUCCESS);
		} else {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS, "Use start or stop");
		}
	}
	
	private StatsTimer getTimer() {
		StatsTimer statsTimer = (StatsTimer) getBundle().get("statsTimer");
		if (statsTimer == null) {
			statsTimer = new StatsTimer(new Timer());
			getBundle().put("statsTimer", statsTimer);
		}
		return statsTimer;
	}

	private static class StatsTimer {
		Timer timer;
		boolean started;
		
		public StatsTimer(Timer timer) {
			this.timer = timer; 
			this.started = false;
		}
	}
}
