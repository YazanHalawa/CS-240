
package client.synchronization;

import shared.modelClasses.Field;
import shared.modelClasses.Project;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import client.drawing.DrawingComponent;
import client.facade.clientFacade;
import client.windows.SuggestionsWindow;



/**
 * @author Yazan Halawa
 *
 */

public class BatchState implements BatchStateListener {
	// Init variables
	private static int batchID;
	private static ArrayList<Field> fieldList;
	private static Project proj;
	private static String[][] values;
	private static Cell selectedCell;
	private static transient List<BatchStateListener> listeners;
	private static BatchStateListener caller;
	private static ArrayList<Cell> redHighlightedCells;
	private static int scrollPosTableVert;
	private static int scrollPosTableHorz;
	private static int scrollPosFormVert;
	private static int scrollPosFormHorz;
	private static int scrollPosFieldVert;
	private static int scrollPosFieldHorz;
	private static DrawingComponent drawComp;
	private static Dimension indexingWinSize;
	private static int HorizSplitPos;
	private static int VertSplitPos;
	private static String imagePath;
	private static Point indexingWinPos;
	private static clientFacade cltFacade;
	private static int xCoord;
	private static int yCoord;

	/**
	 * This is the constructor for the BatchState class
	 * @param cltFacade
	 * @param records
	 * @param fields
	 * @param fieldList
	 * @param project
	 */
	public BatchState(clientFacade cltFacade, int records, int fields, ArrayList<Field> fieldList, Project project) {
		// Set the variables
		proj = project;
		BatchState.fieldList = fieldList;
		values = new String[records][fields];
		selectedCell = null;
		caller = null;
		listeners = new ArrayList<BatchStateListener>();
		redHighlightedCells = new ArrayList<Cell>();
	}


	/**
	 * @return the xCoord
	 */
	public static int getxCoord() {
		return xCoord;
	}


	/**
	 * @param xCoord the xCoord to set
	 */
	public static void setxCoord(int xCoord) {
		BatchState.xCoord = xCoord;
	}


	/**
	 * @return the yCoord
	 */
	public static int getyCoord() {
		return yCoord;
	}


	/**
	 * @param yCoord the yCoord to set
	 */
	public static void setyCoord(int yCoord) {
		BatchState.yCoord = yCoord;
	}


	/**
	 * @return the cltFacade
	 */
	public static clientFacade getCltFacade() {
		return cltFacade;
	}

	/**
	 * @param cltFacade the cltFacade to set
	 */
	public static void setCltFacade(clientFacade cltFacade) {
		BatchState.cltFacade = cltFacade;
	}

	/**
	 * @return the drawComp
	 */
	public static DrawingComponent getDrawComp() {
		return drawComp;
	}

	/**
	 * @param drawComp the drawComp to set
	 * @return 
	 */
	public static void setDrawComp(DrawingComponent drawComp) {
		BatchState.drawComp = drawComp;
	}
	
	/**
	 * @return the imagePath
	 */
	public static String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public static void setImagePath(String imagePath) {
		BatchState.imagePath = imagePath;
	}

	/**
	 * @return the scrollPosTableVert
	 */
	public static int getScrollPosTableVert() {
		return scrollPosTableVert;
	}

	/**
	 * @param scrollPosTableVert the scrollPosTableVert to set
	 */
	public static void setScrollPosTableVert(int scrollPosTableVert) {
		BatchState.scrollPosTableVert = scrollPosTableVert;
	}

	/**
	 * @return the scrollPosTableHorz
	 */
	public static int getScrollPosTableHorz() {
		return scrollPosTableHorz;
	}

	/**
	 * @param scrollPosTableHorz the scrollPosTableHorz to set
	 */
	public static void setScrollPosTableHorz(int scrollPosTableHorz) {
		BatchState.scrollPosTableHorz = scrollPosTableHorz;
	}

