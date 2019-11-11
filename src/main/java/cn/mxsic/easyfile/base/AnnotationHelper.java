package cn.mxsic.easyfile.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cn.mxsic.easyfile.annotation.Cols;
import cn.mxsic.easyfile.annotation.Format;
import cn.mxsic.easyfile.annotation.ScopeType;
import cn.mxsic.easyfile.annotation.Title;
import cn.mxsic.easyfile.annotation.Transient;
import cn.mxsic.easyfile.annotation.Unravel;
import cn.mxsic.easyfile.exception.ExportException;
import cn.mxsic.easyfile.utils.ObjectUtils;

/**
 * 获取注解信息
 *
 * @author siqishangshu
 */
public final class AnnotationHelper {

    /**
     * 获取导入导出POJO类的
     * 注解信息
     */
    private static List<DocField> getFields(Class clazz, ScopeType scopeType) {
        List<DocField> fieldList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Transient transient_ = field.getAnnotation(Transient.class);
            if (ObjectUtils.isNotEmpty(transient_)) {
                if (transient_.scopeType().equals(scopeType) || transient_.scopeType().equals(ScopeType.BOTH)) {
                    continue;
                }
            }
            DocField docField = new DocField();
            field.setAccessible(true);
            docField.setField(field);
            Title title = field.getAnnotation(Title.class);
            if (ObjectUtils.isNotEmpty(title)) {
                docField.setTitle(title.value());
                docField.setTitleScope(title.scopeType());
            }
            Cols cols = field.getAnnotation(Cols.class);
            if (ObjectUtils.isNotEmpty(cols)) {
                docField.setCols(cols.value());
            }
            Format format = field.getAnnotation(Format.class);
            if (ObjectUtils.isNotEmpty(format)) {
                docField.setFormatter(ObjectUtils.getInstance(format.value()));
                docField.setFormatScope(format.scopeType());
            }
            Unravel unravel = field.getAnnotation(Unravel.class);
            if (ObjectUtils.isNotEmpty(unravel)) {
                docField.setUnravelList(getFields(field.getDeclaringClass(),scopeType));
            }
            fieldList.add(docField);
        }
        return fieldList;
    }

    /**
     * 获取表格头
     */
    public static DocField[] getAnnotationFields(Class clazz, ScopeType scopeType) {
        List<DocField> fieldList = AnnotationHelper.getFields(clazz, scopeType);
        if (fieldList.isEmpty()) {
            throw new ExportException("export nothing");
        }
        int maxCol = fieldList.size();
        DocField maxField = fieldList.stream().filter(f -> ObjectUtils.isNotEmpty(f.getCols())).max(
                Comparator.comparing(DocField::getCols)).orElse(null);
        if (ObjectUtils.isNotEmpty(maxField)) {
            maxCol = Math.max(maxCol, maxField.getCols());
        }
        DocField[] fieldArr = new DocField[maxCol];
        List<DocField> temp = new ArrayList<>();

        for (DocField docField : fieldList) {
            if (ObjectUtils.isNotEmpty(docField.getCols())) {
                if (ObjectUtils.isEmpty(fieldArr[docField.getCols() - 1])) {
                    fieldArr[docField.getCols() - 1] = docField;
                    continue;
                }
            }
            temp.add(docField);
        }
        int index = 0;
        for (DocField docField : temp) {
            while (ObjectUtils.isNotEmpty(fieldArr[index])) {
                index++;
            }
            fieldArr[index] = docField;
            index++;
        }
        return fieldArr;
    }


    /**
     * 表名头名称
     *
     * @param docFields 获出需要导出字段title数组
     */
    public static String[] getHeadFieldTitles(DocField[] docFields) {
        String[] headTitles = new String[docFields.length];
        for (int i = 0; i < docFields.length; i++) {
            if (ObjectUtils.isNotEmpty(docFields[i])) {
                if (docFields[i].exportTitle()) {
                    headTitles[i] = docFields[i].getTitle();
                } else {
                    headTitles[i] = docFields[i].getField().getName();
                }
            } else {
                /**
                 * 占位
                 */
                headTitles[i] = "";
            }
        }
        return headTitles;
    }
}
