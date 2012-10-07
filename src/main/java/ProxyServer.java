import server.GenericServer;

public class ProxyServer {

	public static void main(final String[] args) {
		new GenericServer().run(8080, ProxySocketServer.class);
	}
}
