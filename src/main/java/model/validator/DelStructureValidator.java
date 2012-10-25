package model.validator;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.util.CollectionUtil;

import org.apache.log4j.Logger;


public class DelStructureValidator implements EmailValidator {

	private static Logger logger = Logger.getLogger(DelStructureValidator.class);
	private static KeyValueConfiguration deleteStructureConfig = Config.getInstance().getKeyValueConfig("notdelete_structure");

	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		if (user == null || email == null) {
			logger.info("User and Email cant be null");
			throw new IllegalStateException();
		}
		String[] desiredStructure = CollectionUtil.splitAndTrim(deleteStructureConfig.get(user.getMail()), ",");
		if (desiredStructure == null || desiredStructure.length == 0) {
			// No restrictions for this user
			return;
		}
		for (String field: desiredStructure) {
			if ("dissabledAttachments".equals(field) && email.hasAttachments()) {
				String message = "Restricting message deletion because mail has attachments";
				throw new MailValidationException(message);
			}
		}
	}
	
}