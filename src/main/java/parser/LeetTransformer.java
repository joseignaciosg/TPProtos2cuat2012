package parser;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import util.ProcessUtil;

public class LeetTransformer extends Transformer {

	@Override
	public File transform(File part, String partheaders) throws IOException {
		// TODO llamar app externa
		// TODO VER SI ES TXT EN LOS HEADERS PARA TRANSFORMAR
		List<String> commands = new LinkedList<String>();
		commands.add("java");
		commands.add("-jar");
		commands.add("apps/toL33t.jar");
		commands.add(part.getAbsolutePath());
		try {
			ProcessUtil.executeApp(commands);
		} catch (Exception e) {
			System.out.println("dsadsadakdks");
		}
		return part;
	}
}
// File transformedPart = File.createTempFile("transformedpart", "proxy");
// BufferedWriter out = new BufferedWriter(new FileWriter(transformedPart));
// Scanner partScanner = new Scanner(part);
// while (partScanner.hasNextLine()) {
// String line = partScanner.nextLine();
// line = line.replaceAll("a", "4");
// line = line.replaceAll("e", "3");
// line = line.replaceAll("i", "1");
// line = line.replaceAll("o", "0");
// out.write(line + "\r\n");
// }
// out.close();
// partScanner.close();
// return transformedPart;
// }

