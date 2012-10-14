package validator;

import model.Email;

public class DelStructureValidator implements EmailValidator {

	@Override
	public boolean validate(Email email) {
		return false;
	}

}
