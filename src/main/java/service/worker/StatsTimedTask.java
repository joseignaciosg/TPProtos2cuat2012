package service.worker;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;


public class StatsTimedTask extends TimerTask {

	private String taskName;
	private DataOutputStream outputStream;

	public StatsTimedTask(String objectName, DataOutputStream outputStream) {
		this.taskName = objectName;
		this.outputStream = outputStream;
	}

	@Override
	public void run() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String current_time = format.format(new Date());
		try {
			outputStream.writeBytes("Current time: " + current_time + "\r\n");
			outputStream.writeBytes(".\r\n");
		} catch (IOException e) {
			throw new IllegalStateException("Could not write data to output stream.");
		}
	}
	
	public String getTaskName() {
		return taskName;
	}
	
}