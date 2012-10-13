package service.command.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import service.AbstractSockectService;
import service.command.ServiceCommand;
import util.Config;
import util.IOUtil;

public class RemoveLineCommand extends ServiceCommand {

	public RemoveLineCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		if (params.length < 2) {
			owner.echoLine(101, "Invalid Parameters: missing arguments");
			return;
		}
		int lineNumberToRemove;
		try {
			lineNumberToRemove = Integer.valueOf(params[0]);
		} catch (NumberFormatException e) {
			owner.echoLine(101, "Invalid parameters: " + params[0] + " is not a number");
			return;
		}
		String resPath = Config.getInstance().get("specific_conf_dir") + params[1];
		String fullPath = IOUtil.fullPath(resPath);
		if (fullPath == null) {
			owner.echoLine(102, "Invalid parameters: File not found");
			return;
		}
		StringBuilder text = getTextWithoutLine(resPath, lineNumberToRemove);
		try {
			PrintWriter out = new PrintWriter(fullPath);
			out.print(text.toString());
			out.close();
			owner.echoLine(0, "File updated");
		} catch (FileNotFoundException e) {
			// should never happen...
			e.printStackTrace();
		}
	}
	
	public StringBuilder getTextWithoutLine(String resPath, int lineNumber) {
		StringBuilder modifiedFile = new StringBuilder();
		Scanner scanner = IOUtil.createScanner(resPath);
		int currLine = 1;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (currLine != lineNumber) {
				modifiedFile.append(line + "\r\n");
			}
			currLine++;
		}
		scanner.close();
		return modifiedFile;
	}

}
