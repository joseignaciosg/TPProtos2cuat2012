package model.validator.emailvalidator;

import java.util.Arrays;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.parser.mime.ContentTypeUtil;
import model.util.CollectionUtil;
import model.validator.MailValidator;
import model.validator.MailValidationException;

public class DelContentTypeValidator implements MailValidator {

	private static KeyValueConfiguration deleteContentTypeConfig = Config.getInstance().getKeyValueConfig("notdelete_content_type");

	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		String userRestrictions = deleteContentTypeConfig.get(user.getMail());
		if (userRestrictions == null) {
			return;
		}
		String[] fileExtensions = CollectionUtil.trimAll(userRestrictions.split(","));
		for (String extension : fileExtensions) {
			String mimeType = ContentTypeUtil.getContentType(extension);
			if (email.hasAttachmentWithExtension(mimeType)) {
				String message = "Restricting message deletion because mail has attachments with the following extension: " + Arrays.toString(fileExtensions);
				throw new MailValidationException(message);
			}
		}
	}

}
