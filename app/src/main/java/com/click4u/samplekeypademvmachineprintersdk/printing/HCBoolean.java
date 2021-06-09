package com.click4u.samplekeypademvmachineprintersdk.printing;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HCBoolean {
    public HCBoolean() {
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String) {
            return ((String)obj).trim().equals("");
        } else if (obj instanceof Collection) {
            Collection<?> coll = (Collection)obj;
            return coll.size() == 0;
        } else if (obj instanceof Map) {
            Map<?, ?> map = (Map)obj;
            return map.size() == 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(String source) {
        return source == null || source.trim().isEmpty();
    }

    public static boolean isNotEmpty(String source) {
        return source != null && !source.trim().isEmpty();
    }

    public static boolean isEmpty(String[] source) {
        return source == null || source.length == 0;
    }

    public static boolean isNotEmpty(String[] source) {
        return source != null && source.length > 0;
    }

    public static boolean isEmpty(List<Object> source) {
        return source == null || source.size() == 0;
    }

    public static boolean isNotEmpty(List<Object> source) {
        return source != null && source.size() > 0;
    }

    public static boolean isNotNull(Object source) {
        return source != null;
    }

    public static boolean isNull(Object source) {
        return source == null;
    }

    public static boolean isWXWeb(String agent) {
        if (isEmpty(agent)) {
            return false;
        } else {
            return agent.contains("MicroMessenger");
        }
    }

    public static boolean isAliWeb(String agent) {
        if (isEmpty(agent)) {
            return false;
        } else {
            return agent.contains("Alipay");
        }
    }

    public static boolean isWXQR(String agent) {
        if (isEmpty(agent)) {
            return false;
        } else {
            return agent.startsWith("13");
        }
    }

    public static boolean isAliQR(String agent) {
        if (isEmpty(agent)) {
            return false;
        } else {
            return agent.startsWith("28");
        }
    }
}
