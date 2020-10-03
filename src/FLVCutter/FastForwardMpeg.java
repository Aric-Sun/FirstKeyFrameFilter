package FLVCutter;

import Utils.RuntimeUtils.LocalCmdExecutor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * ����ffmpeg
 * errorCodePrefix 2
 * @author AricSun
 * @date 2020.09.02 21:23
 */
public class FastForwardMpeg {
    /**
     * function: ���ݵڶ����ؼ�֡�и�flv
     * @Param [ssTime double���͵ĵڶ����ؼ�֡���룩]
     * @Return java.lang.String �����и����flv
     * ��������·������xml��ͬһĿ¼�£�
     */
    public String cutFlvWithSSTime(String flvPath, double ssTime){
        if (null == flvPath){
            System.err.println("��������Ҫflv·����Ϊ������");
            return "-21";
        }
        if (!Files.exists(Paths.get(flvPath))){
            System.err.println("·�����ļ�ϵͳ�в����ڣ�");
            return "-23";
        }
        // �������ɵ��ļ�
        String flvPathCut = flvPath.substring(
                0, flvPath.indexOf(".")
        ) + "-cut_" + ssTime + "-end.flv";

        //�ж�Ŀ�������ļ��Ƿ���ڣ�ѯ���Ƿ񸲸�
        Path FLVPathCut = Paths.get(flvPathCut);
        if (Files.exists(FLVPathCut)){
            Scanner sc = new Scanner(System.in);
            System.out.print("�Ƿ񸲸� " + flvPathCut +" ? (Y/N): ");
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
        // ffmpeg���·��
        final String ffmpegPath = "lib-apps/ffmpeg.exe";
        String[] arguments = {ffmpegPath,
                "-i", flvPath,
                "-ss", String.valueOf(ssTime),
                "-c", "copy",
                flvPathCut};

        // ִ������
        new LocalCmdExecutor().executeCommand(arguments, "UTF-8");

        // ִ�н����⣬xml�ļ��Ƿ����
        if (Files.exists(FLVPathCut, LinkOption.NOFOLLOW_LINKS)) {
            return flvPathCut;
        } else {
            System.err.println("flv�и�ʧ�ܣ�����flv�ļ��Ƿ���ȷ");
            return "-25";
        }
    }
}
