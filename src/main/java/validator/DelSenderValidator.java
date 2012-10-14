package validator;

import model.Email;
import model.User;

import org.apache.log4j.Logger;

import util.CollectionUtil;
import util.Config;

public class DelSenderValidator implements EmailValidator {

	private static Logger logger = Logger.getLogger(DelSenderValidator.class);
	private static Config deleteSenderConfig = Config.getInstance().getConfig(
			"notdelete_sender");

	@Override
	public boolean validate(User user, Email email) {
		
		if(user == null || email == null){
			logger.info("User and Email cant be null");
			throw new IllegalStateException();
		}
		
		
		String[] bannedSenders = CollectionUtil.splitAndTrim(deleteSenderConfig.get(user.getMail()) , ",");
		if(bannedSenders == null){
			// No restrictions for this user
			return true;
		}
		
		for(String s: bannedSenders){
			if (s.equals(email.getSender())) {
				logger.info("Restricting message deletion because mail sender "
						+ s + " is banned.");
				return false;
			}
		}

		return true;
	}

}