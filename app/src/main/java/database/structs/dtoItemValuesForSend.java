package database.structs;

public class dtoItemValuesForSend implements Cloneable {
    public Integer		ItemInfID;
    public String 		ItemVal	= "";
    public String 		ItemValTyp;
    //public String		PDate;
    //public String 		PTime;

    public  String 		RegisterDateTime;
    public Integer 		UsrID;
    public Integer 		ShiftID;

    public Integer		Id;
    public Integer 		IsSend = 0;

    @Override
    public dtoItemValuesForSend clone() throws CloneNotSupportedException {
        return (dtoItemValuesForSend)super.clone();
    }
}
