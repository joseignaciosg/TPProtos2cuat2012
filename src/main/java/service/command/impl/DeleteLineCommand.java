package service.command.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import model.util.Config;
import model.util.IOUtil;

import service.AbstractSockectService;
import service.StatusCodes;
import service.command.ServiceCommand;

public class DeleteLineCommand extends ServiceCommand {

	public DeleteLineCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		if (params.length < 2) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_ARGUMENTS);
			return;
		}
		int lineNumberToRemove;
		try {
			lineNumberToRemove = Integer.valueOf(params[0]);
		} catch (NumberFormatException e) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_NUMBER,  "input: " + params[0]);
			return;
		}
		String resPath = Config.getInstance().get("specific_conf_dir") + params[1];
		String fullPath = IOUtil.fullPath(resPath);
		if (fullPath == null) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
			return;
		}
		StringBuilder text = getTextWithoutLine(resPath, lineNumberToRemove);
		try {
			PrintWriter out = new PrintWriter(fullPath);
			out.print(text.toString());
			out.close();
			owner.echoLine(StatusCodes.OK_FILE_UPDATED);
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
