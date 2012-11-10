package service;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;


public class WelcomeSocketService implements Runnable {
	
	private static Logger logger = Logger.getLogger(WelcomeSocketService.class);
	
	private int port;
	private Class<? extends AbstractSockectService> serviceClass;
	private ExecutorService threadPool;

	public WelcomeSocketService(int port, Class<? extends AbstractSockectService> serviceClass) {
		this.port = port;
		this.serviceClass = serviceClass;
		threadPool = Executors.newCachedThreadPool();
	}
	
	@Override
	public void run() {
		try {
			ServerSocket welcomeSocket = new ServerSocket(port);
			while (!Thread.interrupted()) {
				final Socket connectionSocket = welcomeSocket.accept();
				AbstractSockectService server = createInstance(serviceClass, connectionSocket);
				logger.info("Conection accepted. Attending Server: " + serviceClass);
				threadPool.submit(server);
			}
			logger.info("TCP Server ended, closing connection.");
			welcomeSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private AbstractSockectService createInstance(Class<? extends AbstractSockectService> serverClass, Socket connectionSocket)  throws Exception {
		return (AbstractSockectService) serverClass.getConstructors()[0].newInstance(connectionSocket);
	}

}
