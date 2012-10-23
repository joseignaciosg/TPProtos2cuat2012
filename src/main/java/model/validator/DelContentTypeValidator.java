package model.validator;

import java.util.Arrays;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.util.CollectionUtil;

import org.apache.log4j.Logger;


public class DelContentTypeValidator implements EmailValidator {

	private static Logger logger = Logger.getLogger(DelContentTypeValidator.class);
	private static KeyValueConfiguration deleteContentTypeConfig = Config.getInstance().getKeyValueConfig("notdelete_content_type");

	@Override
	public boolean validate(User user, Mail email) {
		
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
