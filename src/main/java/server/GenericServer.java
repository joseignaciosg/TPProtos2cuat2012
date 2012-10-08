package server;

import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;


public class GenericServer implements Runnable {
	
	private int port;
	private Class<? extends AbstractSockectServer> serverClass;

	public GenericServer(int port, Class<? extends AbstractSockectServer> serverClass) {
		this.port = port;
		this.serverClass = serverClass;
	}
	
	@Override
	public void run() {
		try {
			ServerSocket welcomeSocket = new ServerSocket(port);
			while (!Thread.interrupted()) {
				final Socket connectionSocket = welcomeSocket.accept();
				AbstractSockectServer server = createInstance(serverClass);
				server.setSocket(connectionSocket);
				System.out.println("Conection accepted. Attending Server: " + serverClass);
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
