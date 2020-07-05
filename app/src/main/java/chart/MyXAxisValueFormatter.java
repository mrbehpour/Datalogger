package chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import ir.saa.android.datalogger.G;

public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return getSlashedStringDate(mValues[(int) value]);
    }

    private String getSlashedStringDate(String nonSlashStrDate){
        int offset;
        if(nonSlashStrDate.trim().length()==0)
            return "";

        if(G.RTL) {
            return String.valueOf( new StringBuilder(new StringBuilder(nonSlashStrDate).insert(4, "/"))
                    .insert(7,"/")).split("-")[0];

        }else {
            return String.valueOf( new StringBuilder(new StringBuilder(nonSlashStrDate).insert(4, "/")).insert(7,"/"));
        }
    }


}