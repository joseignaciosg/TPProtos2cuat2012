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
	Scanner partScanner = new Scanner(args[1]);
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
    }
}