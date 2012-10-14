package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ProcessUtil {

    public static void executeApp(List<String> commands) throws IOException,
	    InterruptedException {
	commands.add("toL33t.jar");
	ProcessBuilder pb = new ProcessBuilder(commands);
	Process process = pb.start();
	int ret = process.waitFor();
//	Scanner scanner = new Scanner(process.getInputStream());
//	File tmp = File.createTempFile("resp", "proxy");
//	FileWriter writer = new FileWriter(tmp, true);
//	while (scanner.hasNextLine()) {
//	    String line = scanner.nextLine();
//	    System.out.println(line);
//	    writer.write(line);
//	}
    }

}
