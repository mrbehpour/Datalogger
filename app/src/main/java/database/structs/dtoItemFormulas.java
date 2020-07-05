package database.structs;

import java.util.ArrayList;

public class dtoItemFormulas  implements Cloneable{
	public Integer		ItemInfID;
	public Integer		FormulaID;
	public String 		ItemSelect;
	public Integer		constantNumber;
	
	@Override
	public dtoItemFormulas clone() throws CloneNotSupportedException {
		return (dtoItemFormulas)super.clone();
	}
}


