package database.structs;

import java.util.ArrayList;

public class dtoRemarkGroup  implements Cloneable{
	public Integer		RemGrpID;
	public String 		RemGrpName;
	public String 		Des;
	
	@Override
	public dtoRemarkGroup clone() throws CloneNotSupportedException {
		return (dtoRemarkGroup)super.clone();
	}
}


