package validator;

import model.Email;

public class DelSenderValidator implements EmailValidator {

	@Override
	public boolean validate(Email email) {
		return false;
	}

}
