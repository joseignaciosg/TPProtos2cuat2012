package model.validator;

import java.util.Collection;

import model.configuration.Config;
import model.configuration.SimpleListConfiguration;

import org.apache.commons.net.util.SubnetUtils;

public class IpValidator {

	public boolean isBanned(String userIp) {
		SimpleListConfiguration bannedListConfig = Config.getInstance().getSimpleListConfig("banned_ip");
		Collection<String> bannedList = bannedListConfig.getValues();
		boolean isClientIpBanned = false;
		for (String ip : bannedList) {
			String[] inetAddressParts = ip.split("/");
			if (inetAddressParts.length < 2) { // No subnet mask declaration
				isClientIpBanned = userIp.equals(inetAddressParts[0]);
			} else {
				// access_ip.conf has an ip with subnet mask declaration
				// (i.e. 200.232.44.0/16 )
				SubnetUtils subnetIp = new SubnetUtils(ip);
				isClientIpBanned = subnetIp.getInfo().isInRange(userIp);
			}
			if (isClientIpBanned) {
				return true;
			}
		}
		return isClientIpBanned;
	}
}
