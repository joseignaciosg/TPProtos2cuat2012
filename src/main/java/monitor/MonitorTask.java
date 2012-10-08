package monitor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class MonitorTask extends TimerTask {

	private String taskName; // A string to output

	public MonitorTask(String objectName) {
		this.taskName = objectName;
	}

	/**
	 * When the timer executes, this code is run.
	 */
	public void run() {
		// Get current date/time and format it for output
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
		String current_time = format.format(date);

		// Output to user the name of the objecet and the current time
		System.out.println(taskName + " - Current time: " + current_time);
	}
}