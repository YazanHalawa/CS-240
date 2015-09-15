
package shared.communicationClasses;

import java.util.ArrayList;

/**
 * This method holds the information for the Search method results
 * It holds the matches
 * @author Yazan Halawa
 *
 */
public class Search_Result {
	private ArrayList<Search_Result_Tuple> matches;
	
	/**
	 * The constructor for the Searc_Result class
	 */
	public Search_Result() {
		matches = null;
	}
	/**
	 * @return the matches
	 */
	public ArrayList<Search_Result_Tuple> getMatches() {
		return matches;
	}
	/**
	 * @param matches the matches to set
	 */
	public void setMatches(ArrayList<Search_Result_Tuple> matches) {
		this.matches = matches;
	}
	
	@Override
	public String toString(){
		StringBuilder output = new StringBuilder();
		if (matches != null && matches.size() > 0){
			for (Search_Result_Tuple tuple: matches){
				output.append(tuple.toString());
			}
		}
		else{
			return "Failed\n";
		}
		return output.toString();
	}
}