	/**
	 * @return the scrollPosFormVert
	 */
	public static int getScrollPosFormVert() {
		return scrollPosFormVert;
	}

	/**
	 * @param scrollPosFormVert the scrollPosFormVert to set
	 */
	public static void setScrollPosFormVert(int scrollPosFormVert) {
		BatchState.scrollPosFormVert = scrollPosFormVert;
	}

	/**
	 * @return the scrollPosFormHorz
	 */
	public static int getScrollPosFormHorz() {
		return scrollPosFormHorz;
	}

	/**
	 * @param scrollPosFormHorz the scrollPosFormHorz to set
	 */
	public static void setScrollPosFormHorz(int scrollPosFormHorz) {
		BatchState.scrollPosFormHorz = scrollPosFormHorz;
	}

	/**
	 * @return the scrollPosFieldVert
	 */
	public static int getScrollPosFieldVert() {
		return scrollPosFieldVert;
	}

	/**
	 * @param scrollPosFieldVert the scrollPosFieldVert to set
	 */
	public static void setScrollPosFieldVert(int scrollPosFieldVert) {
		BatchState.scrollPosFieldVert = scrollPosFieldVert;
	}

	/**
	 * @return the scrollPosFieldHorz
	 */
	public static int getScrollPosFieldHorz() {
		return scrollPosFieldHorz;
	}

	/**
	 * @param scrollPosFieldHorz the scrollPosFieldHorz to set
	 */
	public static void setScrollPosFieldHorz(int scrollPosFieldHorz) {
		BatchState.scrollPosFieldHorz = scrollPosFieldHorz;
	}


	/**
	 * @return the indexingWinPos
	 */
	public static Point getIndexingWinPos() {
		return indexingWinPos;
	}

	/**
	 * @param point the indexingWinPos to set
	 */
	public static void setIndexingWinPos(Point point) {
		BatchState.indexingWinPos = point;
	}

	/**
	 * @return the indexingWinSize
	 */
	public static Dimension getIndexingWinSize() {
		return indexingWinSize;
	}

	/**
	 * @param dimension the indexingWinSize to set
	 */
	public static void setIndexingWinSize(Dimension dimension) {
		BatchState.indexingWinSize = dimension;
	}

	/**
	 * @return the horizSplitPos
	 */
	public static int getHorizSplitPos() {
		return HorizSplitPos;
	}

	/**
	 * @param horizSplitPos the horizSplitPos to set
	 */
	public static void setHorizSplitPos(int horizSplitPos) {
		HorizSplitPos = horizSplitPos;
	}

	/**
	 * @return the vertSplitPos
	 */
	public static int getVertSplitPos() {
		return VertSplitPos;
	}

	/**
	 * @param vertSplitPos the vertSplitPos to set
	 */
	public static void setVertSplitPos(int vertSplitPos) {
		VertSplitPos = vertSplitPos;
	}

	/**
	 * @return the redHighlightedCells
	 */
	public static ArrayList<Cell> getRedHighlightedCells() {
		return redHighlightedCells;
	}

	/**
	 * @param redHighlightedCells the redHighlightedCells to set
	 */
	public static void setRedHighlightedCells(ArrayList<Cell> redHighlightedCells) {
		BatchState.redHighlightedCells = redHighlightedCells;
	}

	/**
	 * @return the batchID
	 */
	public static int getBatchID() {
		return batchID;
	}

	/**
	 * @param batchID the batchID to set
	 */
	public static void setBatchID(int batchID) {
		BatchState.batchID = batchID;
	}

	/**
	 * @return the listeners
	 */
	public static List<BatchStateListener> getListeners() {
		return listeners;
	}

	/**
	 * @param listeners the listeners to set
	 */
	public static void setListeners(List<BatchStateListener> listeners) {
		BatchState.listeners = listeners;
	}

	/**
	 * @return the fieldList
	 */
	public static ArrayList<Field> getFieldList() {
		return fieldList;
	}

