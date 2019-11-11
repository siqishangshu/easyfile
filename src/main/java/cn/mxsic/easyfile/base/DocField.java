package cn.mxsic.easyfile.base;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.mxsic.easyfile.annotation.ScopeType;
import cn.mxsic.easyfile.utils.ObjectUtils;
import lombok.Data;

/**
 * 用于文件导出导入时的注释
 *
 * @author siqishangshu
 * @date 19 08 26
 */
@Data
public class DocField {

    private Field field;

    private String title;
    private ScopeType titleScope;

    private Integer cols;

    private Formatter formatter;
    private ScopeType formatScope;

    private List<DocField> unravelList = new ArrayList<>();

    public boolean importTitle() {
        if (ObjectUtils.isEmpty(titleScope) || ObjectUtils.isEmpty(title)) {
            return false;
        }
        if (titleScope.equals(ScopeType.BOTH) || titleScope.equals(ScopeType.IMPORT)) {
            return true;
        }
        return false;
    }

    public boolean exportTitle() {
        if (ObjectUtils.isEmpty(titleScope) || ObjectUtils.isEmpty(title)) {
            return false;
        }
        if (titleScope.equals(ScopeType.BOTH) || titleScope.equals(ScopeType.EXPORT)) {
            return true;
        }
        return false;
    }

    public boolean importFormat() {

        if (ObjectUtils.isEmpty(formatScope) || ObjectUtils.isEmpty(formatter)) {
            return false;
        }
        if (formatScope.equals(ScopeType.BOTH) || formatScope.equals(ScopeType.IMPORT)) {
            return true;
        }
        return false;
    }

    public boolean exportFormat() {
        if (ObjectUtils.isEmpty(formatScope) || ObjectUtils.isEmpty(formatter)) {
            return false;
        }
        if (formatScope.equals(ScopeType.BOTH) || formatScope.equals(ScopeType.EXPORT)) {
            return true;
        }
        return false;
    }


}
