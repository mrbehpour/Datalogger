package database.structs;

import java.util.ArrayList;

public class dtoPosts  implements Cloneable{
	public Integer		PostID;
	public String 		PostName;
	public String 		Des;
	
	@Override
	public dtoPosts clone() throws CloneNotSupportedException {
		return (dtoPosts)super.clone();
	}
}


