package cn.mxsic.easyfile;

import cn.mxsic.easyfile.base.Formatter;

/**
 * Function: DateTimeFormater <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-08-15 17:28:00
 */
public class ManGoodFormater implements Formatter<Boolean> {


    @Override
    public String write(Boolean obj) {
        return obj ? "好人" : "坏人";
    }

    @Override
    public Boolean read(String str) {
        return str.equals("好人") ? true : false;
    }
}
