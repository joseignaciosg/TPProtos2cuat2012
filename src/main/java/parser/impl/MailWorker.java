package parser.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import parser.api.Bufferizer;

public class MailWorker {

	public void parse(final BufferedReader inputBuffer,
			final DataOutputStream outputBuffer) throws IOException {
		String serverResponse;

		serverResponse = inputBuffer.readLine();
		outputBuffer.writeBytes(serverResponse + ".\r\n"); // +OK not part of
															// the mail message
		final int length = Integer.valueOf(serverResponse.split(" ")[1]);
		Bufferizer bufferizer;
		if (length < 512 * 1024) {
			bufferizer = new InMemoryBufferizer();
		} else {
			bufferizer = new FileBufferizer();
		}
		bufferizer.buffer(inputBuffer);
		// bufferizer.transform();
		bufferizer.send(outputBuffer);

		outputBuffer.writeBytes(".\r\n"); // the final point is not part of the
											// mails message
	}
}
