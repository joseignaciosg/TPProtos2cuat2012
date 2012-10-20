package model.parser.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Bufferizer {

	void buffer(final BufferedReader inputBuffer) throws IOException;

	void send(final DataOutputStream outputBuffer) throws IOException;

	void transform() throws IOException;
}
