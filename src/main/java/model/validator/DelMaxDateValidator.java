package model.validator;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class DelMaxDateValidator implements EmailValidator {

	private static Logger logger = Logger.getLogger(DelMaxDateValidator.class);
	private static KeyValueConfiguration deleteDateConfig = Config.getInstance().getKeyValueConfig("notdelete_max_age");

	@Override
	public boolean validate(User user, Mail email) {
		
		if(user == null || email == null){
			logger.info("User and Email cant be null");
			throw new IllegalStateException();
		}
		
		String maxDateString = deleteDateConfig.get(user.getMail());
		if(maxDateString == null){
			// No restrictions for this user
			return true;
		}
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
		LocalDate maxDate = dtf.parseLocalDate(maxDateString.trim());
		if(!email.getDate().isAfter(maxDate)){
			logger.info("Restricting message deletion because mail date: " +  
					email.getDate() + " is lower to configuration file declared" +
							" date " + maxDate + " for this user (" + user.getMail() + ")");
			return false;
		}
		return true;
	}
	
}
