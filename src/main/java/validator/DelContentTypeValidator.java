package validator;

import model.Email;

public class DelContentTypeValidator implements EmailValidator {

	@Override
	public boolean validate(Email email) {
		return false;
	}

}
