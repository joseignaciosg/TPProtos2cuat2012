package service.command.impl;

import java.io.FileWriter;
import java.io.IOException;

import model.configuration.Config;
import model.util.CollectionUtil;
import service.AbstractSockectService;
import service.StatusCodes;
import service.command.ServiceCommand;

public class PutCommand extends ServiceCommand {
	
	public PutCommand(AbstractSockectService owner) {
		super(owner);
	}
	
	@Override
	public void execute(String[] params) {
		if (params.length < 2) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_ARGUMENTS);
			return;
		}
		String line = CollectionUtil.join(params, 1);
		String fullPath = Config.getInstance().getFullPath(params[0]);
		try {
			FileWriter fw = new FileWriter(fullPath, true);
			fw.write("\r\n" + line.trim());
			fw.close();
			owner.echoLine(StatusCodes.OK_FILE_UPDATED, params[0]);
			Config.getInstance().update(params[0]);
		} catch (IOException e) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
		} catch (NullPointerException e) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
		}
	}
	

}
