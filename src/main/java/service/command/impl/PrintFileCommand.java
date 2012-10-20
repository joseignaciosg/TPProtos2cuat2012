package service.command.impl;

import java.util.Scanner;

import model.StatusCodes;
import model.util.CollectionUtil;
import model.util.Config;
import model.util.IOUtil;

import service.AbstractSockectService;
import service.command.ServiceCommand;

public class PrintFileCommand extends ServiceCommand {

	public PrintFileCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		if (CollectionUtil.empty(params)) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_ARGUMENTS);
			return;
		}
		String resPath = Config.getInstance().get("specific_conf_dir") + params[0];
		Scanner scanner = IOUtil.createScanner(resPath);
		if (scanner == null) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
			return;
		}
		owner.echoLine(StatusCodes.OK_FILE_PRINTED, params[0]);
		while (scanner.hasNextLine()) {
			owner.echoLine(scanner.nextLine());
		}
		scanner.close();
		owner.echoLine(".");
		scanner.close();
	}

}
