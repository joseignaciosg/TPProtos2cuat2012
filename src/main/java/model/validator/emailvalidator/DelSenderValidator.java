package model.validator.emailvalidator;

import model.User;
import model.configuration.ConfigUtil;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.util.CollectionUtil;
import model.validator.MailValidator;
import model.validator.MailValidationException;

public class DelSenderValidator implements MailValidator {

	private static KeyValueConfiguration deleteSenderConfig = ConfigUtil.getInstance().getKeyValueConfig("notdelete_sender");

	@Override
	public boolean hasRestrictions(User user) {
		String userRestrictions = deleteSenderConfig.get(user.getMail());
		return userRestrictions != null;
	}
	
	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		if (!hasRestrictions(user)) {
			return;
		}
		String userRestrictions = deleteSenderConfig.get(user.getMail());
		String[] bannedSenders = CollectionUtil.trimAll(userRestrictions
				.split(","));
		String sender = email.getSender();
		if (sender != null) {
			for (String s : bannedSenders) {
				if (s.toLowerCase().equals(sender)) {
					String message = "Restricting message deletion because mail sender "
							+ s + " is banned.";
					throw new MailValidationException(message);
				}
			}
		}
	}

}