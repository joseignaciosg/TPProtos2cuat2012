package service.command.impl;

import java.io.FileWriter;
import java.io.IOException;

import model.StatusCodes;

import service.AbstractSockectService;
import service.command.ServiceCommand;
import util.CollectionUtil;
import util.Config;
import util.IOUtil;


public class AddCommand extends ServiceCommand {
	
	public AddCommand(AbstractSockectService owner) {
		super(owner);
	}
	
	@Override
	public void execute(String[] params) {
		if (params.length < 2) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_ARGUMENTS);
			return;
		}
		String line = CollectionUtil.join(params, 1);
		String resPath = Config.getInstance().get("specific_conf_dir") + params[0];
		try {
			FileWriter fw = new FileWriter(IOUtil.fullPath(resPath), true);
			fw.write(line.trim() + "\r\n");
			fw.close();
			owner.echoLine(StatusCodes.OK_FILE_UPDATED, params[0]);
		} catch (IOException e) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
		} catch (NullPointerException e) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
		}
	}
	

}
