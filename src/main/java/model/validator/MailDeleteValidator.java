package model.validator;

import java.util.ArrayList;
import java.util.List;

import model.User;
import model.mail.Mail;
import model.validator.emailvalidator.DelContentTypeValidator;
import model.validator.emailvalidator.DelHeaderPatternValidator;
import model.validator.emailvalidator.DelMaxDateValidator;
import model.validator.emailvalidator.DelSenderValidator;
import model.validator.emailvalidator.DelSizeValidator;
import model.validator.emailvalidator.DelStructureValidator;

public class MailDeleteValidator {

	private List<MailValidator> validators;
	
	public MailDeleteValidator() {
		validators = new ArrayList<MailValidator>();
		validators.add(new DelContentTypeValidator());
		validators.add(new DelHeaderPatternValidator());
		validators.add(new DelMaxDateValidator());
		validators.add(new DelSenderValidator());
		validators.add(new DelSizeValidator());
		validators.add(new DelStructureValidator());
	}
	
	public boolean hasRestrictions(User user) {
		for (MailValidator validator : validators) {
			if (validator.hasRestrictions(user)) {
				return true;
			}
		}
		return false;
	}
	
	public void validate(User current, Mail mail) throws MailValidationException {
		for (MailValidator validator : validators) {
			validator.validate(current, mail);
		}
	}
}
