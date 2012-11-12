package model.validator.emailvalidator;

import model.User;
import model.configuration.ConfigUtil;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.util.CollectionUtil;
import model.validator.MailValidator;
import model.validator.MailValidationException;

public class DelStructureValidator implements MailValidator {

	private static KeyValueConfiguration deleteStructureConfig = ConfigUtil.getInstance().getKeyValueConfig("notdelete_structure");

	@Override
	public boolean hasRestrictions(User user) {
		String userRestruictions = deleteStructureConfig.get(user.getMail());
		return userRestruictions != null;
	}
	
	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		if (!hasRestrictions(user)) {
			return;
		}
		String userRestruictions = deleteStructureConfig.get(user.getMail());
		String[] desiredStructure = CollectionUtil.trimAll(userRestruictions.split(","));
		for (String field: desiredStructure) {
			if ("dissabledAttachments".equalsIgnoreCase(field) && email.hasAttachments()) {
				String message = "Restricting message deletion because mail has attachments";
				throw new MailValidationException(message);
			}
		}
	}
	
}