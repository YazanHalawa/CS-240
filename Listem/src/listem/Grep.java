/**
 * 
 */
package listem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yazan
 *
 */
public class Grep extends Command implements IGrep {
	private Map <File, List<String>> GrepMap;
	/**
	 * 
	 */
	public Grep() {
		super();
		GrepMap = new HashMap<File, List<String>>();
	}

	/* (non-Javadoc)
	 * @see listem.IGrep#grep(java.io.File, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public Map<File, List<String>> grep(File directory,
			String fileSelectionPattern, String substringSelectionPattern,
			boolean recursive) {
		if (!GrepMap.equals(null))
			GrepMap.clear();
		try {
			traverseFiles(directory, recursive, fileSelectionPattern, substringSelectionPattern);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return GrepMap;
	}
	
	@Override
	public void handleLine(File myFile, String newLine, String substringSelectionPattern, int totalLines){
		Pattern myPattern = Pattern.compile(substringSelectionPattern);
		Matcher myMatcher = myPattern.matcher(newLine);
		if (myMatcher.find()){
			if (GrepMap.containsKey(myFile))
				GrepMap.get(myFile).add(newLine);
			else{
				List<String> newList = new ArrayList<String>();
				newList.add(newLine);
				GrepMap.put(myFile, newList);
			}
				
		}
	}

}
