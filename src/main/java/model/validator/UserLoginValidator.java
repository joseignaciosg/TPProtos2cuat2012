package model.validator;

import model.User;
import model.validator.loginvalidator.MaxLoginPerDayValidator;
import model.validator.loginvalidator.TimeValidator;

import org.apache.log4j.Logger;

public class UserLoginValidator {

	private static final Logger logger = Logger.getLogger(UserLoginValidator.class);

	public void userCanLogin(User user) throws LoginValidationException {		
		String userMail = user.getMail();
		logger.debug("Checking time access for user: " + userMail);
		new TimeValidator(userMail).validate();
		logger.debug("Checking login amount for user: " + userMail);
		new MaxLoginPerDayValidator(user).validate();
	}
	
	public void validateUserLogin(User user) throws LoginValidationException {
		
	}

}
