package database.structs;

import java.util.ArrayList;

import ir.saa.android.datalogger.G;

public class dtoUsers  implements Cloneable{
	public Integer		UsrID;
	public String 		FirstName;
	public String 		LastName;
	public String 		UserName;
	public String 		FullName;
	public Long			UserCode;
	public Integer		ShiftID;
	public String		pass;
	public Integer		UserTypeID;
	public String 		shiftName;
	public Integer 		IsManager;
	public Integer 		NeedTag;
	public Integer 		UserGroupId;
	
	@Override
	public dtoUsers clone() throws CloneNotSupportedException {
		return (dtoUsers)super.clone();
	}
	@Override
	public String toString()
	{
		String MainName;
		if(G.RTL){
			MainName= FirstName+" "+LastName;
		}else{
			MainName=UserName;
		}

	    return MainName;
	    //Be Darkhasteh Bazian Va Kh.Rezaee
	    //return FirstName+" "+LastName;
	}
}


