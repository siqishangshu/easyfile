package cn.mxsic.easyfile.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
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

import cn.mxsic.easyfile.base.AnnotationHelper;
import cn.mxsic.easyfile.base.DataTypeProcessor;
import cn.mxsic.easyfile.base.DocField;
import cn.mxsic.easyfile.base.FileType;
import cn.mxsic.easyfile.base.ScopeType;
import cn.mxsic.easyfile.exception.ImportException;
import cn.mxsic.easyfile.utils.ObjectUtils;

/**
 * @author siqishangshu
 */
public class ExcelImportHelper<T> extends DefaultHandler {


    private Class<T> tClass;

    private List<List<List<String>>> dataMatrix = new ArrayList<>();

    private List<T> data = new ArrayList<>();

    private int indexRow = 1;

    private Map<String, DocField> docFieldMap = new HashMap<>();

    private Workbook workbook;

    private String[] titles;
    /**
     * 设置保留多少位小数
     * 取消科学计数法
     */
    private static NumberFormat numberFormat = NumberFormat.getInstance();

    /**
     *
     * @param file
     * @param tClass
     * @param indexRow
     * @throws FileNotFoundException
     */
    public ExcelImportHelper(File file, FileType fileType, Class<T> tClass, int indexRow) {
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
        this.indexRow = indexRow - 1;
    }

    /**
     *
     * @param inputStream
     * @param fileType
     * @param tClass
     * @param indexRow
     * @throws IOException
     */
    public ExcelImportHelper(InputStream inputStream, FileType fileType, Class<T> tClass, int indexRow) {
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
        this.indexRow = indexRow - 1;
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
     * import
     */
    public List<T> importData() {
        try {
            numberFormat.setMaximumFractionDigits(20);
            numberFormat.setGroupingUsed(false);
            loadDataMatrix();
            return getResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ImportException(e);
        }
    }

    /**
     * import the date to T
     */
    public List<T> getData() {
        return this.data;
    }

    /**
     * 做数据映射
     */
    private List<T> getResult() throws IllegalAccessException {
        DocField[] docFields = AnnotationHelper.getAnnotationFields(this.tClass, ScopeType.IMPORT);
        for (List<List<String>> sheetMatrix : this.dataMatrix) {
            for (int i = 0; i < sheetMatrix.size(); i++) {
                if (i >= this.indexRow) {
                    T t = ObjectUtils.getInstance(this.tClass);
                    for (int j = 0; j < sheetMatrix.get(i).size(); j++) {
                        fillUp(t, j, sheetMatrix.get(i).get(j));
                    }
                    data.add(t);
                } else if (this.indexRow - 1 == i) {
                    List<String> firstRow = sheetMatrix.get(i);
                    this.titles = new String[firstRow.size()];
                    for (int j = 0; j < firstRow.size(); j++) {
                        if (ObjectUtils.isNotEmpty(firstRow.get(j))) {
                            this.titles[j] = firstRow.get(j);
                        }
                    }
                    for (DocField docField : docFields) {
                        if (ObjectUtils.isNotEmpty(docField)) {
                            if (ObjectUtils.isNotEmpty(docField.getTitle()) && docField.readTitle()) {
                                this.docFieldMap.put(docField.getTitle(), docField);
                            }
                            this.docFieldMap.put(docField.getField().getName(), docField);
                        }
                    }
                }
            }
        }
        return data;
    }

    /**
     * 填数据
     */
    private void fillUp(T t, int j, String v) throws IllegalAccessException {
        if (ObjectUtils.isEmpty(v)) {
            return;
        }
        String field = this.titles[j];
        DocField docField = this.docFieldMap.get(field);
        if (ObjectUtils.isEmpty(docField)) {
            return;
        }
        if (docField.readFormat()) {
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
            //第一行为标题
            if (sheet.getLastRowNum() > 0) {
                /**
                 * head line;
                 */
                Row head = sheet.getRow(0);
                int cellSize = head.getLastCellNum();
                for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    ArrayList<String> list = new ArrayList<>(cellSize);
                    for (int j = 0; j < cellSize; j++) {
                        //一行占位无数据。
                        if (ObjectUtils.isEmpty(row)) {
                            list.add(getCellStringVal(null));
                        } else {
                            //根据不同类型转化成字符串
                            list.add(getCellStringVal(row.getCell(j)));
                        }
                    }
                    sheetMatrix.add(list);
                }
                /**
                 * note this may lost the null value cell
                 */
//                for (Row row : sheet) {
//                    ArrayList<String> list = new ArrayList<>(row.getLastCellNum());
//                    for (Cell cell : row) {
//                        //根据不同类型转化成字符串
//                        list.add(getCellStringVal(cell));
//                    }
//                    sheetMatrix.add(list);
//                }
            }
            dataMatrix.add(sheetMatrix);
        }
    }

    private String getCellStringVal(Cell cell) {
        //无数据时
        if (ObjectUtils.isEmpty(cell)) {
            return "";
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
                CellValue cellValue = cell.getSheet().getWorkbook().getCreationHelper()
                        .createFormulaEvaluator().evaluate(cell);
                return cellValue.formatAsString();
            case BLANK:
                return "";
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            default:
                return "";
        }
    }
}
