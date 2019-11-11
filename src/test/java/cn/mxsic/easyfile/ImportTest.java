package cn.mxsic.easyfile;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.mxsic.easyfile.base.FileType;
import cn.mxsic.easyfile.csv.CsvExportHelper;
import cn.mxsic.easyfile.csv.CsvImportHelper;
import cn.mxsic.easyfile.excel.ExcelExportHelper;
import cn.mxsic.easyfile.excel.ExcelImportHelper;
import cn.mxsic.easyfile.utils.DateUtils;

/**
 * Function: ImportMain <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-08-26 11:08:00
 */
public class ImportTest {

    public static void main(String[] args) {
//        excelImport();
        List<Man> manList = generalMan(65530 * 10);
        System.out.println(DateUtils.getNow());
        excelExport(manList);
        System.out.println(DateUtils.getNow());
//        csvExport(manList);
//        csvImport();

//        List<File> fileList = new ArrayList<>();
//        fileList.add(new File("/Users/siqishangshu/temp/export/1.xlsx"));
//        fileList.add(new File("/Users/siqishangshu/temp/export/2.xlsx"));
//        FilesZipper multiFileMerge = new FilesZipper();
//        File file = multiFileMerge.zip("ASDS", fileList);
//        System.out.println(file.getAbsolutePath());
    }

    private static void csvImport() {
        String filePath = "/Users/siqishangshu/Desktop/000.csv";
        System.out.println(new Date() + filePath);
        File file = new File(filePath);
        CsvImportHelper<Man> manCsvImportHelper = new CsvImportHelper<>(file, Man.class, 2);
        manCsvImportHelper.importData();
        System.out.println(new Date() + "excel size:" + manCsvImportHelper.getData().size());
    }

    private static void csvExport(List<Man> manList) {
        System.out.println("start export ");
        CsvExportHelper<Man> csvExportHelper = new CsvExportHelper<>(Man.class);
        csvExportHelper.export(manList);
        System.out.println(csvExportHelper.getFile().getAbsolutePath());
//        csvExportHelper.clear();
    }


    private static void excelImport() {
        String filePath = "/Users/siqishangshu/Desktop/333.xlsx";
        System.out.println(new Date() + filePath);
        File file = new File(filePath);
        ExcelImportHelper<Man> manExcelImportHelper = new ExcelImportHelper<>(file, FileType.getFileType(file.getName()), Man.class);
        manExcelImportHelper.setFirstSheetHeadLine(5);
        manExcelImportHelper.setEverySheetHaveSameHead(false);
        manExcelImportHelper.importData();
        System.out.println(new Date() + "excel size:" + manExcelImportHelper.getData().size());
    }

    private static void excelExport(List<Man> manList) {
        System.out.println("start export ");
        ExcelExportHelper<Man> excelExportHelper = new ExcelExportHelper<>(Man.class, FileType.XLSX);
        List<List<String>> lists = new ArrayList<>();
        List<String> list = new ArrayList<>();
        list.add("这是一个人的导出表格");
        lists.add(list);
        list = new ArrayList<>();
        list.add("每一条都是一个属性");
        list.add("有些有转义");
        lists.add(list);
        list = new ArrayList<>();
        list.add("注：每一页最多120行");
        list.add("注释信息也占行数据");
        lists.add(list);
        excelExportHelper.setDescribe(lists);
        excelExportHelper.export(manList);
        System.out.println(excelExportHelper.getFile().getAbsolutePath());
//        excelExportHelper.clear();
    }


    private static List<Man> generalMan(int size) {
        List<Man> manList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Body body = new Body();
            body.setFoot("foot" + ((char) i));
            body.setHead("Head" + ((char) i));
            body.setHigh(i);
            body.setState(i % 2 == 1);
            if (i % 2 == 0) {
                manList.add(new Man("ALi,''" + i, 12 + i, i % 2 == 1, DateUtils.getNow(), body, "tech", "tech"));
            } else if (i % 3 == 0) {
                manList.add(new Man("Bob\"\"" + i, 12 + i, i % 2 == 1, DateUtils.getNow(), body, "singer", "singer"));
            } else if (i % 5 == 0) {
                manList.add(new Man("Chalie\n  " + i, 12 + i, i % 2 == 1, DateUtils.getNow(), body, "writer", "writer"));
            } else if (i % 7 == 0) {
                manList.add(new Man("Domen \r\n" + i, 12 + i, i % 2 == 1, DateUtils.getNow(), body, "worker", "worker"));
            } else {
                manList.add(new Man("Tom]" + i, 12 + i, i % 2 == 1, DateUtils.getNow(), body, "loser", "loser"));
            }
        }
        return manList;
    }


}
