package chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class MyYAxisValueFormatter implements IAxisValueFormatter {

    public MyYAxisValueFormatter() {

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return String.valueOf((int) value);
    }

    public int getDecimalDigits() {
        return 0;
    }
}