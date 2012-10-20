package service.start;

import org.apache.log4j.Logger;

public class ProxyInitializer {

	private static Logger logger = Logger.getLogger(ProxyInitializer.class);

	public static void main(final String[] args) {
		new ProxyInitializer().initialize();
	}

	public void initialize() {
		logger.trace("Initializing proxy.....");
		new ServerInitializer().initialize("server.init");
		logger.trace("Proxy Started succesfully!");
	}

}
