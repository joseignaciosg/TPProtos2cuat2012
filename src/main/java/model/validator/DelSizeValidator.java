package model.validator;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;

import org.apache.log4j.Logger;



public class DelSizeValidator implements EmailValidator {

	private static Logger logger = Logger.getLogger(DelSizeValidator.class);
	private static KeyValueConfiguration deleteSizeConfig = Config.getInstance().getKeyValueConfig("notdelete_size");

	@Override
	public boolean validate(User user, Mail email) {
		if (user == null || email == null) {
			logger.info("User and Email cant be null");
			throw new IllegalStateException();
		}
		String maxSizeAccepted = deleteSizeConfig.get(user.getMail()).trim();
		if (maxSizeAccepted == null) {
			return true;
		}
		if (Long.valueOf(maxSizeAccepted) > email.getSizeInBytes()) {
			logger.info("Restricting message deletion because it's size ("
				+ maxSizeAccepted + " bytes) is bigger than the maximum accepted size for" +
				" this account (" + email.getSizeInBytes() + "bytes)");
			return false;
		}
		return true;
	}
	
}