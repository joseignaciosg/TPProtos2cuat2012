import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class MimeParser {

	public void parse(final BufferedReader inFromOriginServer,
			final DataOutputStream outToMUA) throws IOException {
		String serverResponse;
		do {
			serverResponse = inFromOriginServer.readLine();
			// parsea
			outToMUA.writeBytes(serverResponse + "\r\n");
		} while (!serverResponse.equals("."));

	}

}
