/**
 * @author Hassan Eskandari
 * Copyright (c) 2016, PM Project.
 */

package database.helper;

import database.fields.Tbl_ItemRanges;
import database.fields.Tbl_UsrPost;
import database.structs.dtoItemRanges;
import database.structs.dtoUsrPost;
import ir.saa.android.datalogger.G;

import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
import database.fields.Tbl_Remarks;
import database.fields.Tbl_Setting;
import database.fields.Tbl_Shifts;
import database.fields.Tbl_SubEquipments;
import database.fields.Tbl_UserLogshit;
import database.fields.Tbl_UserShift;
import database.fields.Tbl_Users;
import database.queries.*;
import database.structs.dtoCorsRems;
import database.structs.dtoEquipments;
import database.structs.dtoFormulas;
import database.structs.dtoItemFormulas;
import database.structs.dtoItemTypeValues;
import database.structs.dtoItemValues;
import database.structs.dtoItems;
import database.structs.dtoLogShits;
import database.structs.dtoLogics;
import database.structs.dtoMaxItemVal;
import database.structs.dtoMeasures;
import database.structs.dtoPosts;
import database.structs.dtoRelateUserLogshitTbl;
import database.structs.dtoRemarkGroup;
import database.structs.dtoRemarks;
import database.structs.dtoSetting;
import database.structs.dtoShifts;
import database.structs.dtoSubEquipments;
import database.structs.dtoUserShift;
import database.structs.dtoUsers;
import ir.saa.android.datalogger.R;
import ir.saa.android.datalogger.Tarikh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.http.ParseException;

public class DatabaseHelper extends SQLiteOpenHelper {

    Context myContext;
    //	SharedPreferences sharedpreferences;
    private static final String DB_NAME = "dl.db";
    private static final String DB_PATH = G.DIR_DATABASE;
    private static final int DATABASE_VERSION = 3;
    SQLiteDatabase myDB;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        Log.i("db", "Constructor SQLiteDatabase");
        myDB = context.openOrCreateDatabase(DB_NAME, 0, null);
        myContext = context;
        onCreate(myDB);
    }

//    @Override
//    public void onConfigure(SQLiteDatabase db) {
//        super.onConfigure(db);
//        db.setLocale(new Locale("fa","IR"));
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTable.Tbl_Users_Create());
        db.execSQL(CreateTable.Tbl_Shifts_Create());
        db.execSQL(CreateTable.Tbl_UserShift_Create());

        if (!HasUsersAnyRecords(db)) {
            dtoUsers oUser = new dtoUsers();
            oUser.UsrID = 1;
            oUser.FirstName = (String) G.context.getText(R.string.FirstName);
            oUser.LastName = (String) G.context.getText(R.string.LastName);
            oUser.UserName = "Admin System";
            oUser.FullName = (String) G.context.getText(R.string.FullName);
            oUser.pass = "123";
            oUser.IsManager = 1;
            oUser.shiftName = "A";
            oUser.UserTypeID = 1;
            oUser.ShiftID = 1;
            oUser.UserCode = (long)1;
            oUser.UserGroupId=1;
            InsertUser(oUser, db);
        }
        db.execSQL(CreateTable.Tbl_Setting_Create());

        db.execSQL(CreateTable.Tbl_Posts_Create());
        db.execSQL(CreateTable.Tbl_LogShits_Create());
        db.execSQL(CreateTable.Tbl_Equipments_Create());
        db.execSQL(CreateTable.Tbl_SubEquipments_Create());
        db.execSQL(CreateTable.Tbl_Items_Create());
        db.execSQL(CreateTable.Tbl_Logics_Create());
        db.execSQL(CreateTable.Tbl_Remarks_Create());
        db.execSQL(CreateTable.Tbl_ItemTypeValues_Create());
        db.execSQL(CreateTable.Tbl_Measures_Create());
        db.execSQL(CreateTable.Tbl_Formulas_Create());
        db.execSQL(CreateTable.Tbl_ItemFormulas_Create());
        db.execSQL(CreateTable.Tbl_RemarkGroup_Create());
        db.execSQL(CreateTable.Tbl_UserLogshit_Create());

        db.execSQL(CreateTable.Tbl_ItemValues_Create());
        db.execSQL(CreateTable.Tbl_ItemRanges_Create());
        db.execSQL(CreateTable.Tbl_MaxItemVal_Create());
        db.execSQL(CreateTable.Tbl_CorsRems_Create());
        db.execSQL(CreateTable.Tbl_UsrPost_Create());

        Log.i("db", "onCreate SQLiteDatabase");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("db", "onUpgrade SQLiteDatabase from " + oldVersion + " to " + newVersion);

        db.execSQL(DropTable.Tbl_Users_Drop());
        db.execSQL(DropTable.Tbl_Shifts_Drop());
        db.execSQL(DropTable.Tbl_UserShift_Drop());
        db.execSQL(DropTable.Tbl_Setting_Drop());

        db.execSQL(DropTable.Tbl_Posts_Drop());
        db.execSQL(DropTable.Tbl_LogShits_Drop());
        db.execSQL(DropTable.Tbl_Equipments_Drop());
        db.execSQL(DropTable.Tbl_SubEquipments_Drop());
        db.execSQL(DropTable.Tbl_Items_Drop());
        db.execSQL(DropTable.Tbl_Logics_Drop());
        db.execSQL(DropTable.Tbl_Remarks_Drop());
        db.execSQL(DropTable.Tbl_ItemTypeValues_Drop());
        db.execSQL(DropTable.Tbl_Measures_Drop());
        db.execSQL(DropTable.Tbl_Formulas_Drop());
        db.execSQL(DropTable.Tbl_ItemFormulas_Drop());
        db.execSQL(DropTable.Tbl_RemarkGroup_Drop());
        db.execSQL(DropTable.Tbl_UserLogshit_Drop());

        db.execSQL(DropTable.Tbl_ItemValues_Drop());
        db.execSQL(DropTable.Tbl_MaxItemVal_Drop());
        db.execSQL(DropTable.Tbl_CorsRems_Drop());
        db.execSQL(DropTable.Tbl_UsrPost_Drop());

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("db", "onDowngrade SQLiteDatabase from " + oldVersion + " to " + newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i("db", "onOpen SQLiteDatabase");

        super.onOpen(db);
    }


    //--------------------DataLogger-------------------------
