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
			DataOutputStream outToOriginServer;
			BufferedReader inFromMUA;
			BufferedReader inFromOriginServer;
			DataOutputStream outToMUA;
			Socket proxyToOriginServerSocket;

			// Abro socket hacia el origin server en el puerto 110 (ej. pop3.alu.itba.edu.ar)
			proxyToOriginServerSocket = new Socket("mail.josegalindo.com.ar", 110);

			inFromOriginServer = new BufferedReader(new InputStreamReader(
					proxyToOriginServerSocket.getInputStream()));

			// Respuesta del origin server
			originServerSentence = inFromOriginServer.readLine();
			System.out.println("PROXY: Received from Origin Server: "
					+ originServerSentence);

			// Le forwardeo los datos que me envió el origin server al MUA
			outToMUA = new DataOutputStream(muaSocket.getOutputStream());
			outToMUA.writeBytes(originServerSentence + "\r\n");
			
			inFromMUA = new BufferedReader(new InputStreamReader(
					muaSocket.getInputStream()));

			// Leo datos del MUA
			muaSentence = inFromMUA.readLine();
			System.out.println("PROXY: Received MUA Sentence: " + muaSentence);

			// Forwardeo datos del MUA hacia Origin Server

			outToOriginServer = new DataOutputStream(
					proxyToOriginServerSocket.getOutputStream());

			// Le envio al origin server lo que me paso el MUA
			outToOriginServer.writeBytes(muaSentence + "\r\n");

			// clientSocket.close();
			do {
				
				if(muaSentence.equals("CAPA") || muaSentence.equals("LIST") || muaSentence.equals("UIDL")
				  || muaSentence.contains("RETR")){
					originServerSentence = inFromOriginServer.readLine();
					while(!originServerSentence.equals(".")){
						// Le forwardeo los datos que me envió el origin server al MUA
						System.out.println("PROXY: Forwarding to MUA: " + originServerSentence);
						outToMUA.writeBytes(originServerSentence + "\r\n");
						
						// Respuesta del origin server
						originServerSentence = inFromOriginServer.readLine();
						System.out.println("PROXY: Received from Origin Server: " + originServerSentence);
						
					}
					
					// Le forwardeo los datos que me envió el origin server al MUA
					outToMUA.writeBytes(".\r\n");

				}else{				
					// Respuesta del origin server
					originServerSentence = inFromOriginServer.readLine();
					System.out.println("PROXY: Received from Origin Server: "
							+ originServerSentence);
	
					// Le forwardeo los datos que me envió el origin server al MUA
					outToMUA.writeBytes(originServerSentence + "\r\n");
				}
				
				// Leo datos del MUA
				muaSentence = inFromMUA.readLine();
				System.out.println("PROXY: Received MUA Sentence: "
						+ muaSentence);

				// Le envio al origin server lo que me paso el MUA
				outToOriginServer.writeBytes(muaSentence + "\r\n");

			} while (!muaSentence.toUpperCase().equals("END"));
			System.out.println("Cerrando conexión...");
			this.muaSocket.close();
		} catch (final Exception e) {
		}

	}

}
