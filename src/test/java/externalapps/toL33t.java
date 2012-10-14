package externalapps;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class toL33t {

    public static void main(String[] args) throws IOException {	
	//TODO pisar archivo
	File transformedPart = File.createTempFile("transformedpart", "proxy");
	BufferedWriter out = new BufferedWriter(new FileWriter(transformedPart));
	File mail = new File(args[1]);
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
	
	partScanner = new Scanner(transformedPart);
	FileWriter out2 = new FileWriter(mail);
	while (partScanner.hasNextLine()) {
	    String line = partScanner.nextLine();
	    out2.write(line + "\r\n");
	}
	out2.close();
	partScanner.close();
    }
    
}