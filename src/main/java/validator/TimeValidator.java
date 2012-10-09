package validator;

import org.joda.time.Interval;
import org.joda.time.LocalTime;

import util.Config;

public class TimeValidator {
	
	private static Config accessTimeConfig = Config.getInstance().getConfig(
			"access_time");

	public boolean validate(String user){
		
		String timeRestriction = accessTimeConfig.get(user);
		if(timeRestriction == null){ 
			
			// No restriction entries found for this user; access guaranteed
			return true;
		}
		
		// It's important to remove all whitespaces and blanks so the date builders wont 
		// throw unnecesary exceptions
		// Maybe we should make the "replaceAll" in the get method of Config class		
		timeRestriction = timeRestriction.replaceAll("\\s","");

		String[] times = timeRestriction.split("-");
		String[] startTimeParts = times[0].split(":");
		String[] endTimeParts = times[1].split(":");
		LocalTime startTime = new LocalTime(Integer.valueOf(startTimeParts[0]), Integer.valueOf(startTimeParts[1]));
		LocalTime endTime = new LocalTime(Integer.valueOf(endTimeParts[0]), Integer.valueOf(endTimeParts[1]));
		Interval timeRestrictionInterval = new Interval(startTime.toDateTimeToday(), endTime.toDateTimeToday());

		// Finally we check if the current time is between the interval
		return timeRestrictionInterval.containsNow();
		
	}
	
}
