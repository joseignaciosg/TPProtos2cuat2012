package model.validator.emailvalidator;

import model.User;
import model.configuration.ConfigUtil;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.parser.mime.ContentTypeUtil;
import model.util.CollectionUtil;
import model.validator.MailValidationException;
import model.validator.MailValidator;

public class DelContentTypeValidator implements MailValidator {

	private static KeyValueConfiguration deleteContentTypeConfig = ConfigUtil.getInstance().getKeyValueConfig("notdelete_content_type");

	@Override
	public boolean hasRestrictions(User user) {
		String userRestrictions = deleteContentTypeConfig.get(user.getMail());
		return userRestrictions != null;
	}
	
	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		if (!hasRestrictions(user)) {
			return;
		}
		String userRestrictions = deleteContentTypeConfig.get(user.getMail());
		String[] fileExtensions = CollectionUtil.trimAll(userRestrictions.split(","));
		for (String extension : fileExtensions) {
			String mimeType = ContentTypeUtil.getContentType(extension);
			if (email.hasAttachmentWithExtension(mimeType)) {
				String message = "Restricting message deletion because mail has a " + extension + " attachment.";
				throw new MailValidationException(message);
			}
		}
	}

}
