package com.juli0mendes.capitalgainsms.application.common;

import org.apache.commons.text.StringEscapeUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ScapeUtil {

    public static String scape(Object object) {
        return StringEscapeUtils.escapeJava(String.valueOf(object));
    }
}
