package listem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command {
	private int _totalLines;
	/**
	 * @return the _totalLines
	 */
	public int get_totalLines() {
		return _totalLines;
	}

	/**
	 * @param _totalLines the _totalLines to set
	 */
	public void set_totalLines(int _totalLines) {
		this._totalLines = _totalLines;
	}

	public Command() {
		_totalLines = 0;
	}
	
	protected void traverseFiles(File directory, boolean recursive, String fileSelectionPattern, String substringSelectionPattern) throws FileNotFoundException{
		if (directory.isDirectory()){
			for (File myFile:directory.listFiles()){
				if (myFile.isDirectory() && recursive){
					traverseFiles(myFile, recursive, fileSelectionPattern, substringSelectionPattern);
				}
				else if (myFile.isFile()){
						if (myFile.canRead()){
							Pattern myPattern = Pattern.compile(fileSelectionPattern);
							Matcher myMatcher = myPattern.matcher(myFile.getName());
							if (myMatcher.matches()){
								Scanner myScanner = new Scanner(new BufferedInputStream(new FileInputStream(myFile.getPath())));
								while (myScanner.hasNextLine()){
									if (substringSelectionPattern == null){
										myScanner.nextLine();
										_totalLines++;
									}
									else
										handleLine(myFile, myScanner.nextLine(), substringSelectionPattern, 0);
								}
								if (substringSelectionPattern==null)
									handleLine(myFile, null, substringSelectionPattern, _totalLines);
								myScanner.close();
							}
					}
				}
				_totalLines = 0;
			}
		}
	}
	
	
	protected abstract void handleLine(File myFile, String newLine, String substringSelectionPattern, int totalLines);


}
