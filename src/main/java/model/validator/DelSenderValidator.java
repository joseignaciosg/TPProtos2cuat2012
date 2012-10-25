package model.validator;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.util.CollectionUtil;

public class DelSenderValidator implements EmailValidator {

	private static KeyValueConfiguration deleteSenderConfig = Config.getInstance().getKeyValueConfig("notdelete_sender");

	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		String[] bannedSenders = CollectionUtil.splitAndTrim(deleteSenderConfig.get(user.getMail()), ",");
		if (bannedSenders == null) {
			// No restrictions for this user
			return;
		}
		for (String s : bannedSenders) {
			if (s.equals(email.getSender())) {
				String message = "Restricting message deletion because mail sender " + s + " is banned.";
				throw new MailValidationException(message);
			}
		}
	}

}