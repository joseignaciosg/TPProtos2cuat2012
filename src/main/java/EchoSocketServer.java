import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class EchoSocketServer implements Runnable {

	private Socket connectionSocket;

	public EchoSocketServer(final Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	@Override
	public void run() {
		try {
			String clientSentence;
			String capitalizedSentence;
			do {
				final BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(connectionSocket.getInputStream()));
				final DataOutputStream outToClient = new DataOutputStream(
						connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();
				System.out.println("Received: " + clientSentence);
				capitalizedSentence = clientSentence.toUpperCase() + '\n';
				outToClient.writeBytes(capitalizedSentence);
			} while (!capitalizedSentence.equals("END\n"));
			System.out.println("Cerrando conexión...");
			connectionSocket.close();
		} catch (final Exception e) {
		}

	}

}
