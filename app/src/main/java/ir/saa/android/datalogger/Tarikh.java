package ir.saa.android.datalogger;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import mycomponents.JalaliCalendar;
import mycomponents.MyUtilities;
import saman.zamani.persiandate.PersianDate;


public class Tarikh {

    public class SolarCalendar {

        public String strWeekDay = "";
        public String strMonth   = "";

        int           date;
        int           day;
        int           month;
        int           year;


        public SolarCalendar()
        {
            Date MiladiDate = new Date();
            calcSolarCalendar(MiladiDate);
        }


        public SolarCalendar(Date MiladiDate)
        {
            calcSolarCalendar(MiladiDate);
        }


        private void calcSolarCalendar(Date MiladiDate) {

            int ld;

            int miladiYear = MiladiDate.getYear() + 1900;
            int miladiMonth = MiladiDate.getMonth() + 1;
            int miladiDate = MiladiDate.getDate();
            int WeekDay = MiladiDate.getDay();

            int[] buf1 = new int[12];
            int[] buf2 = new int[12];

            buf1[0] = 0;
            buf1[1] = 31;
            buf1[2] = 59;
            buf1[3] = 90;
            buf1[4] = 120;
            buf1[5] = 151;
            buf1[6] = 181;
            buf1[7] = 212;
            buf1[8] = 243;
            buf1[9] = 273;
            buf1[10] = 304;
            buf1[11] = 334;

            buf2[0] = 0;
            buf2[1] = 31;
            buf2[2] = 60;
            buf2[3] = 91;
            buf2[4] = 121;
            buf2[5] = 152;
            buf2[6] = 182;
            buf2[7] = 213;
            buf2[8] = 244;
            buf2[9] = 274;
            buf2[10] = 305;
            buf2[11] = 335;

            if ((miladiYear % 4) != 0) {
                date = buf1[miladiMonth - 1] + miladiDate;

                if (date > 79) {
                    date = date - 79;
                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = date / 31;
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                } else {
                    if ((miladiYear > 1996) && (miladiYear % 4) == 1) {
                        ld = 11;
                    } else {
                        ld = 10;
                    }
                    date = date + ld;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }
            } else {
                date = buf2[miladiMonth - 1] + miladiDate;

                if (miladiYear >= 1996) {
                    ld = 79;
                } else {
                    ld = 80;
                }
                if (date > ld) {
                    date = date - ld;

                    if (date <= 186) {
                        switch (date % 31) {
                            case 0:
                                month = (date / 31);
                                date = 31;
                                break;
                            default:
                                month = (date / 31) + 1;
                                date = (date % 31);
                                break;
                        }
                        year = miladiYear - 621;
                    } else {
                        date = date - 186;

                        switch (date % 30) {
                            case 0:
                                month = (date / 30) + 6;
                                date = 30;
                                break;
                            default:
                                month = (date / 30) + 7;
                                date = (date % 30);
                                break;
                        }
                        year = miladiYear - 621;
                    }
                }

                else {
                    date = date + 10;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 9;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 10;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 622;
                }

            }

            switch (month) {
                case 1:
                    strMonth = (String) G.context.getText(R.string.Title_Farvardin);
                    break;
                case 2:
                    strMonth = (String) G.context.getText(R.string.Title_Ordibehesht);
                    break;
                case 3:
                    strMonth = (String) G.context.getText(R.string.Title_Khordad);
                    break;
                case 4:
                    strMonth = (String) G.context.getText(R.string.Title_Tir);
                    break;
                case 5:
                    strMonth = (String) G.context.getText(R.string.Title_Mordad);
                    break;
                case 6:
                    strMonth = (String) G.context.getText(R.string.Title_Sharivar);
                    break;
                case 7:
                    strMonth = (String) G.context.getText(R.string.Title_Mehr);
                    break;
                case 8:
                    strMonth = (String) G.context.getText(R.string.Title_Aban);
                    break;
                case 9:
                    strMonth = (String) G.context.getText(R.string.Title_Azar);
                    break;
                case 10:
                    strMonth = (String) G.context.getText(R.string.Title_Dey);
                    break;
                case 11:
                    strMonth = (String) G.context.getText(R.string.Title_Bahman);
                    break;
                case 12:
                    strMonth = (String) G.context.getText(R.string.Title_Esfand);
                    break;
            }

            switch (WeekDay) {

                case 0:
                    strWeekDay = (String) G.context.getText(R.string.Title_YekShanbeh);
                    break;
                case 1:
                    strWeekDay = (String) G.context.getText(R.string.Title_Doshanbeh);
                    break;
                case 2:
                    strWeekDay = (String) G.context.getText(R.string.Title_Seshanbeh);
                    break;
                case 3:
                    strWeekDay = (String) G.context.getText(R.string.Title_CharShanbeh);
                    break;
                case 4:
                    strWeekDay = (String) G.context.getText(R.string.Title_Panjshanbe);
                    break;
                case 5:
                    strWeekDay = (String) G.context.getText(R.string.Title_Gomeh);
                    break;
                case 6:
                    strWeekDay = (String) G.context.getText(R.string.Title_Shanbeh);
                    break;
            }

        }

    }

