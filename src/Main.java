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
            System.err.println("��������Ҫflv�ļ���·������Ϊ������\n");
            System.err.println("ʹ�÷�ʽ�����������¼��룺FirstKeyFrameFilter test.flv\n" +
                    "test.flv����Ҫ������ļ�����FirstKeyFrameFilter�Ǳ���������");
            System.err.println("�����ļ���ק��֧������\n");
            System.err.println("��Ҫ���ܣ�����ͷȱ֡��flv�ĵ�һ���ؼ�֡���������ɾ��");
            System.exit(-1);
        }
//        String flvPath = "D:\\Downloads\\FirstKeyFrameFilter\\TypicalFLV\\20200831-205944-���������ˣ�-589.flv";
//        String flvPath = "D:\\Downloads\\BiliLiveRecords\\3443750-һֻ���㶹\\20200903-205551-��Ӵ��ӴС�ɰ�-436.flv";

        for (String arg : args) {
            System.out.println("��ʼ����" + arg);
            // fib��ȡflv������xml
            String xmlPath = new FlvInteractiveRebase().parseFlvWithFib(arg);
            if ('-' == xmlPath.charAt(0)){
                System.exit(Integer.parseInt(xmlPath));
            }

            // ����xml����ȡ�ڶ����ؼ�֡��ʱ��
            double secondKeyFrameTime = new XMLReader().getSecondKeyFrameTime(xmlPath);
            if (secondKeyFrameTime < 0){
                System.exit((int) secondKeyFrameTime);
            }

            // �����յ���ʱ����Ϊ��ʼʱ���и�flv����ͬһ�ļ����������µ��ļ���Դ�ļ���ɾ��
            String flvPathCut = new FastForwardMpeg().cutFlvWithSSTime(arg, secondKeyFrameTime);
            if ('-' == flvPathCut.charAt(0)){
                System.exit(Integer.parseInt(flvPathCut));
            }

            //ɾ��xml
            try {
                Files.delete(Paths.get(xmlPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
//        if (!Files.exists(Paths.get(xmlPath))) {
//            System.out.println("XML�ļ���ɾ��");
//        }

            System.out.println("�ɹ��и�");
        }

        System.exit(0);
    }
}
