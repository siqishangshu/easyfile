package cn.mxsic.easyfile.base;

import java.util.List;

/**
 * Function: Preprocessor <br>
 *
 * 导入,导出时文件内容预处理
 *
 * @author: siqishangshu <br>
 * @date: 2019-11-13 17:11:00
 */
public interface Preprocessor {

    /**
     * 处理导出与导入，可写头，写内容。
     */
    List<List<List<String>>> process(List<List<List<String>>> dataMatrix);

}
