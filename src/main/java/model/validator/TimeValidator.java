package model.validator;

import model.util.Config;

import org.apache.log4j.Logger;
import org.joda.time.Interval;
import org.joda.time.LocalTime;


public class TimeValidator {

    private static Logger logger = Logger.getLogger(TimeValidator.class);
    private static Config accessTimeConfig = Config.getInstance().getConfig(
	    "access_time");

    public boolean canAccess(final String user) {
	String timeRestriction = accessTimeConfig.get(user);
	if (timeRestriction == null) {
	    return true;
	}
	try {
	    timeRestriction = timeRestriction.trim();
	    final String[] times = timeRestriction.split("-");
	    final String[] startTimeParts = times[0].split(":");
	    final String[] endTimeParts = times[1].split(":");
	    final LocalTime startTime = new LocalTime(
		    Integer.valueOf(startTimeParts[0]),
		    Integer.valueOf(startTimeParts[1]));
	    final LocalTime endTime = new LocalTime(
		    Integer.valueOf(endTimeParts[0]),
		    Integer.valueOf(endTimeParts[1]));
	    final Interval timeRestrictionInterval = new Interval(
		    startTime.toDateTimeToday(), endTime.toDateTimeToday());
	    return timeRestrictionInterval.containsNow();
	} catch (final Exception e) {
	    logger.error("Could not parse schedule for " + user + " - read: "
		    + timeRestriction);
	    return false;
	}
    }
}
