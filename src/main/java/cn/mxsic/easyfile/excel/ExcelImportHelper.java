package cn.mxsic.easyfile.excel;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mxsic.easyfile.annotation.ScopeType;
import cn.mxsic.easyfile.base.AnnotationHelper;
import cn.mxsic.easyfile.base.DataTypeProcessor;
import cn.mxsic.easyfile.base.DocField;
import cn.mxsic.easyfile.base.EasyConstant;
import cn.mxsic.easyfile.base.FileType;
import cn.mxsic.easyfile.base.Preprocessor;
import cn.mxsic.easyfile.exception.ImportException;
import cn.mxsic.easyfile.utils.EasyUtils;

/**
 * @author siqishangshu
 */
public class ExcelImportHelper<T> extends DefaultHandler {


    private Class<T> tClass;

    private List<List<List<String>>> dataMatrix = new ArrayList<>();

    private List<T> data = new ArrayList<>();

    private List<List<T>> sheetData = new ArrayList<>();

    private int headRow = 0;

    private boolean sameHead = false;

    private Map<String, DocField> docFieldMap = new HashMap<>();

    private Workbook workbook;

    private String[] titles;

    private Preprocessor preprocessor;

    /**
     * 设置保留多少位小数
     * 取消科学计数法
     */
    private static NumberFormat numberFormat = NumberFormat.getInstance();

