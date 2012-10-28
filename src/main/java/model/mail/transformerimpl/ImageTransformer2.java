package model.mail.transformerimpl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import model.parser.mime.ContentTypeUtil;
import model.parser.mime.MimeHeader;
import model.util.Base64Util;
import model.util.IOUtil;
import model.util.ImageRotator;

public class ImageTransformer2 implements Transformer {

	private ImageRotator rotator = new ImageRotator();
	private static final List<String> availableTypes = new LinkedList<String>();

	static {
		availableTypes.add(ContentTypeUtil.getContentType("jpg"));
		availableTypes.add(ContentTypeUtil.getContentType("jpeg"));
		availableTypes.add(ContentTypeUtil.getContentType("png"));
	}

	@Override
	public StringBuilder transform(StringBuilder part, Map<String, MimeHeader> partheaders) throws IOException {
		MimeHeader contentType = partheaders.get("Content-Type");
		MimeHeader contentTransferEncoding = partheaders.get("Content-Transfer-Encoding");
		if (contentType == null || !availableTypes.contains(contentType.getValue())) {
			return part;
		}
		if ("base64".equals(contentTransferEncoding.getValue())) {
			try {
				File originalImage = getUnencodedImage(part);
				File rotatedImage = rotator.createRotatedImage(ImageIO.read(originalImage));
				File encodedImage = Base64Util.encodeToFile(rotatedImage);
				StringBuilder rotatedEncodedImage = new StringBuilder();
				Scanner scanner = new Scanner(encodedImage);
				while (scanner.hasNextLine()) {
					rotatedEncodedImage.append(scanner.nextLine() + "\r\n");
				}
				encodedImage.delete();
				rotatedImage.delete();
				originalImage.delete();
				scanner.close();
				return rotatedEncodedImage;
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		} else {
			File originalImage;
			try {
				originalImage = getImage(part);
				File rotatedImage = rotator.createRotatedImage(ImageIO.read(originalImage));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return part;
	}

	private File getUnencodedImage(StringBuilder encodedText) throws IOException, InterruptedException {
		return Base64Util.decodeToFile(encodedText.toString().replace("\r", ""));
	}

	private File getImage(StringBuilder encodedText) throws IOException, InterruptedException {
		return IOUtil.createFileWithContents(encodedText.toString());
	}

}
