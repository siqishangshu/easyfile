package cn.mxsic.easyfile;

import java.text.ParseException;
import java.util.Date;

import cn.mxsic.easyfile.base.Formatter;
import cn.mxsic.easyfile.utils.DateUtils;

/**
 * Function: DateTimeFormater <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-08-15 17:28:00
 */
public class DateTimeFormater implements Formatter<Date> {


    @Override
    public String write(Date obj) {
        return DateUtils.parseString(obj,DateUtils.DATE_FORMAT_SEC);
    }

    @Override
    public Date read(String str) {
        try {
            return DateUtils.parseDate(str, DateUtils.DATE_FORMAT_SEC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