//--------------------Fetch Data From DB-----------------------
    public int getNotSendedItemValuesCount() {
        int count;
        SQLiteDatabase sd = getWritableDatabase();
        //Cursor cur = sd.query(Tbl_Setting.TableName, null, null, null, null, null, null);
        String strQuery = String.format("select count(*) itemcount from %s lastValues where lastValues.%s = %s ", Tbl_ItemValues.TableName,Tbl_ItemValues.IsSend,0);
        Cursor cur = sd.rawQuery(strQuery, null);
        cur.moveToFirst();
        count = cur.getInt(cur.getColumnIndex("itemcount"));
        cur.close();
        return count;
    }
    public dtoSetting getSetting() {
        dtoSetting setting = new dtoSetting();
        String[] cols = new String[]{
                Tbl_Setting.Range1,
                Tbl_Setting.Range2,
                Tbl_Setting.Range3,
                Tbl_Setting.Dsc1,
                Tbl_Setting.Dsc2,
                Tbl_Setting.Dsc3,
                Tbl_Setting.BackligthTime,
                Tbl_Setting.ContorolTime,
                Tbl_Setting.ModTag,
                Tbl_Setting.LayerTag,
                Tbl_Setting.SendItem,
                Tbl_Setting.ShowEmp,
                Tbl_Setting.ShowLastData,
                Tbl_Setting.NoCheckMaxData,
                Tbl_Setting.UseSetUserTime
        };
        SQLiteDatabase sd = getReadableDatabase();
        Cursor cur = sd.query(Tbl_Setting.TableName, cols, null, null, null, null, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                setting.Range1 = cur.getInt(cur.getColumnIndex(Tbl_Setting.Range1));
                setting.Range2 = cur.getInt(cur.getColumnIndex(Tbl_Setting.Range2));
                setting.Range3 = cur.getInt(cur.getColumnIndex(Tbl_Setting.Range3));
                setting.Dsc1 = cur.getString(cur.getColumnIndex(Tbl_Setting.Dsc1));
                setting.Dsc2 = cur.getString(cur.getColumnIndex(Tbl_Setting.Dsc2));
                setting.Dsc3 = cur.getString(cur.getColumnIndex(Tbl_Setting.Dsc3));
                setting.BackligthTime = cur.getInt(cur.getColumnIndex(Tbl_Setting.BackligthTime));
                setting.ContorolTime = cur.getString(cur.getColumnIndex(Tbl_Setting.ContorolTime));
                setting.ModTag = cur.getInt(cur.getColumnIndex(Tbl_Setting.ModTag));
                setting.LayerTag = cur.getInt(cur.getColumnIndex(Tbl_Setting.LayerTag));
                setting.SendItem = cur.getInt(cur.getColumnIndex(Tbl_Setting.SendItem));
                setting.ShowEmp = cur.getInt(cur.getColumnIndex(Tbl_Setting.ShowEmp));
                setting.ShowLastData = cur.getInt(cur.getColumnIndex(Tbl_Setting.ShowLastData));
                setting.NoCheckMaxData = cur.getInt(cur.getColumnIndex(Tbl_Setting.NoCheckMaxData));
                setting.UseSetUserTime = cur.getInt(cur.getColumnIndex(Tbl_Setting.UseSetUserTime));
            } while (cur.moveToNext());
            return setting;
        } else
            return null;
    }

    public ArrayList<dtoUsers> getUsersFromDB() {
        ArrayList<dtoUsers> lstUsers = new ArrayList<dtoUsers>();
        String[] cols = new String[]{
                Tbl_Users.Id,
                Tbl_Users.UserName,
                Tbl_Users.LastName,
                Tbl_Users.FirstName,
                Tbl_Users.FullName,
                Tbl_Users.UserCode,
                Tbl_Users.ShiftID,
                Tbl_Users.pass,
                Tbl_Users.UserTypeID,
                Tbl_Users.shiftName,
                Tbl_Users.IsManager,
                Tbl_Users.NeedTag,
                Tbl_Users.UserGroupId
        };
        SQLiteDatabase sd = getReadableDatabase();
        Cursor cur;
        if(G.RTL) {

            cur = sd.query(Tbl_Users.TableName, cols, null, null, null, null, null);//Tbl_Users.LastName + " ASC , " + Tbl_Users.FirstName + " ASC");
        }else {

            cur = sd.query(Tbl_Users.TableName, cols, null, null, null, null, Tbl_Users.UserName + " ASC " );
        }
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoUsers user = new dtoUsers();
                user.UsrID = cur.getInt(cur.getColumnIndex(Tbl_Users.Id));
                user.FirstName = cur.getString(cur.getColumnIndex(Tbl_Users.FirstName));
                user.LastName = cur.getString(cur.getColumnIndex(Tbl_Users.LastName));
                user.UserName = cur.getString(cur.getColumnIndex(Tbl_Users.UserName));
                user.FullName = cur.getString(cur.getColumnIndex(Tbl_Users.FullName));
                user.UserCode = cur.getLong(cur.getColumnIndex(Tbl_Users.UserCode));
                user.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_Users.ShiftID));
                user.pass = cur.getString(cur.getColumnIndex(Tbl_Users.pass));
                user.UserTypeID = cur.getInt(cur.getColumnIndex(Tbl_Users.UserTypeID));
                user.shiftName = cur.getString(cur.getColumnIndex(Tbl_Users.shiftName));
                user.IsManager = cur.getInt(cur.getColumnIndex(Tbl_Users.IsManager));
                user.NeedTag = cur.getInt(cur.getColumnIndex(Tbl_Users.NeedTag));
                user.UserGroupId=cur.getInt(cur.getColumnIndex(Tbl_Users.UserGroupId));
                lstUsers.add(user);
            } while (cur.moveToNext());
        }
        cur.close();


        if(G.RTL) {
            Collator collator = Collator.getInstance(new Locale("fa", "IR"));
            collator.setStrength(Collator.PRIMARY);
            Collections.sort(lstUsers, (o1, o2) -> collator.compare(o1.LastName, o2.LastName));
        }

        return lstUsers;
    }

    public ArrayList<dtoShifts> getShiftsByUserId(dtoUsers user) {
        ArrayList<dtoShifts> lstShifts = new ArrayList<dtoShifts>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_UserShift.TableName,
                Tbl_Shifts.TableName,
                user.UsrID
        };

        String strQuery = String.format("select shifts.* from %s userShift inner join %s shifts on userShift.ShiftID = shifts.ID where userShift.UserID = %s ORDER BY shifts.Name ASC;", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoShifts shift = new dtoShifts();
                shift.ID = cur.getInt(cur.getColumnIndex(Tbl_Shifts.Id));
                shift.Name = cur.getString(cur.getColumnIndex(Tbl_Shifts.Name));
                lstShifts.add(shift);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstShifts;
    }

    public ArrayList<dtoPosts> GetVAHEDList() { //Handling Which user should see which unit is inside the method
        ArrayList<dtoPosts> lstPosts = new ArrayList<dtoPosts>();
        SQLiteDatabase sd = getReadableDatabase();

        String strQuery = "";
//			if(G.currentUser.IsManager!=null && G.currentUser.IsManager==1){
//				strQuery = "select * from "+Tbl_Posts.TableName;
//			}else{
        Object[] args = new Object[]{
                Tbl_Posts.TableName,
                Tbl_UsrPost.TableName,
                Tbl_UsrPost.PostID,
                Tbl_Posts.Id,
                Tbl_UsrPost.UsrID,
                G.currentUser.UsrID
        };
        strQuery = String.format("select DISTINCT posts.* from %s posts inner join %s userpost on userpost.%s=posts.%s where userpost.%s = %s", args);
        //strQuery = String.format("select DISTINCT posts.* from %s userLogshit inner join %s logshits on userLogshit.%s = logshits.%s  inner join %s posts on posts.%s = logshits.%s inner join %s userpost on userpost.%s=posts.%s where userLogshit.%s = %s AND userpost.%s = %s", args);
//			}
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoPosts post = new dtoPosts();
                post.PostID = cur.getInt(cur.getColumnIndex(Tbl_Posts.Id));
                post.PostName = cur.getString(cur.getColumnIndex(Tbl_Posts.PostName));
                post.Des = cur.getString(cur.getColumnIndex(Tbl_Posts.Des));
                lstPosts.add(post);
            } while (cur.moveToNext());
        }
        return lstPosts;
    }

    public ArrayList<dtoLogShits> GetLogshitListByPostId(Integer selectedPostId) { //Handling Which user should see which logshit is inside the method
        ArrayList<dtoLogShits> lstLogshits = new ArrayList<dtoLogShits>();
        SQLiteDatabase sd = getReadableDatabase();

        Object[] args = new Object[]{
                Tbl_LogShits.TableName,
                Tbl_Posts.TableName,
                Tbl_LogShits.PostID,
                Tbl_Posts.Id,
                Tbl_UserLogshit.TableName,
                Tbl_UserLogshit.LogshitID,
                Tbl_LogShits.LogshitID,
                Tbl_UsrPost.TableName,
                Tbl_UsrPost.PostID,
                Tbl_Posts.Id,
                Tbl_LogShits.PostID,
                selectedPostId,
                Tbl_UserLogshit.UsrID,
                G.currentUser.UsrID
        };

        String strQuery = String.format("select DISTINCT logshits.* from %s logshits inner join %s posts on logshits.%s = posts.%s  inner join %s userLogshit on userLogshit.%s = logshits.%s inner join %s userpost on userpost.%s = posts.%s  where logshits.%s = %s AND userLogshit.%s = %s ", args); //AND userLogshit.%s = %s
        //String strQuery = String.format("select DISTINCT logshits.* from %s logshits inner join %s posts on logshits.%s = posts.%s  inner join %s userLogshit on userLogshit.%s = logshits.%s inner join %s userpost on userpost.%s = posts.%s where logshits.%s = %s  ", args); //AND userLogshit.%s = %s

        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoLogShits logshit = new dtoLogShits();
                logshit.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_LogShits.LogshitInfID));
                logshit.LogshitID = cur.getInt(cur.getColumnIndex(Tbl_LogShits.LogshitID));
                logshit.LogshitName = cur.getString(cur.getColumnIndex(Tbl_LogShits.LogshitName));
                logshit.PostID = cur.getInt(cur.getColumnIndex(Tbl_LogShits.PostID));
                logshit.Ver = cur.getString(cur.getColumnIndex(Tbl_LogShits.Ver));
                logshit.TagID = cur.getString(cur.getColumnIndex(Tbl_LogShits.TagID));
                logshit.Des = cur.getString(cur.getColumnIndex(Tbl_LogShits.Des));//change1
                lstLogshits.add(logshit);
            } while (cur.moveToNext());
        }
        return lstLogshits;
    }

    public dtoLogShits GetLogshitByTagId(String tagId) {
        dtoLogShits logshit = null;
        SQLiteDatabase sd = getReadableDatabase();

        Object[] args = new Object[]{
                Tbl_LogShits.TableName,
                Tbl_Posts.TableName,
                Tbl_LogShits.PostID,
                Tbl_Posts.Id,
                Tbl_UserLogshit.TableName,
                Tbl_UserLogshit.LogshitID,
                Tbl_LogShits.LogshitID,
                Tbl_UsrPost.TableName,
                Tbl_UsrPost.PostID,
                Tbl_Posts.Id,
                Tbl_LogShits.TagID,
                tagId.toUpperCase(),
                Tbl_UserLogshit.UsrID,
                G.currentUser.UsrID
        };

        String strQuery = String.format("select DISTINCT logshits.* from %s logshits inner join %s posts on logshits.%s = posts.%s  inner join %s userLogshit on userLogshit.%s = logshits.%s inner join %s userpost on userpost.%s = posts.%s  where TRIM(logshits.%s) = '%s' AND userLogshit.%s = %s ", args); //AND userLogshit.%s = %s


        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                logshit = new dtoLogShits();
                logshit.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_LogShits.LogshitInfID));
                logshit.LogshitID = cur.getInt(cur.getColumnIndex(Tbl_LogShits.LogshitID));
                logshit.LogshitName = cur.getString(cur.getColumnIndex(Tbl_LogShits.LogshitName));
                logshit.PostID = cur.getInt(cur.getColumnIndex(Tbl_LogShits.PostID));
                logshit.Ver = cur.getString(cur.getColumnIndex(Tbl_LogShits.Ver));
                logshit.TagID = cur.getString(cur.getColumnIndex(Tbl_LogShits.TagID));
            } while (cur.moveToNext());
        }
        return logshit;
    }


    public ArrayList<dtoLogShits> GetLogshitListByPosts(ArrayList<dtoPosts> postList) { //Handling Which user should see which logshit is inside the method
        ArrayList<dtoLogShits> lstLogshits = new ArrayList<>();
        if (postList.size() == 0) {
            return lstLogshits;
        }
        String postIds = "(";
        for (int i = 0; i < postList.size(); i++) {
            postIds += postList.get(i).PostID.toString();
            if (i != postList.size() - 1) {
                postIds += ",";
            }
        }
        postIds += ")";

        SQLiteDatabase sd = getReadableDatabase();

        Object[] args = new Object[]{
                Tbl_LogShits.TableName,
                Tbl_Posts.TableName,
                Tbl_LogShits.PostID,
                Tbl_Posts.Id,
                Tbl_UserLogshit.TableName,
                Tbl_UserLogshit.LogshitID,
                Tbl_LogShits.LogshitID,
                Tbl_UsrPost.TableName,
                Tbl_UsrPost.PostID,
                Tbl_Posts.Id,
                Tbl_LogShits.PostID,
                postIds,
                Tbl_UserLogshit.UsrID,
                G.currentUser.UsrID
        };

        String strQuery = String.format("select DISTINCT logshits.* from %s logshits inner join %s posts on logshits.%s = posts.%s  inner join %s userLogshit on userLogshit.%s = logshits.%s inner join %s userpost on userpost.%s = posts.%s  where logshits.%s IN %s AND userLogshit.%s = %s ", args); //AND userLogshit.%s = %s

        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoLogShits logshit = new dtoLogShits();
                logshit.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_LogShits.LogshitInfID));
                logshit.LogshitID = cur.getInt(cur.getColumnIndex(Tbl_LogShits.LogshitID));
                logshit.LogshitName = cur.getString(cur.getColumnIndex(Tbl_LogShits.LogshitName));
                logshit.PostID = cur.getInt(cur.getColumnIndex(Tbl_LogShits.PostID));
                logshit.Ver = cur.getString(cur.getColumnIndex(Tbl_LogShits.Ver));
                logshit.TagID = cur.getString(cur.getColumnIndex(Tbl_LogShits.TagID));
                lstLogshits.add(logshit);
            } while (cur.moveToNext());
        }
        return lstLogshits;
    }

    public ArrayList<dtoItems> getItemsByUserIdAndPostId(Integer UsrId,Integer Postid) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        ArrayList<dtoItems> lstItemValues = new ArrayList<dtoItems>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Items.TableName,
                Tbl_ItemValues.TableName,
                Tbl_ItemValues.UsrID,
                UsrId,
                Tbl_Items.PostID,
                Postid
        };
