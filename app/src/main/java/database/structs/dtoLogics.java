package database.structs;

import java.util.ArrayList;

public class dtoLogics  implements Cloneable{
	public Integer		LogicTypID;
	public String 		LogicVal1;
	public String 		LogicVal2;
	
	@Override
	public dtoLogics clone() throws CloneNotSupportedException {
		return (dtoLogics)super.clone();
	}
}


