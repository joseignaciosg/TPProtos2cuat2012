import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class TCPClient {

	public static void main(String argv[]) {
		try {
			String sentence;
			String modifiedSentence;
			BufferedReader inFromUser = new BufferedReader(
					new InputStreamReader(System.in));
			Socket clientSocket;
			clientSocket = new Socket("localhost", 6789);
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			sentence = inFromUser.readLine();
			outToServer.writeBytes(sentence + '\n');
			modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);
			clientSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
