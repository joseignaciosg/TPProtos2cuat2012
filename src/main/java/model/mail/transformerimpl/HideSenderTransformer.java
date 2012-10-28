package model.mail.transformerimpl;

import java.io.IOException;

import model.mail.HeaderTransformer;
import model.parser.mime.MimeHeader;

public class HideSenderTransformer implements HeaderTransformer {

	@Override
	public void transform(MimeHeader header) throws IOException {
		if ("From".equals(header.getKey())) {
			header.setValue("Anonymous <unknown@unknown.com>");
		}
		if ("Return-path".equals(header.getKey())) {
			header.setValue("<unknown@unknown.com>");
		}
	}

}
