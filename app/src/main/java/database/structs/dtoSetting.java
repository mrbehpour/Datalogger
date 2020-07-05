package database.structs;

import java.util.ArrayList;

public class dtoSetting  implements Cloneable{
	public Integer		Range1;
	public Integer		Range2;
	public Integer		Range3;
	public String 		Dsc1;
	public String 		Dsc2;
	public String		Dsc3;
	public Integer		BackligthTime;
	public String 		ContorolTime;
	public Integer 		ModTag;
	public Integer 		LayerTag;
	public Integer 		SendItem;
	public Integer 		ShowEmp;
	public Integer 		ShowLastData;
	public Integer 		NoCheckMaxData;
	public Integer 		UseSetUserTime;
	
	@Override
	public dtoSetting clone() throws CloneNotSupportedException {
		return (dtoSetting)super.clone();
	}
}


