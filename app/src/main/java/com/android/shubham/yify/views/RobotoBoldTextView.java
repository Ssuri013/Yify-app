package com.android.shubham.yify.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.shubham.yify.util.FontUtil;

/**
 * Created by shubham on 18-Jun-17.
 */

public class RobotoBoldTextView extends TextView {

    public RobotoBoldTextView(Context context) {
        super(context);
        applyCustomFont();
    }

    public RobotoBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont();
    }

    public RobotoBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont();
    }

    private void applyCustomFont() {
        Typeface customFont = FontUtil.getTypeface(FontUtil.ROBOTO_BOLD);
        setTypeface(customFont);
    }
}