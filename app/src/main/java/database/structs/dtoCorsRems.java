package database.structs;

import java.util.ArrayList;

public class dtoCorsRems implements Cloneable{
	public Integer		CorRemID;
	public Integer 		RemGrpID;
	public Integer 		RemID;
	
	@Override
	public dtoCorsRems clone() throws CloneNotSupportedException {
		return (dtoCorsRems)super.clone();
	}
}


