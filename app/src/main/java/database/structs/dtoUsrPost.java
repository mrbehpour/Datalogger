package database.structs;

public class dtoUsrPost implements Cloneable{
	public Integer 		PostID;
	public Integer		UsrID;
	
	@Override
	public dtoUsrPost clone() throws CloneNotSupportedException {
		return (dtoUsrPost)super.clone();
	}
}


