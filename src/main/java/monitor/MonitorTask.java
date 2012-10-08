package monitor;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimerTask;

public class MonitorTask extends TimerTask {

	private String taskName; // A string to output
	private DataOutputStream outputStream;
	InputStream in; // Statistics log file
	private Scanner scanner;

	public MonitorTask(String objectName, DataOutputStream outputStream, InputStream in) {
		this.taskName = objectName;
		this.outputStream = outputStream;
		this.in = in;
		scanner = new Scanner(in);
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
			outputStream.writeBytes("Statistics (" + current_time + ")\n\n");
			
			while(scanner.hasNextLine()){
				outputStream.writeBytes("\n\n" +  scanner.nextLine() + "\n\n" );
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean cancel() {
		scanner.close();
		return super.cancel();
	}
}