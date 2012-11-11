package service.start;

import java.io.File;
import java.io.FileNotFoundException;

import model.configuration.ConfigUtil;
import model.util.CollectionUtil;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ProxyInitializer {

	private static Logger logger = Logger.getLogger(ProxyInitializer.class);

	public static void main(String[] args) {
		if (CollectionUtil.empty(args) || args.length != 3) {
			System.out.println("Usage:");
			System.out.println("\tParam 1: path to server configuration files.");
			System.out.println("\tParam 2: default origin server.");
			System.out.println("\tParam 3: access port origin server.");
			System.out.println("\tExample: ./server.init coyote.itba.edu.ar 110");
			return;
		}
		try {
			new ProxyInitializer().initialize(args[0], args[1], Integer.valueOf(args[2]));
		} catch (NumberFormatException e) {
			System.out.println(args[2] + " is not a number.");
			return;
		}
	}

	public void initialize(String configurationsFolder, String defaultOriginServer, int originServerPort) {
		System.setProperty("defualtOriginServer", defaultOriginServer);
		System.setProperty("originServerPort", originServerPort + "");
		System.setProperty("configurationsFolder", configurationsFolder);
		PropertyConfigurator.configure(ConfigUtil.getInstance().getConfigPath("log4j"));
		logger.trace("Initializing proxy.....");
		try {
			String serverConf = new File(configurationsFolder).getAbsolutePath() + "/server.init";
			new ServerInitializer().initialize(new File(serverConf));
			logger.trace("Proxy Started succesfully!");
		} catch (FileNotFoundException e) {
			logger.error("Confuration file not found.");
		}
	}

}
