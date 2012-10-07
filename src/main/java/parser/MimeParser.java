package parser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class MimeParser {

	public void parse(BufferedReader inputBuffer, DataOutputStream outputBuffer) throws IOException {
		String serverResponse;
		do {
			serverResponse = inputBuffer.readLine();
			outputBuffer.writeBytes(serverResponse + "\r\n");
		} while (!serverResponse.equals("."));
	}

}
