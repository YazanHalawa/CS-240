package client.serialization;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import shared.modelClasses.Field;
import shared.modelClasses.Project;
import client.drawing.DrawingComponent;
import client.facade.clientFacade;
import client.synchronization.BatchStateListener;
import client.synchronization.Cell;

/**
 * This class will store all the data to be serialized because the BatchState has them as static which
 * makes it hard to serialize them
 * @author Yazan Halawa
 *
 */
public class serialData {
	
	// Init variables
	private int batchID;
	private ArrayList<Field> fieldList;
	private Project proj;
	private String[][] values;
	private Cell selectedCell;
	private transient List<BatchStateListener> listeners;
	private transient BatchStateListener caller;
	private transient clientFacade cltFacade;
	private ArrayList<Cell> redHighlightedCells;
	private int scrollPosTableVert;
	private int scrollPosTableHorz;
	private int scrollPosFormVert;
	private int scrollPosFormHorz;
	private int scrollPosFieldVert;
	private int scrollPosFieldHorz;
	private double zoomLevel;
	private boolean highlightsOn;
	private boolean inverting;
	private Point indexingWinPos;
	private Dimension indexingWinSize;
	private int HorizSplitPos;
	private int VertSplitPos;
	private int xCoord;
	private int yCoord;
	
	
	/**
	 * @return the xCoord
	 */
	public int getxCoord() {
		return xCoord;
	}

	/**
	 * @param xCoord the xCoord to set
	 */
	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	/**
	 * @return the yCoord
	 */
	public int getyCoord() {
		return yCoord;
	}

	/**
	 * @param yCoord the yCoord to set
	 */
	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public serialData() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	private String imagePath;

	/**
	 * @return the batchID
	 */
	public int getBatchID() {
		return batchID;
	}

	/**
	 * @param batchID the batchID to set
	 */
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

	/**
	 * @return the fieldList
	 */
	public ArrayList<Field> getFieldList() {
		return fieldList;
	}

	/**
	 * @param fieldList the fieldList to set
	 */
	public void setFieldList(ArrayList<Field> fieldList) {
		this.fieldList = fieldList;
	}

	/**
	 * @return the proj
	 */
	public Project getProj() {
		return proj;
	}

	/**
	 * @param proj the proj to set
	 */
	public void setProj(Project proj) {
		this.proj = proj;
	}

	/**
	 * @return the values
	 */
	public String[][] getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(String[][] values) {
		this.values = values;
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

	/**
	 * @return the listeners
	 */
	public List<BatchStateListener> getListeners() {
		return listeners;
	}

	/**
	 * @param listeners the listeners to set
	 */
	public void setListeners(List<BatchStateListener> listeners) {
		this.listeners = listeners;
	}

	/**
	 * @return the caller
	 */
	public BatchStateListener getCaller() {
		return caller;
	}

	/**
	 * @param caller the caller to set
	 */
	public void setCaller(BatchStateListener caller) {
		this.caller = caller;
	}

	/**
	 * @return the cltFacade
	 */
	public clientFacade getCltFacade() {
		return cltFacade;
	}

	/**
	 * @param cltFacade the cltFacade to set
	 */
	public void setCltFacade(clientFacade cltFacade) {
		this.cltFacade = cltFacade;
	}

	/**
	 * @return the redHighlightedCells
	 */
	public ArrayList<Cell> getRedHighlightedCells() {
		return redHighlightedCells;
	}

	/**
	 * @param redHighlightedCells the redHighlightedCells to set
	 */
	public void setRedHighlightedCells(ArrayList<Cell> redHighlightedCells) {
		this.redHighlightedCells = redHighlightedCells;
	}

	/**
	 * @return the scrollPosTableVert
	 */
	public int getScrollPosTableVert() {
		return scrollPosTableVert;
	}

	/**
	 * @param scrollPosTableVert the scrollPosTableVert to set
	 */
	public void setScrollPosTableVert(int scrollPosTableVert) {
		this.scrollPosTableVert = scrollPosTableVert;
	}

	/**
	 * @return the scrollPosTableHorz
	 */
	public int getScrollPosTableHorz() {
		return scrollPosTableHorz;
	}

	/**
	 * @param scrollPosTableHorz the scrollPosTableHorz to set
	 */
	public void setScrollPosTableHorz(int scrollPosTableHorz) {
		this.scrollPosTableHorz = scrollPosTableHorz;
	}

	/**
	 * @return the scrollPosFormVert
	 */
	public int getScrollPosFormVert() {
		return scrollPosFormVert;
	}

	/**
	 * @param scrollPosFormVert the scrollPosFormVert to set
	 */
	public void setScrollPosFormVert(int scrollPosFormVert) {
		this.scrollPosFormVert = scrollPosFormVert;
	}

	/**
	 * @return the scrollPosFormHorz
	 */
	public int getScrollPosFormHorz() {
		return scrollPosFormHorz;
	}

	/**
	 * @param scrollPosFormHorz the scrollPosFormHorz to set
	 */
	public void setScrollPosFormHorz(int scrollPosFormHorz) {
		this.scrollPosFormHorz = scrollPosFormHorz;
	}

	/**
	 * @return the scrollPosFieldVert
	 */
	public int getScrollPosFieldVert() {
		return scrollPosFieldVert;
	}

	/**
	 * @param scrollPosFieldVert the scrollPosFieldVert to set
	 */
	public void setScrollPosFieldVert(int scrollPosFieldVert) {
		this.scrollPosFieldVert = scrollPosFieldVert;
	}

	/**
	 * @return the scrollPosFieldHorz
	 */
	public int getScrollPosFieldHorz() {
		return scrollPosFieldHorz;
	}

	/**
	 * @param scrollPosFieldHorz the scrollPosFieldHorz to set
	 */
	public void setScrollPosFieldHorz(int scrollPosFieldHorz) {
		this.scrollPosFieldHorz = scrollPosFieldHorz;
	}


	/**
	 * @return the zoomLevel
	 */
	public double getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * @param zoomLevel the zoomLevel to set
	 */
	public void setZoomLevel(double zoomLevel) {
		this.zoomLevel = zoomLevel;
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
	 * @return the indexingWinPos
	 */
	public Point getIndexingWinPos() {
		return indexingWinPos;
	}

	/**
	 * @param indexingWinPos the indexingWinPos to set
	 */
	public void setIndexingWinPos(Point indexingWinPos) {
		this.indexingWinPos = indexingWinPos;
	}

	/**
	 * @return the indexingWinSize
	 */
	public Dimension getIndexingWinSize() {
		return indexingWinSize;
	}

	/**
	 * @param indexingWinSize the indexingWinSize to set
	 */
	public void setIndexingWinSize(Dimension indexingWinSize) {
		this.indexingWinSize = indexingWinSize;
	}

	/**
	 * @return the horizSplitPos
	 */
	public int getHorizSplitPos() {
		return HorizSplitPos;
	}

	/**
	 * @param horizSplitPos the horizSplitPos to set
	 */
	public void setHorizSplitPos(int horizSplitPos) {
		HorizSplitPos = horizSplitPos;
	}

	/**
	 * @return the vertSplitPos
	 */
	public int getVertSplitPos() {
		return VertSplitPos;
	}

	/**
	 * @param vertSplitPos the vertSplitPos to set
	 */
	public void setVertSplitPos(int vertSplitPos) {
		VertSplitPos = vertSplitPos;
	}


}
