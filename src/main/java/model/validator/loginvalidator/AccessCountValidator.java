package model.validator.loginvalidator;

import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.validator.LoginValidationException;
import model.validator.LoginValidator;

import org.apache.log4j.Logger;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import service.command.impl.stats.StatsService;

public class AccessCountValidator implements LoginValidator {

	private static Logger logger = Logger.getLogger(TimeValidator.class);
	private static KeyValueConfiguration accessCountConfig = Config.getInstance().getKeyValueConfig("access_count");
	protected static final StatsService statsService = StatsService.getInstace();
	
	private String mailAddress;
	
	public AccessCountValidator(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	
	@Override
	public void validate() throws LoginValidationException {
		int allowedAccessTimesPerDay = accessCountConfig.getInt(mailAddress);
		if (allowedAccessTimesPerDay == 0) {
			return;
		}
		
		int todayAccesses = statsService.getNumberOfAccessesToday(mailAddress);
		if(todayAccesses  >= allowedAccessTimesPerDay  &&
				todayAccesses != -1){
			throw new LoginValidationException();
		}
	}
}