//        String PDate=Tarikh.getCurrentShamsidateWithoutSlash();
//        String PTime=Tarikh.getTimeWithoutColon().substring(0,2);
        String strQuery = String.format("select distinct Items.* from %s Items inner join %s  itemValues on " +
                " itemValues.ItemInfID=Items.ItemInfID where itemValues.%s = %s  AND Items.%s = %s ", args);
        if (G.currentUser.IsManager == 1 ) {
            //strQuery = "select itemValues.* from tbl_ItemValues itemValues where itemValues.IsSend = 0 ";
            strQuery = "select distinct Items.* from tbl_ItemValues itemValues inner join Tbl_Items Items on" +
                    " itemValues.ItemInfID=Items.ItemInfID where itemValues.IsSend = 0 and items.PostID="+Postid.toString();
            //+" and itemValues.PDate='"+PDate+"' and PTime like '"+PTime+"%'";
        }
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItems item = new dtoItems();
                item.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.ItemInfID));
                item.ItemName = cur.getString(cur.getColumnIndex(Tbl_Items.ItemName));
                item.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_Items.SubEquipID));
                item.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.EquipInfID));
                item.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitInfID));
                item.PostID = cur.getInt(cur.getColumnIndex(Tbl_Items.PostID));
                item.RemGroupID = cur.getInt(cur.getColumnIndex(Tbl_Items.RemGroupID));
                item.AmountTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.AmountTypID));
                item.MeasureUnitName = cur.getString(cur.getColumnIndex(Tbl_Items.MeasureUnitName));
                item.Zarib = cur.getDouble((cur.getColumnIndex(Tbl_Items.Zarib)));
                item.MaxSampleNo = cur.getInt(cur.getColumnIndex(Tbl_Items.MaxSampleNo));
                item.MaxAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount1));
                item.MinAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount1));
                item.MaxAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount2));
                item.MinAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount2));
                item.MaxAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount3));
                item.MinAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount3));
                item.TagID = cur.getString(cur.getColumnIndex(Tbl_Items.TagID));
                item.RemTyp = cur.getInt(cur.getColumnIndex(Tbl_Items.RemTyp));
                item.STTime = cur.getString(cur.getColumnIndex(Tbl_Items.STTime));
                item.PeriodTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTime));
                item.PeriodTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTypTime));
                item.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogicTypID));
                item.Desc = cur.getString(cur.getColumnIndex(Tbl_Items.Desc));
                item.LocateRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LocateRowNo));
                item.LogshitRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitRowNo));
                item.RangeTime = cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTime));
                item.RangeTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTypTime));
                lstItemValues.add(item);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }
    public ArrayList<dtoItemValues> getItemValuesByUserIdAndItemInfIdWithDate(Integer UsrId, Integer itemInfID) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        ArrayList<dtoItemValues> lstItemValues = new ArrayList<dtoItemValues>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.TableName,
                Tbl_ItemValues.UsrID,
                UsrId,
                Tbl_ItemValues.ItemInfID,
                itemInfID,
        };
        //String PDate=Tarikh.getCurrentShamsidateWithoutSlash();
        //String PTime=Tarikh.getTimeWithoutColon().substring(0,2);
        //String strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s AND itemValues.%s = %s AND itemValues.%s = %s  ", args);
        String strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s AND itemValues.%s = %s  ", args);//+" and itemValues.PDate='"+PDate+"' and PTime like '"+PTime+"%'";;
        if (G.currentUser.IsManager == 1 ) {
            strQuery = "select itemValues.* from Tbl_ItemValues itemValues where itemValues.ItemInfID ="+itemInfID;///+" and itemValues.PDate='"+PDate+"' and PTime like '"+PTime+"%'";;
        }
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItemValues itemValue = new dtoItemValues();
                itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
                itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
                itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
                itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
                itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
                itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
                itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
                itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                lstItemValues.add(itemValue);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }
    public ArrayList<dtoEquipments> GetEquipmentListByLogshits(ArrayList<dtoLogShits> logshitList) { //Handling Which user should see which logshit is inside the method
        ArrayList<dtoEquipments> lstEquipments = new ArrayList<>();
        if (logshitList.size() == 0) {
            return lstEquipments;
        }
        String logshitInfIds = "(";
        for (int i = 0; i < logshitList.size(); i++) {
            logshitInfIds += logshitList.get(i).LogshitInfID.toString();
            if (i != logshitList.size() - 1) {
                logshitInfIds += ",";
            }
        }
        logshitInfIds += ")";

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Equipments.TableName,
                Tbl_LogShits.TableName,
                Tbl_Equipments.LogshitInfID,
                Tbl_LogShits.LogshitInfID,
                Tbl_Equipments.LogshitInfID,
                logshitInfIds
        };
        String strQuery = String.format("select equips.* from %s equips inner join %s logshits on equips.%s = logshits.%s where equips.%s IN %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoEquipments equip = new dtoEquipments();
                equip.EquipInfoID = cur.getInt(cur.getColumnIndex(Tbl_Equipments.EquipInfoID));
                equip.EquipName = cur.getString(cur.getColumnIndex(Tbl_Equipments.EquipName));
                equip.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Equipments.LogshitInfID));
                equip.LocationID = cur.getInt(cur.getColumnIndex(Tbl_Equipments.LocationID));
                equip.EquipNo = cur.getString(cur.getColumnIndex(Tbl_Equipments.EquipNo));
                equip.EquipTypID = cur.getInt(cur.getColumnIndex(Tbl_Equipments.EquipTypID));
                lstEquipments.add(equip);
            } while (cur.moveToNext());
        }
        return lstEquipments;
    }

    public dtoItemValues getLastItemValueByItemInfIdforCheckPeriod(Integer itemInfID,String  startTime,String endTime) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        dtoItemValues itemValue = null;
        try {


            SQLiteDatabase sd = getReadableDatabase();
            Object[] args = new Object[]{
                    Tbl_ItemValues.TableName,
                    Tbl_ItemValues.ItemInfID,
                    itemInfID,
                    Tbl_ItemValues.IsSend,
                    0,
                    Tbl_ItemValues.PDate,
                    startTime,
                    endTime,
                    Tbl_ItemValues.Id


            };
            String strQuery="";
            if(G.currentUser.IsManager==1) {
                strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s AND itemValues.%s = %s And (itemValues.%s ) BETWEEN  '%s' And '%s'   ORDER BY %s DESC Limit 1; ", args);
            }else{
                strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s AND itemValues.%s = %s And (itemValues.%s ) BETWEEN  '%s' And '%s' and  itemValues.UsrID="+String.valueOf(G.currentUser.UsrID)+"  ORDER BY %s DESC Limit 1; ", args);
            }
            Cursor cur = sd.rawQuery(strQuery, null);

            if (cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    itemValue = new dtoItemValues();
                    itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                    itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
                    itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
                    itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
                    itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
                    itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
                    itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
                    itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
                    itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
//                    itemValue.VideoPath=cur.getString(cur.getColumnIndex(Tbl_ItemValues.VideoPath));
//                    itemValue.ImagePath=cur.getString(cur.getColumnIndex(Tbl_ItemValues.ImagePath));
//                    itemValue.VoicePath=cur.getString(cur.getColumnIndex(Tbl_ItemValues.VoicePath));

                    break;
                } while (cur.moveToNext());
            }
            cur.close();
        } catch (Exception ex) {
            Log.i("tag", ex.getMessage());
        }
        return itemValue;
    }

    public ArrayList<dtoEquipments> GetEquipmentListByLogshitInfId(Integer selectedLogshitId,Integer selectedPostId ) { //Handling Which user should see which logshit is inside the method
        ArrayList<dtoEquipments> lstEquipments = new ArrayList<dtoEquipments>();
         SQLiteDatabase sd = getReadableDatabase();

        Object[] args = new Object[]{
                Tbl_Equipments.TableName,
                Tbl_LogShits.TableName,
                Tbl_Equipments.LogshitInfID,
                Tbl_LogShits.LogshitInfID,
                Tbl_Equipments.LogshitInfID,
                selectedLogshitId,
                Tbl_LogShits.PostID,
                selectedPostId

        };
        String strQuery="";
        if(selectedPostId==null) {
            strQuery = String.format("select equips.* from %s equips inner join %s logshits on equips.%s = logshits.%s where equips.%s = %s ", args);
        }else {
            strQuery = String.format("select equips.* from %s equips inner join %s logshits on equips.%s = logshits.%s  where  equips.%s = %s and logshits.%s = %s ", args);
        }
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoEquipments equip = new dtoEquipments();
                equip.EquipInfoID = cur.getInt(cur.getColumnIndex(Tbl_Equipments.EquipInfoID));
                equip.EquipName = cur.getString(cur.getColumnIndex(Tbl_Equipments.EquipName));
                equip.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Equipments.LogshitInfID));
                equip.LocationID = cur.getInt(cur.getColumnIndex(Tbl_Equipments.LocationID));
                equip.EquipNo = cur.getString(cur.getColumnIndex(Tbl_Equipments.EquipNo));
                equip.EquipTypID = cur.getInt(cur.getColumnIndex(Tbl_Equipments.EquipTypID));
                equip.Des = cur.getString(cur.getColumnIndex(Tbl_Equipments.Des));//change1
                lstEquipments.add(equip);
            } while (cur.moveToNext());
        }
        return lstEquipments;
    }

    public ArrayList<dtoItemValues> getItemValuesByUserIdAndLogshit(Integer UsrId,Integer Logshit,Integer PostId) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        ArrayList<dtoItemValues> lstItemValues = new ArrayList<dtoItemValues>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.TableName,
                Tbl_Items.TableName,
                Tbl_ItemValues.UsrID,
                UsrId,
                Tbl_ItemValues.IsSend,
                0,
                Tbl_Items.LogshitInfID,
                Logshit,
                Tbl_Items.PostID,
                PostId

        };
//        String PDate=Tarikh.getCurrentShamsidateWithoutSlash();
//        String PTime=Tarikh.getTimeWithoutColon().substring(0,2);
        String strQuery = String.format("select distinct itemValues.iteminfid from %s itemValues inner join %s Items  on " +
                " itemValues.ItemInfID=Items.ItemInfID where itemValues.%s = %s AND itemValues.%s = %s AND Items.%s = %s and Items.%s = %s ", args);//+" and itemValues.PDate='"+PDate+"' and PTime like '"+PTime+"%'";;
        if (G.currentUser.IsManager == 1 ) {
            strQuery = "select distinct itemValues.iteminfid from tbl_ItemValues itemValues inner join Tbl_Items Items on "+
                    "  itemValues.ItemInfID=Items.ItemInfID where itemValues.IsSend = 0 and Items.LogshitInfID=" +Logshit.toString() + " and Items.PostID=" + PostId.toString();
            //+" and itemValues.PDate='"+PDate+"' and PTime like '"+PTime+"%'";
        }
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItemValues itemValue = new dtoItemValues();
                //itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
//                itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
//                itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
//                itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
//                itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
//                itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
//                itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
//                itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                lstItemValues.add(itemValue);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }

    public ArrayList<dtoItemValues> getItemValuesByUserIdAndTajhizId(Integer UsrId,Integer TajhizId,Integer LogshitId) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        ArrayList<dtoItemValues> lstItemValues = new ArrayList<dtoItemValues>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.TableName,
                Tbl_Items.TableName,
                Tbl_ItemValues.UsrID,
                UsrId,
                Tbl_ItemValues.IsSend,
                0,
                Tbl_Items.EquipInfID,
                TajhizId,
                Tbl_Items.LogshitInfID,
                LogshitId

        };
        String PDate=Tarikh.getCurrentShamsidateWithoutSlash();
        String PTime=Tarikh.getTimeWithoutColon().substring(0,2);
        String strQuery = String.format("select distinct itemValues.iteminfid from %s itemValues inner join %s Items  on " +
                " itemValues.ItemInfID=Items.ItemInfID where itemValues.%s = %s AND itemValues.%s = %s AND Items.%s = %s AND Items.%s = %s  ", args);//+" and itemValues.PDate='"+PDate+"' and PTime like '"+PTime+"%'";;
        if (G.currentUser.IsManager == 1 ) {
            strQuery = "select distinct itemValues.iteminfid from tbl_ItemValues itemValues inner join Tbl_Items Items on "+
                    "  itemValues.ItemInfID=Items.ItemInfID where itemValues.IsSend = 0 and Items.EquipInfID=" +TajhizId.toString() +" and Items.LogshitInfID="+LogshitId.toString();
            //+" and itemValues.PDate='"+PDate+"' and PTime like '"+PTime+"%'";
        }
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItemValues itemValue = new dtoItemValues();
                //itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
//                itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
//                itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
//                itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
//                itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
//                itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
//                itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
//                itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                lstItemValues.add(itemValue);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }
    public ArrayList<dtoItemValues> getItemValuesByUserIdAndZirTajhizId(Integer UsrId,Integer ZirTajhizId,Integer TajhizId,Integer LogshitId) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        ArrayList<dtoItemValues> lstItemValues = new ArrayList<dtoItemValues>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.TableName,
                Tbl_Items.TableName,
                Tbl_ItemValues.UsrID,
                UsrId,
                Tbl_ItemValues.IsSend,
                0,
                Tbl_Items.SubEquipID,
                ZirTajhizId,
                Tbl_Items.EquipInfID,
                TajhizId,
                Tbl_Items.LogshitInfID,
                LogshitId
        };
        //String PDate=Tarikh.getCurrentShamsidateWithoutSlash();
        //String PTime=Tarikh.getTimeWithoutColon().substring(0,2);
        String strQuery = String.format("select distinct itemValues.iteminfid from %s itemValues inner join %s Items  on " +
                " itemValues.ItemInfID=Items.ItemInfID where itemValues.%s = %s AND itemValues.%s = %s AND Items.%s = %s and Items.%s=%s And  Items.%s=%s ", args);//+" and itemValues.PDate='"+PDate+"' and PTime like '"+PTime+"%'";;
        if (G.currentUser.IsManager == 1 ) {
            strQuery = "select distinct itemValues.iteminfid from tbl_ItemValues itemValues inner join Tbl_Items Items on "+
                    "  itemValues.ItemInfID=Items.ItemInfID where itemValues.IsSend = 0 and Items.SubEquipID=" +ZirTajhizId.toString() + " and  Items.EquipInfID=" + TajhizId.toString()
                    +" and Items.LogshitInfID="+LogshitId.toString();
            // +" and itemValues.PDate='"+PDate+"' and PTime like '"+PTime+"%'";
        }
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItemValues itemValue = new dtoItemValues();
                //itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
