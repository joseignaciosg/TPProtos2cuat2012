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
	public boolean validate(User user, Mail email) {
		if (user == null || email == null) {
			logger.info("User and Email cant be null");
			throw new IllegalStateException();
		}
		String[] desiredStructure = CollectionUtil.splitAndTrim(deleteStructureConfig.get(user.getMail()), ",");
		if (desiredStructure == null || desiredStructure.length == 0) {
			// No restrictions for this user
			return true;
		}
		for (String field: desiredStructure) {
			if ("dissabledAttachments".equals(field) && email.hasAttachments()) {
				logger.info("Restricting message deletion because mail has attachments");
				return false;
			}
		}
		return true;
	}
	
}