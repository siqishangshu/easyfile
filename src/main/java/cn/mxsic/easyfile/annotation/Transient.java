package cn.mxsic.easyfile.annotation;

import cn.mxsic.easyfile.base.ScopeType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Function: Transient <br>
 * 此属性，可以做内容临时数据，可配置，可不导出，可不导入。
 *
 * @author: siqishangshu <br>
 * @date: 2019-08-15 17:00:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Transient {

    ScopeType scopeType() default ScopeType.BOTH;
}
