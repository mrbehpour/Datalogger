package database.structs;

import java.util.ArrayList;

public class dtoItemValues  implements Cloneable{
	public Integer		ItemInfID;
	public String 		ItemVal	= "";
	public String 		ItemValTyp;
	public String		PDate;
	public String 		PTime;

	///public  String 		RegisterDateTime;
	public Integer 		UsrID;
	public Integer 		ShiftID;

	public Integer		Id;
	public Integer 		IsSend = 0;

	public  String VoicePath="";
	public  String ImagePath="";
	public  String VideoPath="";


	
	@Override
	public dtoItemValues clone() throws CloneNotSupportedException {
		return (dtoItemValues)super.clone();
	}
}


