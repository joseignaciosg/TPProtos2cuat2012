import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	public static void main(final String[] args) {
		try {
			ServerSocket welcomeSocket;
			welcomeSocket = new ServerSocket(8081);
			while (!Thread.interrupted()) {
				final Socket connectionSocket = welcomeSocket.accept();
				System.out.println("Conexión aceptada");
				final Thread test = new Thread(new MyThread(connectionSocket));
				test.start();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
