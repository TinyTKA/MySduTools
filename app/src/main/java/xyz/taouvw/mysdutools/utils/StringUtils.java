package xyz.taouvw.mysdutools.utils;

import java.util.Locale;

public class StringUtils {
    private static final String n = "null";

    /**
     * 去除所有空格
     *
     * @param s：用户输入的参数
     * @return s1：去除空格后的字符串
     */
    public static String removeEmptyAndToUp(String s) {
        String s1 = s.replaceAll("\\s*", "");
        return s1.toUpperCase(Locale.ROOT);
    }

    /**
     * 将去除空格后的字符串处理后查找对应properties的属性值
     *
     * @param s
     * @return
     */
    public static String findMatch(String s) {
        if (s.length() <= 3 && s.length() >= 2) {
            char c = s.charAt(0);
            switch (c) {
                case 'S': {
                    char c1 = s.charAt(1);
                    if (c1 >= '1' && c1 <= '9') {
                        if (s.length() == 3) {
                            char c2 = s.charAt(2);
                            if (c2 == '0' || c2 == '1' || c2 == '3') {
                                return s;
                            } else {
                                return n;
                            }
                        } else {
                            return s;
                        }
                    } else {
                        return n;
                    }
                }
                case 'K': {
                    //K编号只有个位数
                    char c1 = s.charAt(1);
                    if (c1 >= '1' && c1 <= '9') {
                        if (s.length() == 3) {
                            return n;
                        } else {
                            return s;
                        }
                    } else {
                        return n;
                    }
                }
                case 'N': {
                    char c1 = s.charAt(1);
                    if (c1 >= '1' && c1 <= '8') {
                        if (s.length() == 3 || c1 == '4' || c1 == '6') {
                            return n;
                        } else {
                            return s;
                        }
                    } else {
                        return s;
                    }
                }
                case 'T': {
                    char c1 = s.charAt(1);
                    if (c1 == '1' || c1 == '2') {
                        return s;
                    } else {
                        return n;
                    }
                }
                case 'B': {
                    if (s.equals("B12") || s.equals("B7") || s.equals("B4")) {
                        return s;
                    } else {
                        return n;
                    }
                }

                case 'H': {
                    if (s.equals("H1")) {
                        return s;
                    } else {
                        return n;
                    }
                }
                default: {
                }
                break;
            }
            switch (s) {
                case "振声苑": {
                    return "zsy";
                }

                case "图书馆": {
                    return "H1";
                }

                case "华岗苑": {
                    return "hgy";
                }
                case "博物馆": {
                    return "BWG";
                }
                case "双创":{
                    return "SC";
                }
                case "东门": {
                    return "ED";
                }
                case "西门": {
                    return "WD";
                }
                case "北门": {
                    return "ND";
                }
                case "南门": {
                    return "SD";
                }
                default: {
                    return n;
                }
            }
        } else {
            switch (s) {
                case "风雨操场": {
                    return "FYCC";
                }
                case "会文南楼": {
                    return "hws";
                }
                case "会文北楼": {
                    return "hwn";
                }
                default: {
                    return n;
                }
            }
        }
    }
}
