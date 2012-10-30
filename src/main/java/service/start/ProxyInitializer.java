package service.start;

import model.util.IOUtil;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ProxyInitializer {

	private static Logger logger = Logger.getLogger(ProxyInitializer.class);

	public static void main(final String[] args) {
		new ProxyInitializer().initialize();
	}

	public void initialize() {
		PropertyConfigurator.configure(IOUtil.getStream("log4j.properties"));
		logger.trace("Initializing proxy.....");
		new ServerInitializer().initialize("server.init");
		logger.trace("Proxy Started succesfully!");
	}

}
