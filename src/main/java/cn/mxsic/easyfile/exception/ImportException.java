package cn.mxsic.easyfile.exception;

/**
 * @author siqishangshu
 */
public class ImportException extends RuntimeException {


    public ImportException(String message, Throwable cause) {
        super(message, cause);
    }


    public ImportException(Throwable cause) {
        super(cause);
    }


    public ImportException(String message) {
        super(message);
    }
}
