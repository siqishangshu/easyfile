package com.siqishangshu.easyfile.base;

/**
 * Function: FileTypeSupportConstants <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-09-03 10:50:00
 */
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
     * @param fileName
     * @return
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
