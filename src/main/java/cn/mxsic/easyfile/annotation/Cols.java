package cn.mxsic.easyfile.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Function: Cols <br>
 *
 * export only
 *
 * @author: siqishangshu <br>
 * @date: 2019-08-15 17:18:00
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cols {

    int value();

}
