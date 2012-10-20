package model.parser.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import model.parser.FileMailTransformer;
import model.parser.api.Bufferizer;


public class FileBufferizer implements Bufferizer {

	private File tmpMailFile;

	@Override
	public void buffer(BufferedReader inputBuffer) throws IOException {
		String serverResponse;
		tmpMailFile = File.createTempFile("mail", "proxy");
		BufferedWriter out = new BufferedWriter(new FileWriter(tmpMailFile));
		serverResponse = inputBuffer.readLine();
		while (!serverResponse.equals(".")) {
			out.write(serverResponse + "\r\n");
			serverResponse = inputBuffer.readLine();
		}
		out.close();
	}

	@Override
	public void transform() throws IOException {
		new FileMailTransformer(this.tmpMailFile).transform();
	}

	@Override
	public void send(DataOutputStream outputBuffer) throws IOException {
		String serverResponse;
		BufferedReader reader = new BufferedReader(new FileReader(tmpMailFile));
		while ((serverResponse = reader.readLine()) != null) {
			outputBuffer.writeBytes(serverResponse + "\r\n");
		}
		reader.close();
		tmpMailFile.delete();
	}

}