    public static String getCurrentShamsidate() {
        Locale loc = new Locale("en_US");
        Tarikh util = new Tarikh();
        SolarCalendar sc = util.new SolarCalendar();
        return String.valueOf(sc.year) + "/" + String.format(loc, "%02d",
                sc.month) + "/" + String.format(loc, "%02d", sc.date);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getCurrentShamsidatetime() {
        if(G.RTL) {
            Locale loc = new Locale("en_US");
            Tarikh util = new Tarikh();
            SolarCalendar sc = util.new SolarCalendar();
            return String.valueOf(sc.year) + "/" + String.format(loc, "%02d",
                    sc.month) + "/" + String.format(loc, "%02d", sc.date) + "  " + getFullTime();
        }else{
            return getCurrentMiladidatetime();
        }
    }
    public static String setTextValueDate(String[] selVal,int roundNum) {
        String val = "";//&& isNumeric(String.valueOf(selVal))
        if (selVal.length != 0 ) {
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
            df.setParseBigDecimal(true);
            try {

                for(String string:selVal) {
                    BigDecimal bd = (BigDecimal) df.parseObject(String.valueOf(string));
                    val += String.valueOf(bd);

                }

                if(val.length()==14) {
                    String valDate = val.substring(0, 4) + "/"
                            +val.substring(4,6)+ "/"
                            +val.substring(6,8)+" "
                            +val.substring(8,10)+":"
                            +val.substring(10,12)+":"
                            +val.substring(12);
                    val = valDate;
                }
                //val = String.valueOf(bd.setScale(roundNum, BigDecimal.ROUND_DOWN));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return val;
    }
    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getCurrentMiladidatetime() {
        String DateAndTime="";
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateAndTime=MyUtilities.changeNumberLocaleString ((df.format(today).replace("-","").trim()
                .replace(":","")
                .replace(" ","").trim()));
        return  DateAndTime.substring(0,4)+"-"
                +DateAndTime.substring(4,6)+"-"
                +DateAndTime.substring(6,8)+" "
                +DateAndTime.substring(8,10)+":"
                +DateAndTime.substring(10,12)+":"
                +DateAndTime.substring(12,14);

    }
    public static String getCurrentMiladidatetimewithoutSlash() {
        String DateAndTime="";
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.UK);
        DateAndTime=df.format(today);
        return  DateAndTime.replace("/","").trim()
                .replace(":","")
                .replace(" ","").trim();

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getCurrentShamsidatetimeWithoutSlash() {
        Locale loc = new Locale("en_US");
        Tarikh util = new Tarikh();
        SolarCalendar sc = util.new SolarCalendar();
        return  MyUtilities.changeNumberLocale( String.valueOf(sc.year)) + MyUtilities.changeNumberLocale(String.format(loc, "%02d",
                sc.month)) + MyUtilities.changeNumberLocale(String.format(loc, "%02d", sc.date) ) +
                MyUtilities.changeNumberLocale((getFullTime().replace(":","")));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getFullTime(){
        String localTime = "";
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); //TimeZone.getTimeZone("GMT+3:30")
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm:ss");
        // you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getDefault());

        localTime = date.format(currentLocalTime);
        return localTime;
    }
    public static String getTime(){
        String localTime = "";
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        DateFormat date;
        date = new SimpleDateFormat("HHmm");
        // you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getDefault());

        localTime = date.format(currentLocalTime);
        return localTime;
    }

    public static String getTimeWithoutColon(){
        String localTime = "";
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = cal.getTime();
        DateFormat date;
//    	if(cal.get(Calendar.AM_PM)==Calendar.AM){
//            date = new SimpleDateFormat("hhmm");
//        }else{
        date = new SimpleDateFormat("HHmm");
//        }

        date.setTimeZone(TimeZone.getDefault());

        localTime = date.format(currentLocalTime);
        return localTime;
    }
    public static String getCurrentShamsidateWithoutSlash() {
        Locale loc = new Locale("en_US");
        Tarikh util = new Tarikh();
        SolarCalendar sc = util.new SolarCalendar();
        return String.valueOf(sc.year) + String.format(loc, "%02d",
                sc.month) + String.format(loc, "%02d", sc.date);
    }

    public static Date convertStringToDate(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date=new Date();
        try {
            date = format.parse(dateStr
                    .replace('/','-').replace(' ','T')+'Z');



        } catch (org.apache.http.ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getMiladiDate(String ShamsiDateStr){
        PersianDate pdate = new PersianDate();
        ///PersianDateFormat pdformater = new PersianDateFormat();
        String strDateMiladi="";
        //pdformater.format(pdate);
        if(ShamsiDateStr.length()==10) {
            int Ye = Integer.valueOf(ShamsiDateStr.substring(0, 4));
            int Mo = Integer.valueOf(ShamsiDateStr.substring(5, 7));
            int Da = Integer.valueOf(ShamsiDateStr.substring(8, 10));
            int[] g=new int[3];
            g= pdate.toGregorian(Ye,Mo,Da);
            strDateMiladi=String.valueOf(g[0])+"-"+String.format( "%02d",g[1])+"-"+String.format( "%02d",g[2]);
        }else if(ShamsiDateStr.length()==8){
            int Ye = Integer.valueOf(ShamsiDateStr.substring(0, 4));
            int Mo = Integer.valueOf(ShamsiDateStr.substring(4, 6));
            int Da = Integer.valueOf(ShamsiDateStr.substring(6, 8));
            int[] g=new int[3];
            g= pdate.toGregorian(Ye,Mo,Da);
            strDateMiladi=String.valueOf(g[0])+"-"+String.format( "%02d",g[1])+"-"+String.format( "%02d",g[2]);
        }
        return strDateMiladi;
    }

    public static String getFullCurrentShamsidate() {
        Locale loc = new Locale("en_US");
        Tarikh util = new Tarikh();
        SolarCalendar sc = util.new SolarCalendar();
        String res = String.format("امروز %s ،  %s %s %s", sc.strWeekDay, String.format(loc, "%02d", sc.date), sc.strMonth, String.valueOf(sc.year));
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getShamsiDate(Date MiladiDate)
    {
        Locale loc = new Locale("en_US");
        Tarikh util = new Tarikh();
        SolarCalendar sc = util.new SolarCalendar(MiladiDate);
        String.format("%2s", Integer.valueOf(1)).replace(' ', '0');
        return MyUtilities.changeNumberLocaleString( String.valueOf(sc.year)) + "/" + String.format("%2s",MyUtilities.changeNumberLocaleString(
                 Integer.valueOf(sc.month).toString())).replace(' ', '0') + "/" + String.format("%2s",MyUtilities.changeNumberLocaleString(Integer.valueOf(sc.date).toString())).replace(' ', '0');
    }
    public static long getCurrentDateToMinute(){

        return System.currentTimeMillis()/60000;
    }
    public static long persianStartDayToMinute(String selDate){
        String miladiDate =selDate; //getMiladiDate(selDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long timeInMilliseconds = 0;
        try {
            Date d = sdf.parse(miladiDate);
            timeInMilliseconds = d.getTime();
            String dt = d.toString();
        } catch (ParseException ex) {

        }

        return timeInMilliseconds/(long)60000;
    }
    public static long persianDateTimeToMinute(String selDate,String selTime){
        String miladiDate =selDate; //getMiladiDate(selDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long timeInMilliseconds = 0;
        try {
            Date d = sdf.parse(miladiDate);
            timeInMilliseconds = d.getTime();
            String dt = d.toString();
        } catch (ParseException ex) {

        }

        return timeInMilliseconds/(long)60000;
    }
    public static String getShamsiDate(String MiladiDate)
    {
        Locale loc = new Locale("en_US");
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");

        Tarikh util = new Tarikh();
        SolarCalendar sc;
        try {
            //MiladiDate = MiladiDate.substring(0,4)+"/"+MiladiDate.substring(4,6)+"/"+MiladiDate.substring(6,8);
            sc = util.new SolarCalendar(s.parse(MiladiDate
                    .replace("/","-").replace("/","-")
                    .replace("/","-")));
            String.format("%2s", Integer.valueOf(1)).replace(' ', '0');
            return String.valueOf(sc.year) + "/" + String.format( "%2s",
                    Integer.valueOf(sc.month)).replace(' ', '0') + "/" + String.format( "%2s",
                    Integer.valueOf(sc.date)).replace(' ', '0');
        }
        catch (ParseException e) {
            return MiladiDate;
            //e.printStackTrace();
        }

    }
    public String getCurrentDateTimeNoneSpace() {
        DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        Calendar calobj = Calendar.getInstance();
        return df.format(calobj.getTime());
    }

    public static String getSlashedStringDate(String nonSlashStrDate){
        if(nonSlashStrDate.trim().length()==0)
            return "";
        if(nonSlashStrDate.length()==8)
            return new StringBuilder(nonSlashStrDate).insert(4, "/").insert(7, "/").toString();
        else
            return new StringBuilder(nonSlashStrDate).insert(2, "/").insert(5, "/").toString();
    }
    public static String getColonedStringTime(String nonColonedStrTime){
        if(nonColonedStrTime.trim().length()==0)
            return "";
        return new StringBuilder(nonColonedStrTime).insert(2, ":").toString();
    }

    public static Long addMinutesToDateTime(String strDateTime,Integer minutes){
        Integer year = Integer.valueOf(strDateTime.substring(0,4));
        Integer month = Integer.valueOf(strDateTime.substring(4,6));
        Integer day = Integer.valueOf(strDateTime.substring(6,8));
        Integer hour = Integer.valueOf(strDateTime.substring(8,10));
        Integer min = Integer.valueOf(strDateTime.substring(10,12));
        JalaliCalendar jalaliCalendar = new JalaliCalendar(year, month, day, hour, min, 0);
        jalaliCalendar.add(Calendar.MINUTE, minutes);
        jalaliCalendar.get(Calendar.YEAR);

        String resDateTime = String.format("%s%s%s%s%s",
                jalaliCalendar.get(Calendar.YEAR),
                jalaliCalendar.get(Calendar.MONTH)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.MONTH)):String.valueOf(jalaliCalendar.get(Calendar.MONTH)),
                jalaliCalendar.get(Calendar.DAY_OF_MONTH)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.DAY_OF_MONTH)):String.valueOf(jalaliCalendar.get(Calendar.DAY_OF_MONTH)),
                jalaliCalendar.get(Calendar.HOUR_OF_DAY)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.HOUR_OF_DAY)):String.valueOf(jalaliCalendar.get(Calendar.HOUR_OF_DAY)),
                jalaliCalendar.get(Calendar.MINUTE)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.MINUTE)):String.valueOf(jalaliCalendar.get(Calendar.MINUTE))
        );
        return Long.parseLong(resDateTime+"00");
    }
    public static Long subMinutesFromDateTime(String strDateTime,Integer minutes){
        String strDate=getMiladiDate(strDateTime.substring(0,8)).replace("-", "").trim();
        String strTime=strDateTime.substring(8,12);
        Integer year = Integer.valueOf(strDate.substring(0,4));
        Integer month = Integer.valueOf(strDate.substring(4,6));
        Integer day = Integer.valueOf(strDate.substring(6,8));
        Integer hour = Integer.valueOf(strTime.substring(0,2));
        Integer min = Integer.valueOf(strTime.substring(3,4));

        JalaliCalendar jalaliCalendar = new JalaliCalendar(year, month, day, hour, min, 0);
        jalaliCalendar.add(Calendar.MINUTE, -minutes);
        //jalaliCalendar.get(Calendar.YEAR);

        String resDateTime = String.format("%s%s%s%s%s",
                jalaliCalendar.get(Calendar.YEAR),
                jalaliCalendar.get(Calendar.MONTH)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.MONTH)):String.valueOf(jalaliCalendar.get(Calendar.MONTH)),
                jalaliCalendar.get(Calendar.DAY_OF_MONTH)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.DAY_OF_MONTH)):String.valueOf(jalaliCalendar.get(Calendar.DAY_OF_MONTH)),
                jalaliCalendar.get(Calendar.HOUR_OF_DAY)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.HOUR_OF_DAY)):String.valueOf(jalaliCalendar.get(Calendar.HOUR_OF_DAY)),
                jalaliCalendar.get(Calendar.MINUTE)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.MINUTE)):String.valueOf(jalaliCalendar.get(Calendar.MINUTE))
        );
        return Long.parseLong(getShamsiDate(resDateTime.substring(0,8)).replace("/","").trim()+resDateTime.substring(8,12)+"00");
    }
    public static Long subMinutesFromDateTimeMiladi(String strDateTime,Integer minutes) {
        Integer year = Integer.valueOf(strDateTime.substring(0,4));
        Integer month = Integer.valueOf(strDateTime.substring(4,6));
        Integer day = Integer.valueOf(strDateTime.substring(6,8));
        Integer hour = Integer.valueOf(strDateTime.substring(8,10));
        Integer min = Integer.valueOf(strDateTime.substring(10,12));
        Integer sec = Integer.valueOf(strDateTime.substring(12,14));

        Calendar cal = Calendar.getInstance();

        cal.set(year,month,day,hour,min,sec);

        cal.add(Calendar.MINUTE, -minutes);
        SimpleDateFormat fHour=new SimpleDateFormat("yyyyMMddHHmmss",Locale.UK);

        String dateS=String.valueOf(cal.get(Calendar.YEAR))
                +String.valueOf(cal.get(Calendar.MONTH)<10?"0"+String.valueOf(cal.get(Calendar.MONTH)):cal.get(Calendar.MONTH))
                +String.valueOf(cal.get(Calendar.DAY_OF_MONTH)<10?"0"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH)):cal.get(Calendar.DAY_OF_MONTH))
                +String.valueOf(cal.get(Calendar.HOUR_OF_DAY)<10?"0"+String.valueOf(cal.get(Calendar.HOUR)):cal.get(Calendar.HOUR_OF_DAY))
                +String.valueOf(cal.get(Calendar.MINUTE)<10?"0"+String.valueOf(cal.get(Calendar.MINUTE)):cal.get(Calendar.MINUTE))
                +String.valueOf(cal.get(Calendar.SECOND)<10?"0"+String.valueOf(cal.get(Calendar.SECOND)):cal.get(Calendar.SECOND));

        try {
            dateS= fHour.format(fHour.parse(dateS));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  Long.parseLong(dateS) ;
    }
    public static Long getLongDateTime(String strDateTime){
        Integer year = Integer.valueOf(strDateTime.substring(0,4));
        Integer month = Integer.valueOf(strDateTime.substring(4,6));
        Integer day = Integer.valueOf(strDateTime.substring(6,8));
        Integer hour = Integer.valueOf(strDateTime.substring(8,10));
        Integer min = Integer.valueOf(strDateTime.substring(10,12));
        Integer sec=Integer.valueOf(strDateTime.substring(12,14));
        JalaliCalendar jalaliCalendar = new JalaliCalendar(year, month, day, hour, min, sec);

        String resDateTime = String.format("%s%s%s%s%s%s",
                jalaliCalendar.get(Calendar.YEAR),
                jalaliCalendar.get(Calendar.MONTH)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.MONTH)):String.valueOf(jalaliCalendar.get(Calendar.MONTH)),
                jalaliCalendar.get(Calendar.DAY_OF_MONTH)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.DAY_OF_MONTH)):String.valueOf(jalaliCalendar.get(Calendar.DAY_OF_MONTH)),
                jalaliCalendar.get(Calendar.HOUR_OF_DAY)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.HOUR_OF_DAY)):String.valueOf(jalaliCalendar.get(Calendar.HOUR_OF_DAY)),
                jalaliCalendar.get(Calendar.MINUTE)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.MINUTE)):String.valueOf(jalaliCalendar.get(Calendar.MINUTE)),
                jalaliCalendar.get(Calendar.SECOND)<10?"0"+String.valueOf(jalaliCalendar.get(Calendar.SECOND)):String.valueOf(jalaliCalendar.get(Calendar.SECOND))
        );
        return Long.parseLong(resDateTime);
    }
    public static Long getLongDateTimeMiladi(String strDateTime){
        String resDateTime="";
        resDateTime=strDateTime.replace("-","")
                .replace(":","").replace(" ","").trim();
        return Long.parseLong(resDateTime);
    }
    public static boolean isDateValid(String strDate){
        boolean isValid = true;
        try {
            if(Integer.valueOf(strDate.split("/")[1]) > 12){
                isValid = false;
            }else if(Integer.valueOf(strDate.split("/")[2]) > 31){
                isValid = false;
            }
        }catch (Exception ex){
            isValid = false;
        }
        return isValid;
    }
    public static boolean isTimeValid(String strDate){
        boolean isValid = true;
        try {
            if(Integer.valueOf(strDate.split(":")[0]) > 23){
                isValid = false;
            }else if(Integer.valueOf(strDate.split(":")[1]) > 59){
                isValid = false;
            }
        }catch (Exception ex){
            isValid = false;
        }
        return isValid;
    }
}