package service.command.impl.stats;

import org.apache.log4j.Logger;

import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.util.StringUtil;
import service.AbstractSockectService;
import service.StatsSocketService;
import service.command.impl.AuthCommand;

public class StatsAuthCommand extends AuthCommand {
	
	private static final Logger logger = Logger.getLogger(StatsAuthCommand.class);
	private static final KeyValueConfiguration statsServiceConfig = Config.getInstance().getKeyValueConfig("stats_service");
	
	public StatsAuthCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	protected boolean validatePassword(String password) {
		String original = statsServiceConfig.get("password");
		if (StringUtil.empty(original)) {
			logger.error("No password defined for Stats Service. Denegating remote access.");
			return false;
		}
		return original.equals(password);
	}
	
	@Override
	public void loggedIn() {
		((StatsSocketService) owner).loggedIn();
	}
}
