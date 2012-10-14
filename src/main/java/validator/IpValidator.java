package validator;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.apache.log4j.Logger;

import util.Config;
import util.IOUtil;

public class IpValidator {

	private static Logger logger = Logger.getLogger(IpValidator.class);

	public boolean validate(String userIp) {
		String fileName = Config.getInstance().get("specific_conf_dir")
				+ "access_ip.conf";
		Scanner scannerIps = IOUtil.createScanner(fileName);
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
