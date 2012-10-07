package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
		String clientSentence;
		try {
			prtWelcomeMessage();
			do {
				final BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
				clientSentence = inFromClient.readLine();
				outToClient.writeBytes(exec(clientSentence));
			} while (!isEnd(clientSentence));
			socket.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	private void prtWelcomeMessage() throws IOException {
		String welcome = getWelcomeMessage();
		if (welcome != null) {
			final DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
			outToClient.writeBytes(welcome);
		}
	}

	protected String getWelcomeMessage() {
		return null;
	}

	protected abstract String exec(String command);
	
	protected abstract boolean isEnd(String command);
}
