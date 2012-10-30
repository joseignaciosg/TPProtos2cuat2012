package service.command.impl;

import java.io.File;
import java.util.Scanner;

import model.configuration.Config;
import model.util.CollectionUtil;
import service.AbstractSockectService;
import service.StatusCodes;
import service.command.ServiceCommand;

public class GetFileCommand extends ServiceCommand {

	public GetFileCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		if (CollectionUtil.empty(params)) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_ARGUMENTS);
			return;
		}
		String fullPath = Config.getInstance().getConfigFullPath(params[0]);
		if (fullPath == null) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
			return;
		}
		Scanner scanner = new Scanner(new File(fullPath));
		owner.echoLine(StatusCodes.OK_FILE_PRINTED, params[0]);
		while (scanner.hasNextLine()) {
			owner.echoLine(scanner.nextLine());
		}
		scanner.close();
		owner.echoLine(".");
		scanner.close();
	}

}
