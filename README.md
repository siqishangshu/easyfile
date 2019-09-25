# EasyFile


Documentation

    easyfile means import export so easy , you do not have to think to mush about poi csv itself.
    use easyfile you just need little annotation for your custom file.
    this project is still on dev.

Dependence:

    lombok 1.16.18
    junit 4.12
    poi 3.17

Quick Start:

    your op entry is Man.java;
    user file for example and helper also support input stream import

    Import:
        csv
                String filePath = "../.../xxx.csv";
                CsvImportHelper<Man> csvImportHelper = new CsvImportHelper<>(new File(filePath), Man.class);
                List<Man> mans = csvImportHelper.importData();
        excel
                String filePath = "../.../xxx.csv";
                ExcelImportHelper<Man> excelImportHelper = new ExcelImportHelper<>(new File(filePath), Man.class);
                List<Man> mans = excelImportHelper.importData();
     Export:
        csv
                 CsvExportHelper<Man> csvExportHelper = new CsvExportHelper<>(Man.class);
                 /..List<Man> list ../
                 File file = csvExportHelper.export(list);
        excel
                ExcelExportHelper<Man> excelExportHelper = new ExcelExportHelper<>(new File(filePath), Man.class);
                 /..List<Man> list ../
                File file = excelExportHelper.export(list);


TODO:
    1：将多个sheet 合并到同一个文件里
    2：将多个文件合并成一个大文件。
    3：每个sheet做为一个文件导入，减少内存压力。
    4：允许每个sheet都不一样，则能满足复制表格导出。