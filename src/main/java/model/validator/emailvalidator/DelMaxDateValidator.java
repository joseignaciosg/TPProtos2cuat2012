package model.validator.emailvalidator;

import model.User;
import model.configuration.ConfigUtil;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.validator.MailValidator;
import model.validator.MailValidationException;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DelMaxDateValidator implements MailValidator {

	private static KeyValueConfiguration deleteDateConfig = ConfigUtil.getInstance().getKeyValueConfig("notdelete_max_age");

	@Override
	public boolean hasRestrictions(User user) {
		String maxDateString = deleteDateConfig.get(user.getMail());
		return maxDateString != null;
	}
	
	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		if (!hasRestrictions(user)) {
			return;
		}
		String maxDateString = deleteDateConfig.get(user.getMail());
		DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
		LocalDate maxDate = dtf.parseLocalDate(maxDateString.trim());
		if (!email.getDate().isBefore(maxDate)) {
			String message = "Restricting message deletion because mail date: "
				+ email.getDate()
				+ " is newer than config date " + maxDate 
				+ " for this user (" + user.getMail() + ")";
			throw new MailValidationException(message);
		}
	}

}
