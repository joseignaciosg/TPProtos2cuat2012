import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ProxySocketServer implements Runnable {

	private Socket muaSocket;

	public ProxySocketServer(final Socket muaSocket) {
		this.muaSocket = muaSocket;
	}

	@Override
	public void run() {
		try {
			String muaSentence;
			String originServerSentence;
			do {
				// Abro socket hacia el origin server
				Socket proxyToOriginServerSocket;
				proxyToOriginServerSocket = new Socket("localhost", 8081); // pop3.alu.itba.edu.ar

				final BufferedReader inFromMUA = new BufferedReader(
						new InputStreamReader(muaSocket.getInputStream()));
				
				// Leo datos del MUA
				muaSentence = inFromMUA.readLine();
				System.out.println("PROXY: Received MUA Sentence: " + muaSentence);
				
				// Forwardeo datos del MUA hacia Origin Server
				
				final DataOutputStream outToOriginServer = new DataOutputStream(
						proxyToOriginServerSocket.getOutputStream());
				
				// Le envio al origin server lo que me paso el MUA
				outToOriginServer.writeBytes(muaSentence + '\n');

				BufferedReader inFromOriginServer = new BufferedReader(
						new InputStreamReader(System.in));
				
				// Respuesta del origin server
				originServerSentence = inFromOriginServer.readLine();
				System.out.println("PROXY: Received from Origin Server: " + originServerSentence);
				
				// Le forwardeo los datos que me envió el origin server al MUA
				final DataOutputStream outToMUA = new DataOutputStream(
						muaSocket.getOutputStream());
				outToMUA.writeBytes(originServerSentence);

				//clientSocket.close();
				
			} while (!muaSentence.equals("END\n"));
			System.out.println("Cerrando conexión...");
			this.muaSocket.close();
		} catch (final Exception e) {
		}

	}

}
