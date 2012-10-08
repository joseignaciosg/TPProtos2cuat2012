package parser.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Bufferizer {

	public void buffer(final BufferedReader inputBuffer) throws IOException;

	public void send(final DataOutputStream outputBuffer) throws IOException;

}
