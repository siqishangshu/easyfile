package com.siqishangshu.easyfile.excel;

import com.siqishangshu.easyfile.base.AnnotationHelper;
import com.siqishangshu.easyfile.base.DocField;
import com.siqishangshu.easyfile.base.FileType;
import com.siqishangshu.easyfile.base.ScopeType;
import com.siqishangshu.easyfile.exception.ExportException;
import com.siqishangshu.easyfile.utils.ObjectUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

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
     * 写入的总记录数
     */
    private int rowCounter = 0;


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

    /**
     * 导出文件
     * NOTE:max size 65535*3
     */
    public File export(List<T> data) {
        if (data != null && data.size() > MAX_SHEET_RECORDS_COUNT * MAX_SHEET) {
            throw new ExportException("Over export date max size:[" + MAX_SHEET_RECORDS_COUNT * MAX_SHEET + "]");
        }
        this.docFields = AnnotationHelper.getAnnotationFields(tClass, ScopeType.EXPORT);
        Workbook workbook;
        FileOutputStream fos;
        if (this.suffix.toLowerCase().endsWith(FileType.XLSX.getType())) {
            workbook = new XSSFWorkbook();
        } else if (this.suffix.toLowerCase().endsWith(FileType.XLS.getType())) {
            workbook = new HSSFWorkbook();
        } else {
            throw new ExportException("UnSupport file type");
        }
        try {
            int sheetCounter = 1;
            Sheet sheet = workbook.createSheet("sheet-" + sheetCounter);
            this.rowCounter = 0;
            String[] title = AnnotationHelper.getHeadFieldTitles(this.docFields);
            Row titleRow = sheet.createRow(this.rowCounter);
            for (int i = 0; i < title.length; i++) {
                titleRow.createCell(i).setCellValue(title[i]);
            }
            for (T t : data) {
                /**
                 * todo MultiFileMerge will solve the memory problem. every sheet as a file, in the end merge them.
                 */
                if (this.rowCounter != 0 && this.rowCounter % MAX_SHEET_RECORDS_COUNT == 0) {
                    sheetCounter++;
                    //创建sheet
                    sheet = workbook.createSheet("sheet-" + sheetCounter);
                    //在sheet第一行写出表单的各个字段名
                    this.rowCounter = 0;
                    titleRow = sheet.createRow(this.rowCounter);
                    for (int i = 0; i < title.length; i++) {
                        titleRow.createCell(i).setCellValue(title[i]);
                    }
                }
                this.rowCounter++;
                writeRow(t, sheet.createRow(this.rowCounter));
            }
            //写入到文件流中
            this.file = File.createTempFile(this.prefix, this.suffix);
            fos = new FileOutputStream(file);
            workbook.write(fos);
            return this.file;
        } catch (Exception e) {
            throw new ExportException(e);
        } finally {
            IOUtils.closeQuietly(workbook);
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
    private void writeRow(T t, Row row) throws IllegalAccessException {
        for (int i = 0; i < this.docFields.length; i++) {
            DocField docField = this.docFields[i];
            Cell cell = row.createCell(i);
            if (ObjectUtils.isNotEmpty(docField)) {
                String value;
                if (docField.writeFormat()) {
                    value = docField.getFormatter().write(docField.getField().get(t));
                } else {
                    value = String.valueOf(docField.getField().get(t));
                }
                cell.setCellValue(value);
            }
        }
    }
}
