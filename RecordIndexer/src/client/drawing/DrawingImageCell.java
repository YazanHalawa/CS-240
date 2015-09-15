
package client.drawing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import client.synchronization.BatchState;
import client.synchronization.Cell;

/**
 * This class contains all the methods of drawing an ImageCell rectangle highlight
 * @author Yazan Halawa
 *
 */

public class DrawingImageCell implements DrawingShape{

	// Init the variables
	private Cell selectedCell;
    private double x;
    private double y;
    private double width;
    private double height;
	private boolean isSelected;
    private Image image;
    private Rectangle2D.Double rectangle2D;
    
    /**
     * The constructor for the DrawingImageCell class
     * @param image
     * @param targetCell
     */
    public DrawingImageCell(Image image, Cell targetCell) {
    	// Set the variables
		rectangle2D = new Rectangle2D.Double();
        this.selectedCell = targetCell;
        this.image = image;
        int firstX = BatchState.getFieldList().get(selectedCell.getField()).getFirstXCoord();
		int firstY = BatchState.getProj().getFirstYCoord() +
					 BatchState.getProj().getRecordsHeight() * (selectedCell.getRecord());
		int width =  BatchState.getFieldList().get(selectedCell.getField()).getWidth();
		int height = BatchState.getProj().getRecordsHeight();
		rectangle2D.setRect(firstX, firstY, width, height);
        this.x = rectangle2D.getBounds2D().getX();
        this.y = rectangle2D.getBounds2D().getY();
        this.width = this.x = rectangle2D.getBounds2D().getWidth();
        this.height = this.x = rectangle2D.getBounds2D().getHeight();
        this.isSelected = false;
    }
    
    /**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

    /**
	 * @return the selectedCell
	 */
	public Cell getSelectedCell() {
		return selectedCell;
	}

	/**
	 * @param selectedCell the selectedCell to set
	 */
	public void setSelectedCell(Cell selectedCell) {
		this.selectedCell = selectedCell;
	}

    @Override
    public void draw(Graphics2D g2) {
    	if (isSelected)//If the cell is selected, highlight it
    		g2.setColor(new Color(0, 119, 204, 128));
    	else
    		g2.setColor(new Color(0, 0, 0, 0));
        g2.fill(rectangle2D);
    }

    public boolean contains(Graphics2D g2, double x, double y) {
        return rectangle2D.contains(x, y);
    }

    /**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

	@Override
	public Rectangle2D getBounds(Graphics2D g2) {
		return rectangle2D.getBounds2D();
	}

}
