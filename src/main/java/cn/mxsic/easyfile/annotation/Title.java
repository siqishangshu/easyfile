package cn.mxsic.easyfile.annotation;

import cn.mxsic.easyfile.base.ScopeType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
public @interface Title {

    String value();

    ScopeType scopeType() default ScopeType.BOTH;

}
