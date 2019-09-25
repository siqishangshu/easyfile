package cn.mxsic.easyfile.csv;

import org.apache.poi.util.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mxsic.easyfile.base.AnnotationHelper;
import cn.mxsic.easyfile.base.CsvConstant;
import cn.mxsic.easyfile.base.DataTypeProcessor;
import cn.mxsic.easyfile.base.DocField;
import cn.mxsic.easyfile.base.ScopeType;
import cn.mxsic.easyfile.exception.ImportException;
import cn.mxsic.easyfile.utils.ObjectUtils;


/**
 * @author siqishangshu
 */
public class CsvImportHelper<T> {

    /**
     * T class
     */
    private Class<T> tClass;
    /**
     * 导出记录保存的文件
     */
    private BufferedReader reader;
    /**
     * data matrix
     */
    private List<List<String>> dataMatrix = new ArrayList<>();
    /**
     * data
     */
    private List<T> data = new ArrayList<>();
    /**
     * 表头映射
     */
    private Map<String, DocField> docFieldMap = new HashMap<>();
    /**
     * 表头
     */
    private String[] titles;


    public CsvImportHelper(InputStream inputStream, Class<T> tClass) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
        this.tClass = tClass;
    }

    public CsvImportHelper(File file, Class<T> tClass) {
        try {
            InputStream inputStream = new FileInputStream(file);
            this.reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
            this.tClass = tClass;
        } catch (FileNotFoundException e) {
            throw new ImportException(e);
        }
    }


    /**
     * import the date to T
     */
    public List<T> importData() {
        try {
            loadMatrix();
            return getResult();
        } catch (Exception e) {
            throw new ImportException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * import the date to T
     */
    public List<T> getData() {
        return this.data;
    }

    /**
     * read the string row convert to object
     */
    private List<T> getResult() throws IllegalAccessException {
        if (this.dataMatrix.isEmpty()) {
            return data;
        }
        DocField[] docFields = AnnotationHelper.getAnnotationFields(this.tClass, ScopeType.IMPORT);

        List<String> firstRow = this.dataMatrix.get(0);
        this.titles = new String[firstRow.size()];
        for (int i = 0; i < firstRow.size(); i++) {
            this.titles[i] = firstRow.get(i);
        }
        for (DocField docField : docFields) {
            if (ObjectUtils.isNotEmpty(docField)) {
                if (ObjectUtils.isNotEmpty(docField.getTitle()) && docField.readTitle()) {
                    this.docFieldMap.put(docField.getTitle(), docField);
                }
                this.docFieldMap.put(docField.getField().getName(), docField);
            }
        }
        for (int i = 1; i < this.dataMatrix.size(); i++) {
            fullUp(this.dataMatrix.get(i));
        }
        return data;
    }

    private void fullUp(List<String> values) throws IllegalAccessException {
        T t = ObjectUtils.getInstance(this.tClass);
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            if (ObjectUtils.isEmpty(value)) {
                continue;
            }
            String field = this.titles[i];
            DocField docField = this.docFieldMap.get(field);
            if (ObjectUtils.isEmpty(docField)) {
                continue;
            }
            if (docField.readFormat()) {
                docField.getField().set(t, docField.getFormatter().read(value));
            } else {
                docField.getField().set(t, DataTypeProcessor.handle(value, docField.getField().getType().getSimpleName()));
            }
        }
        data.add(t);
    }


    /**
     * load data matrix
     */
    private void loadMatrix() throws IOException {
        String pre = "";
        for (String str = this.reader.readLine(); str != null; str = this.reader.readLine()) {
            if (ObjectUtils.isEmpty(str)) {
                continue;
            }
            if (oneRow(pre + str)) {
                List<String> list = new ArrayList<>();
                list.addAll(encode(pre + str));
                dataMatrix.add(list);
                pre = "";
            } else {
                pre = str + CsvConstant.END_OF_LINE_SYMBOLS;
            }

        }
    }

    private boolean oneRow(String s) {
        return s.split(String.valueOf(CsvConstant.QUOTE_CHAR)).length % 2 == 1;
    }

    /**
     * 反解
     * csv文件默认以英文逗号,做为列分隔符换行符\n作为行分隔符，写入到一个.csv文件即可。含有英文逗号,和换行符会发生数据输出会出现混乱，下面列出一些处理方法。
     * 特殊字符处理
     * 1、含有英文逗号或换行符。这时可以使用双引号"来将该字段内容括起来，csv默认认为由""括起来的内容是一个栏位，
     * 这时不管栏位内容里有除"之外字符的任何字符都可以按原来形式引用；
     * 2、文本是长度超过13位的数值。这时csv默认为数值，会用科学记数法表示，可在前面加个tab键；
     * 3、文本中含有英文双引号时，需将"替换成两个双引号("")，csv会将字段里的两个双引号""显示成一个；
     */
    public List<String> encode(final String input) {
        List<String> list = new ArrayList<>();
        StringBuilder currentColumn = new StringBuilder();
        char delimiter = CsvConstant.DELIMITER;
        char quote = CsvConstant.QUOTE_CHAR;
        int hasQuotes = 0;
        char pre = ' ';
        for (char c : input.toCharArray()) {
            if (c == delimiter) {
                if (hasQuotes % 2 == 0) {
                    String value = currentColumn.toString();
                    if (!value.isEmpty() && value.endsWith(quote + "")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    list.add(value);
                    currentColumn = new StringBuilder();
                    hasQuotes = 0;
                } else {
                    currentColumn.append(c);
                }
            } else if (c == quote) {
                hasQuotes++;
                if (pre == quote) {
                    if (pre != quote || hasQuotes % 2 == 1) {
                        currentColumn.append(quote);
                    }

                }
            } else {
                currentColumn.append(c);
            }
            pre = c;
        }
        if (ObjectUtils.isNotEmpty(currentColumn.toString())) {
            String value = currentColumn.toString();
            if (!value.isEmpty() && value.endsWith(quote + "")) {
                value = value.substring(1, value.length() - 1);
            }
            list.add(value);
        }
        return list;
    }
}
