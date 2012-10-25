package model.validator;

import java.util.Arrays;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.util.CollectionUtil;

public class DelContentTypeValidator implements EmailValidator {

	private static KeyValueConfiguration deleteContentTypeConfig = Config.getInstance().getKeyValueConfig("notdelete_content_type");

	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		String[] fileExtensions = CollectionUtil.splitAndTrim(deleteContentTypeConfig.get(user.getMail()), ",");
		if (fileExtensions == null) {
			// No restrictions for this user
			return;
		}
		for (String extension : fileExtensions) {
			if (email.hasAttachmentWithExtension(extension)) {
				String message = "Restricting message deletion because mail has attachments with the following extension: " + Arrays.toString(fileExtensions);
				throw new MailValidationException(message);
			}
		}
	}

}
