package com.mrkostua.mathalarm;

import android.util.Log;

import com.mrkostua.mathalarm.Tools.ConstantValues;

/** @author Konstantyn
 * Created by Администратор on 14.03.2017.
 */

public  class ShowLogs {
    private static final String TAG = "MathAlarm";

    public static boolean LOG_STATUS= ConstantValues.INSTANCE.getLOG_DEBUG_STATUS() ? true : false;

    public static void i(String logMessage) {
        Log.i(TAG," "+logMessage);
    }
}
