package database.structs;

import java.util.ArrayList;

public class dtoLogShits  implements Cloneable{
	public Integer		LogshitInfID;
	public Integer		LogshitID;
	public String 		LogshitName;
	public Integer		PostID;
	public String 		Ver;
	public String 		TagID;
	public String		Des; //change1
	
	@Override
	public dtoLogShits clone() throws CloneNotSupportedException {
		return (dtoLogShits)super.clone();
	}
}


