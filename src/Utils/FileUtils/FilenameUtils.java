package Utils.FileUtils;

/**
 * �ļ���������
 * @author AricSun
 * @date 2020.10.07 22:36
 */
public class FilenameUtils {
    /*
     * function: ��ȡ������չ�����ļ���
     * @Param [filename�ļ���]
     * @Return java.lang.String
     * from: https://my.oschina.net/liting/blog/535479
     */
    public static String getFilenameWithoutExtension(String filename){
        if ((filename != null) && (filename.length() > 0)){
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < filename.length())){
                return filename.substring(0,dot);
            }
        }
        return filename;
    }
}
