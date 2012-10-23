package model.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.mail.Mail;
import model.util.CollectionUtil;

import org.apache.log4j.Logger;



public class DelHeaderPatternValidator implements EmailValidator {

	private static Logger logger = Logger.getLogger(DelContentTypeValidator.class);
	private static KeyValueConfiguration deleteHeaderPatternConfig = Config.getInstance().getKeyValueConfig("notdelete_header_pattern");

	@Override
	public boolean validate(User user, Mail email) {
		
		if(user == null || email == null){
			logger.info("User and Email cant be null");
			throw new IllegalStateException();
		}
		
		String[] headerPatterns = CollectionUtil.splitAndTrim(deleteHeaderPatternConfig.get(user.getMail()) , ",");
		if(headerPatterns == null){
			// No restrictions for this user
			return true;
		}
		Map<String, String> headerPatternMap = new HashMap<String, String>();
		for(String s: headerPatterns){
			String[] lineSplit = CollectionUtil.splitAndTrim(s, "eq");
			headerPatternMap.put(lineSplit[0], lineSplit[1]);
		}
		for (Entry<String, String> entry: headerPatternMap.entrySet()) {
			if (email.containsHeader(entry.getKey(), entry.getValue())) {
				logger.info("Restricting message deletion because mail has the following header " + entry);
				return false;
			}
		}
		return true;
	}
	
}