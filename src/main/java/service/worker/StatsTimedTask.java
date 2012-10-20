package service.worker;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import util.Config;
import util.IOUtil;

public class StatsTimedTask extends TimerTask {

	private static Logger logger = Logger.getLogger(TimerTask.class);
	
	private String taskName;
	private DataOutputStream outputStream;

	public StatsTimedTask(String objectName, DataOutputStream outputStream) {
		this.taskName = objectName;
		this.outputStream = outputStream;
	}

	/**
	 * When the timer executes, this code is run.
	 */
	public void run() {
		// logger.debug(taskName + " is now excecuting.");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String current_time = format.format(new Date());
		try {
			outputStream.writeBytes("Statistics (" + current_time + ")\n");
			Scanner scanner = reset();
			while (scanner.hasNextLine()) {
				outputStream.writeBytes("\t" +scanner.nextLine() + "\r\n");
			}
			scanner.close();
		} catch (IOException e) {
			logger.error(taskName + " / ");
			e.printStackTrace();
		}
	}
	
	private Scanner reset() {
		return IOUtil.createScanner(Config.getInstance().get("statistics_file"));
	}
	
}