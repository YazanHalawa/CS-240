package client.drawing;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

/**
 * This class contains all the methods of drawing an image
 * @author Yazan Halawa
 *
 */
public class DrawingImage implements DrawingShape {

	private Image image;
	private Rectangle2D rect;
	
	public DrawingImage(Image image, Rectangle2D rect) {
		this.image = image;
		this.rect = rect;
	}

	@Override
	public boolean contains(Graphics2D g2, double x, double y) {
		return rect.contains(x, y);
	}

	@Override
	public void draw(Graphics2D g2) {
		g2.drawImage(image, 0, 0, null);
	}	
	
	@Override
	public Rectangle2D getBounds(Graphics2D g2) {
		return rect.getBounds2D();
	}

}
