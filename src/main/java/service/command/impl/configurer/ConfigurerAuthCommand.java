package service.command.impl.configurer;

import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.util.StringUtil;
import service.AbstractSockectService;
import service.ConfigurerService;
import service.command.impl.AuthCommand;


public class ConfigurerAuthCommand extends AuthCommand {

	private static final KeyValueConfiguration configurerServiceConfig = Config.getInstance().getKeyValueConfig("configurer");
	
	public ConfigurerAuthCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	protected boolean validatePassword(String password) {
		String original = configurerServiceConfig.get("password");
		if (StringUtil.empty(original)) {
			logger.error("No password defined for Configurer Service. Denegating remote access.");
			return false;
		}
		return original.equals(password);
	}
	
	@Override
	public void loggedIn() {
		((ConfigurerService) owner).loggedIn();
	}


}
