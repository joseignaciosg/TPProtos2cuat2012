package service.command.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import model.configuration.ConfigUtil;
import model.util.CollectionUtil;
import service.AbstractSockectService;
import service.StatusCodes;
import service.command.ServiceCommand;

public class PutCommand extends ServiceCommand {
	
	public PutCommand(AbstractSockectService owner) {
		super(owner);
	}
	
	@Override
	public void execute(String[] params) throws URISyntaxException {
		if (params.length < 2) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_ARGUMENTS);
			return;
		}
		String line = CollectionUtil.join(params, 1, " ");
		String path = ConfigUtil.getInstance().getConfigPath(params[0]);
		if (path == null) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
			return;
		}
		try {
			FileWriter fw = new FileWriter(new File(path), true);
			fw.write("\r\n" + line.trim());
			fw.close();
			owner.echoLine(StatusCodes.OK_FILE_UPDATED, params[0]);
			ConfigUtil.getInstance().update(params[0]);
		} catch (IOException e) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
		}
	}
	

}
