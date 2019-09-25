package cn.mxsic.easyfile;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mxsic.easyfile.base.EasyFileConstant.Excel.FileType;
import cn.mxsic.easyfile.csv.CsvExportHelper;
import cn.mxsic.easyfile.csv.CsvImportHelper;
import cn.mxsic.easyfile.excel.ExcelExportHelper;
import cn.mxsic.easyfile.excel.ExcelImportHelper;

/**
 * Function: ImportMain <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-08-26 11:08:00
 */
public class ImportTest {

    @Test
    public void test() {
//        excelCusImport();
        excelImport();
//        List<Man> manList = generalMan(195);
//        excelExport(manList);
//        csvExport(manList);
//        csvImport();
    }

    private void csvImport() {
        String filePath = "/Users/siqishangshu/Desktop/fromexcel.csv";
        System.out.println(new Date() + filePath);
        File file = new File(filePath);
        CsvImportHelper<Man> manCsvImportHelper = new CsvImportHelper<>(file, Man.class);
        manCsvImportHelper.importData();
        System.out.println(new Date() + "excel size:" + manCsvImportHelper.getData().size());
    }

    private void csvExport(List<Man> manList) {
        System.out.println("start export ");
        CsvExportHelper<Man> csvExportHelper = new CsvExportHelper<>(Man.class);
        csvExportHelper.export(manList);
        System.out.println(csvExportHelper.getFile().getAbsolutePath());
//        csvExportHelper.clear();
    }


    private void excelImport() {
        String filePath = "/Users/siqishangshu/Desktop/111.xlsx";
        System.out.println(new Date() + filePath);
        File file = new File(filePath);
        ExcelImportHelper<Man> manExcelImportHelper = new ExcelImportHelper<>(file, FileType.getFileType(file.getName()), Man.class);
        manExcelImportHelper.importData();
        System.out.println(new Date() + "excel size:" + manExcelImportHelper.getData().size());
    }

    private void excelCusImport() {
        String filePath = "/Users/siqishangshu/Desktop/cus.xlsx";
        System.out.println(new Date() + filePath);
        File file = new File(filePath);
        ExcelImportHelper<Customer> manExcelImportHelper = new ExcelImportHelper<>(file, FileType.getFileType(file.getName()), Customer.class);
        manExcelImportHelper.importData();
        System.out.println(new Date() + "excel size:" + manExcelImportHelper.getData().size());
    }

    private void excelExport(List<Man> manList) {
        System.out.println("start export ");
        ExcelExportHelper<Man> excelExportHelper = new ExcelExportHelper<>(Man.class, FileType.XLSX);
        excelExportHelper.export(manList);
        System.out.println(excelExportHelper.getFile().getAbsolutePath());
//        excelExportHelper.clear();
    }


    private List<Man> generalMan(int size) {
        List<Man> manList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                manList.add(new Man("ALi,''" + i, 12 + i, i % 2 == 1, new Date(), "tech", "tech"));
            } else if (i % 3 == 0) {
                manList.add(new Man("Bob\"\"" + i, 12 + i, i % 2 == 1, new Date(), "singer", "singer"));
            } else if (i % 5 == 0) {
                manList.add(new Man("Chalie\n  " + i, 12 + i, i % 2 == 1, new Date(), "writer", "writer"));
            } else if (i % 7 == 0) {
                manList.add(new Man("Domen \r\n" + i, 12 + i, i % 2 == 1, new Date(), "worker", "worker"));
            } else {
                manList.add(new Man("Tom]" + i, 12 + i, i % 2 == 1, new Date(), "loser", "loser"));
            }
        }
        return manList;
    }
}
