import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import parser.LeetTransformer;
import util.IOUtil;


public class TransformerTest {

    private File tmpMailFile;
    private LeetTransformer transformer;
    
    @Before
    public void init() throws IOException{
	String path = IOUtil.fullPath("test_mail.txt");
	if (path !=  null){
	    tmpMailFile = new File(path);
	}
	transformer = new LeetTransformer();
    }
    
    @Test
    public void applyTest() throws IOException{
	printFile(tmpMailFile);
	File transMail = transformer.apply(tmpMailFile);
	System.out.println("_________________________________");
	printFile(transMail);
	
    }
    
    private  void printFile(File file) throws FileNotFoundException{
	Scanner s = new Scanner(file);
	while (s.hasNextLine()){
	    System.out.println(s.nextLine());
	}
	s.close();
    }
    
    
}
