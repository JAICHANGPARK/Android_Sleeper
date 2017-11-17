package com.dreamwalker.sleeper.Utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by 2E313JCP on 2017-11-16.
 */

public class XAxisValueFormatter implements IAxisValueFormatter{

    private String[] mValue;

    public XAxisValueFormatter(String[] mValue) {
        this.mValue = mValue;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValue[(int)value];
    }
}
