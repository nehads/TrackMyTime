package com.javac.dell.trackmytime.customFonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class MyEditText_Lato_Regular extends android.support.v7.widget.AppCompatEditText {

    public MyEditText_Lato_Regular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEditText_Lato_Regular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText_Lato_Regular(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Regular.ttf");
            setTypeface(tf);
        }
    }

}