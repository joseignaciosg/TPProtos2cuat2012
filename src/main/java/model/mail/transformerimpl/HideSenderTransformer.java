package model.mail.transformerimpl;

import java.io.IOException;
import java.util.Map;

import model.mail.HeaderTransformer;
import model.parser.mime.MimeHeader;

public class HideSenderTransformer implements HeaderTransformer{
	
	private Map<String, MimeHeader> mailHeaders;
	
	public HideSenderTransformer(Map<String, MimeHeader> mailHeaders) {
		this.mailHeaders = mailHeaders;
	}

	@Override
	public void transform() throws IOException {
		if(mailHeaders.get("From") != null){
			mailHeaders.put("From", new MimeHeader("From: Anonymous <cia@fbi.gov>"));
		}
		
		if(mailHeaders.get("Return-path") != null){
			mailHeaders.put("Return-path", new MimeHeader("Return-path: <cia@fbi.gov>"));
		}
		
	}

}
