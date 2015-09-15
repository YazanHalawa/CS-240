
package shared.communicationClasses;

import java.util.ArrayList;
import shared.modelClasses.*;
/**
 * This class holds information about the GetProjects method results
 * It holds a list of Projects
 * @author Yazan Halawa
 *
 */
public class GetProjects_Result {
	ArrayList<Project> projects;
	
	/**
	 * The constructor for the GetProjects_Results class
	 */
	public GetProjects_Result() {
		projects = null;
	}
	/**
	 * @return the projects
	 */
	public ArrayList<Project> getProjects() {
		return projects;
	}
	/**
	 * @param projects the projects to set
	 */
	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}

	@Override
	public String toString(){
		StringBuilder output = new StringBuilder();
		if (projects != null){
			for (Project proj: projects){
				output.append(proj.getId() + "\n" + proj.getTitle() + "\n");
			}
		}
		else{
			return "Failed\n";
		}
		return output.toString();
	}
}
