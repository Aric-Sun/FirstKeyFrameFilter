import FLVCutter.FastForwardMpeg;
import XMLGenerator.FlvInteractiveRebase;
import XMLParsing.StAX.XMLReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author AricSun
 * @date 2020.09.02 4:22
 */
public class Main {
    public static void main(String[] args) {
        if (args.length==0){
            System.err.println("本程序需要flv文件（路径）作为参数！\n");
            System.err.println("使用方式：在命令行下键入：FirstKeyFrameFilter test.flv\n" +
                    "test.flv是你要处理的文件名，FirstKeyFrameFilter是本程序名。");
            System.err.println("建议文件拖拽，支持批量\n");
            System.err.println("主要功能：将开头缺帧的flv的第一个关键帧间隔的内容删除");
            System.exit(-1);
        }
//        String flvPath = "D:\\Downloads\\FirstKeyFrameFilter\\TypicalFLV\\20200831-205944-想死你们了！-589.flv";
//        String flvPath = "D:\\Downloads\\BiliLiveRecords\\3443750-一只萌豌豆\\20200903-205551-哎哟哎哟小可爱-436.flv";

        for (String arg : args) {
            System.out.println("开始处理" + arg);
            // fib读取flv，生成xml
            String xmlPath = new FlvInteractiveRebase().parseFlvWithFib(arg);
            if ('-' == xmlPath.charAt(0)){
                System.exit(Integer.parseInt(xmlPath));
            }

            // 解析xml，读取第二个关键帧的时间
            double secondKeyFrameTime = new XMLReader().getSecondKeyFrameTime(xmlPath);
            if (secondKeyFrameTime < 0){
                System.exit((int) secondKeyFrameTime);
            }

            // 将接收到的时间作为开始时间切割flv，在同一文件夹下生成新的文件，源文件不删除
            String flvPathCut = new FastForwardMpeg().cutFlvWithSSTime(arg, secondKeyFrameTime);
            if ('-' == flvPathCut.charAt(0)){
                System.exit(Integer.parseInt(flvPathCut));
            }

            //删除xml
            try {
                Files.delete(Paths.get(xmlPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
//        if (!Files.exists(Paths.get(xmlPath))) {
//            System.out.println("XML文件已删除");
//        }

            System.out.println("成功切割");
        }

        System.exit(0);
    }
}
