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

高级功能：


    以下注解都在POJO的属性上：

        @Title("姓名") 表头的名称
        @Cols(1)  表头的顺序
        @Format(value = ManGoodFormater.class) 导入导出数据格式化（Formatter 实现其接口的方法）
        @Transient 临时数据，可配置选择是否导入，导出。

    FilesZipper
    支持多个文件合并成zip。解决大文件每次导出一个，最后再一次压缩，解决多个不同文件的导出。

    ExcelImportHelper
    提供了，头行参参数，setFirstSheetHeadLine，并可以通过setEverySheetHaveSameHead设置每一个sheet是否相同。

    ExcelImportHelper，ExcelExportHelper
    提供了Preprocessor 注入功能，通过setPreprocessor，可以在导入后生成前，导出后行成前对数据矩阵进行加工处理。
