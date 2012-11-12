package model.validator.loginvalidator;

import model.User;
import model.configuration.ConfigUtil;
import model.configuration.KeyValueConfiguration;
import model.validator.LoginValidationException;
import model.validator.LoginValidator;
import service.command.impl.stats.StatsService;

public class MaxLoginPerDayValidator implements LoginValidator {

	private static final KeyValueConfiguration maxLoginConfig = ConfigUtil.getInstance().getKeyValueConfig("access_amount");
	private static final StatsService statsService = StatsService.getInstace();
	
	private User user;
	
	public MaxLoginPerDayValidator(User user) {
		this.user = user;
	}
	
	@Override
	public void validate() throws LoginValidationException {
		String value = maxLoginConfig.get(user.getMail());
		String userMail = user.getMail();
		if (value == null) {
			statsService.incrementNumberOfAccesses(userMail);
			return;
		}
		int maxAmount = Integer.valueOf(value);
		int todayAccesses = statsService.incrementNumberOfAccesses(userMail);
		if (maxAmount < todayAccesses) {
			statsService.decrementNumberOfAccesses(userMail);
			throw new LoginValidationException("Max number of login acceses reached for the day.");			
		}
	}
	
	
}
