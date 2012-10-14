package validator;

import model.Email;

public class DelSizeValidator implements EmailValidator {

	@Override
	public boolean validate(Email email) {
		return false;
	}

}
