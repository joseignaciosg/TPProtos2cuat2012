package externalapps;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class RotateImage {

    
    public static void main(String[] args) {
	String toFlip = args[0];
	InputStream is=null;
	if (toFlip != null){
	    try {
		is = new BufferedInputStream(new FileInputStream(
		    toFlip));
	    } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
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
	final BufferedImage dimg = new BufferedImage(w, h, img.getColorModel()
			.getTransparency());
	final Graphics2D g = dimg.createGraphics();
	g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
	g.dispose();
	try {
		ImageIO.write(dimg, "png", out);
	} catch (final IOException e) {
		e.printStackTrace();
	}
	out.toByteArray();
	final File outimage = new File(toFlip);
	try {
		ImageIO.write(dimg, "png", outimage);
	} catch (final IOException e) {
		e.printStackTrace();
	}
    }
}
