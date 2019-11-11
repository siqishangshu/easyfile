package cn.mxsic.easyfile.base;


import org.apache.poi.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cn.mxsic.easyfile.exception.ExportException;

/**
 * Function: FilesZipper <br>
 *
 * @author: siqishangshu <br>
 * @date: 2019-09-05 17:15:00
 */
public class FilesZipper {
    /**
     * 合并结果
     */
    private File file;

    /**
     * the final file;
     */
    public File getFile() {
        return file;
    }

    /**
     *
     * @param prefix
     * @param fileList
     * @return
     */
    public File zip(String prefix, List<File> fileList) {
        FileOutputStream fous = null;
        ZipOutputStream zipos = null;
        try {
            this.file = File.createTempFile(prefix.length() < 3 ? prefix + "000" : prefix
                    , FileType.ZIP.getSuffix());
            fous = new FileOutputStream(file);
            zipos = new ZipOutputStream(new BufferedOutputStream(fous));
            for (File f : fileList) {
                zipFile(f, zipos);
            }
        } catch (Exception e) {
            throw new ExportException("导出文件失败！", e);
        } finally {
            if (!Objects.isNull(zipos)) {
                IOUtils.closeQuietly(zipos);
            }
            if (!Objects.isNull(fous)) {
                IOUtils.closeQuietly(fous);
            }
            return this.file;
        }
    }

    private void zipFile(File inputFile, ZipOutputStream outputStream) {
        if (inputFile.exists()) {
            if (inputFile.isFile()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(inputFile);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, 1024);
                    ZipEntry entry = new ZipEntry(inputFile.getName());
                    outputStream.putNextEntry(entry);
                    IOUtils.copy(bufferedInputStream, outputStream);
                    bufferedInputStream.close();
                    fileInputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    File[] files = inputFile.listFiles();
                    for (File temp : files) {
                        zipFile(temp, outputStream);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
