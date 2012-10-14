package validator;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import util.CollectionUtil;
import util.Config;
import model.Email;
import model.User;


public class DelContentTypeValidator implements EmailValidator {

	private static Logger logger = Logger.getLogger(DelContentTypeValidator.class);
	private static Config deleteContentTypeConfig = Config.getInstance().getConfig(
			"notdelete_content_type");

	@Override
	public boolean validate(User user, Email email) {
		
		if(user == null || email == null){
			logger.info("User and Email cant be null");
			throw new IllegalStateException();
		}
		
		String[] fileExtensions = CollectionUtil.splitAndTrim(deleteContentTypeConfig.get(user.getMail()) , ",");
		if(fileExtensions == null){
			// No restrictions for this user
			return true;
		}
		
		for(String extension: fileExtensions){
			if(email.hasAttachmentWithExtension(extension)){
				logger.info("Restricting message deletion because mail has attachments with " +
						"the following extension: " +  extension);
				return false;
			}
		}
		
		return true;
	}
	
}