    /**
     *
     * @param file
     * @param tClass
     * @param headRow
     * @throws FileNotFoundException
     */
    @Deprecated
    public ExcelImportHelper(File file, FileType fileType, Class<T> tClass, int headRow) {
        try {
            if (fileType.equals(FileType.XLS)) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
                IOUtils.closeQuietly(workbook);
            } else if (fileType.equals(FileType.XLSX)) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
                IOUtils.closeQuietly(workbook);
            } else {
                throw new ImportException("UnSupport file type");
            }
        } catch (IOException e) {
            throw new ImportException(e);
        }
        this.tClass = tClass;
        this.headRow = headRow - 1;
    }


    /**
     *
     * @param inputStream
     * @param fileType
     * @param tClass
     * @param headRow
     * @throws IOException
     */
    @Deprecated
    public ExcelImportHelper(InputStream inputStream, FileType fileType, Class<T> tClass, int headRow) {
        try {
            if (fileType.equals(FileType.XLS)) {
                workbook = new HSSFWorkbook(inputStream);
                IOUtils.closeQuietly(workbook);
            } else if (fileType.equals(FileType.XLSX)) {
                workbook = new XSSFWorkbook(inputStream);
                IOUtils.closeQuietly(workbook);
            } else {
                throw new ImportException("UnSupport file type");
            }
        } catch (IOException e) {
            throw new ImportException(e);
        }
        this.tClass = tClass;
        this.headRow = headRow - 1;
    }

    /**
     *
     * @param file
     * @param tClass
     * @throws FileNotFoundException
     */
    public ExcelImportHelper(File file, FileType fileType, Class<T> tClass) {
        try {
            if (fileType.equals(FileType.XLS)) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
                IOUtils.closeQuietly(workbook);
            } else if (fileType.equals(FileType.XLSX)) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
                IOUtils.closeQuietly(workbook);
            } else {
                throw new ImportException("UnSupport file type");
            }
        } catch (IOException e) {
            throw new ImportException(e);
        }
        this.tClass = tClass;
    }

    /**
     *
     * @param inputStream
     * @param tClass
     * @throws FileNotFoundException
     */
    public ExcelImportHelper(InputStream inputStream, FileType fileType, Class<T> tClass) {
        try {
            if (fileType.equals(FileType.XLS)) {
                workbook = new HSSFWorkbook(inputStream);
                IOUtils.closeQuietly(workbook);
            } else if (fileType.equals(FileType.XLSX)) {
                workbook = new XSSFWorkbook(inputStream);
                IOUtils.closeQuietly(workbook);
            } else {
                throw new ImportException("UnSupport file type");
            }
        } catch (IOException e) {
            throw new ImportException(e);
        }
        this.tClass = tClass;
    }

    /**
     * 设置预处理器
     */
    public void setPreprocessor(Preprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }

    /**
     * import
     */
    public List<T> importData() {
        try {
            numberFormat.setMaximumFractionDigits(20);
            numberFormat.setGroupingUsed(false);
            loadDataMatrix();
            if (preprocessor != null) {
                this.dataMatrix = this.preprocessor.process(this.dataMatrix);
            }
            return getResult();
        } catch (Exception e) {
            throw new ImportException(e);
        }
    }

    /**
     * set if all the sheet have the same format head or not
     */
    public void setEverySheetHaveSameHead(boolean sameHead) {
        this.sameHead = sameHead;
    }

    /**
     * set first sheet head format data start line index
     */
    public void setFirstSheetHeadLine(int headRow) {
        this.headRow = headRow - 1;
    }

    /**
     * import the date to T
     */
    public List<T> getData() {
        return this.data;
    }

    /**
     * import data as sheet
     */
    public List<List<T>> getSheetData() {
        return this.sheetData;
    }

    /**
     * 做数据映射
     */
    private List<T> getResult() throws IllegalAccessException {
        DocField[] docFields = AnnotationHelper.getAnnotationFields(this.tClass, ScopeType.IMPORT);
        for (DocField docField : docFields) {
            if (EasyUtils.isNotEmpty(docField)) {
                if (EasyUtils.isNotEmpty(docField.getTitle()) && docField.importTitle()) {
                    this.docFieldMap.put(docField.getTitle(), docField);
                }
                this.docFieldMap.put(docField.getField().getName(), docField);
            }
        }
        for (int i = 0; i < this.dataMatrix.size(); i++) {
            int sheetHeadLine = 1;
            if (i == 0) {
                List<String> head = this.dataMatrix.get(i).get(this.headRow);
                this.titles = new String[head.size()];
                for (int j = 0; j < head.size(); j++) {
                    if (EasyUtils.isNotEmpty(head.get(j))) {
                        this.titles[j] = head.get(j);
                    }
                }
                sheetHeadLine = this.headRow + 1;
            }
            if (this.sameHead) {
                sheetHeadLine = this.headRow + 1;
            }
            List<T> list = new ArrayList<>();
            for (int j = sheetHeadLine; j < this.dataMatrix.get(i).size(); j++) {
                T t = EasyUtils.getInstance(this.tClass);
                for (int k = 0; k < this.dataMatrix.get(i).get(j).size(); k++) {
                    fillUp(t, k, this.dataMatrix.get(i).get(j).get(k));
                }
                list.add(t);
                data.add(t);
            }
            sheetData.add(list);
        }
        return data;
    }

    /**
     * 填数据
     */
    private void fillUp(T t, int j, String v) throws IllegalAccessException {
        if (EasyUtils.isEmpty(v)) {
            return;
        }
        String field = this.titles[j];
        DocField docField = this.docFieldMap.get(field);
        if (EasyUtils.isEmpty(docField)) {
            return;
        }
        if (docField.importFormat()) {
            docField.getField().set(t, docField.getFormatter().read(v));
        } else {
            docField.getField().set(t, DataTypeProcessor.handle(v, docField.getField().getType().getSimpleName()));
        }
    }


    /**
     * 获取所有行数据
     */
    private void loadDataMatrix() {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            //读取第一个工作页sheet
            ArrayList<List<String>> sheetMatrix = new ArrayList<>();
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getLastRowNum() > 0) {
                //第一行为标题
                Row head = sheet.getRow(this.headRow);
                int cellSize = head.getLastCellNum();
                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    ArrayList<String> list = new ArrayList<>(cellSize);
                    for (int j = 0; j < cellSize; j++) {
                        //一行占位无数据。
                        if (EasyUtils.isEmpty(row)) {
                            list.add(getCellStringVal(null));
                        } else {
                            //根据不同类型转化成字符串
                            list.add(getCellStringVal(row.getCell(j)));
                        }
                    }
                    sheetMatrix.add(list);
                }
            }
            dataMatrix.add(sheetMatrix);
        }
    }

    private String getCellStringVal(Cell cell) {
        //无数据时
        if (EasyUtils.isEmpty(cell)) {
            return EasyConstant.EMPTY;
        }
        CellType cellType = cell.getCellTypeEnum();
        switch (cellType) {
            case NUMERIC:
                return numberFormat.format(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return EasyConstant.EMPTY;
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            default:
                return EasyConstant.EMPTY;
        }
    }
}
