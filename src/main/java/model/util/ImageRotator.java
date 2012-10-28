package model.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageRotator {

	private static final double _theta = 1.57;	// 90 degrees
	private static final int _thetaInDegrees = 90;

	public File createRotatedImage(BufferedImage image) throws IOException {
		File tmp = File.createTempFile("rotated_", ".png");
		BufferedImage rotated = rotate(image);
		if (ImageIO.write(rotated, "png", tmp)) {
			return tmp;
		}
		return null;
	}
	
	public BufferedImage rotate(BufferedImage image) {
		AffineTransform xform = new AffineTransform();
		if (image.getWidth() > image.getHeight()) {
			xform.setToTranslation(0.5 * image.getWidth(), 0.5 * image.getWidth());
			xform.rotate(_theta);
			int diff = image.getWidth() - image.getHeight();
			switch (_thetaInDegrees) {
			case 90:
				xform.translate(-0.5 * image.getWidth(), -0.5 * image.getWidth() + diff);
				break;
			case 180:
				xform.translate(-0.5 * image.getWidth(), -0.5 * image.getWidth() + diff);
				break;
			default:
				xform.translate(-0.5 * image.getWidth(), -0.5 * image.getWidth());
				break;
			}
		} else if (image.getHeight() > image.getWidth()) {
			xform.setToTranslation(0.5 * image.getHeight(), 0.5 * image.getHeight());
			xform.rotate(_theta);
			int diff = image.getHeight() - image.getWidth();
			switch (_thetaInDegrees) {
			case 180:
				xform.translate(-0.5 * image.getHeight() + diff, -0.5 * image.getHeight());
				break;
			case 270:
				xform.translate(-0.5 * image.getHeight() + diff, -0.5 * image.getHeight());
				break;
			default:
				xform.translate(-0.5 * image.getHeight(), -0.5 * image.getHeight());
				break;
			}
		} else {
			xform.setToTranslation(0.5 * image.getWidth(), 0.5 * image.getHeight());
			xform.rotate(_theta);
			xform.translate(-0.5 * image.getHeight(), -0.5 * image.getWidth());
		}
		AffineTransformOp op = new AffineTransformOp(xform, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(image, null);
	}

}
