package validator;

import model.Email;
import model.User;

import org.apache.log4j.Logger;

import util.Config;

public class DelSizeValidator implements EmailValidator {

	private static Logger logger = Logger.getLogger(DelSizeValidator.class);
	private static Config deleteSizeConfig = Config.getInstance().getConfig(
			"notdelete_size");

	@Override
	public boolean validate(User user, Email email) {
		
		if(user == null || email == null){
			logger.info("User and Email cant be null");
			throw new IllegalStateException();
		}
		
		
		String maxSizeAccepted = deleteSizeConfig.get(user.getMail()).trim();
		if(maxSizeAccepted == null){
			// No restrictions for this user
			return true;
		}

		if(Long.valueOf(maxSizeAccepted) > email.getSize()){
			logger.info("Restricting message deletion because it's size ("
					+ maxSizeAccepted + " bytes) is bigger than the maximum accepted size for" +
							" this account (" + email.getSize() + "bytes)");
			return false;
		}

		return true;
	}
	
}