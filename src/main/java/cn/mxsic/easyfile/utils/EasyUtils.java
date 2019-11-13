package cn.mxsic.easyfile.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import cn.mxsic.easyfile.exception.ExportException;

/**
 * Function: ObjectUtils <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-09-09 13:53:00
 */

public abstract class EasyUtils {


    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Optional) {
            return !((Optional) obj).isPresent();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        // else
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static <T> T getInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception var2) {
            throw new ExportException(" new instance  " + var2.getMessage() + " failed ");
        }
    }
}
