package database.structs;

import java.util.ArrayList;

public class dtoSubEquipments  implements Cloneable{
	public Integer		SubEquipID;
	public String 		SubEquipName;
	public Integer		EquipInfID;
	public String		Des; //change1
	
	@Override
	public dtoSubEquipments clone() throws CloneNotSupportedException {
		return (dtoSubEquipments)super.clone();
	}
}


