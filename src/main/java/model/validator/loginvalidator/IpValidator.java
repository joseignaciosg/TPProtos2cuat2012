package model.validator.loginvalidator;

import java.util.Collection;

import model.configuration.ConfigUtil;
import model.configuration.SimpleListConfiguration;
import model.validator.LoginValidationException;
import model.validator.LoginValidator;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.log4j.Logger;

public class IpValidator implements LoginValidator {

	private static final Logger logger = Logger.getLogger(IpValidator.class);
	
	private String userIp;
	
	public IpValidator(String userIp) {
		this.userIp = userIp;
	}
	
	@Override
	public void validate() throws LoginValidationException {
		SimpleListConfiguration bannedListConfig = ConfigUtil.getInstance().getSimpleListConfig("banned_ip");
		Collection<String> bannedList = bannedListConfig.getValues();
		boolean isClientIpBanned = false;
		for (String ip : bannedList) {
			try {
				String[] inetAddressParts = ip.split("/");
				if (inetAddressParts.length < 2) { // No subnet mask declaration
					isClientIpBanned = userIp.equals(inetAddressParts[0]);
				} else {
					// access_ip.conf has an ip with subnet mask declaration
					// (i.e. 200.232.44.0/16 )
					isClientIpBanned = new SubnetUtils(ip).getInfo().isInRange(userIp);
				}
				if (isClientIpBanned) {
					throw new LoginValidationException("IP " + userIp + " is banned from the server.");
				}
			} catch (IllegalArgumentException e) {
				logger.error("Could not parse Ip " + ip + ". " + e.getMessage());
			}
		}
	}
}
