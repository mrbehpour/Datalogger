package database.structs;

import android.content.ClipData;

public class dtoItemRanges {
    public Integer		ItemInfID;
    public Integer 		ItemBaseRangesMin;

    public dtoItemRanges(Integer itemInfID, Integer itemBaseRangesMin){
        ItemInfID = itemInfID;
        ItemBaseRangesMin = itemBaseRangesMin;
    }

    @Override
    public dtoItemRanges clone() throws CloneNotSupportedException {
        return (dtoItemRanges)super.clone();
    }
}


