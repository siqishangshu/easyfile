package cn.mxsic.easyfile.base;

import java.math.BigDecimal;

/**
 * Function: DataTypeProcessor <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-09-02 18:10:00
 */
public enum DataTypeProcessor {
    INT("int"),
    INTEGER("java.lang.Integer"),
    LON("long"),
    LONG("java.lang.Long"),
    SHO("short"),
    SHORT("java.lang.Short"),
    STR("String"),
    STRING("java.lang.String"),
    DOU("double"),
    DOUBLE("java.lang.Double"),
    BTE("byte"),
    BYTE("java.lang.Byte"),
    BOL("boolean"),
    BOOLEAN("java.lang.Boolean"),
    BIG_DECIMAL("java.math.BigDecimal");

    private String simpleName;

    DataTypeProcessor(String simpleName) {
        this.simpleName = simpleName;
    }

    public static Object handle(String v, String simpleName) {
        if (DataTypeProcessor.INT.simpleName.equalsIgnoreCase(simpleName)) {
            if (v.contains(CsvExcelConstant.NUMERIC)) {
                v = v.substring(0,v.lastIndexOf(CsvExcelConstant.NUMERIC));
            }
            return Integer.parseInt(v);
        } else if (DataTypeProcessor.INTEGER.simpleName.equalsIgnoreCase(simpleName)) {
            if (v.contains(CsvExcelConstant.NUMERIC)) {
                v = v.substring(0,v.lastIndexOf(CsvExcelConstant.NUMERIC));
            }
            return Integer.parseInt(v);
        } else if (DataTypeProcessor.LON.simpleName.equalsIgnoreCase(simpleName)) {
            if (v.contains(CsvExcelConstant.NUMERIC)) {
                v = v.substring(0,v.lastIndexOf(CsvExcelConstant.NUMERIC));
            }
            return Long.parseLong(v);
        } else if (DataTypeProcessor.LONG.simpleName.equalsIgnoreCase(simpleName)) {
            if (v.contains(CsvExcelConstant.NUMERIC)) {
                v = v.substring(0,v.lastIndexOf(CsvExcelConstant.NUMERIC));
            }
            return Long.parseLong(v);
        } else if (DataTypeProcessor.DOU.simpleName.equalsIgnoreCase(simpleName)) {
            return Double.parseDouble(v);
        } else if (DataTypeProcessor.DOUBLE.simpleName.equalsIgnoreCase(simpleName)) {
            return Double.parseDouble(v);
        } else if (DataTypeProcessor.SHO.simpleName.equalsIgnoreCase(simpleName)) {
            if (v.contains(CsvExcelConstant.NUMERIC)) {
                v = v.substring(0,v.lastIndexOf(CsvExcelConstant.NUMERIC));
            }
            return Short.parseShort(v);
        } else if (DataTypeProcessor.SHORT.simpleName.equalsIgnoreCase(simpleName)) {
            if (v.contains(CsvExcelConstant.NUMERIC)) {
                v = v.substring(0,v.lastIndexOf(CsvExcelConstant.NUMERIC));
            }
            return Short.parseShort(v);
        } else if (DataTypeProcessor.STR.simpleName.equalsIgnoreCase(simpleName)) {
            return String.valueOf(v);
        } else if (DataTypeProcessor.STRING.simpleName.equalsIgnoreCase(simpleName)) {
            return String.valueOf(v);
        } else if (DataTypeProcessor.BTE.simpleName.equalsIgnoreCase(simpleName)) {
            return Byte.parseByte(v);
        } else if (DataTypeProcessor.BYTE.simpleName.equalsIgnoreCase(simpleName)) {
            return Byte.parseByte(v);
        } else if (DataTypeProcessor.BOL.simpleName.equalsIgnoreCase(simpleName)) {
            return Boolean.parseBoolean(v);
        } else if (DataTypeProcessor.BOOLEAN.simpleName.equalsIgnoreCase(simpleName)) {
            return Boolean.parseBoolean(v);
        } else if (DataTypeProcessor.BIG_DECIMAL.simpleName.equalsIgnoreCase(simpleName)) {
            return BigDecimal.valueOf(Double.parseDouble(v));
        }
        /**
         * return null will not case cost Exception
         */
        return null;
    }
}
