package database.structs;

import java.util.ArrayList;

public class dtoFormulas  implements Cloneable{
	public Integer		FormulID;
	public String 		FormulName;
	
	@Override
	public dtoFormulas clone() throws CloneNotSupportedException {
		return (dtoFormulas)super.clone();
	}
}


