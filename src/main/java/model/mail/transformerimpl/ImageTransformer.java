package model.mail.transformerimpl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import model.parser.mime.MimeHeader;

import org.apache.commons.codec.binary.Base64;

public class ImageTransformer implements Transformer {

	private static final String[] supportedImageTypes = { "jpeg", "rgb", "tiff", "xbm", "png", "jpg" };

	@Override
	public StringBuilder transform(StringBuilder text, Map<String, MimeHeader> partheaders) throws IOException {
		MimeHeader contentType = partheaders.get("Content-Type");
		MimeHeader contentTransferEncoding = partheaders.get("Content-Transfer-Encoding");
		StringBuilder ret = new StringBuilder();
		if (contentType.getValue().startsWith("image/")) {
			File inimage = File.createTempFile("transformed_", "sourceimage");
			FileWriter fr = new FileWriter(inimage);
			fr.write(text.toString() + "\r\n");
			fr.close();
			// si se hace base64 -D inimage , da la imagen bien
			inimage = decodeBase64(text.toString(), contentTransferEncoding, inimage);
			InputStream is = null;
			if (inimage != null) {
				try {
					is = new BufferedInputStream(new FileInputStream(inimage));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			BufferedImage img = null;
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				img = ImageIO.read(is);
			} catch (final IOException e) {
				e.printStackTrace();
			}
			final int w = img.getWidth();
			final int h = img.getHeight();
			final BufferedImage dimg = new BufferedImage(w, h, img.getColorModel().getTransparency());
			final Graphics2D g = dimg.createGraphics();
			g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
			g.dispose();
			try {
				ImageIO.write(dimg, "png", out);
			} catch (final IOException e) {
				e.printStackTrace();
			}
			out.toByteArray();
			final File outimage = File.createTempFile("transformed_", "retimage");
			try {
				ImageIO.write(dimg, "png", outimage);
			} catch (final IOException e) {
				e.printStackTrace();
			}

			Scanner scanner = new Scanner(outimage);
			while (scanner.hasNextLine()) {
				ret.append(scanner.nextLine() + "\r\n");
			}
			scanner.close();
			return ret;
		}
		return text;
	}

	private File decodeBase64(String image, MimeHeader contentTransferEncoding, File inimage) {
		StringBuilder tobedecoded = new StringBuilder();
		String[] lines = image.split("\r\n");
		for (String line : lines) {
			tobedecoded.append(line);
		}
		if (contentTransferEncoding.getValue().startsWith("base64")) {
			Base64 base64 = new Base64();
			String decoded = new String(base64.decode(tobedecoded.toString()));
			try {
				final File decodedinimage = File.createTempFile("transformed_", "sourceimage");
				BufferedWriter filew = new BufferedWriter(new FileWriter(decodedinimage));
				filew.write(decoded);
				filew.close();
				return decodedinimage;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return inimage;

	}

}
