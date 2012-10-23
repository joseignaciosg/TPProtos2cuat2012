package model.mail.transformerimpl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import model.util.ProcessUtil;


public class LeetTransformer extends Transformer {

	@Override
	public File transform(File part, String partheaders) throws IOException {
		if (Pattern.matches(".*text/plain.*", partheaders)
				|| Pattern.matches(".*.txt.*", partheaders)) {
			List<String> commands = new LinkedList<String>();
			commands.add("java");
			commands.add("-jar");
			commands.add("apps/toL33t.jar");
			commands.add(part.getAbsolutePath());
			try {
				ProcessUtil.executeApp(commands);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return part;
	}
}
