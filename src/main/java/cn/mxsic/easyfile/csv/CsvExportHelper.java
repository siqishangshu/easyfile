package cn.mxsic.easyfile.csv;

import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import cn.mxsic.easyfile.base.AnnotationHelper;
import cn.mxsic.easyfile.base.EasyField;
import cn.mxsic.easyfile.base.EasyFileConstant;
import cn.mxsic.easyfile.base.ScopeType;
import cn.mxsic.easyfile.exception.ExportException;
import cn.mxsic.easyfile.utils.ObjectUtils;

/**
 * @author siqishangshu
 */
public class CsvExportHelper<T> {


    private static final int MAX_RECORDS_COUNT = 65535 * 3;
    private Class<T> tClass;
    /**
     * 导出记录保存的文件
     */
    private File file;
    /**
     * file name prefix
     */
    private String prefix;
    /**
     * 导出标题，及字段信息
     */
    private EasyField[] docFields;

    /**
     * writer
     */
    private FileWriter writer;
    /**
     * titles
     */
    private String[] titles;

    public CsvExportHelper(Class<T> cls, String prefix) {
        this.tClass = cls;
        this.prefix = prefix.length() < 3 ? prefix + "000" : prefix;
    }

    public CsvExportHelper(Class<T> cls) {
        this.tClass = cls;
        this.prefix = tClass.getSimpleName().length() < 3 ? tClass.getSimpleName() + "000" : tClass.getSimpleName();
    }


    /**
     * export data to csv file
     * NOTE:max size 65535*3
     */
    public File export(List<T> data) throws ExportException {
        if (data != null && data.size() > MAX_RECORDS_COUNT) {
            throw new ExportException("Over export date max size:[" + MAX_RECORDS_COUNT + "]");
        }
        try {

            this.docFields = AnnotationHelper.getAnnotationFields(tClass, ScopeType.EXPORT);
            this.titles = AnnotationHelper.getHeadFieldTitles(this.docFields);
            this.file = File.createTempFile(this.prefix, EasyFileConstant.Csv.SUFFIX);
            writer = new FileWriter(file);
            /**
             * write the title
             */
            writeRow(this.titles);
            for (T t : data) {
                fullUp(t);
            }
            return this.file;
        } catch (Exception e) {
            throw new ExportException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * 获取导出文件
     */
    public File getFile() {
        return this.file;
    }


    /**
     * 清理文件
     */
    public void clear() {
        if(this.file !=null){
            try {
                Files.delete(this.file.toPath());
            } catch (IOException e) {
                //do nothing
            }
        }
    }
    /**
     * write one fullUP data
     */
    private void fullUp(T t) throws IOException, IllegalAccessException {
        String[] cells = new String[this.docFields.length];
        for (int i = 0; i < this.docFields.length; i++) {
            EasyField docField = this.docFields[i];
            if (ObjectUtils.isNotEmpty(docField)) {
                String value;
                if (docField.writeFormat()) {
                    value = docField.getFormatter().write(docField.getField().get(t));
                } else {
                    value = String.valueOf(docField.getField().get(t));
                }
                cells[i] = value;
            } else {
                cells[i] = EasyFileConstant.Csv.EMPTY;
            }
        }
        writeRow(cells);
    }

    private void writeRow(String[] cells) throws IOException {
        for (int i = 0; i < cells.length; i++) {
            writer.write(encode(cells[i]));
            if (i < cells.length - 1) {
                writer.write(EasyFileConstant.Csv.DELIMITER);
            }
        }
        writer.write(EasyFileConstant.Csv.END_OF_LINE_SYMBOLS);
    }

    /**
     * csv文件默认以英文逗号,做为列分隔符换行符\n作为行分隔符，写入到一个.csv文件即可。含有英文逗号,和换行符会发生数据输出会出现混乱，下面列出一些处理方法。
     * 特殊字符处理
     * 1、含有英文逗号或换行符。这时可以使用双引号"来将该字段内容括起来，csv默认认为由""括起来的内容是一个栏位，
     * 这时不管栏位内容里有除"之外字符的任何字符都可以按原来形式引用；
     * 2、文本是长度超过13位的数值。这时csv默认为数值，会用科学记数法表示，可在前面加个tab键；
     * 3、文本中含有英文双引号时，需将"替换成两个双引号("")，csv会将字段里的两个双引号""显示成一个；
     */
    public String encode(final String input) {
        StringBuilder currentColumn = new StringBuilder();
        char delimiter = EasyFileConstant.Csv.DELIMITER;
        char quote = EasyFileConstant.Csv.QUOTE_CHAR;
        String endOfLineSymbols = EasyFileConstant.Csv.END_OF_LINE_SYMBOLS;
        int lastCharIndex = input.length() - 1;
        boolean quotesRequiredForSpecialChar = false;
        boolean skipNewline = false;
        for (int i = 0; i <= lastCharIndex; i++) {
            final char c = input.charAt(i);
            if (skipNewline) {
                skipNewline = false;
                if (c == '\n') {
                    continue;
                }
            }
            if (c == delimiter) {
                quotesRequiredForSpecialChar = true;
                currentColumn.append(c);
            } else if (c == quote) {
                quotesRequiredForSpecialChar = true;
                currentColumn.append(quote);
                currentColumn.append(quote);
            } else if (c == '\r') {
                quotesRequiredForSpecialChar = true;
                /**
                 * remove \r may cause problem in windows
                 */
            } else if (c == '\n') {
                skipNewline = true;
                quotesRequiredForSpecialChar = true;
                currentColumn.append(endOfLineSymbols);
            } else {
                currentColumn.append(c);
            }
        }
        final boolean quotesRequiredForSurroundingSpaces = input.length() > 0 && (input.charAt(0) == ' ' || input.charAt(input.length() - 1) == ' ');
        if (quotesRequiredForSpecialChar || quotesRequiredForSurroundingSpaces) {
            currentColumn.insert(0, quote).append(quote);
        }

        return currentColumn.toString();
    }
}
