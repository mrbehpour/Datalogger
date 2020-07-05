package database.structs;

import java.util.ArrayList;

public class dtoRelateUserLogshitTbl  implements Cloneable{
	public Integer		UsrID;
	public Integer 		LogshitID;
	
	@Override
	public dtoRelateUserLogshitTbl clone() throws CloneNotSupportedException {
		return (dtoRelateUserLogshitTbl)super.clone();
	}
}


