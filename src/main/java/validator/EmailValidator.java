package validator;

import model.Email;
import model.User;

public interface EmailValidator {
	
	public boolean validate(User user, Email email);

}
