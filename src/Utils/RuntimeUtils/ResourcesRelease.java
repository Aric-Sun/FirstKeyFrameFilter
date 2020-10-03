package Utils.RuntimeUtils;

/*
* 2020.9.5.21:15
* 此Class终结，作为学习笔记保存
* 起因：jar在运行时无法访问自身的资源文件
* 通过查阅资料得知，可以通过类加载器等方法将资源取出
*
* 本意为将相对目录lib-apps中的exe拷贝到temp临时目录以使用
* 但尝试几次发现inputStream并没有取得预期的数据
* 目标文件夹及文件虽然存在，但是并没有数据。
* 回顾查阅的资料，均为在另一个java项目中读取jar
* 并将其中资源释放出来，
 *
* 一来我觉得再新建一个项目，有点麻烦（心理上）
* 二来，隐藏依赖的exe，将其打进jar
* 后封装成单独的exe，目的是保护原作者的知识产权
* 但是目前这种将exe释放到本地目录的方法，与打包成压缩包分发
* 的行为在结果上并没有什么不同，都将完整的exe文件发送到
* 用户的机器上了，就算即用即删，也是可以被捕捉到的，
* 而且对时间性能也会有影响，显然这样的作法不够高明（不是。
* 唯一不一样的就是用户的使用体验，本来可以只点击一个exe就可以使用
* 现在可能需要辨别执行哪个exe，或者因为相当路径的问题不能运行等等。
* 这个等真的会有用户再说吧（
*
* 而且也违背了开源精神，
* 我已经向作者提出请求，等待中
* 如果fib的作者不同意我上传fib到github，
* 我将留下其fib的获取方式
*
* 参考：
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
// * 将资源文件释放到temp目录
// * errorCodePrefix 5
// * @author AricSun
// * @date 2020.09.05 4:27
// */
public class ResourcesRelease {
//    // 系统临时文件夹
//    private static String userTemp = System.getenv("TEMP");
//    // 项目名称
//    private static String projectName = "FirstKeyFrameFilter";
//    // 本项目临时文件夹目录
//    private static String tempFolderPathStr = userTemp + "\\" + projectName;
//
//    /**
//     * function: 释放文件夹中的资源
//     * @Param [folderPathStr：文件夹相对路径]
//     * @Return void
//     */
//    public void copyFilesByFolder(String folderPathStr){
//        // 获取文件夹的classpath
//        Path folderPath = Paths.get(this.getClass().getResource(folderPathStr).getPath());
//    }
//
//    /**
//     * function: 释放指定路径的文件
//     * @Param [filePathStr文件的相对路径]
//     * @Return 绝对路径
//     */
//    public String copyFile(String filePathStr){
//        try {
//            // 流式读取，使用classpath
////            URL filePathURL = this.getClass().getResource(filePathStr);
////            URLConnection urlConnection = filePathURL.openConnection();
////            InputStream is = urlConnection.getInputStream();
////            InputStream is = this.getClass().getResourceAsStream(filePathStr);
//
//            // 指定输出文件目录 绝对路径
//            File outputFileAbsolute = new File(tempFolderPathStr + "\\" + Paths.get(filePathStr).getFileName());
//            Path tempFolderPath = Paths.get(tempFolderPathStr);
//
//            // 创建目标文件夹
//            /*
//             * 如果父文件夹不存在，就创建它
//             * 如果文件夹已经存在，不会重复创建，没有异常抛出
//             * 如果因为磁盘IO出现异常，则抛出IOException.
//             * */
//            Files.createDirectories(tempFolderPath);
//
//            /*
//             * 返回true表示文件成功
//             * false i表示文件已经存在
//             * */
////            outputFileAbsolute.createNewFile();
//
//            // 复制文件
//            try (
//                    BufferedInputStream fis = new BufferedInputStream(
//                        ClassLoader.getSystemResourceAsStream(filePathStr)
//                    );
//                    OutputStream fos = new FileOutputStream(outputFileAbsolute);) {
//                int index = 0;
//                byte[] bytes = new byte[1024];
//                while ((index = fis.read(bytes)) != -1) {
//                    /*
//                     * function: 输出文件
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
