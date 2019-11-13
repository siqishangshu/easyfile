package cn.mxsic.easyfile.excel;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import cn.mxsic.easyfile.annotation.ScopeType;
import cn.mxsic.easyfile.base.AnnotationHelper;
import cn.mxsic.easyfile.base.DocField;
import cn.mxsic.easyfile.base.FileType;
import cn.mxsic.easyfile.base.Preprocessor;
import cn.mxsic.easyfile.exception.ExportException;
import cn.mxsic.easyfile.utils.EasyUtils;

/**
 * @author siqishangshu
 */
public class ExcelExportHelper<T> {

    /**
     * 每每个sheet最大行数 65535
     */
    private static final int MAX_SHEET_RECORDS_COUNT = 65535;
    private static final int MAX_SHEET = 3;
    /**
     * input date type
     */
    private Class<T> tClass;
    /**
     * file prefix name;
     */
    private String suffix;
    /**
     * 导出结果
     */
    private File file;
    /**
     * 文件名
     */
    private String prefix;

    /**
     * 获取注解
     */
    private DocField[] docFields;
    /**
     * 描述信息
     */
    private List<List<String>> describes = new ArrayList<>();
    /**
     * data
     */
    private List<T> data = new ArrayList<>();
    /**
     * preprocessor after format
     */
    private Preprocessor preprocessor;

    /**
     * all the data must be string once
     */
    private List<List<List<String>>> dataMatrix = new ArrayList<>();


    public ExcelExportHelper(Class<T> tClass, String prefix, FileType fileType) {
        this.tClass = tClass;
        this.prefix = prefix.length() < 3 ? prefix + "000" : prefix;
        this.suffix = "." + fileType.getType();

    }

    public ExcelExportHelper(Class<T> tClass, FileType fileType) {
        this.tClass = tClass;
        this.prefix = tClass.getSimpleName().length() < 3 ? tClass.getSimpleName() + "000" : tClass.getSimpleName();
        this.suffix = "." + fileType.getType();

    }

    public ExcelExportHelper(Class<T> tClass) {
        this.tClass = tClass;
        this.prefix = tClass.getSimpleName().length() < 3 ? tClass.getSimpleName() + "000" : tClass.getSimpleName();
        this.suffix = "." + FileType.XLSX.getType();
    }

    public void setDescribe(List<List<String>> describes) {
        this.describes = describes;
    }

    private void loadMatrix() throws IllegalAccessException {
        this.docFields = AnnotationHelper.getAnnotationFields(tClass, ScopeType.EXPORT);
        List<List<String>> sheet = new ArrayList<>();
        this.dataMatrix.add(sheet);
        /**
         * 描述信息头。
         */
        for (List<String> describe : describes) {
            List<String> desc = new ArrayList<>();
            for (int i = 0; i < describe.size(); i++) {
                desc.add(describe.get(i));
            }
            sheet.add(desc);
        }

        String[] title = AnnotationHelper.getHeadFieldTitles(this.docFields);
        List<String> titleRow = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            titleRow.add(title[i]);
        }
        sheet.add(titleRow);
        for (T t : data) {
            if (sheet.size() == MAX_SHEET_RECORDS_COUNT) {
                //创建sheet
                sheet = new ArrayList<>();
                this.dataMatrix.add(sheet);
                //在sheet第一行写出表单的各个字段名
                titleRow = new ArrayList<>();
                for (int i = 0; i < title.length; i++) {
                    titleRow.add(title[i]);
                }
                sheet.add(titleRow);
            }
            sheet.add(writeRow(t));
        }
    }

    /**
     * 导出文件
     * NOTE:max size 65535*3
     */
    public File export(List<T> data) {
        if (data != null && data.size() + describes.size() > MAX_SHEET_RECORDS_COUNT * MAX_SHEET) {
            throw new ExportException("Over export date max size:[" + MAX_SHEET_RECORDS_COUNT * MAX_SHEET + "]");
        }
        Workbook workbook;
        if (this.suffix.toLowerCase().endsWith(FileType.XLSX.getType())) {
            workbook = new SXSSFWorkbook();
        } else if (this.suffix.toLowerCase().endsWith(FileType.XLS.getType())) {
            workbook = new HSSFWorkbook();
        } else {
            throw new ExportException("UnSupport file type");
        }
        this.data = data;
        try {
            loadMatrix();
            if (preprocessor != null) {
                this.dataMatrix = this.preprocessor.process(this.dataMatrix);
            }
            return generate(workbook);
        } catch (Exception e) {
            throw new ExportException(e);
        } finally {
            IOUtils.closeQuietly(workbook);
        }
    }

    private File generate(Workbook workbook) throws IOException {
        FileOutputStream fos;
        for (int i = 0; i < this.dataMatrix.size(); i++) {
            Sheet sheet = workbook.createSheet("Page_" + i);
            for (int j = 0; j < this.dataMatrix.get(i).size(); j++) {
                Row row = sheet.createRow(j);
                for (int k = 0; k < this.dataMatrix.get(i).get(j).size(); k++) {
                    row.createCell(k).setCellValue(this.dataMatrix.get(i).get(j).get(k));
                }
            }
        }
        //写入到文件流中
        this.file = File.createTempFile(this.prefix, this.suffix);
        fos = new FileOutputStream(file);
        workbook.write(fos);
        return this.file;
    }

    /**
     * 获取导出文件
     */
    public File getFile() {
        return this.file;
    }

    /**
     * 设置预处理器
     */
    public void setPreprocessor(Preprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }

    /**
     * 清理文件
     */
    public void clear() {
        if (this.file != null) {
            try {
                Files.delete(this.file.toPath());
            } catch (IOException e) {
                //do nothing
            }
        }
    }


    /**
     * 写入一行
     */
    private List<String> writeRow(T t) throws IllegalAccessException {
        List<String> row = new ArrayList<>();
        for (int i = 0; i < this.docFields.length; i++) {
            DocField docField = this.docFields[i];

            if (EasyUtils.isNotEmpty(docField)) {
                String value;
                if (docField.exportFormat()) {
                    value = docField.getFormatter().write(docField.getField().get(t));
                } else {
                    value = docField.getField().get(t) == null ? "" : String.valueOf(docField.getField().get(t));
                }
                row.add(value);
            }
        }
        return row;
    }
}
