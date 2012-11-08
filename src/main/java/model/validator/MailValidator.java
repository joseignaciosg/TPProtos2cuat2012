package model.validator;

import model.User;
import model.mail.Mail;

public interface MailValidator {
	
	boolean hasRestrictions(User user);
	
	void validate(User user, Mail email) throws MailValidationException;

}
