package validator;

import java.util.Arrays;

import model.Email;
import model.User;

import org.apache.log4j.Logger;
import org.mockito.internal.util.ArrayUtils;

import util.CollectionUtil;
import util.Config;


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
						"the following extension: " +  Arrays.toString(fileExtensions));
				return false;
			}
		}
		
		return true;
	}
	
}
