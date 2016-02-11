package de.mxapplications.checkablegridview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by Sebastian Dombrowski on 11/18/2015.
 */
public class NoFlingHorizontalScrollView extends HorizontalScrollView {
    public NoFlingHorizontalScrollView(Context context) {
        super(context);
    }

    public NoFlingHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoFlingHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NoFlingHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void fling(int velocityX) {
        super.fling(0);
    }
}
