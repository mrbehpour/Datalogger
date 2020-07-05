package database.structs;

import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class dtoItems  implements Cloneable{
	public Integer		ItemInfID;
	public String 		ItemName;
	public Integer		SubEquipID;
	public Integer 		EquipInfID;
	public Integer 		LogshitInfID;
	public Integer 		PostID;
	public Integer 		RemGroupID;
	public Integer 		AmountTypID;
	public String 		MeasureUnitName; 
	public Double 		Zarib;
	public Integer 		MaxSampleNo;
	public String 		MaxAmount1;
	public String 		MinAmount1;
	public String 		MaxAmount2;
	public String 		MinAmount2;
	public String 		MaxAmount3;
	public String 		MinAmount3;
	public String 		TagID;
	public Integer 		RemTyp;
	public String 		STTime;
	public Integer 		PeriodTime;
	public Integer 		PeriodTypTime;
	public Integer 		LogicTypID;
	public String 		Desc;
	public Integer		RangeTime;
	public Integer		RangeTypTime;
	public Integer		LocateRowNo;
	public Integer		LogshitRowNo;


	public Boolean 		IsFullOfMaxData = false;
	public Boolean 		IsInTimeRange = true;
	public Boolean		HasTag = false;
	
	@Override
	public dtoItems clone() throws CloneNotSupportedException {
		return (dtoItems)super.clone();
	}
}


