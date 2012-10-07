package server;

import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;


public class GenericServer {
	
	public void run(int port, Class<? extends AbstractSockectServer> serverClass) {
		try {
			System.out.println("TCP Server initialized for attending class " + serverClass);
			ServerSocket welcomeSocket = new ServerSocket(port);
			while (!Thread.interrupted()) {
				final Socket connectionSocket = welcomeSocket.accept();
				AbstractSockectServer server = createInstance(serverClass);
				server.setSocket(connectionSocket);
				System.out.println("Conexión aceptada - new instance of " + serverClass);
				new Thread(server).start();
			}
			System.out.println("TCP Server ended, closing connection.");
			welcomeSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private AbstractSockectServer createInstance(Class<? extends AbstractSockectServer> serverClass) 
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
		return (AbstractSockectServer) serverClass.getConstructors()[0].newInstance();
	}

}
