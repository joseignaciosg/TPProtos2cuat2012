package model.validator;

import model.Mail;
import model.User;

public interface EmailValidator {
	
	public boolean validate(User user, Mail email);

}
