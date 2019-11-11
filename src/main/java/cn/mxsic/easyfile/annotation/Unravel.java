package cn.mxsic.easyfile.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Function: Decollate <br>
 *  此属性可以用于，将字类进行分开子表头。二级菜单。
 * @author: siqishangshu <br>
 * @date: 2019-11-11 10:59:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Unravel {

}
