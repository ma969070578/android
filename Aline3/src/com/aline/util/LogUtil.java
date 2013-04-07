package com.aline.util;

/** 
 * Utility class for LogCat.
 *
 * @author maq
 */
public class LogUtil {
    
    @SuppressWarnings("unchecked")
    public static String makeLogTag(Class cls) {
        return "_" + cls.getSimpleName();
    }

}
