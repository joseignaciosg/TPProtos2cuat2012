package validator;

import model.Email;

public class DelMaxAgeValidator implements EmailValidator {

	@Override
	public boolean validate(Email email) {
		return false;
	}

}
