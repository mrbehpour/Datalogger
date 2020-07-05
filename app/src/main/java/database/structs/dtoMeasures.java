package database.structs;

import java.util.ArrayList;

public class dtoMeasures  implements Cloneable{
	public Integer		MeasureID;
	public String		MeasureName;
	public String 		Des;
	
	@Override
	public dtoMeasures clone() throws CloneNotSupportedException {
		return (dtoMeasures)super.clone();
	}
}


