package service.command.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import model.configuration.Config;
import model.util.CollectionUtil;
import model.util.IOUtil;
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
		String line = CollectionUtil.join(params, 1);
		String path = Config.getInstance().getConfigResourcePath(params[0]);
		if(path == null){
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
		}
		try {
			// Find out where the JAR is:
			String path2 = PutCommand.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			path = path2.substring(0, path.lastIndexOf('/')+1);
			// Create the project-folder-file:
			File root = new File(path2);
			
			FileWriter fw = new FileWriter(root, true);
			fw.write("\r\n" + line.trim());
			fw.close();
			owner.echoLine(StatusCodes.OK_FILE_UPDATED, params[0]);
			Config.getInstance().update(params[0]);
		} catch (IOException e) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
			e.printStackTrace();
		} catch (NullPointerException e) {
			owner.echoLine(StatusCodes.ERR_INVALID_PARAMETERS_FILE);
			e.printStackTrace();
		}
	}
	

}
