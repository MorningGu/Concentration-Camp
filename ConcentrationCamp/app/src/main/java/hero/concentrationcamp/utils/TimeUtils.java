package hero.concentrationcamp.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by hero on 2016/12/6 0006.
 */

public class TimeUtils {
    public static String parseTzTime(String tzTime){
        if(TextUtils.isEmpty(tzTime)){
            return "";
        }
        tzTime = tzTime.replace("Z", " UTC");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        try {
            Date dt = sdf.parse(tzTime);
            TimeZone tz = sdf.getTimeZone();
            Calendar c = sdf.getCalendar();
            return getTimeString(c);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    private static String getTimeString(Calendar c) {
        StringBuffer result = new StringBuffer();
        result.append(c.get(Calendar.YEAR));
        result.append("-");
        result.append((c.get(Calendar.MONTH) + 1));
        result.append("-");
        result.append(c.get(Calendar.DAY_OF_MONTH));
        result.append(" ");
        result.append(c.get(Calendar.HOUR_OF_DAY));
        result.append(":");
        result.append(c.get(Calendar.MINUTE));
        result.append(":");
        result.append(c.get(Calendar.SECOND));
        return result.toString();
    }
}
