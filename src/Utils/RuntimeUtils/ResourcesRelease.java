package Utils.RuntimeUtils;

/*
* 2020.9.5.21:15
* ��Class�սᣬ��Ϊѧϰ�ʼǱ���
* ����jar������ʱ�޷������������Դ�ļ�
* ͨ���������ϵ�֪������ͨ����������ȷ�������Դȡ��
*
* ����Ϊ�����Ŀ¼lib-apps�е�exe������temp��ʱĿ¼��ʹ��
* �����Լ��η���inputStream��û��ȡ��Ԥ�ڵ�����
* Ŀ���ļ��м��ļ���Ȼ���ڣ����ǲ�û�����ݡ�
* �ع˲��ĵ����ϣ���Ϊ����һ��java��Ŀ�ж�ȡjar
* ����������Դ�ͷų�����
 *
* һ���Ҿ������½�һ����Ŀ���е��鷳�������ϣ�
* ����������������exe��������jar
* ���װ�ɵ�����exe��Ŀ���Ǳ���ԭ���ߵ�֪ʶ��Ȩ
* ����Ŀǰ���ֽ�exe�ͷŵ�����Ŀ¼�ķ�����������ѹ�����ַ�
* ����Ϊ�ڽ���ϲ�û��ʲô��ͬ������������exe�ļ����͵�
* �û��Ļ������ˣ����㼴�ü�ɾ��Ҳ�ǿ��Ա���׽���ģ�
* ���Ҷ�ʱ������Ҳ����Ӱ�죬��Ȼ�����������������������ǡ�
* Ψһ��һ���ľ����û���ʹ�����飬��������ֻ���һ��exe�Ϳ���ʹ��
* ���ڿ�����Ҫ���ִ���ĸ�exe��������Ϊ�൱·�������ⲻ�����еȵȡ�
* �������Ļ����û���˵�ɣ�
*
* ����ҲΥ���˿�Դ����
* ���Ѿ�������������󣬵ȴ���
* ���fib�����߲�ͬ�����ϴ�fib��github��
* �ҽ�������fib�Ļ�ȡ��ʽ
*
* �ο���
* https://blog.csdn.net/sxinfosoft/article/details/7088888
* https://blog.csdn.net/userwyh/article/details/53558688
* https://blog.csdn.net/yanhanhui1/article/details/108157344
* https://www.cnblogs.com/0616--ataozhijia/p/4094952.html
* https://blog.csdn.net/mm_bit/article/details/50372229
* */
//
//import sun.misc.OSEnvironment;
//
//import java.io.*;
//import java.net.URL;
//import java.net.URLConnection;
//import java.nio.file.FileAlreadyExistsException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Objects;
//
///**
// * ����Դ�ļ��ͷŵ�tempĿ¼
// * errorCodePrefix 5
// * @author AricSun
// * @date 2020.09.05 4:27
// */
public class ResourcesRelease {
//    // ϵͳ��ʱ�ļ���
//    private static String userTemp = System.getenv("TEMP");
//    // ��Ŀ����
//    private static String projectName = "FirstKeyFrameFilter";
//    // ����Ŀ��ʱ�ļ���Ŀ¼
//    private static String tempFolderPathStr = userTemp + "\\" + projectName;
//
//    /**
//     * function: �ͷ��ļ����е���Դ
//     * @Param [folderPathStr���ļ������·��]
//     * @Return void
//     */
//    public void copyFilesByFolder(String folderPathStr){
//        // ��ȡ�ļ��е�classpath
//        Path folderPath = Paths.get(this.getClass().getResource(folderPathStr).getPath());
//    }
//
//    /**
//     * function: �ͷ�ָ��·�����ļ�
//     * @Param [filePathStr�ļ������·��]
//     * @Return ����·��
//     */
//    public String copyFile(String filePathStr){
//        try {
//            // ��ʽ��ȡ��ʹ��classpath
////            URL filePathURL = this.getClass().getResource(filePathStr);
////            URLConnection urlConnection = filePathURL.openConnection();
////            InputStream is = urlConnection.getInputStream();
////            InputStream is = this.getClass().getResourceAsStream(filePathStr);
//
//            // ָ������ļ�Ŀ¼ ����·��
//            File outputFileAbsolute = new File(tempFolderPathStr + "\\" + Paths.get(filePathStr).getFileName());
//            Path tempFolderPath = Paths.get(tempFolderPathStr);
//
//            // ����Ŀ���ļ���
//            /*
//             * ������ļ��в����ڣ��ʹ�����
//             * ����ļ����Ѿ����ڣ������ظ�������û���쳣�׳�
//             * �����Ϊ����IO�����쳣�����׳�IOException.
//             * */
//            Files.createDirectories(tempFolderPath);
//
//            /*
//             * ����true��ʾ�ļ��ɹ�
//             * false i��ʾ�ļ��Ѿ�����
//             * */
////            outputFileAbsolute.createNewFile();
//
//            // �����ļ�
//            try (
//                    BufferedInputStream fis = new BufferedInputStream(
//                        ClassLoader.getSystemResourceAsStream(filePathStr)
//                    );
//                    OutputStream fos = new FileOutputStream(outputFileAbsolute);) {
//                int index = 0;
//                byte[] bytes = new byte[1024];
//                while ((index = fis.read(bytes)) != -1) {
//                    /*
//                     * function: ����ļ�
//                     * @param      b     the data.
//                     * @param      off   the start offset in the data.
//                     * @param      len   the number of bytes to write.
//                     * @Return void
//                     */
//                    fos.write(bytes, 0, index);
//                }
//                fos.flush();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return outputFileAbsolute.toString();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        return "-51";
//    }
}
