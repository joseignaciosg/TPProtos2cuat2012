package service.command.impl;

import java.io.FileWriter;
import java.io.IOException;

import service.command.ServiceCommand;
import util.CollectionUtil;
import util.Config;
import util.IOUtil;


public class AddCommand extends ServiceCommand {
	
	@Override
	public void execute(String[] params) {
		if (params.length < 2) {
			owner.echoLine(101, "Invalid Parameters: missing arguments");
			return;
		}
		String line = CollectionUtil.join(params, 1);
		String resPath = Config.getInstance().get("specific_conf_dir") + params[0];
		try {
			FileWriter fw = new FileWriter(IOUtil.fullPath(resPath), true);
			fw.write(line.trim() + "\r\n");
			fw.close();
			owner.echoLine(0, "File " + params[0] + " updated");
		} catch (IOException e) {
			owner.echoLine(102, "Invalid Parameters: file does not exists");
		} catch (NullPointerException e) {
			owner.echoLine(102, "Invalid Parameters: file does not exists");
		}
	}
	

}
