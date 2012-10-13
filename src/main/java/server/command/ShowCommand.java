package server.command;

import java.util.Scanner;

import util.CollectionUtil;
import util.Config;
import util.IOUtil;

public class ShowCommand extends ServiceCommand {

	@Override
	public void execute(String[] params) {
		if (CollectionUtil.empty(params)) {
			owner.echoLine(101, "Invalid Parameters: missing arguments");
			return;
		}
		String resPath = Config.getInstance().get("specific_conf_dir") + params[0];
		Scanner scanner = IOUtil.createScanner(resPath);
		if (scanner == null) {
			owner.echoLine(102, "Invalid Parameters: File not exists");
			return;
		}
		while (scanner.hasNextLine()) {
			owner.echoLine(scanner.nextLine());
		}
		scanner.close();
		owner.echoLine(".");
		scanner.close();
		owner.echoLine(0, "File " + params[0] + " updated");
	}

}
