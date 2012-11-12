package model.validator.emailvalidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import model.User;
import model.configuration.ConfigUtil;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.util.CollectionUtil;
import model.validator.MailValidationException;
import model.validator.MailValidator;

import org.apache.log4j.Logger;

public class DelHeaderPatternValidator implements MailValidator {

	private static Logger logger = Logger.getLogger(DelHeaderPatternValidator.class);
	private static KeyValueConfiguration deleteHeaderPatternConfig = ConfigUtil.getInstance().getKeyValueConfig("notdelete_header_pattern");

	@Override
	public boolean hasRestrictions(User user) {
		String userRestrictions = deleteHeaderPatternConfig.get(user.getMail());
		return userRestrictions != null;
	}
	
	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		if (!hasRestrictions(user)) {
			return;
		}
		String userRestrictions = deleteHeaderPatternConfig.get(user.getMail());
		String[] headerPatterns = CollectionUtil.trimAll(userRestrictions.split(","));
		Map<String, String> headerPatternMap = new HashMap<String, String>();
		for (String s : headerPatterns) {
			String[] lineSplit = CollectionUtil.trimAll(s.split("like"));
			if (lineSplit == null || lineSplit.length != 2) {
				logger.error("Invalid cofnig line: " + s + " for user: " + user.getMail());
				continue;
			}
			headerPatternMap.put(lineSplit[0], lineSplit[1]);
		}
		for (Entry<String, String> entry : headerPatternMap.entrySet()) {
			if (email.headerMatches(entry.getKey(), entry.getValue())) {
				String message = "Restricting message deletion because mail has the following header " + entry.getKey();
				throw new MailValidationException(message);
			}
		}
	}

}