package cn.mxsic.easyfile;


import cn.mxsic.easyfile.base.Formatter;

/**
 * Function: BodyStateFormater <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-11-11 11:16:00
 */
public class BodyStateFormater implements Formatter<Boolean> {

    @Override
    public String write(Boolean obj) {
        return obj ? "好" : "坏";
    }

    @Override
    public Boolean read(String str) {
        return str.equals("好") ? true : false;
    }
}
