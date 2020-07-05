package database.structs;

import java.util.ArrayList;

public class dtoShifts  implements Cloneable{
	public Integer		ID;
	public String 		Name;
	public String 		Description;
	
	@Override
	public dtoShifts clone() throws CloneNotSupportedException {
		return (dtoShifts)super.clone();
	}

	@Override
	public String toString()
	{
	    return Name;
	}
}


