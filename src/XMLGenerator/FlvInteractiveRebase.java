package XMLGenerator;

import Utils.RuntimeUtils.LocalCmdExecutor;
import Utils.RuntimeUtils.ResourcesRelease;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * 调用fib读取flv
 * errorCodePrefix 1
 * @author AricSun
 * @date 2020.09.01 3:33
 */
public class FlvInteractiveRebase {
    /*
     * function: 读取flv生成xml，其功能是在flv所在目录下生成xml文件
     * @Param: [flvPath flv文件路径]
     * @Return: java.lang.String xml文件路径
     */
    public String parseFlvWithFib(String flvPath) {
        if (null == flvPath){
            System.err.println("本程序需要flv路径作为参数！");
            return "-11";
        }
        if (!Files.exists(Paths.get(flvPath))){
            System.err.println("路径在文件系统中不存在！");
            return "-13";
        }
//        Path flvPath = Paths.get("D:\\Downloads\\FirstKeyFrameFilter\\TypicalFLV\\20200901-210722-想死你们了！-650.flv");
//        Path flvFileName = flvPath.getFileName();  // flv文件名，带后缀，不带路径
//        Path flvFolderPath = flvPath.getParent();  // flv所在目录路径
//        String flvPathStr = String.valueOf(flvPath);
        String xmlPath = flvPath.substring(0,
                flvPath.lastIndexOf(".")
        ).concat(".xml");  // 生成xml的绝对路径

        //判断目标生成文件是否存在，询问是否覆盖
        Path XMLPath = Paths.get(xmlPath);
        if (Files.exists(XMLPath)){
            Scanner sc = new Scanner(System.in);
            System.out.print("是否覆盖 " + xmlPath + " ?" +
                    "\n若选择否，则使用目标xml文件进行解析切割 (Y/N): ");
            if ("Y".equals(sc.next().toUpperCase())){
                try {
                    Files.delete(XMLPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                return xmlPath;
            }
        }
//        System.out.println(flvFolderPath);
//        System.out.println(flvFileName.substring(0,flvFileName.lastIndexOf(".")));
        final String fibPathStr = "lib-apps/FlvInteractiveRebase.exe";  // fib相对路径

        // 绝对路径
//        String fibPathStrAbsolute = new ResourcesRelease().copyFile(fibPathStr);

        String[] arguments = {fibPathStr, "parse", flvPath, xmlPath};  // cmd命令

        // 执行命令
        new LocalCmdExecutor().executeCommand(arguments);

        // function: 判断XML文件是否已经生成
        /*
        * LinkOption.NOFOLLOW_LINKS 表明判断文件存在的方法
        * 不应该根据文件系统中的符号连接来判断路径是否存在
        * */
        if (Files.exists(Paths.get(xmlPath), LinkOption.NOFOLLOW_LINKS)){
            return xmlPath;
        } else {
            System.err.println("fib未成功执行，请重试");
            // 删除.net临时文件夹
            String dotNetTempPath = System.getenv("TEMP").concat("\\.net");
            Path path = Paths.get(dotNetTempPath);
            /*
            * 遍历path的目录，按照字符串反向排序
            * 这样文件的路径一定在其所在目录之前
            * 即文件删完才会删其所在文件夹
            * foreach挨个执行静态方法，删除文件和目录
            * */
            try(Stream<Path> walk = Files.walk(path)){
                walk.sorted(Comparator.reverseOrder())
                    .forEach(FlvInteractiveRebase::deleteDirectoryStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "-14";
        }
    }

    /**
     * function: 删除文件或空文件夹
     * @Param [path文件（夹）路径]
     * @Return void
     */
    private static void deleteDirectoryStream(Path path){
        try {
            Files.delete(path);
            System.out.println("删除文件成功：" + path);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("无法删除路径:" + path);
        }
    }
}