package database.structs;

import java.util.ArrayList;

public class dtoPackItems  implements Cloneable{
	public ArrayList<dtoPosts>					tbl_Posts;
	public ArrayList<dtoLogShits> 				tbl_LogShits;
	public ArrayList<dtoEquipments> 			tbl_Equipments;
	public ArrayList<dtoSubEquipments> 			tbl_SubEquipments;
	public ArrayList<dtoItems> 					tbl_Items;
	public ArrayList<dtoLogics>					tbl_Logics;
	public ArrayList<dtoRemarks>				tbl_Remarks;
	public ArrayList<dtoItemTypeValues>			tbl_ItemTypeValues;
	public ArrayList<dtoMeasures>				tbl_Measures;
	public ArrayList<dtoFormulas> 				tbl_Formulas;
	public ArrayList<dtoItemFormulas> 			tbl_ItemFormulas;
	public ArrayList<dtoRemarkGroup> 			tbl_RemarkGroup;
	public ArrayList<dtoRelateUserLogshitTbl> 	tbl_RelateUserLogshitTbl;
	public ArrayList<dtoCorsRems> 				tbl_CorsRems;
	public ArrayList<dtoUsrPost> 				tbl_UsrPost;
	
	@Override
	public dtoPackItems clone() throws CloneNotSupportedException {
		return (dtoPackItems)super.clone();
	}
}


