package cn.mxsic.easyfile.base;

/**
 * @author siqishangshu
 * @param <T>
 */
public interface Formatter<T> {

    /**
     * write to file
     * @param obj
     * @return
     */
    String write(T obj);

    /**
     * read from file
     * @param str
     * @return
     */
    T read(String str);

}
