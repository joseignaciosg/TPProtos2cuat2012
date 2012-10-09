package validator;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import util.Config;

public class IpValidator {
	private static Config accessIpConfig = Config.getInstance().getConfig(
			"access_ip");

	public boolean validate(String user, String userIp) {
		String ipString = accessIpConfig.get(user);
		if (ipString == null) { // No entries for this user so he can connect
			System.out.println("IPValidator: No lo encontré.");
			return true;
		}

		String[] inetAddressParts = ipString.split("/");

		if (inetAddressParts.length < 2) { // No subnet mask declaration
			return userIp.equals(inetAddressParts[0]);
		} else { // access_ip.conf has an ip with subnet mask declaration (i.e.
					// 200.232.44.0/16 )
			return belongsToSubnet(inetAddressParts[0], inetAddressParts[1]);
		}

	}

	private boolean belongsToSubnet(String ipAddress, String subnet) { 

		String[] parts = subnet.split("/");
		String subnetIp = parts[0];
		int prefix;

		if (parts.length < 2) {
			prefix = 0;
		} else {
			prefix = Integer.parseInt(parts[1]);
		}

		Inet4Address subnetInetAddress = null;
		Inet4Address userInetAddress = null;
		try {
			subnetInetAddress = (Inet4Address) InetAddress.getByName(subnetIp);
			userInetAddress = (Inet4Address) InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		byte[] b = subnetInetAddress.getAddress();
		int ipInt = ((b[0] & 0xFF) << 24) | ((b[1] & 0xFF) << 16)
				| ((b[2] & 0xFF) << 8) | ((b[3] & 0xFF) << 0);

		byte[] b1 = userInetAddress.getAddress();
		int ipInt1 = ((b1[0] & 0xFF) << 24) | ((b1[1] & 0xFF) << 16)
				| ((b1[2] & 0xFF) << 8) | ((b1[3] & 0xFF) << 0);

		int mask = ~((1 << (32 - prefix)) - 1);

		if ((ipInt & mask) == (ipInt1 & mask)) {
			return true;
		} else {
			return false;
		}
	}

}
