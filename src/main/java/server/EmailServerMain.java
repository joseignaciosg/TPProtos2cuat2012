package server;

import mock.MockedSocketEmailServer;

public class EmailServerMain {

	public static void main(String[] args) {
		new GenericServer().run(8082, MockedSocketEmailServer.class);
	}

}
