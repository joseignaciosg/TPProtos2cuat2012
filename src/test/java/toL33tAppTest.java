import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import junit.framework.Assert;

import org.junit.Test;

import util.ProcessUtil;

public class toL33tAppTest {

    @Test
    public void test() throws IOException {
	File part = File.createTempFile("transfrmedpart", "proxy");
	FileWriter writer = new FileWriter(part, true);
	writer.write("Esto es una prueba");
	writer.close();
	List<String> commands = new LinkedList<String>();
	commands.add("java");
	commands.add("-jar");
	commands.add("apps/toL33t.jar");
	commands.add(part.getAbsolutePath());
	try {
	    ProcessUtil.executeApp(commands);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	Scanner s = new Scanner(part);
	String line = s.nextLine();
	System.out.println(line);
	s.close();
	Assert.assertTrue(line.equals("Est0 3s un4 pru3b4"));
    }

}
