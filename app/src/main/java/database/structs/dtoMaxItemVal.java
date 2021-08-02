package database.structs;

import java.util.ArrayList;

public class dtoMaxItemVal  implements Cloneable{
	public Integer		Radif;
	public Integer		ItemInfID;
	public String 		ItemVal;
	public String 		RemAns;
	public String		ItemValTyp;
	public String		RemValues;
	public  String 		VoicePath="";
	public  String 		ImagePath="";
	public  String 		VideoPath="";
	@Override
	public dtoMaxItemVal clone() throws CloneNotSupportedException {
		return (dtoMaxItemVal)super.clone();
	}
}


