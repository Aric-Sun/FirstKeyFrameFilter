package Utils;

/**
 * 数据类型转换工具类
 * @author AricSun
 * @date 2020.10.07 23:02
 */
public class DataTypeConverter{
    /*
     * function: 将timestamp的PTxx.xxS通过正则匹配出浮点型数据
     * @Param: [timeStamp时间戳]
     * @Return: java.lang.Double
     */
    public static Double TimeStamp2Double(String timeStamp){
        return Double.parseDouble(timeStamp.replaceAll("[^\\d.]", ""));
    }

}
