package server.start;

import server.GenericServer;
import server.ProxySocketServer;
import util.Config;

public class ProxyServer {

	public static void main(final String[] args) {
		int port = Config.getInstance().getInt("proxy_port");
		new GenericServer().run(port, ProxySocketServer.class);
	}
}