//                itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
//                itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
//                itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
//                itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
//                itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
//                itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
//                itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                lstItemValues.add(itemValue);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }


    public ArrayList<dtoSubEquipments> GetSubEquipmentListByEquipments(ArrayList<dtoEquipments> tajhizList) { //Handling Which user should see which logshit is inside the method
        ArrayList<dtoSubEquipments> lstSubEquips = new ArrayList<>();
        if (tajhizList.size() == 0) {
            return lstSubEquips;
        }

        String parentIds = "(";
        for (int i = 0; i < tajhizList.size(); i++) {
            parentIds += tajhizList.get(i).EquipInfoID.toString();
            if (i != tajhizList.size() - 1) {
                parentIds += ",";
            }
        }
        parentIds += ")";

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_SubEquipments.TableName,
                Tbl_SubEquipments.EquipInfID,
                parentIds
        };
        String strQuery = String.format("select subEquips.* from %s subEquips where subEquips.%s IN %s ", args);
        //String strQuery = String.format("select subEquips.* from %s subEquips inner join %s equips on subEquips.%s = equips.%s where subEquips.%s = %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoSubEquipments equip = new dtoSubEquipments();
                equip.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_SubEquipments.SubEquipID));
                equip.SubEquipName = cur.getString(cur.getColumnIndex(Tbl_SubEquipments.SubEquipName));
                equip.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_SubEquipments.EquipInfID));
                lstSubEquips.add(equip);
            } while (cur.moveToNext());
        }
        return lstSubEquips;
    }

    public ArrayList<dtoSubEquipments> GetSubEquipmentList(Integer selectedEquipId) { //Handling Which user should see which logshit is inside the method
        ArrayList<dtoSubEquipments> lstSubEquips = new ArrayList<dtoSubEquipments>();
        SQLiteDatabase sd = getReadableDatabase();

        Object[] args = new Object[]{
                Tbl_SubEquipments.TableName,
                Tbl_SubEquipments.EquipInfID,
                selectedEquipId
        };
        String strQuery = String.format("select subEquips.* from %s subEquips where subEquips.%s = %s ", args);
        //String strQuery = String.format("select subEquips.* from %s subEquips inner join %s equips on subEquips.%s = equips.%s where subEquips.%s = %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoSubEquipments equip = new dtoSubEquipments();
                equip.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_SubEquipments.SubEquipID));
                equip.SubEquipName = cur.getString(cur.getColumnIndex(Tbl_SubEquipments.SubEquipName));
                equip.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_SubEquipments.EquipInfID));
                equip.Des = cur.getString(cur.getColumnIndex(Tbl_SubEquipments.Des));//change1
                lstSubEquips.add(equip);
            } while (cur.moveToNext());
        }
        return lstSubEquips;
    }

    public ArrayList<dtoItems> GetItemListWithValue(Integer selectedEquipId, Integer selectedSubEquipId) { //Handling Which user should see which logshit is inside the method
        ArrayList<dtoItems> lstItems = new ArrayList<dtoItems>();
        SQLiteDatabase sd = getReadableDatabase();

        Object[] args = new Object[]{
                Tarikh.getCurrentDateToMinute(),
                G.selectedVahedId,
                G.selectedLogshitId,
                selectedEquipId,
                selectedSubEquipId,
                G.selectedVahedId,
                G.selectedLogshitId,
                selectedEquipId,
                selectedSubEquipId,
        };
        String strWhereUserId="tbl_ItemValues.UsrID="+G.currentUser.UsrID.toString()+" AND ";
        if(G.currentUser.UserGroupId==2 || G.currentUser.IsManager==1){
            strWhereUserId="";
        }
        String strQuery = String.format("SELECT tbl_Items.*,ItemCount FROM tbl_Items LEFT JOIN \n" +
                "(\n" +
                "SELECT tbl.ItemInfID,count(*) ItemCount FROM\n" +
                "(\n" +
                "SELECT \n" +
                "tbl_ItemRanges.ItemInfID,\n" +
                "tbl_ItemRanges.ItemBaseRangeMin,\n" +
                "ABS(SaveDateTimeToMin - (BaseRange+ItemBaseRangeMin)) SaveRanges,\n" +
                "ABS(%s - (BaseRange+ItemBaseRangeMin)) CurRanges,\n" +
                "CASE RangeTypTime WHEN 1 THEN RangeTime*60 WHEN 3 THEN RangeTime*24*60 ELSE RangeTime END RangeTime\n" +
                "from tbl_ItemValues \n" +
                "inner join tbl_Items on tbl_ItemValues.ItemInfID = tbl_Items.ItemInfID  \n" +
                "inner join tbl_ItemRanges on tbl_ItemRanges.ItemInfID = tbl_ItemValues.ItemInfID \n" +
                "WHERE "+strWhereUserId+" tbl_Items.PostID=%s AND tbl_Items.LogshitInfID=%s AND tbl_Items.EquipInfID=%s AND tbl_Items.SubEquipID=%s \n" +
                ") tbl where SaveRanges <= RangeTime AND CurRanges<=RangeTime GROUP BY tbl.ItemInfID\n" +
                ") tbl2  \n" +
                "ON tbl_Items.ItemInfID=tbl2.ItemInfID \n" +
                "WHERE tbl_Items.PostID=%s AND tbl_Items.LogshitInfID=%s AND tbl_Items.EquipInfID=%s AND tbl_Items.SubEquipID=%s", args);

        String strOrderBy="";
        if(G.sharedPref.getString("pref_items_order_type","1").compareTo("1")==0){
            strOrderBy = String.format(" ORDER BY %s ASC",Tbl_Items.LocateRowNo);
        }else if(G.sharedPref.getString("pref_items_order_type","1").compareTo("2")==0){
            strOrderBy = String.format(" ORDER BY %s DESC",Tbl_Items.LocateRowNo);
        }else if(G.sharedPref.getString("pref_items_order_type","1").compareTo("3")==0){
            strOrderBy = String.format(" ORDER BY %s ASC",Tbl_Items.LogshitRowNo);
        }else if(G.sharedPref.getString("pref_items_order_type","1").compareTo("4")==0){
            strOrderBy = String.format(" ORDER BY %s DESC",Tbl_Items.LogshitRowNo);
        }else if(G.sharedPref.getString("pref_items_order_type","1").compareTo("5")==0){
            strOrderBy = String.format(" ORDER BY %s ASC",Tbl_Items.ItemName);
        }else if(G.sharedPref.getString("pref_items_order_type","1").compareTo("6")==0){
            strOrderBy = String.format(" ORDER BY %s DESC",Tbl_Items.ItemName);
        }

        Cursor cur = sd.rawQuery(strQuery + strOrderBy, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItems item = new dtoItems();
                item.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.ItemInfID));
                item.ItemName = cur.getString(cur.getColumnIndex(Tbl_Items.ItemName));
                item.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_Items.SubEquipID));
                item.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.EquipInfID));
                item.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitInfID));
                item.PostID = cur.getInt(cur.getColumnIndex(Tbl_Items.PostID));
                item.RemGroupID = cur.getInt(cur.getColumnIndex(Tbl_Items.RemGroupID));
                item.AmountTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.AmountTypID));
                item.MeasureUnitName = cur.getString(cur.getColumnIndex(Tbl_Items.MeasureUnitName));
                item.Zarib = cur.getDouble((cur.getColumnIndex(Tbl_Items.Zarib)));
                item.MaxSampleNo = cur.getInt(cur.getColumnIndex(Tbl_Items.MaxSampleNo));
                item.MaxAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount1));
                item.MinAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount1));
                item.MaxAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount2));
                item.MinAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount2));
                item.MaxAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount3));
                item.MinAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount3));
                item.TagID = cur.getString(cur.getColumnIndex(Tbl_Items.TagID));
                item.RemTyp = cur.getInt(cur.getColumnIndex(Tbl_Items.RemTyp));
                item.STTime = cur.getString(cur.getColumnIndex(Tbl_Items.STTime));
                item.PeriodTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTime));
                item.PeriodTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTypTime));
                item.RangeTime = cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTime));
                item.RangeTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTypTime));
                item.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogicTypID));
                item.Desc = cur.getString(cur.getColumnIndex(Tbl_Items.Desc));
                item.LocateRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LocateRowNo));
                item.LogshitRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitRowNo));
                item.HasValueInRange = cur.getInt(cur.getColumnIndex("ItemCount"))!=0 ? true : false;

                lstItems.add(item);
            } while (cur.moveToNext());
        }
        return lstItems;
    }

    public ArrayList<dtoItems> GetItemListByEquipmentsAndSubEquipments(ArrayList<dtoEquipments> equipments, ArrayList<dtoSubEquipments> subEquipments) {
        ArrayList<dtoItems> lstItems = new ArrayList<>();
        if (equipments.size() == 0 || subEquipments.size() == 0) {
            return lstItems;
        }

        String equipInfoIds = "(";
        for (int i = 0; i < equipments.size(); i++) {
            equipInfoIds += equipments.get(i).EquipInfoID.toString();
            if (i != equipments.size() - 1) {
                equipInfoIds += ",";
            }
        }
        equipInfoIds += ")";

        String subEquipIds = "(";
        for (int i = 0; i < subEquipments.size(); i++) {
            subEquipIds += subEquipments.get(i).SubEquipID.toString();
            if (i != subEquipments.size() - 1) {
                subEquipIds += ",";
            }
        }
        subEquipIds += ")";

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Items.TableName,
                Tbl_Items.EquipInfID,
                equipInfoIds,
                Tbl_Items.SubEquipID,
                subEquipIds
        };
        String strQuery = String.format("select items.* from %s items where items.%s IN %s AND items.%s IN %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItems item = new dtoItems();
                item.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.ItemInfID));
                item.ItemName = cur.getString(cur.getColumnIndex(Tbl_Items.ItemName));
                item.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_Items.SubEquipID));
                item.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.EquipInfID));
                item.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitInfID));
                item.PostID = cur.getInt(cur.getColumnIndex(Tbl_Items.PostID));
                item.RemGroupID = cur.getInt(cur.getColumnIndex(Tbl_Items.RemGroupID));
                item.AmountTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.AmountTypID));
                item.MeasureUnitName = cur.getString(cur.getColumnIndex(Tbl_Items.MeasureUnitName));
                item.Zarib = cur.getDouble((cur.getColumnIndex(Tbl_Items.Zarib)));
                item.MaxSampleNo = cur.getInt(cur.getColumnIndex(Tbl_Items.MaxSampleNo));
                item.MaxAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount1));
                item.MinAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount1));
                item.MaxAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount2));
                item.MinAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount2));
                item.MaxAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount3));
                item.MinAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount3));
                item.TagID = cur.getString(cur.getColumnIndex(Tbl_Items.TagID));
                item.RemTyp = cur.getInt(cur.getColumnIndex(Tbl_Items.RemTyp));
                item.STTime = cur.getString(cur.getColumnIndex(Tbl_Items.STTime));
                item.PeriodTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTime));
                item.PeriodTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTypTime));
                item.RangeTime = cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTime));
                item.RangeTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTypTime));
                item.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogicTypID));
                item.Desc = cur.getString(cur.getColumnIndex(Tbl_Items.Desc));
                item.LocateRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LocateRowNo));
                item.LogshitRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitRowNo));
                lstItems.add(item);
            } while (cur.moveToNext());
        }
        return lstItems;
    }

    public ArrayList<dtoItems> GetItemList(Integer selectedEquipId, Integer selectedSubEquipId) { //Handling Which user should see which logshit is inside the method
        ArrayList<dtoItems> lstItems = new ArrayList<dtoItems>();
        SQLiteDatabase sd = getReadableDatabase();

        Object[] args = new Object[]{
                Tbl_Items.TableName,
                Tbl_Items.EquipInfID,
                selectedEquipId,
                Tbl_Items.SubEquipID,
                selectedSubEquipId,
                Tbl_Items.PostID,
                G.selectedVahedId,
                Tbl_Items.LogshitInfID,
                G.selectedLogshitId
        };
        String strOrderBy="";
        if(G.sharedPref.getString("pref_items_order_type","1").compareTo("1")==0){
            strOrderBy = String.format(" ORDER BY %s ASC",Tbl_Items.LocateRowNo);
        }else if(G.sharedPref.getString("pref_items_order_type","1").compareTo("2")==0){
            strOrderBy = String.format(" ORDER BY %s DESC",Tbl_Items.LocateRowNo);
        }else if(G.sharedPref.getString("pref_items_order_type","1").compareTo("3")==0){
            strOrderBy = String.format(" ORDER BY %s ASC",Tbl_Items.LogshitRowNo);
        }else if(G.sharedPref.getString("pref_items_order_type","1").compareTo("4")==0){
            strOrderBy = String.format(" ORDER BY %s DESC",Tbl_Items.LogshitRowNo);
        }else if(G.sharedPref.getString("pref_items_order_type","1").compareTo("5")==0){
            strOrderBy = String.format(" ORDER BY %s ASC",Tbl_Items.ItemName);
        }else if(G.sharedPref.getString("pref_items_order_type","1").compareTo("6")==0){
            strOrderBy = String.format(" ORDER BY %s DESC",Tbl_Items.ItemName);
        }

        String strQuery = String.format("select items.* from %s items where items.%s = %s AND items.%s = %s  AND items.%s = %s  AND items.%s = %s"+strOrderBy, args);
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItems item = new dtoItems();
                item.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.ItemInfID));
                item.ItemName = cur.getString(cur.getColumnIndex(Tbl_Items.ItemName));
                item.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_Items.SubEquipID));
                item.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.EquipInfID));
                item.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitInfID));
                item.PostID = cur.getInt(cur.getColumnIndex(Tbl_Items.PostID));
                item.RemGroupID = cur.getInt(cur.getColumnIndex(Tbl_Items.RemGroupID));
                item.AmountTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.AmountTypID));
                item.MeasureUnitName = cur.getString(cur.getColumnIndex(Tbl_Items.MeasureUnitName));
                item.Zarib = cur.getDouble((cur.getColumnIndex(Tbl_Items.Zarib)));
                item.MaxSampleNo = cur.getInt(cur.getColumnIndex(Tbl_Items.MaxSampleNo));
                item.MaxAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount1));
                item.MinAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount1));
                item.MaxAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount2));
                item.MinAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount2));
                item.MaxAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount3));
                item.MinAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount3));
                item.TagID = cur.getString(cur.getColumnIndex(Tbl_Items.TagID));
                item.RemTyp = cur.getInt(cur.getColumnIndex(Tbl_Items.RemTyp));
                item.STTime = cur.getString(cur.getColumnIndex(Tbl_Items.STTime));
                item.PeriodTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTime));
                item.PeriodTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTypTime));
                item.RangeTime = cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTime));
                item.RangeTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTypTime));
                item.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogicTypID));
                item.Desc = cur.getString(cur.getColumnIndex(Tbl_Items.Desc));
                item.LocateRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LocateRowNo));
                item.LogshitRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitRowNo));
                lstItems.add(item);
            } while (cur.moveToNext());
        }
        return lstItems;
    }

    public ArrayList<dtoItems> GetItemList(Integer selectedEquipId, Integer selectedSubEquipId, Integer selectedItemInfId) { //Handling Which user should see which logshit is inside the method
        ArrayList<dtoItems> lstItems = new ArrayList<dtoItems>();
        SQLiteDatabase sd = getReadableDatabase();

        Object[] args = new Object[]{
                Tbl_Items.TableName,
                Tbl_Items.EquipInfID,
                selectedEquipId,
                Tbl_Items.SubEquipID,
                selectedSubEquipId,
                Tbl_Items.ItemInfID,
                selectedItemInfId
        };
        String strQuery = String.format("select items.* from %s items where items.%s = %s AND items.%s = %s AND  items.%s = %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItems item = new dtoItems();
                item.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.ItemInfID));
                item.ItemName = cur.getString(cur.getColumnIndex(Tbl_Items.ItemName));
                item.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_Items.SubEquipID));
                item.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.EquipInfID));
                item.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitInfID));
                item.PostID = cur.getInt(cur.getColumnIndex(Tbl_Items.PostID));
                item.RemGroupID = cur.getInt(cur.getColumnIndex(Tbl_Items.RemGroupID));
                item.AmountTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.AmountTypID));
                item.MeasureUnitName = cur.getString(cur.getColumnIndex(Tbl_Items.MeasureUnitName));
                item.Zarib = cur.getDouble((cur.getColumnIndex(Tbl_Items.Zarib)));
                item.MaxSampleNo = cur.getInt(cur.getColumnIndex(Tbl_Items.MaxSampleNo));
                item.MaxAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount1));
                item.MinAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount1));
                item.MaxAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount2));
                item.MinAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount2));
                item.MaxAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount3));
                item.MinAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount3));
                item.TagID = cur.getString(cur.getColumnIndex(Tbl_Items.TagID));
                item.RemTyp = cur.getInt(cur.getColumnIndex(Tbl_Items.RemTyp));
                item.STTime = cur.getString(cur.getColumnIndex(Tbl_Items.STTime));
                item.PeriodTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTime));
                item.PeriodTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTypTime));
                item.RangeTime = cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTime));
                item.RangeTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTypTime));
                item.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogicTypID));
                item.Desc = cur.getString(cur.getColumnIndex(Tbl_Items.Desc));
                item.LocateRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LocateRowNo));
                item.LogshitRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitRowNo));
                lstItems.add(item);
            } while (cur.moveToNext());
        }
        return lstItems;
    }

    public dtoItems GetItemByItemInfId(Integer itemInfoId) { //Handling Which user should see which logshit is inside the method
        dtoItems item = new dtoItems();
        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Items.TableName,
                Tbl_Items.ItemInfID,
                itemInfoId
        };
        String strQuery = String.format("select items.* from %s items where items.%s = %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                item.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.ItemInfID));
                item.ItemName = cur.getString(cur.getColumnIndex(Tbl_Items.ItemName));
                item.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_Items.SubEquipID));
                item.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.EquipInfID));
                item.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitInfID));
                item.PostID = cur.getInt(cur.getColumnIndex(Tbl_Items.PostID));
                item.RemGroupID = cur.getInt(cur.getColumnIndex(Tbl_Items.RemGroupID));
                item.AmountTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.AmountTypID));
                item.MeasureUnitName = cur.getString(cur.getColumnIndex(Tbl_Items.MeasureUnitName));
                item.Zarib = cur.getDouble((cur.getColumnIndex(Tbl_Items.Zarib)));
                item.MaxSampleNo = cur.getInt(cur.getColumnIndex(Tbl_Items.MaxSampleNo));
                item.MaxAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount1));
                item.MinAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount1));
                item.MaxAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount2));
                item.MinAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount2));
                item.MaxAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount3));
                item.MinAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount3));
                item.TagID = cur.getString(cur.getColumnIndex(Tbl_Items.TagID));
                item.RemTyp = cur.getInt(cur.getColumnIndex(Tbl_Items.RemTyp));
                item.STTime = cur.getString(cur.getColumnIndex(Tbl_Items.STTime));
                item.PeriodTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTime));
                item.PeriodTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTypTime));
                item.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogicTypID));
                item.Desc = cur.getString(cur.getColumnIndex(Tbl_Items.Desc));
                item.LocateRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LocateRowNo));
                item.LogshitRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitRowNo));
                item.RangeTime=cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTime));
                item.RangeTypTime=cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTypTime));
            } while (cur.moveToNext());
        }
        return item;
    }

    public dtoItems GetItemByPostId(Integer PostId,int usrID) { //Handling Which user should see which logshit is inside the method
        dtoItems item = new dtoItems();
        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Items.TableName,
                Tbl_ItemValues.TableName,
                Tbl_Items.PostID,
                PostId,
                Tbl_ItemValues.UsrID,
                usrID
        };
        String strQuery = String.format("select items.* from %s items inner join %s itemsVal on items.ItemInfID=itemsVal.ItemInfID  where items.%s = %s and itemsVal.%s = %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                item.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.ItemInfID));
                item.ItemName = cur.getString(cur.getColumnIndex(Tbl_Items.ItemName));
                item.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_Items.SubEquipID));
                item.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.EquipInfID));
                item.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitInfID));
                item.PostID = cur.getInt(cur.getColumnIndex(Tbl_Items.PostID));
                item.RemGroupID = cur.getInt(cur.getColumnIndex(Tbl_Items.RemGroupID));
                item.AmountTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.AmountTypID));
                item.MeasureUnitName = cur.getString(cur.getColumnIndex(Tbl_Items.MeasureUnitName));
                item.Zarib = cur.getDouble((cur.getColumnIndex(Tbl_Items.Zarib)));
                item.MaxSampleNo = cur.getInt(cur.getColumnIndex(Tbl_Items.MaxSampleNo));
                item.MaxAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount1));
                item.MinAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount1));
                item.MaxAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount2));
                item.MinAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount2));
                item.MaxAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount3));
                item.MinAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount3));
                item.TagID = cur.getString(cur.getColumnIndex(Tbl_Items.TagID));
                item.RemTyp = cur.getInt(cur.getColumnIndex(Tbl_Items.RemTyp));
                item.STTime = cur.getString(cur.getColumnIndex(Tbl_Items.STTime));
                item.PeriodTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTime));
                item.PeriodTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTypTime));
                item.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogicTypID));
                item.Desc = cur.getString(cur.getColumnIndex(Tbl_Items.Desc));
                item.LocateRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LocateRowNo));
                item.LogshitRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitRowNo));
                item.RangeTime=cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTime));
                item.RangeTypTime=cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTypTime));

            } while (cur.moveToNext());
        }
        return item;
    }

    public dtoItems GetItemBylogsheetId(Integer PostId,Integer LogsheetId,int usrID) { //Handling Which user should see which logshit is inside the method
        dtoItems item = new dtoItems();
        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Items.TableName,
                Tbl_ItemValues.TableName,
                Tbl_Items.PostID,
                PostId,
                Tbl_Items.LogshitInfID,
                LogsheetId,
                Tbl_ItemValues.UsrID,
                usrID
        };
        String strQuery = String.format("select items.* from %s items " +
                "inner join %s itemsVal on items.ItemInfID=itemsVal.ItemInfID where items.%s = %s And items.%s=%s and itemsVal.%s = %s", args);
        Cursor cur = sd.rawQuery(strQuery, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                item.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.ItemInfID));
                item.ItemName = cur.getString(cur.getColumnIndex(Tbl_Items.ItemName));
                item.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_Items.SubEquipID));
                item.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.EquipInfID));
                item.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitInfID));
                item.PostID = cur.getInt(cur.getColumnIndex(Tbl_Items.PostID));
                item.RemGroupID = cur.getInt(cur.getColumnIndex(Tbl_Items.RemGroupID));
                item.AmountTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.AmountTypID));
                item.MeasureUnitName = cur.getString(cur.getColumnIndex(Tbl_Items.MeasureUnitName));
                item.Zarib = cur.getDouble((cur.getColumnIndex(Tbl_Items.Zarib)));
                item.MaxSampleNo = cur.getInt(cur.getColumnIndex(Tbl_Items.MaxSampleNo));
                item.MaxAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount1));
                item.MinAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount1));
                item.MaxAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount2));
                item.MinAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount2));
                item.MaxAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount3));
                item.MinAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount3));
                item.TagID = cur.getString(cur.getColumnIndex(Tbl_Items.TagID));
                item.RemTyp = cur.getInt(cur.getColumnIndex(Tbl_Items.RemTyp));
                item.STTime = cur.getString(cur.getColumnIndex(Tbl_Items.STTime));
                item.PeriodTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTime));
                item.PeriodTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTypTime));
                item.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogicTypID));
                item.Desc = cur.getString(cur.getColumnIndex(Tbl_Items.Desc));
                item.LocateRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LocateRowNo));
                item.LogshitRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitRowNo));
                item.RangeTime=cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTime));
                item.RangeTypTime=cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTypTime));

            } while (cur.moveToNext());
        }
        return item;
    }

    public dtoItems GetItemByTajhiz(Integer LogsheetId,Integer Tajhizid,int usrID) { //Handling Which user should see which logshit is inside the method
        dtoItems item = new dtoItems();
        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Items.TableName,
                Tbl_ItemValues.TableName,
                Tbl_Items.LogshitInfID,
                LogsheetId,
                Tbl_Items.EquipInfID,
                Tajhizid,
                Tbl_ItemValues.UsrID,
                usrID

        };
        String strQuery = String.format("select items.* from %s items " +
                "inner join %s itemsVal on items.ItemInfID = itemsVal.ItemInfID" +
                "  where  items.%s=%s And items.%s=%s and itemsVal.%s = %s", args);
        Cursor cur = sd.rawQuery(strQuery, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                item.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.ItemInfID));
                item.ItemName = cur.getString(cur.getColumnIndex(Tbl_Items.ItemName));
                item.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_Items.SubEquipID));
                item.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.EquipInfID));
                item.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitInfID));
                item.PostID = cur.getInt(cur.getColumnIndex(Tbl_Items.PostID));
                item.RemGroupID = cur.getInt(cur.getColumnIndex(Tbl_Items.RemGroupID));
                item.AmountTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.AmountTypID));
                item.MeasureUnitName = cur.getString(cur.getColumnIndex(Tbl_Items.MeasureUnitName));
                item.Zarib = cur.getDouble((cur.getColumnIndex(Tbl_Items.Zarib)));
                item.MaxSampleNo = cur.getInt(cur.getColumnIndex(Tbl_Items.MaxSampleNo));
                item.MaxAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount1));
                item.MinAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount1));
                item.MaxAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount2));
                item.MinAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount2));
                item.MaxAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount3));
                item.MinAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount3));
                item.TagID = cur.getString(cur.getColumnIndex(Tbl_Items.TagID));
                item.RemTyp = cur.getInt(cur.getColumnIndex(Tbl_Items.RemTyp));
                item.STTime = cur.getString(cur.getColumnIndex(Tbl_Items.STTime));
                item.PeriodTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTime));
                item.PeriodTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTypTime));
                item.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogicTypID));
                item.Desc = cur.getString(cur.getColumnIndex(Tbl_Items.Desc));
                item.LocateRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LocateRowNo));
                item.LogshitRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitRowNo));
                item.RangeTime=cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTime));
                item.RangeTypTime=cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTypTime));

            } while (cur.moveToNext());
        }
        return item;
    }

    public dtoItems GetItemByZirTajhiz(Integer Tajhizid,Integer ZirTajhizId,int usrID) { //Handling Which user should see which logshit is inside the method
        dtoItems item = new dtoItems();
        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Items.TableName,
                Tbl_ItemValues.TableName,
                Tbl_Items.EquipInfID,
                Tajhizid,
                Tbl_Items.SubEquipID,
                ZirTajhizId,
                Tbl_ItemValues.UsrID,
                usrID
        };
        String strQuery = String.format("select items.* from %s items " +
                "inner join %s itemsVal on items.ItemInfID=itemsVal.ItemInfID  where items.%s = %s And items.%s=%s and itemsVal.%s = %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                item.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.ItemInfID));
                item.ItemName = cur.getString(cur.getColumnIndex(Tbl_Items.ItemName));
                item.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_Items.SubEquipID));
                item.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.EquipInfID));
                item.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitInfID));
                item.PostID = cur.getInt(cur.getColumnIndex(Tbl_Items.PostID));
                item.RemGroupID = cur.getInt(cur.getColumnIndex(Tbl_Items.RemGroupID));
                item.AmountTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.AmountTypID));
                item.MeasureUnitName = cur.getString(cur.getColumnIndex(Tbl_Items.MeasureUnitName));
                item.Zarib = cur.getDouble((cur.getColumnIndex(Tbl_Items.Zarib)));
                item.MaxSampleNo = cur.getInt(cur.getColumnIndex(Tbl_Items.MaxSampleNo));
                item.MaxAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount1));
                item.MinAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount1));
                item.MaxAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount2));
                item.MinAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount2));
                item.MaxAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount3));
                item.MinAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount3));
                item.TagID = cur.getString(cur.getColumnIndex(Tbl_Items.TagID));
                item.RemTyp = cur.getInt(cur.getColumnIndex(Tbl_Items.RemTyp));
                item.STTime = cur.getString(cur.getColumnIndex(Tbl_Items.STTime));
                item.PeriodTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTime));
                item.PeriodTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTypTime));
                item.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogicTypID));
                item.Desc = cur.getString(cur.getColumnIndex(Tbl_Items.Desc));
                item.LocateRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LocateRowNo));
                item.LogshitRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitRowNo));
                item.RangeTime=cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTime));
                item.RangeTypTime=cur.getInt(cur.getColumnIndex(Tbl_Items.RangeTypTime));

            } while (cur.moveToNext());
        }
        return item;
    }

    public dtoItems GetItemByTagId(String tagId) {
        dtoItems item = null;
        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Items.TableName,
                Tbl_LogShits.TableName,
                Tbl_Items.LogshitInfID,
                Tbl_LogShits.LogshitInfID,
                Tbl_UserLogshit.TableName,
                Tbl_LogShits.LogshitID,
                Tbl_UserLogshit.LogshitID,
                Tbl_Items.TagID,
                tagId.toUpperCase(),
                Tbl_UserLogshit.UsrID,
                G.currentUser.UsrID
        };
        String strQuery = String.format("select items.* from %s items inner join %s logshits on items.%s = logshits.%s inner join %s userLogshits on logshits.%s = userLogshits.%s where TRIM(items.%s) = '%s' AND userLogshits.%s = %s LIMIT 1;", args);
        //String strQuery = String.format("select items.* from %s items inner join %s logshits on items.%s = logshits.%s inner join %s userLogshits on logshits.%s = userLogshits.%s ORDER BY items.Id DESC ", args);
        Cursor cur = sd.rawQuery(strQuery, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                item = new dtoItems();
                item.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.ItemInfID));
                item.ItemName = cur.getString(cur.getColumnIndex(Tbl_Items.ItemName));
                item.SubEquipID = cur.getInt(cur.getColumnIndex(Tbl_Items.SubEquipID));
                item.EquipInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.EquipInfID));
                item.LogshitInfID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitInfID));
                item.PostID = cur.getInt(cur.getColumnIndex(Tbl_Items.PostID));
                item.RemGroupID = cur.getInt(cur.getColumnIndex(Tbl_Items.RemGroupID));
                item.AmountTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.AmountTypID));
                item.MeasureUnitName = cur.getString(cur.getColumnIndex(Tbl_Items.MeasureUnitName));
                item.Zarib = cur.getDouble((cur.getColumnIndex(Tbl_Items.Zarib)));
                item.MaxSampleNo = cur.getInt(cur.getColumnIndex(Tbl_Items.MaxSampleNo));
                item.MaxAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount1));
                item.MinAmount1 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount1));
                item.MaxAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount2));
                item.MinAmount2 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount2));
                item.MaxAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MaxAmount3));
                item.MinAmount3 = cur.getString(cur.getColumnIndex(Tbl_Items.MinAmount3));
                item.TagID = cur.getString(cur.getColumnIndex(Tbl_Items.TagID));
                item.RemTyp = cur.getInt(cur.getColumnIndex(Tbl_Items.RemTyp));
                item.STTime = cur.getString(cur.getColumnIndex(Tbl_Items.STTime));
                item.PeriodTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTime));
                item.PeriodTypTime = cur.getInt(cur.getColumnIndex(Tbl_Items.PeriodTypTime));
                item.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Items.LogicTypID));
                item.Desc = cur.getString(cur.getColumnIndex(Tbl_Items.Desc));
                item.LocateRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LocateRowNo));
                item.LogshitRowNo = cur.getInt(cur.getColumnIndex(Tbl_Items.LogshitRowNo));
            } while (cur.moveToNext());
        }
        return item;
    }

    public dtoLogics getLogicByLogicTypeId(Integer logicTypeId) {
        //ArrayList<dtoLogics> lstLogics = new ArrayList<dtoLogics>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Logics.TableName,
                Tbl_Logics.Id,
                logicTypeId
        };

        String strQuery = String.format("select logics.* from %s logics where logics.%s = %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);
        dtoLogics logic = null;
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                logic = new dtoLogics();
                logic.LogicTypID = cur.getInt(cur.getColumnIndex(Tbl_Logics.Id));
                logic.LogicVal1 = cur.getString(cur.getColumnIndex(Tbl_Logics.LogicVal1));
                logic.LogicVal2 = cur.getString(cur.getColumnIndex(Tbl_Logics.LogicVal2));
//				lstLogics.add(logic);
            } while (cur.moveToNext());
        }
        cur.close();
        return logic;
    }

    private String getDateForLineChart(String DateForDb){
        String mainDate="";

        if(DateForDb!=""){
            if(G.RTL){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    Date date = format.parse(DateForDb
                            .replace('/','-').replace(' ','T')+'Z');
                    String dateString=Tarikh.getShamsiDate(date).replace("/","").trim()+"-"+
                            String.valueOf(date.getHours())+String.valueOf(date.getMinutes())+String.valueOf(date.getSeconds());
                    mainDate=dateString;

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }



           }else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'");

               mainDate = DateForDb.replace("/", "" ).substring(0,9);
           }
        }

        return  mainDate;
    }
    public Map<String, Integer> GetDateAndCountMapByTopNumber(Integer topNum) { //Handling Which user should see which logshit is inside the method
        Map<String, Integer> mapDateCount = new HashMap<>();
        if (topNum == 0) {
            return mapDateCount;
        }

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.PDate,
                Tbl_ItemValues.TableName,
                Tbl_ItemValues.UsrID,
                G.currentUser.UsrID,
                Tbl_ItemValues.PDate,
                Tbl_ItemValues.PDate,
                topNum
        };
        String strQuery="";
        if (G.currentUser.IsManager == 1 || G.currentUser.UserGroupId==2) {
            strQuery = String.format("SELECT itemvals.PDate, COUNT(*) AS ItemCount FROM tbl_ItemValues itemvals    GROUP BY substr(itemvals.PDate,0,11) ORDER BY CAST(itemvals.PDate as INTEGER)  DESC  LIMIT 7;");
        }else {
             strQuery = String.format("SELECT itemvals.%s, COUNT(*) AS ItemCount FROM %s itemvals WHERE itemvals.%s=%s GROUP BY  substr(itemvals.%s,0,11) ORDER BY CAST(itemvals.%s as INTEGER)  DESC  LIMIT %s;", args);

        }
        Cursor cur = null;
        try {
            cur = sd.rawQuery(strQuery, null);

            if (cur.getCount() > 0) {
                cur.moveToFirst();
                do {

                    String strDate =getDateForLineChart( cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate)));
                    Integer itemCount = cur.getInt(cur.getColumnIndex("ItemCount"));
                    if(strDate.equals(null)==false && itemCount!=null) {
                        mapDateCount.put(strDate, itemCount);
                    }
                } while (cur.moveToNext());
            }
        } catch (Exception ex) {
            Log.i("tag", ex.getMessage());
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }
        return mapDateCount;
    }

    public ArrayList<dtoRemarks> getRemarksByRemarkGroupId(Integer remarkGroupId) {
        ArrayList<dtoRemarks> lstRemarks = new ArrayList<dtoRemarks>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Remarks.TableName,
                Tbl_CorsRems.TableName,
                Tbl_Remarks.Id,
                Tbl_CorsRems.RemID,
                Tbl_CorsRems.RemGrpID,
                remarkGroupId
        };

        String strQuery = String.format("select remarks.* from %s remarks inner join %s corsRems ON remarks.%s = corsRems.%s where corsRems.%s = %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoRemarks remark = new dtoRemarks();
                remark.RemID = cur.getInt(cur.getColumnIndex(Tbl_Remarks.Id));
                remark.RemName = cur.getString(cur.getColumnIndex(Tbl_Remarks.RemName));
                remark.Des = cur.getString(cur.getColumnIndex(Tbl_Remarks.Des));
                lstRemarks.add(remark);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstRemarks;
    }


    public ArrayList<dtoItemValues> getAllItemValues() { // for collecting itemvalues for send in ActivityDrawer TaskSend
        ArrayList<dtoItemValues> lstItemValues = new ArrayList<dtoItemValues>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.TableName,
                Tbl_ItemValues.IsSend,
                0
        };

        String strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s ", args);

        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItemValues itemValue = new dtoItemValues();
                itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
                itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
                itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
                itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
                itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
                itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
                itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
                itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                lstItemValues.add(itemValue);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }

    public ArrayList<dtoItemValues> getItemValuesByUserId(Integer UsrId) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        ArrayList<dtoItemValues> lstItemValues = new ArrayList<dtoItemValues>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.TableName,
                Tbl_ItemValues.UsrID,
                UsrId

        };

        String strQuery = String.format("select itemValues.* from %s itemValues  where itemValues.%s = %s   ", args);
        if (G.currentUser.IsManager == 1 || G.currentUser.UserGroupId==2) {

            //strQuery = "select itemValues.* from tbl_ItemValues itemValues where itemValues.IsSend = 0 ";
            //قرار داد فی ما بین محمدی و خانم رضایی شرط isend برداشته شود
            strQuery = "select itemValues.* from tbl_ItemValues itemValues ";
        }
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItemValues itemValue = new dtoItemValues();
                itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
                itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
                itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
                itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
                itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
                itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
                itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
                itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                lstItemValues.add(itemValue);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }

    public ArrayList<dtoItemValues> getItemValuesByUserIdDistinct(Integer UsrId) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        ArrayList<dtoItemValues> lstItemValues = new ArrayList<dtoItemValues>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.UsrID,
                Tbl_ItemValues.ItemInfID,
                Tbl_ItemValues.TableName,
                Tbl_ItemValues.UsrID,
                UsrId,
                Tbl_ItemValues.IsSend,
                0
        };

        String strQuery = String.format("select DISTINCT itemValues.%s,itemValues.%s from %s itemValues  where itemValues.%s = %s AND itemValues.%s = %s  ", args);
        if (G.currentUser.IsManager == 1) {
            strQuery = "select DISTINCT itemValues.UsrID,itemValues.ItemInfID from tbl_ItemValues itemValues where itemValues.IsSend = 0 ";
        }
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItemValues itemValue = new dtoItemValues();
                //itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
                //itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
                //itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
                //itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
                //itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
                itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
                //itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
                //itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                lstItemValues.add(itemValue);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }


    public ArrayList<dtoItemValues> getItemValuesByUserIdAndItemInfoId(Integer UsrId, Integer ItemInfoId) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        ArrayList<dtoItemValues> lstItemValues = new ArrayList<dtoItemValues>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.TableName,
                Tbl_ItemValues.UsrID,
                UsrId,
                Tbl_ItemValues.ItemInfID,
                ItemInfoId,
        };

        //String strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s AND itemValues.%s = %s AND itemValues.%s = %s  ", args);
        String strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s AND itemValues.%s = %s  ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItemValues itemValue = new dtoItemValues();
                itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
                itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
                itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
                itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
                itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
                itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
                itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
                itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                lstItemValues.add(itemValue);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }
    public ArrayList<dtoItemValues> getItemValuesByUserIdAndItemInfId(Integer UsrId, Integer itemInfID) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        ArrayList<dtoItemValues> lstItemValues = new ArrayList<dtoItemValues>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.TableName,
                Tbl_ItemValues.UsrID,
                UsrId,
                Tbl_ItemValues.ItemInfID,
                itemInfID,
        };

        //String strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s AND itemValues.%s = %s AND itemValues.%s = %s  ", args);
        String strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s AND itemValues.%s = %s  ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItemValues itemValue = new dtoItemValues();
                itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
                itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
                itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
                itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
                itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
                itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
                itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
                itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                lstItemValues.add(itemValue);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }
    public ArrayList<dtoItemValues> getItemValuesByItemInfoIdHistory( Integer itemInfID) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        ArrayList<dtoItemValues> lstItemValues = new ArrayList<dtoItemValues>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.TableName,
                Tbl_ItemValues.ItemInfID,
                itemInfID
        };
        String strQuery=String.format("select itemValues.* from %s itemValues where itemValues.%s = %s ", args)+" and itemValues.UsrID="+G.currentUser.UsrID.toString();
        if(G.currentUser.UserGroupId==2 || G.currentUser.IsManager==1 ){
             strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s ", args);
        }
        //String strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s AND itemValues.%s = %s AND itemValues.%s = %s  ", args);


        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItemValues itemValue = new dtoItemValues();
                itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
                itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
                itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
                itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
                itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
                itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
                itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
                itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                lstItemValues.add(itemValue);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }



    public dtoItemValues getLastItemValueByItemInfId(Integer itemInfID) { // for collecting itemvalues for send in ActivityDrawer TaskSend
        dtoItemValues itemValue = null;
        try {


            SQLiteDatabase sd = getReadableDatabase();
            Object[] args = new Object[]{
                    Tbl_ItemValues.TableName,
                    Tbl_ItemValues.ItemInfID,
                    itemInfID,
                    Tbl_ItemValues.IsSend,
                    0,
                    Tbl_ItemValues.UsrID,
                    G.currentUser.UsrID,
                    Tbl_ItemValues.Id

            };

            String strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s AND itemValues.%s = %s AND itemValues.%s = %s ORDER BY %s DESC LIMIT 1; ", args);
            if(G.currentUser.UserGroupId==2 || G.currentUser.IsManager ==1) {
                strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s AND itemValues.%s = %s ORDER BY %s DESC LIMIT 1; ", args);
            }
            Cursor cur = sd.rawQuery(strQuery, null);

            if (cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    itemValue = new dtoItemValues();
                    itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                    itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
                    itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
                    itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
                    itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
                    itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
                    itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
                    itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
                    itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                    itemValue.VideoPath=cur.getString(cur.getColumnIndex(Tbl_ItemValues.VideoPath));
                    itemValue.ImagePath=cur.getString(cur.getColumnIndex(Tbl_ItemValues.ImagePath));
                    itemValue.VoicePath=cur.getString(cur.getColumnIndex(Tbl_ItemValues.VoicePath));
                    break;
                } while (cur.moveToNext());
            }
            cur.close();
        } catch (Exception ex) {
            Log.i("tag", ex.getMessage());
        }
        return itemValue;
    }

    public ArrayList<dtoItemValues> getItemValuesByItemInfoId(Integer itemInfoId) {
        ArrayList<dtoItemValues> lstItemValues = new ArrayList<dtoItemValues>();

        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemValues.TableName,
                Tbl_ItemValues.ItemInfID,
                itemInfoId
//                Tbl_ItemValues.IsSend,
//                0
        };

        String strQuery = String.format("select itemValues.* from %s itemValues where itemValues.%s = %s   ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                dtoItemValues itemValue = new dtoItemValues();
                itemValue.Id = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.Id));
                itemValue.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ItemInfID));
                itemValue.ItemVal = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemVal));
                itemValue.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_ItemValues.ItemValTyp));
                itemValue.PDate = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PDate));
                itemValue.PTime = cur.getString(cur.getColumnIndex(Tbl_ItemValues.PTime));
                itemValue.UsrID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.UsrID));
                itemValue.ShiftID = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.ShiftID));
                itemValue.IsSend = cur.getInt(cur.getColumnIndex(Tbl_ItemValues.IsSend));
                lstItemValues.add(itemValue);
            } while (cur.moveToNext());
        }
        cur.close();
        return lstItemValues;
    }

    public dtoItemFormulas getItemFormula(Integer itemInfoId) {
        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_ItemFormulas.TableName,
                Tbl_ItemFormulas.ItemInfID,
                itemInfoId
        };

        String strQuery = String.format("select itemfurmulas.* from %s itemfurmulas where itemfurmulas.%s = %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        dtoItemFormulas itemFormula = null;
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                itemFormula = new dtoItemFormulas();
                itemFormula.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_ItemFormulas.ItemInfID));
                itemFormula.FormulaID = cur.getInt(cur.getColumnIndex(Tbl_ItemFormulas.FormulaID));
                itemFormula.ItemSelect = cur.getString(cur.getColumnIndex(Tbl_ItemFormulas.ItemSelect));
                itemFormula.constantNumber = cur.getInt(cur.getColumnIndex(Tbl_ItemFormulas.constantNumber));
            } while (cur.moveToNext());
        }
        cur.close();
        return itemFormula;
    }

    public dtoFormulas getFormula(Integer formulId) {
        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_Formulas.TableName,
                Tbl_Formulas.Id,
                formulId
        };

        String strQuery = String.format("select furmulas.* from %s furmulas where furmulas.%s = %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        dtoFormulas formula = null;
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                formula = new dtoFormulas();
                formula.FormulID = cur.getInt(cur.getColumnIndex(Tbl_Formulas.Id));
                formula.FormulName = cur.getString(cur.getColumnIndex(Tbl_Formulas.FormulName));
            } while (cur.moveToNext());
        }
        cur.close();
        return formula;
    }

    public dtoMaxItemVal getLastValueByItemInfoId(Integer itemInfoId) {
        SQLiteDatabase sd = getReadableDatabase();
        Object[] args = new Object[]{
                Tbl_MaxItemVal.TableName,
                Tbl_MaxItemVal.ItemInfID,
                itemInfoId
        };

        String strQuery = String.format("select lastValues.* from %s lastValues where lastValues.%s = %s ", args);
        Cursor cur = sd.rawQuery(strQuery, null);

        dtoMaxItemVal maxItemVal = null;
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                maxItemVal = new dtoMaxItemVal();
                maxItemVal.ItemInfID = cur.getInt(cur.getColumnIndex(Tbl_MaxItemVal.ItemInfID));
                maxItemVal.ItemVal = cur.getString(cur.getColumnIndex(Tbl_MaxItemVal.ItemVal));
                maxItemVal.RemAns = cur.getString(cur.getColumnIndex(Tbl_MaxItemVal.RemAns));
                maxItemVal.ItemValTyp = cur.getString(cur.getColumnIndex(Tbl_MaxItemVal.ItemValTyp));
            } while (cur.moveToNext());
        }
        cur.close();
        return maxItemVal;
    }

    //-----------------------------Validations-----------------------------------
    public boolean IsLoginOk(String Username, String Password) {
        boolean res = false;
        String[] cols = new String[]{
                Tbl_Users.UserName,
                Tbl_Users.pass
        };
        String[] args = new String[]{
                Username.toLowerCase(),
                Password
        };
        SQLiteDatabase sd = getReadableDatabase();
        Cursor cur = sd.query(Tbl_Users.TableName, cols, "LOWER(" + Tbl_Users.UserName + ")=? and " + Tbl_Users.pass + "=?", args, null, null, null);
        //Cursor cur = sd.query(Tbl_Users.TableName, cols, "LOWER(" + Tbl_Users.UserName + ")=? and " + Tbl_Users.pass + "=?", args, null, null, null);
        if (cur.getCount() > 0) {
            res = true;
        }

        return res;
    }

    public int GetSavedItemCount(int postID,int logshitID,int equipID,int subEquipID,int itemInfID){

        String query = String.format("SELECT \n" +
                "tbl_ItemRanges.ItemInfID,\n" +
                "tbl_ItemRanges.ItemBaseRangeMin,\n" +
                "ABS(SaveDateTimeToMin - (BaseRange+ItemBaseRangeMin)) SaveRanges,\n" +
                "ABS(%s - (BaseRange+ItemBaseRangeMin)) CurRanges,\n" +
                "CASE RangeTypTime WHEN 1 THEN RangeTime*60 WHEN 3 THEN RangeTime*24*60 ELSE RangeTime END RangeTime\n" +
                "from tbl_ItemValues \n" +
                "inner join tbl_Items on tbl_ItemValues.ItemInfID = tbl_Items.ItemInfID  \n" +
                "inner join tbl_ItemRanges on tbl_ItemRanges.ItemInfID = tbl_ItemValues.ItemInfID \n" +
                "WHERE 1=1   ",Tarikh.getCurrentDateToMinute());
        if(G.currentUser.IsManager!=1 && G.currentUser.UserGroupId!=2 ){
            query+=String.format(" and tbl_ItemValues.UsrID=%s",G.currentUser.UsrID.toString());
        }

        if(postID>-1) query += String.format( " AND tbl_Items.PostID=%s",String.valueOf(postID));
        if(logshitID>-1) query += String.format( " AND tbl_Items.LogshitInfID=%s",String.valueOf(logshitID));
        if(equipID>-1) query += String.format( " AND tbl_Items.EquipInfID=%s",String.valueOf(equipID));
        if(subEquipID>-1) query += String.format( " AND tbl_Items.SubEquipID=%s",String.valueOf(subEquipID));
        if(itemInfID>-1) query += String.format( " AND tbl_Items.ItemInfID=%s",String.valueOf(itemInfID));

        SQLiteDatabase sd = getWritableDatabase();
        Cursor cursor = sd.rawQuery(String.format("SELECT tbl.ItemInfID,count(tbl.ItemInfID) ItemCount FROM (%s) tbl where SaveRanges <= RangeTime AND CurRanges<=RangeTime", query),null);

        int itemCount = 0;
        if(cursor.moveToFirst()){
            itemCount = cursor.getInt(cursor.getColumnIndex("ItemCount"));
        }

        return itemCount;
    }
    public boolean InsertItemRanges(dtoItemRanges itemRange) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_ItemRanges.ItemInfID, itemRange.ItemInfID);
        cv.put(Tbl_ItemRanges.ItemBaseRangeMin, itemRange.ItemBaseRangesMin);


        int count = (int) sd.insert(Tbl_ItemRanges.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }
    public boolean HasItemValuesAnyRecords() {
        boolean res = false;

        SQLiteDatabase sd = getReadableDatabase();
        Cursor cur = sd.query(Tbl_ItemValues.TableName, null, null, null, null, null, null);
        if (cur.getCount() > 0) {
            res = true;
        }
        return res;
    }

    public boolean HasUsersAnyRecords(SQLiteDatabase sd) {
        boolean res = false;

        if (sd == null) {
            sd = getReadableDatabase();
        }
        Cursor cur = sd.query(Tbl_Users.TableName, null, null, null, null, null, null);
        if (cur.getCount() > 0) {
            res = true;
        }
        return res;
    }

    //-------------------------Truncate Operations-------------------------------------
    public void TruncatePackUser() {

        SQLiteDatabase sd = getWritableDatabase();

        sd.execSQL("delete from " + Tbl_Users.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_Shifts.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_UserShift.TableName + " ;vacuum;");
        Log.i("tag", "db truncate packuser");
        //sd.close();
    }

    public void TruncatePackItems() {

        SQLiteDatabase sd = getWritableDatabase();

        sd.execSQL("delete from " + Tbl_Posts.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_LogShits.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_Equipments.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_SubEquipments.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_Items.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_Logics.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_Remarks.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_ItemTypeValues.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_Measures.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_Formulas.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_ItemFormulas.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_RemarkGroup.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_UserLogshit.TableName + " ;vacuum;");
        sd.execSQL("delete from " + Tbl_CorsRems.TableName + " ;vacuum;");

        Log.i("tag", "db TruncatePackItems");
        //sd.close();
    }

    public void TruncateItemValues() {

        SQLiteDatabase sd = getWritableDatabase();

        if(G.RTL) {
            if(G.currentUser.UserGroupId==2 || G.currentUser.IsManager==1){
                sd.execSQL("delete from " + Tbl_ItemValues.TableName  + " ;vacuum;");
                return;
            }else{
                sd.execSQL("delete from " + Tbl_ItemValues.TableName + " where UsrID="+  G.currentUser.UsrID.toString() + " ;vacuum;");
            }
        }else {

            if(G.currentUser.UserGroupId==2 || G.currentUser.IsManager==1){
                sd.execSQL("delete from " + Tbl_ItemValues.TableName  + " ;vacuum;");
                return;
            }else{
                sd.execSQL("delete from " + Tbl_ItemValues.TableName + " where UsrID="+  G.currentUser.UsrID.toString() + " ;vacuum;");
            }
        }

        Log.i("tag", "db TruncateItemValues");
        //sd.close();
    }

    public void TruncateSetting() {
        SQLiteDatabase sd = getWritableDatabase();
        sd.execSQL("delete from " + Tbl_Setting.TableName + " ;vacuum;");
        Log.i("tag", "db truncate Tbl_Setting");
    }

    public void TruncateMaxItemVal() {
        SQLiteDatabase sd = getWritableDatabase();
        sd.execSQL("delete from " + Tbl_MaxItemVal.TableName + " ;vacuum;");
        Log.i("tag", "db Truncate Tbl_MaxItemVal");
    }
