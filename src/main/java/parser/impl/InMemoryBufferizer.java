package parser.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

import parser.api.Bufferizer;

public class InMemoryBufferizer implements Bufferizer {

	private StringBuilder buffer;

	@Override
	public void buffer(final BufferedReader inputBuffer) throws IOException {
		String serverResponse;
		serverResponse = inputBuffer.readLine();
		this.buffer = new StringBuilder();
		while (!serverResponse.equals(".")) {
			this.buffer.append(serverResponse + "\r\n");
			serverResponse = inputBuffer.readLine();
		}
	}

	@Override
	public void send(final DataOutputStream outputBuffer) throws IOException {
		final String[] lines = this.buffer.toString().split("\r\n");
		for (final String line : lines) {
			outputBuffer.writeBytes(line + "\r\n");
		}
	}

}
