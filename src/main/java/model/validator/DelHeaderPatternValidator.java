package model.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.util.CollectionUtil;

public class DelHeaderPatternValidator implements EmailValidator {

	private static Logger logger = Logger.getLogger(DelHeaderPatternValidator.class);
	private static KeyValueConfiguration deleteHeaderPatternConfig = Config.getInstance().getKeyValueConfig("notdelete_header_pattern");

	@Override
	public void validate(User user, Mail email) throws MailValidationException {
		String userRestrictions = deleteHeaderPatternConfig.get(user.getMail());
		if (userRestrictions == null) {
			return;
		}
		String[] headerPatterns = CollectionUtil.trimAll(userRestrictions.split(","));
		Map<String, String> headerPatternMap = new HashMap<String, String>();
		for (String s : headerPatterns) {
			String[] lineSplit = CollectionUtil.trimAll(s.split("eq"));
			if (lineSplit == null || lineSplit.length != 2) {
				logger.error("Invalid cofnig line: " + s + " for user: " + user.getMail());
				continue;
			}
			headerPatternMap.put(lineSplit[0], lineSplit[1]);
		}
		for (Entry<String, String> entry : headerPatternMap.entrySet()) {
			if (email.containsHeader(entry.getKey(), entry.getValue())) {
				String message = "Restricting message deletion because mail has the following header " + entry;
				throw new MailValidationException(message);
			}
		}
	}

}