package service.start;

import java.io.File;
import java.io.FileNotFoundException;

import model.configuration.Config;
import model.util.CollectionUtil;
import model.util.IOUtil;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ProxyInitializer {

	private static Logger logger = Logger.getLogger(ProxyInitializer.class);

	public static void main(String[] args) {
		if (CollectionUtil.empty(args) || args.length != 3) {
			System.out.println("Usage:");
			System.out.println("\tParam 1: path to server.init file configuration.");
			System.out.println("\tParam 2: default origin server.");
			System.out.println("\tParam 3: access port origin server.");
			System.out.println("\tExample: ./server.init coyote.itba.edu.ar 110");
			return;
		}
		System.setProperty("defualtOriginServer", args[1]);
		try {
			int port = Integer.valueOf(args[2]);
			System.setProperty("originServerPort", port + "");
		} catch (NumberFormatException e) {
			System.out.println(args[2] + " is not a number.");
			return;
		}
		new ProxyInitializer().initialize(args[0]);
	}

	public void initialize(String configurationFile) {
		String configFile = Config.getInstance().getConfigResourcePath("log4j");
		PropertyConfigurator.configure(IOUtil.getResource(configFile));
		logger.trace("Initializing proxy.....");
		try {
			new ServerInitializer().initialize(new File(configurationFile));
			logger.trace("Proxy Started succesfully!");
		} catch (FileNotFoundException e) {
			logger.error("Confuration fie not found.");
		}
	}

}
