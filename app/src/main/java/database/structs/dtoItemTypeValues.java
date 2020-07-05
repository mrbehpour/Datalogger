package database.structs;

import java.util.ArrayList;

public class dtoItemTypeValues  implements Cloneable{
	public Integer		ItemTypValID;
	public String 		ItemTypValName;
	public String 		Des;
	
	@Override
	public dtoItemTypeValues clone() throws CloneNotSupportedException {
		return (dtoItemTypeValues)super.clone();
	}
}


