package validator;

import model.Email;
import model.User;

public class DelSenderValidator implements EmailValidator {

	@Override
	public boolean validate(User user, Email email) {
		return false;
	}

}