	/**
	 * @param fieldList the fieldList to set
	 */
	public static void setFieldList(ArrayList<Field> fieldList) {
		BatchState.fieldList = fieldList;
	}

	/**
	 * @return the proj
	 */
	public static Project getProj() {
		return proj;
	}

	/**
	 * @param proj the proj to set
	 */
	public static void setProj(Project proj) {
		BatchState.proj = proj;
	}

	/**
	 * @return the values
	 */
	public static String[][] getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public static void setValues(String[][] values) {
		BatchState.values = new String[values.length][values[0].length];
		for (int i = 0; i < BatchState.values.length; i++){
			for (int j = 0; j < BatchState.values[i].length; j++){
				BatchState.setValue(new Cell(i, j), values[i][j]);
			}
		}
		BatchState.values = values;
	}

	public void addListener(BatchStateListener l) {
		listeners.add(l);
	}

	public static void setValue(Cell cell, String value) {

		values[cell.getRecord()][cell.getField()] = value;
		
		for (BatchStateListener l : listeners) {
			if (l.getClass() != caller.getClass())
				l.valueChanged(cell, value);
		}
		
		SuggestionsWindow suggestionsWin = new SuggestionsWindow(cell.getField(), cell.getRecord(), false);
		ArrayList<String> suggestions = suggestionsWin.getSuggestions();

		if (suggestionsWin.isNotInKnownValues()){
			highlightAllRed(cell.getRecord(), cell.getField());
		}
		else{
			unhighlightAllRed(cell.getRecord(), cell.getField());
		}
	}
	
	public static void highlightAllRed(int recordID, int fieldID){
		redHighlightedCells.add(selectedCell);
		for (BatchStateListener l : listeners) {
			l.highlightRed(recordID, fieldID);
		}
	}
	
	public static void unhighlightAllRed(int recordID, int fieldID){
		for (int i = 0; i < redHighlightedCells.size(); i++){
			if (redHighlightedCells.get(i).getRecord() == recordID &&
					redHighlightedCells.get(i).getField() == fieldID)
				redHighlightedCells.remove(i);
		}
		for (BatchStateListener l : listeners) {
			l.unhighlightRed(recordID, fieldID);
		}
	}

	public String getValue(Cell cell) {
		return values[cell.getRecord()][cell.getField()];
	}

	public static void setSelectedCell(Cell selCell) {

		selectedCell = selCell;

		for (BatchStateListener l : listeners) {
			if (l.getClass() != caller.getClass())
				l.selectedCellChanged(selCell);
		}
	}

	/**
	 * @return the caller
	 */
	public static BatchStateListener getCaller() {
		return caller;
	}

	/**
	 * @param caller the caller to set
	 */
	public static void setCaller(BatchStateListener caller) {
		BatchState.caller = caller;
	}

	public static Cell getSelectedCell() {
		return selectedCell;
	}

	@Override
	public void valueChanged(Cell cell, String newValue) {
		values[cell.getRecord()][cell.getField()] = newValue;

		for (BatchStateListener l : listeners) {
			if (l.getClass() != caller.getClass())
				l.valueChanged(cell, newValue);
		}
	}

	@Override
	public void selectedCellChanged(Cell newSelectedCell) {
		selectedCell = newSelectedCell;

		for (BatchStateListener l : listeners) {
			if (l.getClass() != caller.getClass())
				l.selectedCellChanged(newSelectedCell);
		}	
	}

	@Override
	public void highlightRed(int recordID, int fieldID) {
		// Do nothing
	}

	@Override
	public void unhighlightRed(int recordID, int fieldID) {
		// Do nothing
	}
	
	public static boolean isRedHighlightedCell(Cell testCell){
		for (Cell cell: redHighlightedCells){
			if (cell.getRecord() == testCell.getRecord() && 
					cell.getField() == testCell.getField())
				return true;	
		}
		return false;
	}
}
