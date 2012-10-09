package validator;

import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import util.Config;

public class IpValidator {

	private static Logger logger = Logger.getLogger(IpValidator.class);

	public boolean validate(String userIp) {
		String fileName = Config.getInstance().get("specific_conf_dir") + "access_ip.conf";
		InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
		Scanner scannerIps = new Scanner(in);
		boolean hasAccess = false;
		while (scannerIps.hasNextLine() && !hasAccess) {
			String ip = scannerIps.nextLine();
			String[] inetAddressParts = ip.split("/");
			if (inetAddressParts.length < 2) { // No subnet mask declaration
				hasAccess = userIp.equals(inetAddressParts[0]);
			} else { 
				// access_ip.conf has an ip with subnet mask declaration
				// (i.e. 200.232.44.0/16 )
				hasAccess = belongsToSubnet(userIp, inetAddressParts[1]);
			}
		}
		scannerIps.close();
		return hasAccess;
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
			String error = "Could not get subnet or inet address. ";
			error += "Subnet: " + subnetIp + ", Address: " + ipAddress;
			logger.error(error);
			return false;
		}
		byte[] b = subnetInetAddress.getAddress();
		int ipInt = ((b[0] & 0xFF) << 24) | ((b[1] & 0xFF) << 16) | ((b[2] & 0xFF) << 8) | ((b[3] & 0xFF) << 0);
		byte[] b1 = userInetAddress.getAddress();
		int ipInt1 = ((b1[0] & 0xFF) << 24) | ((b1[1] & 0xFF) << 16) | ((b1[2] & 0xFF) << 8) | ((b1[3] & 0xFF) << 0);
		int mask = ~((1 << (32 - prefix)) - 1);
		if ((ipInt & mask) == (ipInt1 & mask)) {
			return true;
		} else {
			return false;
		}
	}

}
