package cn.mxsic.easyfile.exception;


/**
 * @author siqishangshu
 */
public class ExportException  extends  RuntimeException {


    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }


    public ExportException(Throwable cause) {
        super(cause);
    }



    public ExportException(String message) {
        super(message);
    }
}
