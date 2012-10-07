package server.start;

import server.GenericServer;
import server.ProxySocketServer;

public class ProxyServer {

	public static void main(final String[] args) {
		new GenericServer().run(8080, ProxySocketServer.class);
	}
}
