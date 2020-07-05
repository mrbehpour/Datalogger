package database.structs;

import java.util.ArrayList;

public class dtoRemarks  implements Cloneable{
	public Integer		RemID;
	public String 		RemName;
	public String 		Des;
	
	@Override
	public dtoRemarks clone() throws CloneNotSupportedException {
		return (dtoRemarks)super.clone();
	}
}


