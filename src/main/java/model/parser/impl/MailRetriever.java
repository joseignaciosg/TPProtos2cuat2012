package model.parser.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import model.parser.api.Bufferizer;
import model.util.Config;


public class MailRetriever {
	
	private final static int maxMemorySize = Config.getInstance().getInt("min_size_to_save_in_disk_kb");
	
	public void retrieve(final BufferedReader inputBuffer, final DataOutputStream outputBuffer) throws IOException {
		String serverResponse = inputBuffer.readLine();
		outputBuffer.writeBytes(serverResponse + ".\r\n"); // +OK not part of
															// the mail message
		final int length = Integer.valueOf(serverResponse.split(" ")[1]);
		Bufferizer bufferizer;
		if (length < maxMemorySize) {
			bufferizer = new InMemoryBufferizer();
		} else {
			bufferizer = new FileBufferizer();
		}
		bufferizer.buffer(inputBuffer);
		bufferizer.transform();
		bufferizer.send(outputBuffer);
		outputBuffer.writeBytes(".\r\n"); // the final point is not part of the
	}
	
}
