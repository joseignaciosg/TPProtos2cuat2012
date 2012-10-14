package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class LeetTransformer extends Transformer {

    @Override
    public File transform(File part, String partheaders) throws IOException {
	//TODO llamar app externa
	//TODO VER SI ES TXT EN LOS HEADERS PARA TRANSFORMAR
	List<String> commands = new LinkedList<String>(); 
//	commands.add("java");
//	commands.add("-jar");
//	commands.add("l33tTransformer");
//	commands.add(part.getAbsolutePath());
	commands.add("ls");
	ProcessBuilder pb = new ProcessBuilder(commands);
	Process process = pb.start();
	try {
	    int ret = process.waitFor();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	 Scanner scanner = new Scanner( process.getInputStream() );
	 while(scanner.hasNextLine()){
	    System.out.println(scanner.nextLine()); 
	 }
	return part;
    }	 
}	
//	File transformedPart = File.createTempFile("transformedpart", "proxy");
//	BufferedWriter out = new BufferedWriter(new FileWriter(transformedPart));
//	Scanner partScanner = new Scanner(part);
//	while (partScanner.hasNextLine()) {
//	    String line = partScanner.nextLine();
//	    line = line.replaceAll("a", "4");
//	    line = line.replaceAll("e", "3");
//	    line = line.replaceAll("i", "1");
//	    line = line.replaceAll("o", "0");
//	    out.write(line + "\r\n");
//	}
//	out.close();
//	partScanner.close();
//	return transformedPart;
//  }
    
