import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ImageRotationTest {

	@Test
	public void rotateImageTest() {
	    	String toFlip = "src/test/resources/images/image1.jpg";
	    	List<String> commands = new LinkedList<String>();
		commands.add("java");
		commands.add("-jar");
		commands.add("apps/rotateImage.jar");
		commands.add(toFlip);
		ProcessBuilder pb = new ProcessBuilder(commands);
		try {
			Process process = pb.start();
			process.waitFor();
		} catch (Exception e) {
		}
	}
}
