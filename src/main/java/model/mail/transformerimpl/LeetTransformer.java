package model.mail.transformerimpl;

import java.io.IOException;
import java.util.List;

import model.parser.mime.MimeHeader;


public class LeetTransformer extends Transformer {

    @Override
    public StringBuilder transform(StringBuilder part, List<MimeHeader> partheaders)
	    throws IOException {
	StringBuilder ret = new StringBuilder();
	String[] lines = part.toString().split("\r\n");
	if(partheaders.contains(new MimeHeader("Content-Type:text/plain"))){
	    for (int i=0; i<lines.length; i++){
		String line = lines[i];
		line = line.replaceAll("a", "4");
		line = line.replaceAll("e", "3");
		line = line.replaceAll("i", "1");
		line = line.replaceAll("o", "0");
		ret.append(line + "\r\n");
	    }
	}
	return ret;
    }

}