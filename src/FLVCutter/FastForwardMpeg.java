package FLVCutter;

import Utils.FileUtils.FilenameUtils;
import Utils.RuntimeUtils.LocalCmdExecutor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 调用ffmpeg
 * errorCodePrefix 2
 * @author AricSun
 * @date 2020.09.02 21:23
 */
public class FastForwardMpeg {
    /**
     * function: 根据第二个关键帧切割flv
     * @Param [ssTime double类型的第二个关键帧（秒）]
     * @Return java.lang.String 生成切割过的flv
     * 并返回其路径（与xml在同一目录下）
     */
    public String cutFlvWithSSTime(String flvPath, double ssTime){
        if (null == flvPath){
            System.err.println("本程序需要flv路径作为参数！");
            return "-21";
        }
        if (!Files.exists(Paths.get(flvPath))){
            System.err.println("路径在文件系统中不存在！");
            return "-23";
        }
        // 最终生成的文件
        String flvPathCut = FilenameUtils.getFilenameWithoutExtension(flvPath)
                 + "-cut_" + ssTime + "-end.flv";

        //判断目标生成文件是否存在，询问是否覆盖
        Path FLVPathCut = Paths.get(flvPathCut);
        if (Files.exists(FLVPathCut)){
            Scanner sc = new Scanner(System.in);
            System.out.print("是否覆盖 " + flvPathCut +" ? (Y/N): ");
            if ("Y".equals(sc.next().toUpperCase())){
                try {
                    Files.delete(FLVPathCut);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                return "-26";
            }
        }
        // ffmpeg相对路径
        final String ffmpegPath = "lib-apps/ffmpeg.exe";
        String[] arguments = {ffmpegPath,
                "-i", flvPath,
                "-ss", String.valueOf(ssTime),
                "-c", "copy",
                "-copyts",  // keep the original timestamps
                flvPathCut};

        // 执行命令
        new LocalCmdExecutor().executeCommand(arguments, "UTF-8");

        // 执行结果检测，xml文件是否存在
        if (Files.exists(FLVPathCut, LinkOption.NOFOLLOW_LINKS)) {
            return flvPathCut;
        } else {
            System.err.println("flv切割失败！请检查flv文件是否正确");
            return "-25";
        }
    }
}
