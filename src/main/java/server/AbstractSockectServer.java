package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public abstract class AbstractSockectServer implements Runnable {

	protected Socket socket;
		
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		boolean endOfTransmission;
		try {
			initialize();
			do {
				final BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String clientSentence = inFromClient.readLine(); 
				System.out.println(getClass().getSimpleName() + " -- Command: " + clientSentence);
				endOfTransmission = exec(clientSentence);
			} while (!endOfTransmission);
			finalizeTransmission();
			socket.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void finalizeTransmission() throws Exception{
		
	}

	protected void initialize() throws Exception {
	}
	
	protected abstract boolean exec(String command) throws Exception;
	
}
