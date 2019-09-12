package cn.mxsic.easyfile.base;

import cn.mxsic.easyfile.exception.ExportException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Function: MultiFileMerge <br>
 *
 * TODO 1：将多个sheet 合并到同一个文件里
 * TODO 2：将多个文件合并成一个大文件。
 * TODO 3：每个sheet做为一个文件导入，减少内存压力。
 * TODO 4：允许每个sheet都不一样，则能满足复制表格导出。
 *
 * @author: siqishangshu <br>
 * @date: 2019-09-05 17:15:00
 */
public class MultiFileMerge {
    /**
     * 合并结果
     */
    private File file;

    /**
     * the final file;
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     * TODO 尚未测试，莫要用。
     * the files must the same type
     * @param fileType
     * @param fileList
     * @param sheetNames
     * @return
     */
    @Deprecated
    public File merge(FileType fileType, List<File> fileList, List<String> sheetNames) {
        FileOutputStream out = null;
        ZipOutputStream zos = null;
        ZipFile zip = null;
        try {
            Workbook workbook;
            if (fileType.equals(FileType.XLSX)) {
                workbook = new XSSFWorkbook();
            } else if (fileType.equals(FileType.XLS)) {
                workbook = new HSSFWorkbook();
            } else {
                throw new ExportException("UnSupport file type");
            }
            List<String> sheetEntryName = new ArrayList<String>();
            Sheet sheet = null;
            String sheetRef = null;
            for (int i = 0; i < sheetNames.size(); i++) {
                sheet = workbook.createSheet();
                sheetRef = sheet.getSheetName();
                sheetEntryName.add(sheetRef.substring(1));
            }
            File tempWorkSheetFile = File.createTempFile("temp", "xlsx");
            FileOutputStream output = new FileOutputStream(tempWorkSheetFile);
            workbook.write(output);
            output.close();
            workbook.close();
            zip = new ZipFile(tempWorkSheetFile);
            out = new FileOutputStream(this.file);
            zos = new ZipOutputStream(out);
            Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
            while (en.hasMoreElements()) {
                ZipEntry ze = en.nextElement();
                if (!sheetNames.contains(ze.getName())) {
                    zos.putNextEntry(new ZipEntry(ze.getName()));
                    InputStream is = zip.getInputStream(ze);
                    IOUtils.copy(is, zos);
                    is.close();
                }
            }
            int i = 0;
            for (File file : fileList) {
                zos.putNextEntry(new ZipEntry(sheetNames.get(i)));
                i++;
                InputStream is = new FileInputStream(file);
                IOUtils.copy(is, zos);
                is.close();
            }
            zos.close();
            zip.close();
        } catch (Exception e) {
            throw new ExportException("导出创建临时文件失败！", e);
        } finally {
            if (!Objects.isNull(zos)) {
                IOUtils.closeQuietly(zos);
            }
            if (!Objects.isNull(zip)) {
                IOUtils.closeQuietly(zip);
            }
            if (!Objects.isNull(out)) {
                IOUtils.closeQuietly(out);
            }
        }
        return this.file;
    }
}
