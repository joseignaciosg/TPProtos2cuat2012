package service;

import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;


public class GenericServer implements Runnable {
	
	private static Logger logger = Logger.getLogger(GenericServer.class);
	
	private int port;
	private Class<? extends AbstractSockectService> serverClass;
	private ExecutorService threadPool;

	public GenericServer(int port, Class<? extends AbstractSockectService> serverClass) {
		this.port = port;
		this.serverClass = serverClass;
		threadPool = Executors.newCachedThreadPool();
	}
	
	@Override
	public void run() {
		try {
			ServerSocket welcomeSocket = new ServerSocket(port);
			while (!Thread.interrupted()) {
				final Socket connectionSocket = welcomeSocket.accept();
				AbstractSockectService server = createInstance(serverClass, connectionSocket);
				logger.info("Conection accepted. Attending Server: " + serverClass);
				threadPool.submit(server);
			}
			logger.info("TCP Server ended, closing connection.");
			welcomeSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private AbstractSockectService createInstance(Class<? extends AbstractSockectService> serverClass, Socket connectionSocket) 
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
		return (AbstractSockectService) serverClass.getConstructors()[0].newInstance(connectionSocket);
	}

}
