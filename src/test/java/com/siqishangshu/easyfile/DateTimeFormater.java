package com.siqishangshu.easyfile;

import com.siqishangshu.easyfile.base.Formatter;
import com.siqishangshu.easyfile.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Function: DateTimeFormater <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-08-15 17:28:00
 */
public class DateTimeFormater implements Formatter<Date> {
   private static   SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.DATE_FORMAT_DAY);

    @Override
    public String write(Date obj) {
        return simpleDateFormat.format(obj);
    }

    @Override
    public Date read(String str) {
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
