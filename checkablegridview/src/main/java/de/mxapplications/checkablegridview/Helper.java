package de.mxapplications.checkablegridview;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Sebastian Dombrowski on 2/9/2016.
 */
class Helper {
    static int convertDpToPixels(int dp){
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics()));
    }
}
