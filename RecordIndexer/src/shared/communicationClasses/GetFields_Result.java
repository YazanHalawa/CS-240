
package shared.communicationClasses;

import java.util.ArrayList;
import shared.modelClasses.*;

/**
 * This class holds the information for hte GetFields method results
 * It holds a list of chosen fields
 * @author Yazan
 *
 */
public class GetFields_Result {
	ArrayList<Field> fields;
	
	/**
	 * The constructor for the GetFields_Results class
	 */
	public GetFields_Result() {
		fields = null;
	}
	/**
	 * @return the fields
	 */
	public ArrayList<Field> getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	@Override
	public String toString(){
		StringBuilder output = new StringBuilder();
		if (fields != null){
			for (Field field: fields){
				output.append(field.getProject_id() + "\n" + field.getId() + "\n" +
								field.getTitle() + "\n");
			}
		}
		else{
			return "Failed\n";
		}
		return output.toString();
	}
}
