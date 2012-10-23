package service.command.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import model.configuration.Config;

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
		String fullPath = Config.getInstance().getFullPath(params[1]);
		if (fullPath == null) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
			return;
		}
		StringBuilder text = getTextWithoutLine(fullPath, lineNumberToRemove);
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
	
	public StringBuilder getTextWithoutLine(String fullPath, int lineNumber) {
		StringBuilder modifiedFile = new StringBuilder();
		Scanner scanner = new Scanner(fullPath);
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
