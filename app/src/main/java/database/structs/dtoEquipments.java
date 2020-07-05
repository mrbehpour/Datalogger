package database.structs;

import java.util.ArrayList;

public class dtoEquipments  implements Cloneable{
	public Integer		EquipInfoID;
	public String 		EquipName;
	public Integer		LogshitInfID;
	public Integer 		LocationID;
	public String 		EquipNo;
	public Integer 		EquipTypID;
	public String		Des; //change1
	
	@Override
	public dtoEquipments clone() throws CloneNotSupportedException {
		return (dtoEquipments)super.clone();
	}
}


