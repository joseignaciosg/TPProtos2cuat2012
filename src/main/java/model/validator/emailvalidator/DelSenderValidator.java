package model.validator.emailvalidator;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.util.CollectionUtil;
import model.validator.MailValidator;
import model.validator.MailValidationException;

public class DelSenderValidator implements MailValidator {

	private static KeyValueConfiguration deleteSenderConfig = Config.getInstance().getKeyValueConfig("notdelete_sender");

	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		String userRestrictions = deleteSenderConfig.get(user.getMail());
		if (userRestrictions == null) {
			return;
		}
		String[] bannedSenders = CollectionUtil.trimAll(userRestrictions.split(","));
		for (String s : bannedSenders) {
			if (s.equals(email.getSender())) {
				String message = "Restricting message deletion because mail sender " + s + " is banned.";
				throw new MailValidationException(message);
			}
		}
	}

}