package model.validator;

import java.util.Scanner;

import model.configuration.Config;
import model.configuration.SimpleListConfiguration;

import org.apache.commons.net.util.SubnetUtils;



public class IpValidator {

	public boolean isBanned(String userIp) {
		SimpleListConfiguration bannedList = Config.getInstance().getSimpleListConfig("banned_ip");
		Scanner scannerIps = bannedList.createScanner();
		boolean isClientIpBanned = false;
		while (scannerIps.hasNextLine() && !isClientIpBanned) {
			String ip = scannerIps.nextLine();
			String[] inetAddressParts = ip.split("/");
			if (inetAddressParts.length < 2) { // No subnet mask declaration
				isClientIpBanned = userIp.equals(inetAddressParts[0]);
			} else {
				// access_ip.conf has an ip with subnet mask declaration
				// (i.e. 200.232.44.0/16 )
				SubnetUtils subnetIp = new SubnetUtils(ip);
				isClientIpBanned = subnetIp.getInfo().isInRange(userIp);
			}
		}
		scannerIps.close();
		return isClientIpBanned;
	}
}
