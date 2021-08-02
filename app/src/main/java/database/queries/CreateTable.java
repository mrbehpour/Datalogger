package database.queries;
import android.util.Log;
import database.fields.*;

public class CreateTable {

	//--------------------------------------------------------------
//	("locale" TEXT DEFAULT 'en_US')
	public static String Tbl_Users_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"   '%s' INTEGER)"
						,Tbl_Users.TableName
						,Tbl_Users.Id
						,Tbl_Users.FirstName
						,Tbl_Users.LastName
						,Tbl_Users.UserName
						,Tbl_Users.FullName
						,Tbl_Users.UserCode
						,Tbl_Users.ShiftID
						,Tbl_Users.pass
						,Tbl_Users.UserTypeID
						,Tbl_Users.shiftName
						,Tbl_Users.IsManager
						,Tbl_Users.NeedTag
						,Tbl_Users.UserGroupId
				);
		Log.i("db", "onCreate TableUserCreate");
		return query;
	}

	public static String Tbl_SubEquipments_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' INTEGER," +
								" 	'%s' TEXT," +
								" 	'%s' TEXT," +
								"	'%s' INTEGER)"
								,Tbl_SubEquipments.TableName
								,Tbl_SubEquipments.Id
								,Tbl_SubEquipments.SubEquipID
								,Tbl_SubEquipments.SubEquipName
								,Tbl_SubEquipments.Des
								,Tbl_SubEquipments.EquipInfID
								);
		Log.i("db", "onCreate TableSubEquipmentsCreate");
		return query;
	}
	
	public static String Tbl_Setting_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER)"
								,Tbl_Setting.TableName
								,Tbl_Setting.Id
								,Tbl_Setting.Range1
								,Tbl_Setting.Range2
								,Tbl_Setting.Range3
								,Tbl_Setting.Dsc1
								,Tbl_Setting.Dsc2
								,Tbl_Setting.Dsc3
								,Tbl_Setting.BackligthTime
								,Tbl_Setting.ContorolTime
								,Tbl_Setting.ModTag
								,Tbl_Setting.LayerTag
								,Tbl_Setting.SendItem
								,Tbl_Setting.ShowEmp
								,Tbl_Setting.ShowLastData
								,Tbl_Setting.NoCheckMaxData
								,Tbl_Setting.UseSetUserTime
								);
		Log.i("db", "onCreate TableSettingCreate");
		return query;
	}
	
	public static String Tbl_Remarks_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' TEXT," +
								"	'%s' TEXT)"
								,Tbl_Remarks.TableName
								,Tbl_Remarks.Id
								,Tbl_Remarks.RemName
								,Tbl_Remarks.Des
								);
		Log.i("db", "onCreate TableRemarksCreate");
		return query;
	}

	public static String Tbl_RemarkGroup_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' TEXT," +
								"	'%s' TEXT)"
								,Tbl_RemarkGroup.TableName
								,Tbl_RemarkGroup.Id
								,Tbl_RemarkGroup.RemGrpName
								,Tbl_RemarkGroup.Des
								);
		Log.i("db", "onCreate TableRemarkGroupCreate");
		return query;
	}

	public static String Tbl_Posts_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' TEXT," +
								"	'%s' TEXT)"
								,Tbl_Posts.TableName
								,Tbl_Posts.Id
								,Tbl_Posts.PostName
								,Tbl_Posts.Des
								);
		Log.i("db", "onCreate TablePostsCreate");
		return query;
	}

	public static String Tbl_Measures_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' TEXT," +
								"	'%s' TEXT)"
								,Tbl_Measures.TableName
								,Tbl_Measures.Id
								,Tbl_Measures.MeasureName
								,Tbl_Measures.Des
								);
		Log.i("db", "onCreate TableMeasuresCreate");
		return query;
	}

	public static String Tbl_MaxItemVal_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT)"
								,Tbl_MaxItemVal.TableName
								,Tbl_MaxItemVal.Id
								,Tbl_MaxItemVal.ItemInfID
								,Tbl_MaxItemVal.ItemVal
								,Tbl_MaxItemVal.RemAns
								,Tbl_MaxItemVal.ItemValTyp
								);
		Log.i("db", "onCreate TableMaxItemValCreate");
		return query;
	}

	public static String Tbl_LogShits_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								" 	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT)"
								,Tbl_LogShits.TableName
								,Tbl_LogShits.Id
								,Tbl_LogShits.LogshitInfID
								,Tbl_LogShits.LogshitID
								,Tbl_LogShits.LogshitName
								,Tbl_LogShits.PostID
								,Tbl_LogShits.Ver
								,Tbl_LogShits.TagID
								,Tbl_LogShits.Des
								);
		Log.i("db", "onCreate TableLogShitsCreate");
		return query;
	}

	public static String Tbl_Logics_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' TEXT," +
								"	'%s' TEXT)"
								,Tbl_Logics.TableName
								,Tbl_Logics.Id
								,Tbl_Logics.LogicVal1
								,Tbl_Logics.LogicVal2
								);
		Log.i("db", "onCreate TableLogicsCreate");
		return query;
	}

	public static String Tbl_ItemTypeValues_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' TEXT," +
								"	'%s' TEXT)"
								,Tbl_ItemTypeValues.TableName
								,Tbl_ItemTypeValues.Id
								,Tbl_ItemTypeValues.ItemTypValName
								,Tbl_ItemTypeValues.Des
								);
		Log.i("db", "onCreate TableItemTypeValuesCreate");
		return query;
	}

	public static String Tbl_Items_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								"	'%s' INTEGER," +
								" 	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' NUMERIC," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER)"
								,Tbl_Items.TableName
								,Tbl_Items.Id
								,Tbl_Items.ItemInfID
								,Tbl_Items.ItemName
								,Tbl_Items.SubEquipID
								,Tbl_Items.EquipInfID
								,Tbl_Items.LogshitInfID
								,Tbl_Items.PostID
								,Tbl_Items.RemGroupID
								,Tbl_Items.AmountTypID
								,Tbl_Items.MeasureUnitName
								,Tbl_Items.Zarib
								,Tbl_Items.MaxSampleNo
								,Tbl_Items.MaxAmount1
								,Tbl_Items.MinAmount1
								,Tbl_Items.MaxAmount2
								,Tbl_Items.MinAmount2
								,Tbl_Items.MaxAmount3
								,Tbl_Items.MinAmount3
								,Tbl_Items.TagID
								,Tbl_Items.RemTyp
								,Tbl_Items.STTime
								,Tbl_Items.PeriodTime
								,Tbl_Items.PeriodTypTime
								,Tbl_Items.LogicTypID
								,Tbl_Items.Desc
								,Tbl_Items.RangeTime
								,Tbl_Items.RangeTypTime
								,Tbl_Items.LocateRowNo
								,Tbl_Items.LogshitRowNo
								);
		Log.i("db", "onCreate TableItemsCreate");
		return query;
	}

	public static String Tbl_ItemFormulas_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' INTEGER)"
								,Tbl_ItemFormulas.TableName
								,Tbl_ItemFormulas.Id
								,Tbl_ItemFormulas.ItemInfID
								,Tbl_ItemFormulas.FormulaID
								,Tbl_ItemFormulas.ItemSelect
								,Tbl_ItemFormulas.constantNumber
								);
		Log.i("db", "onCreate TableItemFormulasCreate");
		return query;
	}

	public static String Tbl_ItemRanges_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								"	'%s' INTEGER," +
								" 	'%s' INTEGER)"
						,Tbl_ItemRanges.TableName
						,Tbl_ItemRanges.Id
						,Tbl_ItemRanges.ItemInfID
						,Tbl_ItemRanges.ItemBaseRangeMin
				);
		Log.i("db", "onCreate TableItemsRangesCreate");
		return query;
	}
	public static String Tbl_Formulas_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								"	'%s' TEXT)"
								,Tbl_Formulas.TableName
								,Tbl_Formulas.Id
								,Tbl_Formulas.FormulName
								);
		Log.i("db", "onCreate TableFormulasCreate");
		return query;
	}

	public static String Tbl_Equipments_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' TEXT," +
								" 	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' INTEGER)"
								,Tbl_Equipments.TableName
								,Tbl_Equipments.Id
								,Tbl_Equipments.EquipInfoID
								,Tbl_Equipments.EquipName
								,Tbl_Equipments.LogshitInfID
								,Tbl_Equipments.LocationID
								,Tbl_Equipments.EquipNo
								,Tbl_Equipments.Des
								,Tbl_Equipments.EquipTypID
								);
		Log.i("db", "onCreate TableEquipmentsCreate");
		return query;
	}

	public static String Tbl_ItemValues_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' INTEGER," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER,"+
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"	'%s' TEXT," +
								"   '%s' INTEGER," +
								"	'%s' TEXT, "+
								"   '%s' NUMERIC)"
								,Tbl_ItemValues.TableName
								,Tbl_ItemValues.Id
								,Tbl_ItemValues.ItemInfID
								,Tbl_ItemValues.ItemVal
								,Tbl_ItemValues.ItemValTyp
								,Tbl_ItemValues.PDate
								,Tbl_ItemValues.PTime
								,Tbl_ItemValues.UsrID
								,Tbl_ItemValues.ShiftID
								,Tbl_ItemValues.IsSend
								,Tbl_ItemValues.VideoPath
								,Tbl_ItemValues.ImagePath
								,Tbl_ItemValues.VoicePath
								,Tbl_ItemValues.BaseRange
								,Tbl_ItemValues.RemValues
								,Tbl_ItemValues.SaveDateTimeToMin
								);
		Log.i("db", "onCreate TableItemValuesCreate");
		return query;
	}

	public static String Tbl_UserShift_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								" 	'%s' INTEGER," +
								"	'%s' INTEGER)"
								,Tbl_UserShift.TableName
								,Tbl_UserShift.Id
								,Tbl_UserShift.UserID
								,Tbl_UserShift.ShiftID
								);
		Log.i("db", "onCreate TableUserShiftCreate");
		return query;
	}
	
	public static String Tbl_Shifts_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								"	'%s' TEXT," +
								"	'%s' TEXT)"
								,Tbl_Shifts.TableName
								,Tbl_Shifts.Id
								,Tbl_Shifts.Name
								,Tbl_Shifts.Description
								);
		Log.i("db", "onCreate TableShiftsCreate");
		return query;
	}

	public static String Tbl_UserLogshit_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER)"
								,Tbl_UserLogshit.TableName
								,Tbl_UserLogshit.Id
								,Tbl_UserLogshit.UsrID
								,Tbl_UserLogshit.LogshitID
								);
		Log.i("db", "onCreate Tbl_UserLogshit_Create");
		return query;
	}

	public static String Tbl_CorsRems_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER)"
								,Tbl_CorsRems.TableName
								,Tbl_CorsRems.Id
								,Tbl_CorsRems.RemGrpID
								,Tbl_CorsRems.RemID
								);
		Log.i("db", "onCreate Tbl_CorsRems_Create");
		return query;
	}
	public static String Tbl_UsrPost_Create(){
		String query =
				String.format(	"	CREATE TABLE IF NOT EXISTS '%s' " +
								"	('%s' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE ," +
								"	'%s' INTEGER," +
								"	'%s' INTEGER)"
						,Tbl_UsrPost.TableName
						,Tbl_UsrPost.Id
						,Tbl_UsrPost.PostID
						,Tbl_UsrPost.UsrID
				);
		Log.i("db", "onCreate Tbl_UsrPost_Create");
		return query;
	}
}
