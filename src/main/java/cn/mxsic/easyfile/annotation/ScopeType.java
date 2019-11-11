package cn.mxsic.easyfile.annotation;

/**
 * Function: ExportScope <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-08-15 17:11:00
 */
public enum ScopeType {
    /**
     * only when file input may use this annotation Priority is lower than attribute itself
     */
    IMPORT,

    /**
     * only when file output may user this annotation  Priority is lower than attribute itself
     */

    EXPORT,

    /**
     * use this annotation when input and output  Priority is lower than attribute itself
     */
    BOTH,
}
