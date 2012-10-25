package model.validator;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;

public class DelSizeValidator implements EmailValidator {

	private static KeyValueConfiguration deleteSizeConfig = Config.getInstance().getKeyValueConfig("notdelete_size");

	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		String maxSizeAccepted = deleteSizeConfig.get(user.getMail()).trim();
		if (maxSizeAccepted == null) {
			return;
		}
		if (Long.valueOf(maxSizeAccepted) > email.getSizeInBytes()) {
			String message = "Restricting message deletion because it's size ("
					+ maxSizeAccepted
					+ " bytes) is bigger than the maximum accepted size for"
					+ " this account (" + email.getSizeInBytes() + "bytes)";
			throw new MailValidationException(message);
		}
	}

}