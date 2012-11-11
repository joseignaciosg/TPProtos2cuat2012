package model.validator.emailvalidator;

import model.User;
import model.configuration.ConfigUtil;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.validator.MailValidator;
import model.validator.MailValidationException;

public class DelSizeValidator implements MailValidator {

	private static KeyValueConfiguration deleteSizeConfig = ConfigUtil.getInstance().getKeyValueConfig("notdelete_size");

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
			String message = "Restricting message deletion because it's size ("
					+ email.getSizeInBytes()
					+ " bytes) is lower than the minimum deletion accepted size for"
					+ " this account (" + maxSizeAccepted + "bytes)";
			throw new MailValidationException(message);
		}
	}

}