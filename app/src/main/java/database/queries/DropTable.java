package database.queries;

import database.fields.Tbl_CorsRems;
import database.fields.Tbl_Equipments;
import database.fields.Tbl_Formulas;
import database.fields.Tbl_ItemFormulas;
import database.fields.Tbl_ItemTypeValues;
import database.fields.Tbl_ItemValues;
import database.fields.Tbl_Items;
import database.fields.Tbl_LogShits;
import database.fields.Tbl_Logics;
import database.fields.Tbl_MaxItemVal;
import database.fields.Tbl_Measures;
import database.fields.Tbl_Posts;
import database.fields.Tbl_RemarkGroup;
import database.fields.Tbl_Setting;
import database.fields.Tbl_SubEquipments;
import database.fields.Tbl_UserLogshit;
import database.fields.Tbl_Users;
import database.fields.Tbl_Shifts;
import database.fields.Tbl_UserShift;
import database.fields.Tbl_UsrPost;

public class DropTable {

	private static String getDropQuery(String tblName){
		return String.format("Drop table if exists '%s' " ,tblName);
	}
	public static String Tbl_Users_Drop(){
		return getDropQuery(Tbl_Users.TableName);
	}
	public static String Tbl_SubEquipments_Drop(){
		return getDropQuery(Tbl_SubEquipments.TableName);
	}
	public static String Tbl_Setting_Drop(){
		return getDropQuery(Tbl_Setting.TableName);
	}
	public static String Tbl_Remarks_Drop(){
		return getDropQuery(database.fields.Tbl_Remarks.TableName);
	}
	public static String Tbl_RemarkGroup_Drop(){
		return getDropQuery(Tbl_RemarkGroup.TableName);
	}
	public static String Tbl_Posts_Drop(){
		return getDropQuery(Tbl_Posts.TableName);
	}
	public static String Tbl_Measures_Drop(){
		return getDropQuery(Tbl_Measures.TableName);
	}
	public static String Tbl_MaxItemVal_Drop(){
		return getDropQuery(Tbl_MaxItemVal.TableName);
	}
	public static String Tbl_LogShits_Drop(){
		return getDropQuery(Tbl_LogShits.TableName);
	}
	public static String Tbl_Logics_Drop(){
		return getDropQuery(Tbl_Logics.TableName);
	}
	public static String Tbl_ItemTypeValues_Drop(){
		return getDropQuery(Tbl_ItemTypeValues.TableName);
	}
	public static String Tbl_Items_Drop(){
		return getDropQuery(Tbl_Items.TableName);
	}
	public static String Tbl_ItemFormulas_Drop(){
		return getDropQuery(Tbl_ItemFormulas.TableName);
	}
	public static String Tbl_Formulas_Drop(){
		return getDropQuery(Tbl_Formulas.TableName);
	}
	public static String Tbl_Equipments_Drop(){
		return getDropQuery(Tbl_Equipments.TableName);
	}
	public static String Tbl_ItemValues_Drop(){
		return getDropQuery(Tbl_ItemValues.TableName);
	}
	public static String Tbl_UserShift_Drop(){
		return getDropQuery(Tbl_UserShift.TableName);
	}
	public static String Tbl_Shifts_Drop(){
		return getDropQuery(Tbl_Shifts.TableName);
	}
	public static String Tbl_UserLogshit_Drop(){
		return getDropQuery(Tbl_UserLogshit.TableName);
	}
	public static String Tbl_CorsRems_Drop(){
		return getDropQuery(Tbl_CorsRems.TableName);
	}
	public static String Tbl_UsrPost_Drop(){return getDropQuery(Tbl_UsrPost.TableName);	}
	
}
