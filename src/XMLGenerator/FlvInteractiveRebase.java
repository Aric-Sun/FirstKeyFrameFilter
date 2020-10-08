package XMLGenerator;

import Utils.RuntimeUtils.LocalCmdExecutor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

import Utils.FileUtils.FilenameUtils;

/**
 * ����fib��ȡflv
 * errorCodePrefix 1
 * @author AricSun
 * @date 2020.09.01 3:33
 */
public class FlvInteractiveRebase {
    /*
     * function: ��ȡflv����xml���书������flv����Ŀ¼������xml�ļ�
     * @Param: [flvPath flv�ļ�·��]
     * @Return: java.lang.String xml�ļ�·��
     */
    public String parseFlvWithFib(String flvPath) {
        if (null == flvPath){
            System.err.println("��������Ҫflv·����Ϊ������");
            return "-11";
        }
        if (!Files.exists(Paths.get(flvPath))){
            System.err.println("·�����ļ�ϵͳ�в����ڣ�");
            return "-13";
        }
//        Path flvPath = Paths.get("D:\\Downloads\\FirstKeyFrameFilter\\TypicalFLV\\20200901-210722-���������ˣ�-650.flv");
//        Path flvFileName = flvPath.getFileName();  // flv�ļ���������׺������·��
//        Path flvFolderPath = flvPath.getParent();  // flv����Ŀ¼·��
//        String flvPathStr = String.valueOf(flvPath);
        String xmlPath = FilenameUtils.getFilenameWithoutExtension(flvPath)
                .concat(".xml");  // ����xml�ľ���·��

        //�ж�Ŀ�������ļ��Ƿ���ڣ�ѯ���Ƿ񸲸�
        Path XMLPath = Paths.get(xmlPath);
        if (Files.exists(XMLPath)){
            Scanner sc = new Scanner(System.in);
            System.out.print("�Ƿ񸲸� " + xmlPath + " ?" +
                    "\n��ѡ�����ʹ��Ŀ��xml�ļ����н����и� (Y/N): ");
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
        final String fibPathStr = "lib-apps/FlvInteractiveRebase.exe";  // fib���·��

        // ����·��
//        String fibPathStrAbsolute = new ResourcesRelease().copyFile(fibPathStr);

        String[] arguments = {fibPathStr, "parse", flvPath, xmlPath};  // cmd����

        // ִ������
        new LocalCmdExecutor().executeCommand(arguments);

        // function: �ж�XML�ļ��Ƿ��Ѿ�����
        /*
        * LinkOption.NOFOLLOW_LINKS �����ж��ļ����ڵķ���
        * ��Ӧ�ø����ļ�ϵͳ�еķ����������ж�·���Ƿ����
        * */
        if (Files.exists(Paths.get(xmlPath), LinkOption.NOFOLLOW_LINKS)){
            return xmlPath;
        } else {
            System.err.println("fibδ�ɹ�ִ�У�������");
            // ɾ��.net��ʱ�ļ���
            String dotNetTempPath = System.getenv("TEMP").concat("\\.net");
            Path path = Paths.get(dotNetTempPath);
            /*
            * ����path��Ŀ¼�������ַ�����������
            * �����ļ���·��һ����������Ŀ¼֮ǰ
            * ���ļ�ɾ��Ż�ɾ�������ļ���
            * foreach����ִ�о�̬������ɾ���ļ���Ŀ¼
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
     * function: ɾ���ļ�����ļ���
     * @Param [path�ļ����У�·��]
     * @Return void
     */
    private static void deleteDirectoryStream(Path path){
        try {
            Files.delete(path);
            System.out.println("ɾ���ļ��ɹ���" + path);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("�޷�ɾ��·��:" + path);
        }
    }
}