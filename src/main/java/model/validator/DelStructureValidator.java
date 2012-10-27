package model.validator;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.util.CollectionUtil;

public class DelStructureValidator implements EmailValidator {

	private static KeyValueConfiguration deleteStructureConfig = Config.getInstance().getKeyValueConfig("notdelete_structure");

	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		String userRestruictions = deleteStructureConfig.get(user.getMail());
		if (userRestruictions == null) {
			return;
		}
		String[] desiredStructure = CollectionUtil.trimAll(userRestruictions.split(","));
		for (String field: desiredStructure) {
			if ("dissabledAttachments".equals(field) && email.hasAttachments()) {
				String message = "Restricting message deletion because mail has attachments";
				throw new MailValidationException(message);
			}
		}
	}
	
}