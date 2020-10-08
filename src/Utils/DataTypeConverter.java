package Utils;

/**
 * ��������ת��������
 * @author AricSun
 * @date 2020.10.07 23:02
 */
public class DataTypeConverter{
    /*
     * function: ��timestamp��PTxx.xxSͨ������ƥ�������������
     * @Param: [timeStampʱ���]
     * @Return: java.lang.Double
     */
    public static Double TimeStamp2Double(String timeStamp){
        return Double.parseDouble(timeStamp.replaceAll("[^\\d.]", ""));
    }

}
