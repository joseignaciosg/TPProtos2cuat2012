package validator;

import org.apache.log4j.Logger;

import util.CollectionUtil;
import util.Config;
import model.Email;
import model.User;

public class DelStructureValidator implements EmailValidator {

	private static Logger logger = Logger.getLogger(DelStructureValidator.class);
	private static Config deleteStructureConfig = Config.getInstance().getConfig("notdelete_structure");

	@Override
	public boolean validate(User user, Email email) {
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