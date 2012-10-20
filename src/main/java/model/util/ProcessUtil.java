package model.util;

import java.util.List;

public class ProcessUtil {

	public static int executeApp(List<String> commands) throws Exception {
		ProcessBuilder pb = new ProcessBuilder(commands);
		Process process = pb.start();
		return process.waitFor();
	}

}
