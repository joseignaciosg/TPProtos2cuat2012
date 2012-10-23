package model.validator;

import model.User;
import model.mail.Mail;

public interface EmailValidator {
	
	public boolean validate(User user, Mail email);

}
