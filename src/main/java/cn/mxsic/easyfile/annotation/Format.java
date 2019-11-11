package cn.mxsic.easyfile.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.mxsic.easyfile.base.Formatter;

/**
 * Function: Title <br>
 * 导出的表列名称
 *
 * @author: siqishangshu <br>
 * @date: 2019-08-15 17:00:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Format {

    Class<? extends Formatter> value();

    ScopeType scopeType() default ScopeType.BOTH;
}
