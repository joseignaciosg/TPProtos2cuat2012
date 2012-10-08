package server;

import java.io.BufferedReader;
import java.io.IOException;
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
				if (clientSentence != null) {					
					endOfTransmission = exec(clientSentence);
				} else {
					// Connection has been closed or pipe broken...
					endOfTransmission = true;
				}
			} while (!endOfTransmission);
			finalizeTransmission();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void finalizeTransmission() throws Exception{
		
	}

	protected void initialize() throws Exception {
	}
	
	protected abstract boolean exec(String command) throws Exception;
	
}
