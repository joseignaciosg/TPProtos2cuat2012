package model.validator.emailvalidator;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.validator.MailValidator;
import model.validator.MailValidationException;

public class DelSizeValidator implements MailValidator {

	private static KeyValueConfiguration deleteSizeConfig = Config.getInstance().getKeyValueConfig("notdelete_size");

	@Override
	public boolean hasRestrictions(User user) {
		String maxSizeAccepted = deleteSizeConfig.get(user.getMail());
		return maxSizeAccepted != null;
	}
	
	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		if (!hasRestrictions(user)) {
			return;
		}
		String maxSizeAccepted = deleteSizeConfig.get(user.getMail());
		maxSizeAccepted = maxSizeAccepted.trim();
		if (Long.valueOf(maxSizeAccepted) > email.getSizeInBytes()) {
			String message = "Restricting message deletion because mail size is "
				+ email.getSizeInBytes() + " bytes. Restriction: Max " + maxSizeAccepted + " bytes";
			throw new MailValidationException(message);
		}
	}

}