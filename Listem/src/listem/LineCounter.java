/**
 * 
 */
package listem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yazan
 *
 */
public class LineCounter extends Command implements ILineCounter {
	private Map<File, Integer> LineCounterMap;
	/**
	 * 
	 */
	public LineCounter() {
		super();
		LineCounterMap =  new HashMap<File, Integer>();
	}

	/* (non-Javadoc)
	 * @see listem.ILineCounter#countLines(java.io.File, java.lang.String, boolean)
	 */
	@Override
	public Map<File, Integer> countLines(File directory,
			String fileSelectionPattern, boolean recursive) {
		LineCounterMap.clear();
		this.set_totalLines(0);
		try {
			traverseFiles(directory, recursive, fileSelectionPattern, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return LineCounterMap;
	}

	@Override
	protected void handleLine(File myFile, String newLine, String substringSelectionPattern, int totalLines) {
		if (!LineCounterMap.containsKey(myFile))
			LineCounterMap.put(myFile, totalLines);
	}

}
