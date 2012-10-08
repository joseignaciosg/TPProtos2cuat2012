package parser.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import parser.api.Bufferizer;

public class FileBufferizer implements Bufferizer {

	private File tmp;

	@Override
	public void buffer(final BufferedReader inputBuffer) throws IOException {
		String serverResponse;
		this.tmp = File.createTempFile("mail", "proxy");
		final BufferedWriter out = new BufferedWriter(new FileWriter(this.tmp));
		serverResponse = inputBuffer.readLine();
		while (!serverResponse.equals(".")) {
			out.write(serverResponse + "\r\n");
			serverResponse = inputBuffer.readLine();
		}
		out.close();

	}

	@Override
	public void send(final DataOutputStream outputBuffer) throws IOException {
		String serverResponse;
		final BufferedReader reader = new BufferedReader(new FileReader(
				this.tmp));
		while ((serverResponse = reader.readLine()) != null) {
			outputBuffer.writeBytes(serverResponse + "\r\n");
		}
		this.tmp.delete();
	}

}
