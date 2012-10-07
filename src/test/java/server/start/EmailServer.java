package server.start;

import mock.MockedSocketEmailServer;
import server.GenericServer;

public class EmailServer {

	public static void main(final String[] args) {
		new GenericServer().run(8082, MockedSocketEmailServer.class);
	}

}
