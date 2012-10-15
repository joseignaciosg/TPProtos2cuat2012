package externalapps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ToL33t {

	public static void main(String[] args) throws IOException {
		File transformedPart = File.createTempFile("transfrmedpart", "proxy");
		BufferedWriter out = new BufferedWriter(new FileWriter(transformedPart));
		File mail = new File(args[0]);
		Scanner partScanner = new Scanner(mail);
		while (partScanner.hasNextLine()) {
			String line = partScanner.nextLine();
			line = line.replaceAll("a", "4");
			line = line.replaceAll("e", "3");
			line = line.replaceAll("i", "1");
			line = line.replaceAll("o", "0");
			out.write(line + "\r\n");
		}
		out.close();
		partScanner.close();

		Scanner partScanner2 = new Scanner(transformedPart);
		FileWriter out2 = new FileWriter(mail);
		while (partScanner2.hasNextLine()) {
			String line = partScanner2.nextLine();
			out2.write(line + "\r\n");
		}
		out2.close();
		partScanner2.close();
	}

}