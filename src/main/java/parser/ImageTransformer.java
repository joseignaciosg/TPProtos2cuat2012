package parser;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import util.ProcessUtil;

public class ImageTransformer extends Transformer {

    @Override
    public File transform(File part, String partheaders) throws IOException {
	List<String> commands = new LinkedList<String>();
	if(Pattern.matches(".*image.*",partheaders) ||
           Pattern.matches(".*.jpg.*",partheaders)	){
	    commands.add("java");
	    commands.add("-jar");
	    commands.add("apps/rotateImage.jar");
	    commands.add(part.getAbsolutePath());
	    try {
		ProcessUtil.executeApp(commands);
	    } catch (Exception e) {
		e.printStackTrace();
	    }	    
	}
	return part;
    }

}
