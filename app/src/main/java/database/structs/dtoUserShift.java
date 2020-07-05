package database.structs;

import java.util.ArrayList;

public class dtoUserShift  implements Cloneable{
	public Integer		UserID;
	public Integer 		ShiftID;
	
	@Override
	public dtoUserShift clone() throws CloneNotSupportedException {
		return (dtoUserShift)super.clone();
	}
}


