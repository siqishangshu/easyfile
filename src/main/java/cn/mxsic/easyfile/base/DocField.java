package cn.mxsic.easyfile.base;

import cn.mxsic.easyfile.utils.ObjectUtils;

import java.lang.reflect.Field;

import lombok.Data;

/**
 * 用于文件导出导入时的注释
 *
 * @author siqishangshu
 * @date 19 08 26
 */
@Data
public class DocField {
    /**
     * class field
     */
    private Field field;
    /**
     * title in excel
     */
    private String title;
    /**
     * title in use scope
     */
    private ScopeType titleScope;

    /**
     * cols index
     */
    private Integer cols;
    /**
     * formatter instants
     */
    private Formatter formatter;
    /**
     * format scope
     */
    private ScopeType formatScope;

    /**
     * use title for read form file
     * @return
     */
    public boolean readTitle() {
        if (ObjectUtils.isEmpty(titleScope) || ObjectUtils.isEmpty(title)) {
            return false;
        }
        if (titleScope.equals(ScopeType.BOTH) || titleScope.equals(ScopeType.IMPORT)) {
            return true;
        }
        return false;
    }

    /**
     * use title for write to file
     * @return
     */
    public boolean writeTitle() {
        if (ObjectUtils.isEmpty(titleScope) || ObjectUtils.isEmpty(title)) {
            return false;
        }
        if (titleScope.equals(ScopeType.BOTH) || titleScope.equals(ScopeType.EXPORT)) {
            return true;
        }
        return false;
    }
    /**
     * use formatter for read form file
     * @return
     */
    public boolean readFormat() {

        if (ObjectUtils.isEmpty(formatScope) || ObjectUtils.isEmpty(formatter)) {
            return false;
        }
        if (formatScope.equals(ScopeType.BOTH) || formatScope.equals(ScopeType.IMPORT)) {
            return true;
        }
        return false;
    }

    /**
     * use formatter for write to file
     * @return
     */
    public boolean writeFormat() {
        if (ObjectUtils.isEmpty(formatScope) || ObjectUtils.isEmpty(formatter)) {
            return false;
        }
        if (formatScope.equals(ScopeType.BOTH) || formatScope.equals(ScopeType.EXPORT)) {
            return true;
        }
        return false;
    }


}
