
package client.drawing;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;

import client.synchronization.BatchState;
import client.synchronization.BatchStateListener;
import client.synchronization.Cell;

import java.util.*;
import java.io.*;
import java.net.URL;

/**
 * This class contains all the methods of drawing the component
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("serial")
public class DrawingComponent extends JComponent implements BatchStateListener{

	// Init the variables
	private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	private int w_translateX;
	private int w_translateY;
	private double scale;
	private String imagePath;
	private Image selectedImage;
	private DrawingImage selectedImageDraw;
	private ArrayList<DrawingShape> shapes;
	private JComponent JComp;
	private DrawingImageCell selectedCell;
	private boolean highlightsOn;
	private boolean inverting;
	private boolean dragging;
	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartTranslateX;
	private int w_dragStartTranslateY;
	private AffineTransform dragTransform;
	private double MAX_SCROLL = 1.0f;
	private double MIN_SCROLL = 0.2f;
	private DrawingImageCell newCell;
	
	/**
	 * The constructor for the Drawing Component Class
	 */
	public DrawingComponent(String path, boolean addActions) {
		// Set the variables
		imagePath = path;
		w_translateX = 0;
		w_translateY = 0;
		scale = 0.5;
		JComp = this;
		highlightsOn = true;
		inverting = false;
		initDrag();
		shapes = new ArrayList<DrawingShape>();


		this.setPreferredSize(new Dimension(700, 500));
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(1000, 1000));

		if (addActions){
			this.addMouseListener(mouseAdapter);
			this.addMouseMotionListener(mouseAdapter);
			this.addMouseWheelListener(mouseAdapter);
		}

		if (imagePath != null){
			BatchState.setImagePath(imagePath);
			selectedImage = loadImage(imagePath);
			selectedImageDraw = new DrawingImage(selectedImage, new Rectangle2D.Double(0, 0, selectedImage.getWidth(null), selectedImage.getHeight(null)));
			shapes.add(selectedImageDraw);
			drawCells();
		}
		if (BatchState.getListeners() != null)
			BatchState.getListeners().add(this);
	}
	
	
	/**
	 * @return the w_translateY
	 */
	public int getW_translateY() {
		return w_translateY;
	}


	/**
	 * @param w_translateY the w_translateY to set
	 */
	public void setW_translateY(int w_translateY) {
		this.w_translateY = w_translateY;
	}


	/**
	 * @return the highlightsOn
	 */
	public boolean isHighlightsOn() {
		return highlightsOn;
	}

	/**
	 * @param highlightsOn the highlightsOn to set
	 */
	public void setHighlightsOn(boolean highlightsOn) {
		this.highlightsOn = highlightsOn;
	}

	/**
	 * @return the w_translateX
	 */
	public int getW_translateX() {
		return w_translateX;
	}

	/**
	 * @param w_translateX the w_translateX to set
	 */
	public void setW_translateX(int w_translateX) {
		this.w_translateX = w_translateX;
	}

	/**
	 * @return the selectedCell
	 */
	public DrawingImageCell getSelectedCell() {
		return selectedCell;
	}

	/**
	 * @param selectedCell the selectedCell to set
	 */
	public void setSelectedCell(DrawingImageCell newCell, int recordID, int fieldID) {
		this.selectedCell = newCell;
		this.selectedCell.setSelected(true);
		BatchState.setCaller(this);
		BatchState.setSelectedCell(new Cell(recordID, fieldID));
	}

	/**
	 * This method will draw the cell highlights
	 */
	public void drawCells() {
		for (int i = 0; i < BatchState.getValues().length; i++){
			for (int j = 0; j < BatchState.getValues()[i].length; j++){
				
				// Init the cell rectangle
				Cell targetCell = new Cell(i, j);
				newCell = new DrawingImageCell(selectedImage, targetCell);
				
				// start with the top left cell selected
				if (i==0 && j==0 && !inverting){
					setSelectedCell(newCell, i, j);
					newCell.setSelected(true);
				}
				// If the image is being inverted, highlight the selected cell
				else if (inverting && (newCell.getSelectedCell().getRecord()==
						BatchState.getSelectedCell().getRecord()) &&
						(newCell.getSelectedCell().getField()==
						BatchState.getSelectedCell().getField())){
					setSelectedCell(newCell, i, j);
					newCell.setSelected(true);	
					inverting = false;
				}
				// Add the cell rectangle to the shapes array
				shapes.add(newCell);
			}
		}
	}

	/**
	 * @return the selectedImage
	 */
	public Image getSelectedImage() {
		return selectedImage;
	}

	/**
	 * @param selectedImage the selectedImage to set
	 */
	public void setSelectedImage(Image selectedImage, boolean inverting) {
		this.inverting = inverting;
		this.selectedImage = selectedImage;
		selectedImageDraw = new DrawingImage(this.selectedImage, new Rectangle2D.Double(0, 0, selectedImage.getWidth(null), selectedImage.getHeight(null)));
		shapes.clear();
		shapes.add(selectedImageDraw);
		drawCells();
		if (!BatchState.getListeners().contains(this))
			BatchState.getListeners().add(this);
		this.repaint();
	}

	/**
	 * @return the inverting
	 */
	public boolean isInverting() {
		return inverting;
	}

	/**
	 * @param inverting the inverting to set
	 */
	public void setInverting(boolean inverting) {
		this.inverting = inverting;
	}

	/**
	 * @return the scale
	 */
	public double getScale() {
		return scale;
	}

	private void initDrag() {
		dragging = false;
		w_dragStartX = 0;
		w_dragStartY = 0;
		w_dragStartTranslateX = 0;
		w_dragStartTranslateY = 0;
		dragTransform = null;
	}


	private Image loadImage(String imageFile) {
		try {
			return ImageIO.read(new URL(imageFile));
		}
		catch (IOException e) {
			return NULL_IMAGE;
		}
	}

	public void setScale(double newScale) {
		if (newScale > 0.2 && newScale < 1.8 )
			scale = newScale;
		this.repaint();
	}

	public void setTranslation(int w_newTranslateX, int w_newTranslateY) {
		w_translateX = w_newTranslateX;
		w_translateY = w_newTranslateY;
		this.repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		drawBackground(g2);

		g2.translate(this.getWidth()/2, this.getHeight()/2);
		g2.scale(scale, scale);
		g2.translate(w_translateX-this.getWidth()/2, w_translateY-this.getHeight()/2);

		drawShapes(g2);
	}

	private void drawBackground(Graphics2D g2) {
		g2.setColor(getBackground());
		g2.fillRect(0,  0, getWidth(), getHeight());
	}

	private void drawShapes(Graphics2D g2) {
		for (DrawingShape shape : shapes) {
			if (shape.getClass() == newCell.getClass()){
				if (highlightsOn)
					shape.draw(g2);
				else
					continue;
			}
			else
				shape.draw(g2);
		}
	}

	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();

			AffineTransform transform = new AffineTransform();

			transform.translate(JComp.getWidth()/2, JComp.getHeight()/2);
			transform.scale(scale, scale);
			transform.translate(w_translateX-JComp.getWidth()/2, w_translateY-JComp.getHeight()/2);

			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();

			boolean hitShape = false;

			Graphics2D g2 = (Graphics2D)getGraphics();

			for (DrawingShape shape : shapes) {
				if (shape.contains(g2, w_X, w_Y)) {
					if (shape.getClass() == newCell.getClass() && highlightsOn){
						DrawingImageCell tmp = (DrawingImageCell) shape;
						selectedCell.setSelected(false);
						setSelectedCell(tmp, tmp.getSelectedCell().getRecord(), tmp.getSelectedCell().getField());
						tmp.setSelected(true);
						tmp.draw(g2);
					}
					hitShape = true;
				}
			}

			if (hitShape) {
				dragging = true;		
				w_dragStartX = w_X;
				w_dragStartY = w_Y;		
				w_dragStartTranslateX = w_translateX;
				w_dragStartTranslateY = w_translateY;
				dragTransform = transform;
			}
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {		
			if (dragging) {
				int d_X = e.getX();
				int d_Y = e.getY();

				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					dragTransform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) {
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();

				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;

				w_translateX = w_dragStartTranslateX + w_deltaX;
				w_translateY = w_dragStartTranslateY + w_deltaY;

				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initDrag();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {

			int rotation = e.getWheelRotation();
			if((rotation > 0)  && scale > MIN_SCROLL ) {
				setScale(scale - 0.02f);
			} else if (scale < MAX_SCROLL) {
				setScale(scale + 0.02f);
			}
		}
	};


	@Override
	public void valueChanged(Cell cell, String newValue) {
		//Do nothing
	}

	@Override
	public void selectedCellChanged(Cell newSelectedCell) {
		for (DrawingShape shape : shapes) {
			if (shape.getClass() == newCell.getClass() && highlightsOn){
				DrawingImageCell tmp = (DrawingImageCell) shape;
				if (tmp.getSelectedCell().getRecord() == newSelectedCell.getRecord() &&
						tmp.getSelectedCell().getField() == newSelectedCell.getField()){
					selectedCell.setSelected(false);
					this.selectedCell = tmp;
					tmp.setSelected(true);
				}
			}
		}
		repaint();
	}

	@Override
	public void highlightRed(int recordID, int fieldID) {
		// Do nothing
		
	}

	@Override
	public void unhighlightRed(int recordID, int fieldID) {
		// Do nothing
		
	}



}
