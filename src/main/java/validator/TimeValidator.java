package validator;

import org.apache.log4j.Logger;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import util.Config;

public class TimeValidator {

	private static Logger logger = Logger.getLogger(TimeValidator.class);
	private static Config accessTimeConfig = Config.getInstance().getConfig("access_time");

	public boolean validate(String user) {
		String timeRestriction = accessTimeConfig.get(user);
		if (timeRestriction == null) {
			return true;
		}
		try {
			timeRestriction = timeRestriction.trim();
			String[] times = timeRestriction.split("-");
			String[] startTimeParts = times[0].split(":");
			String[] endTimeParts = times[1].split(":");
			LocalTime startTime = new LocalTime(Integer.valueOf(startTimeParts[0]), Integer.valueOf(startTimeParts[1]));
			LocalTime endTime = new LocalTime(Integer.valueOf(endTimeParts[0]), Integer.valueOf(endTimeParts[1]));
			Interval timeRestrictionInterval = new Interval(startTime.toDateTimeToday(), endTime.toDateTimeToday());
			return timeRestrictionInterval.containsNow();
		} catch (Exception e) {
			logger.error("Could not parse schedule for " + user + " - read: " + timeRestriction);
			return false;
		}
	}
}
