package validator;

import model.Email;
import model.User;

public class DelStructureValidator implements EmailValidator {

	@Override
	public boolean validate(User user, Email email) {
		return false;
	}

}
