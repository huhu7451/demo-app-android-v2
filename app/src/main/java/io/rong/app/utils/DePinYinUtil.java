package io.rong.app.utils;


import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/3/21.
 */
public class DePinYinUtil {

    /**
     * 汉语拼音转换工具
     *
     * @param chinese
     * @return
     */
    public String converterToPinYin(String chinese) {
        String pinyinString = "";
        char[] charArray = chinese.toCharArray();
        // 根据需要定制输出格式，我用默认的即可
//        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//        try {
//            // 遍历数组，ASC码大于128进行转换
//            for (int i = 0; i < charArray.length; i++) {
//                if (charArray[i] > 128) {
//                    // charAt(0)取出首字母
//                    if (charArray[i] >= 0x4e00 && charArray[i] <= 0x9fa5) {    //判断是否中文
//                        pinyinString += PinyinHelper.toHanyuPinyinStringArray(
//                                charArray[i], defaultFormat)[0].charAt(0);
//                    } else {                          //不是中文的打上未知，所以无法处理韩文日本等等其他文字
//                        pinyinString += "?";
//                    }
//                } else {
//                    pinyinString += charArray[i];
//                }
//            }
//            return pinyinString;
//        } catch (BadHanyuPinyinOutputFormatCombination e) {
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }


    /**
     * 把单个英文字母或者字符串转换成数字ASCII码
     *
     * @param input
     * @return
     */
    public static int character2ASCII(String input) {
        char[] temp = input.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char each : temp) {
            builder.append((int) each);
        }
        String result = builder.toString();
        return Integer.parseInt(result);
    }

    /**
     * 混合排序工具
     */
    public class MixComparator implements Comparator<String> {
        public int compare(String o1, String o2) {
            // 判断是否为空""
            if (isEmpty(o1) && isEmpty(o2))
                return 0;
            if (isEmpty(o1))
                return -1;
            if (isEmpty(o2))
                return 1;
            String str1 = "";
            String str2 = "";
            try {
                str1 = (o1.toUpperCase()).substring(0, 1);
                str2 = (o2.toUpperCase()).substring(0, 1);
            } catch (Exception e) {
                System.out.println("某个str为\" \" 空");
            }
            if (isWord(str1) && isWord(str2)) {               //字母
                return str1.compareTo(str2);
            } else if (isNumeric(str1) && isWord(str2)) {     //数字字母
                return 1;
            } else if (isNumeric(str2) && isWord(str1)) {
                return -1;
            } else if (isNumeric(str1) && isNumeric(str2)) {       //数字数字
                if (Integer.parseInt(str1) > Integer.parseInt(str2)) {
                    return 1;
                } else {
                    return -1;
                }
            } else if (isAllWord(str1) && (!isAllWord(str2))) {      //数字字母  其他字符
                return -1;
            } else if ((!isAllWord(str1)) && isWord(str2)) {
                return 1;
            } else {
                return 1;
            }
        }
    }

    /**
     * 判断空
     *
     * @param str
     * @return
     */
    private boolean isEmpty(String str) {
        return "".equals(str.trim());
    }

    /**
     * 判断数字
     *
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判读字母
     *
     * @param str
     * @return
     */
    public boolean isWord(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断字母数字混合
     *
     * @param str
     * @return
     */
    public boolean isAllWord(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        } else {
            return true;
        }
    }
}
