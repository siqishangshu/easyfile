package cn.mxsic.easyfile.base;

/**
 * Function: EasyFileConstant <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-09-03 14:10:00
 */
public class EasyFileConstant {

    public static class Csv {
        /**
         * empty value
         */
        public static final String EMPTY = "";
        /**
         * file suffix
         */
        public static final String SUFFIX = ".csv";
        /**
         * Ready to use configuration that should cover 99% of all usages.
         */
        public static final char DELIMITER = ',';
        /**
         * end of line
         */
        public static final String END_OF_LINE_SYMBOLS = "\n";

        /**
         * quote char
         */
        public static final char QUOTE_CHAR = '"';
    }

    public static class Excel {

        /**
         * empty value
         */
        public static final String EMPTY = "";


        public enum FileType {
            XLS("xls"),
            XLSX("xlsx");

            private String type;

            FileType(String type) {
                this.type = type;
            }

            public String getType() {
                return type;
            }

            /**
             * 通过文件名称获取文件类型
             */
            public static FileType getFileType(String fileName) {
                for (FileType fileType : FileType.values()) {
                    if (fileName.endsWith(fileType.type)) {
                        return fileType;
                    }
                }
                return null;
            }
        }

    }
}
