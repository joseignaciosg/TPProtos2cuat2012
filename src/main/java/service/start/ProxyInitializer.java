package service.start;

import model.configuration.Config;
import model.util.IOUtil;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ProxyInitializer {

	private static Logger logger = Logger.getLogger(ProxyInitializer.class);

	public static void main(final String[] args) {
		new ProxyInitializer().initialize();
	}

	public void initialize() {
		String configFile = Config.getInstance().getConfigResourcePath("log4j");
		PropertyConfigurator.configure(IOUtil.getResource(configFile));
		logger.trace("Initializing proxy.....");
		new ServerInitializer().initialize("server.init");
		logger.trace("Proxy Started succesfully!");
	}

}
