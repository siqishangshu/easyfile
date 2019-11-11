package cn.mxsic.easyfile.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * <p>时间处理工具类</p>
 *
 * @author siqishangshu
 */
public class DateUtils {


    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DAY_2 = "yyyy/MM/dd";
    public static final String TIME_FORMAT_SEC = "HH:mm:ss";
    public static final String DATE_FORMAT_SEC = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MSEC = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_MSEC_T = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String DATE_FORMAT_MSEC_T_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_FORMAT_DAY_SIMPLE = "y/M/d";

    /**
     * yyyy-MM-dd
     */
    private static final String DATE_REG = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
    /**
     * yyyy/MM/dd
     */
    private static final String DATE_REG_2 = "^[1-9]\\d{3}/(0[1-9]|1[0-2])/(0[1-9]|[1-2][0-9]|3[0-1])$";
    /**
     * y/M/d
     */
    private static final String DATE_REG_SIMPLE_2 = "^[1-9]\\d{3}/([1-9]|1[0-2])/([1-9]|[1-2][0-9]|3[0-1])$";
    /**
     * HH:mm:ss
     */
    private static final String TIME_SEC_REG = "^(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    private static final String DATE_TIME_REG = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s" +
            "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    private static final String DATE_TIME_MSEC_REG = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s" +
            "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d\\.\\d{3}$";
    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    private static final String DATE_TIME_MSEC_T_REG = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T" +
            "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d\\.\\d{3}$";
    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     */
    private static final String DATE_TIME_MSEC_T_Z_REG = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T" +
            "(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d\\.\\d{3}Z$";


    public static Date read(String str) {
        str = str.trim();
        SimpleDateFormat sdf = null;
        if (isMatched(str, DATE_REG)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_DAY);
        }
        if (isMatched(str, DATE_REG_2)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_DAY_2);
        }
        if (isMatched(str, DATE_REG_SIMPLE_2)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_DAY_SIMPLE);
        }
        if (isMatched(str, TIME_SEC_REG)) {
            sdf = new SimpleDateFormat(TIME_FORMAT_SEC);
        }
        if (isMatched(str, DATE_TIME_REG)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_SEC);
        }
        if (isMatched(str, DATE_TIME_MSEC_REG)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_MSEC);
        }
        if (isMatched(str, DATE_TIME_MSEC_T_REG)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_MSEC_T);
        }
        if (isMatched(str, DATE_TIME_MSEC_T_Z_REG)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_MSEC_T_Z);
        }
        if (null != sdf) {
            try {
                return sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static boolean isMatched(String pattern, String reg) {
        Pattern compile = Pattern.compile(reg);
        return compile.matcher(pattern).matches();
    }

    public static String parseString(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat ft = new SimpleDateFormat(pattern);
        return ft.format(date);
    }

    public static Date parseDate(String date, String pattern) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat ft = new SimpleDateFormat(pattern);
        return ft.parse(date);
    }

    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }
}