package monitor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimerTask;

import util.Config;

public class MonitorTask extends TimerTask {

	private String taskName; // A string to output
	private DataOutputStream outputStream;

	public MonitorTask(String objectName, DataOutputStream outputStream) {
		this.taskName = objectName;
		this.outputStream = outputStream;
	}

	/**
	 * When the timer executes, this code is run.
	 */
	public void run() {
		// Get current date/time and format it for output
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String current_time = format.format(date);
		// Output to user the name of the objecet and the current time
		try {
			outputStream.writeBytes("Statistics (" + current_time + ")\n");
			Scanner scanner = reset();
			while (scanner.hasNextLine()) {
				outputStream.writeBytes("\t" +scanner.nextLine() + "\r\n");
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Scanner reset() {
		InputStream in = getClass().getClassLoader().getResourceAsStream(Config.getInstance().get("statistics_file"));
		return new Scanner(in);
	}
	
	@Override
	public boolean cancel() {
		return super.cancel();
	}
}