//---------------------------Insert Operations----------------------------------

    public boolean InsertUser(dtoUsers user, SQLiteDatabase db) {
        boolean res = false;
        if (db == null) {
            db = getWritableDatabase();
        }
        ContentValues cv = new ContentValues();
        cv.put(Tbl_Users.Id, user.UsrID);
        cv.put(Tbl_Users.FirstName, user.FirstName);
        cv.put(Tbl_Users.LastName, user.LastName);
        cv.put(Tbl_Users.UserName, user.UserName);
        cv.put(Tbl_Users.FullName, user.FullName);
        cv.put(Tbl_Users.pass, user.pass);
        cv.put(Tbl_Users.IsManager, user.IsManager);
        cv.put(Tbl_Users.shiftName, user.shiftName);
        cv.put(Tbl_Users.UserTypeID, user.UserTypeID);
        cv.put(Tbl_Users.ShiftID, user.ShiftID);
        cv.put(Tbl_Users.UserCode, user.UserCode);
        cv.put(Tbl_Users.UserGroupId,user.UserGroupId);
        try {
            int count = (int) db.insertOrThrow(Tbl_Users.TableName, null, cv);

            //sd.close();
            if (count > 0) {
                res = true;
            }
        } catch (SQLException ex) {
            Log.i("db", ex.getMessage());
        }
        return res;
    }

    public boolean InsertUser(dtoUsers user) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_Users.Id, user.UsrID);
        cv.put(Tbl_Users.FirstName, user.FirstName);
        cv.put(Tbl_Users.LastName, user.LastName);
        cv.put(Tbl_Users.UserName, user.UserName);
        cv.put(Tbl_Users.FullName, user.FullName);
        cv.put(Tbl_Users.UserCode, user.UserCode);
        cv.put(Tbl_Users.ShiftID, user.ShiftID);
        cv.put(Tbl_Users.pass, user.pass);
        cv.put(Tbl_Users.UserTypeID, user.UserTypeID);
        cv.put(Tbl_Users.shiftName, user.shiftName);
        cv.put(Tbl_Users.IsManager, user.IsManager);
        cv.put(Tbl_Users.NeedTag, user.NeedTag);
        cv.put(Tbl_Users.UserGroupId,user.UserGroupId);

        int count = (int) sd.insert(Tbl_Users.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertShift(dtoShifts shift) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_Shifts.Id, shift.ID);
        cv.put(Tbl_Shifts.Name, shift.Name);
        cv.put(Tbl_Shifts.Description, shift.Description);

        int count = (int) sd.insert(Tbl_Shifts.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertUserShift(dtoUserShift userShift) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_UserShift.UserID, userShift.UserID);
        cv.put(Tbl_UserShift.ShiftID, userShift.ShiftID);

        int count = (int) sd.insert(Tbl_UserShift.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertSetting(dtoSetting setting) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_Setting.Range1, setting.Range1);
        cv.put(Tbl_Setting.Range2, setting.Range2);
        cv.put(Tbl_Setting.Range3, setting.Range3);
        cv.put(Tbl_Setting.Dsc1, setting.Dsc1);
        cv.put(Tbl_Setting.Dsc2, setting.Dsc2);
        cv.put(Tbl_Setting.Dsc3, setting.Dsc3);
        cv.put(Tbl_Setting.BackligthTime, setting.BackligthTime);
        cv.put(Tbl_Setting.ContorolTime, setting.ContorolTime);
        cv.put(Tbl_Setting.ModTag, setting.ModTag);
        cv.put(Tbl_Setting.LayerTag, setting.LayerTag);
        cv.put(Tbl_Setting.SendItem, setting.SendItem);
        cv.put(Tbl_Setting.ShowEmp, setting.ShowEmp);
        cv.put(Tbl_Setting.ShowLastData, setting.ShowLastData);
        cv.put(Tbl_Setting.NoCheckMaxData, setting.NoCheckMaxData);
        cv.put(Tbl_Setting.UseSetUserTime, setting.UseSetUserTime);

        int count = (int) sd.insert(Tbl_Setting.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    //----PackItems Insert----
    public boolean InsertItemValues(dtoItemValues itemValues) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_ItemValues.ItemInfID, itemValues.ItemInfID);
        cv.put(Tbl_ItemValues.ItemVal, G.convertToEnglishDigits(itemValues.ItemVal,false));
        cv.put(Tbl_ItemValues.ItemValTyp, itemValues.ItemValTyp);
        cv.put(Tbl_ItemValues.PDate, itemValues.PDate);
        cv.put(Tbl_ItemValues.PTime, itemValues.PTime);
        cv.put(Tbl_ItemValues.UsrID, itemValues.UsrID);
        cv.put(Tbl_ItemValues.ShiftID, itemValues.ShiftID);
        cv.put(Tbl_ItemValues.IsSend, itemValues.IsSend);
        if(G.RTL==false) {
            cv.put(Tbl_ItemValues.VideoPath, itemValues.VideoPath);
            cv.put(Tbl_ItemValues.ImagePath, itemValues.ImagePath);
            cv.put(Tbl_ItemValues.VoicePath, itemValues.VoicePath);
        }
        cv.put(Tbl_ItemValues.BaseRange, Tarikh.persianStartDayToMinute(itemValues.PDate.length()>=10?
                itemValues.PDate.substring(0,10) : itemValues.PDate));
        cv.put(Tbl_ItemValues.SaveDateTimeToMin, Tarikh.persianDateTimeToMinute(itemValues.PDate,itemValues.PTime));
        int count = (int) sd.insert(Tbl_ItemValues.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertPost(dtoPosts post) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_Posts.Id, post.PostID);
        cv.put(Tbl_Posts.PostName, post.PostName);
        cv.put(Tbl_Posts.Des, post.Des);

        int count = (int) sd.insert(Tbl_Posts.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertLogshit(dtoLogShits logshit) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_LogShits.LogshitInfID, logshit.LogshitInfID);
        cv.put(Tbl_LogShits.LogshitID, logshit.LogshitID);
        cv.put(Tbl_LogShits.LogshitName, logshit.LogshitName);
        cv.put(Tbl_LogShits.PostID, logshit.PostID);
        cv.put(Tbl_LogShits.Ver, logshit.Ver==null?"":logshit.Ver);
        cv.put(Tbl_LogShits.TagID, logshit.TagID==null?"":logshit.TagID.trim());
        cv.put(Tbl_LogShits.Des, logshit.Des==null?"":logshit.Des);//change1

        int count = (int) sd.insert(Tbl_LogShits.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertEquipment(dtoEquipments equipment) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_Equipments.EquipInfoID, equipment.EquipInfoID);
        cv.put(Tbl_Equipments.EquipName, equipment.EquipName);
        cv.put(Tbl_Equipments.LogshitInfID, equipment.LogshitInfID);
        cv.put(Tbl_Equipments.LocationID, equipment.LocationID);
        cv.put(Tbl_Equipments.EquipNo, equipment.EquipNo);
        cv.put(Tbl_Equipments.EquipTypID, equipment.EquipTypID);
        cv.put(Tbl_Equipments.Des, equipment.Des);//change1

        int count = (int) sd.insert(Tbl_Equipments.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertSubEquipment(dtoSubEquipments subEquipment) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_SubEquipments.SubEquipID, subEquipment.SubEquipID);
        cv.put(Tbl_SubEquipments.SubEquipName, subEquipment.SubEquipName);
        cv.put(Tbl_SubEquipments.EquipInfID, subEquipment.EquipInfID);
        cv.put(Tbl_SubEquipments.Des, subEquipment.Des);//change1

        int count = (int) sd.insert(Tbl_SubEquipments.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertItem(dtoItems item) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_Items.ItemInfID, item.ItemInfID);
        cv.put(Tbl_Items.ItemName, item.ItemName);
        cv.put(Tbl_Items.SubEquipID, item.SubEquipID);
        cv.put(Tbl_Items.EquipInfID, item.EquipInfID);
        cv.put(Tbl_Items.LogshitInfID, item.LogshitInfID);
        cv.put(Tbl_Items.PostID, item.PostID);
        cv.put(Tbl_Items.RemGroupID, item.RemGroupID);
        cv.put(Tbl_Items.AmountTypID, item.AmountTypID);
        cv.put(Tbl_Items.MeasureUnitName, item.MeasureUnitName);
        cv.put(Tbl_Items.Zarib, item.Zarib);
        cv.put(Tbl_Items.MaxSampleNo, item.MaxSampleNo);
        cv.put(Tbl_Items.MaxAmount1, item.MaxAmount1);
        cv.put(Tbl_Items.MinAmount1, item.MinAmount1);
        cv.put(Tbl_Items.MaxAmount2, item.MaxAmount2);
        cv.put(Tbl_Items.MinAmount2, item.MinAmount2);
        cv.put(Tbl_Items.MaxAmount3, item.MaxAmount3);
        cv.put(Tbl_Items.MinAmount3, item.MinAmount3);
        cv.put(Tbl_Items.TagID, item.TagID.trim());
        cv.put(Tbl_Items.RemTyp, item.RemTyp);
        cv.put(Tbl_Items.STTime, item.STTime);
        cv.put(Tbl_Items.PeriodTime, item.PeriodTime);
        cv.put(Tbl_Items.PeriodTypTime, item.PeriodTypTime);
        cv.put(Tbl_Items.LogicTypID, item.LogicTypID);
        cv.put(Tbl_Items.Desc, item.Desc);
        cv.put(Tbl_Items.RangeTime, item.RangeTime);
        cv.put(Tbl_Items.RangeTypTime, item.RangeTypTime);
        cv.put(Tbl_Items.LocateRowNo, item.LocateRowNo);
        cv.put(Tbl_Items.LogshitRowNo, item.LogshitRowNo);

        int count = (int) sd.insert(Tbl_Items.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertLogic(dtoLogics logic) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_Logics.Id, logic.LogicTypID);
        cv.put(Tbl_Logics.LogicVal1, logic.LogicVal1);
        cv.put(Tbl_Logics.LogicVal2, logic.LogicVal2);

        int count = (int) sd.insert(Tbl_Logics.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertRemark(dtoRemarks remark) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_Remarks.Id, remark.RemID);
        cv.put(Tbl_Remarks.RemName, remark.RemName);
        cv.put(Tbl_Remarks.Des, remark.Des);

        int count = (int) sd.insert(Tbl_Remarks.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertItemTypeValue(dtoItemTypeValues itemTypeValue) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_ItemTypeValues.Id, itemTypeValue.ItemTypValID);
        cv.put(Tbl_ItemTypeValues.ItemTypValName, itemTypeValue.ItemTypValName);
        cv.put(Tbl_ItemTypeValues.Des, itemTypeValue.Des);

        int count = (int) sd.insert(Tbl_ItemTypeValues.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertMeasure(dtoMeasures measure) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_Measures.Id, measure.MeasureID);
        cv.put(Tbl_Measures.MeasureName, measure.MeasureName);
        cv.put(Tbl_Measures.Des, measure.Des);

        int count = (int) sd.insert(Tbl_Measures.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertFormula(dtoFormulas formula) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_Formulas.Id, formula.FormulID);
        cv.put(Tbl_Formulas.FormulName, formula.FormulName);

        int count = (int) sd.insert(Tbl_Formulas.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertItemFormula(dtoItemFormulas itemFormula) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_ItemFormulas.ItemInfID, itemFormula.ItemInfID);
        cv.put(Tbl_ItemFormulas.FormulaID, itemFormula.FormulaID);
        cv.put(Tbl_ItemFormulas.ItemSelect, itemFormula.ItemSelect);
        cv.put(Tbl_ItemFormulas.constantNumber, itemFormula.constantNumber);

        int count = (int) sd.insert(Tbl_ItemFormulas.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertRemarkGroup(dtoRemarkGroup remarkGroup) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_RemarkGroup.Id, remarkGroup.RemGrpID);
        cv.put(Tbl_RemarkGroup.RemGrpName, remarkGroup.RemGrpName);
        cv.put(Tbl_RemarkGroup.Des, remarkGroup.Des);

        int count = (int) sd.insert(Tbl_RemarkGroup.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertUserLogshit(dtoRelateUserLogshitTbl userLogshit) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_UserLogshit.UsrID, userLogshit.UsrID);
        cv.put(Tbl_UserLogshit.LogshitID, userLogshit.LogshitID);

        int count = (int) sd.insert(Tbl_UserLogshit.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertCorsRems(dtoCorsRems corsRems) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_CorsRems.Id, corsRems.CorRemID);
        cv.put(Tbl_CorsRems.RemGrpID, corsRems.RemGrpID);
        cv.put(Tbl_CorsRems.RemID, corsRems.RemID);

        int count = (int) sd.insert(Tbl_CorsRems.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }

    public boolean InsertUsrPost(dtoUsrPost usrPost) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_UsrPost.PostID, usrPost.PostID);
        cv.put(Tbl_UsrPost.UsrID, usrPost.UsrID);

        int count = (int) sd.insert(Tbl_UsrPost.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }
