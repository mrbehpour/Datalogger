package database.structs;

import java.util.ArrayList;

public class dtoPackUser  implements Cloneable{
	public ArrayList<dtoUsers>			Users;
	public ArrayList<dtoUserShift> 		RelateUserToShift; //UserShiftTbl
	public ArrayList<dtoShifts> 		Shifts; //Shift
	
	@Override
	public dtoPackUser clone() throws CloneNotSupportedException {
		return (dtoPackUser)super.clone();
	}
	
}