//------MaxItemVal-------

    public boolean InsertMaxItemVal(dtoMaxItemVal maxItemVal) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Tbl_MaxItemVal.Id, maxItemVal.Radif);
        cv.put(Tbl_MaxItemVal.ItemInfID, maxItemVal.ItemInfID);
        cv.put(Tbl_MaxItemVal.ItemVal, maxItemVal.ItemVal);
        cv.put(Tbl_MaxItemVal.RemAns, maxItemVal.RemAns);
        cv.put(Tbl_MaxItemVal.ItemValTyp, maxItemVal.ItemValTyp);

        int count = (int) sd.insert(Tbl_MaxItemVal.TableName, null, cv);
        //sd.close();
        if (count > 0) {
            res = true;
        }
        return res;
    }
//-----------------Update---------------------

    public boolean UpdateItemValueById(Integer id, dtoItemValues itemValue) {
        boolean res = false;

        SQLiteDatabase sd = getWritableDatabase();
        String[] arg = new String[]{
                String.valueOf(id)
        };

        ContentValues cv = new ContentValues();
        cv.put(Tbl_ItemValues.ItemInfID, itemValue.ItemInfID);
        cv.put(Tbl_ItemValues.ItemVal, itemValue.ItemVal);
        cv.put(Tbl_ItemValues.ItemValTyp, itemValue.ItemValTyp);
        cv.put(Tbl_ItemValues.PDate, itemValue.PDate);
        cv.put(Tbl_ItemValues.PTime, itemValue.PTime);
        cv.put(Tbl_ItemValues.UsrID, itemValue.UsrID);
        cv.put(Tbl_ItemValues.ShiftID, itemValue.ShiftID);
        cv.put(Tbl_ItemValues.IsSend, itemValue.IsSend);

        int count = sd.update(Tbl_ItemValues.TableName, cv, Tbl_ItemValues.Id + "=?", arg);
        //sd.close();
        if (count > 0) {
            res = true;
        }

        return res;
    }


}
