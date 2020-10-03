package Utils.RuntimeUtils;

import java.io.IOException;

/**
 * ��������ִ����
 * ���븴�ã��������
 * @author AricSun
 * @date 2020.09.04 0:06
 */
public class LocalCmdExecutor {
    /**
     * function:ִ��cmd/shell������ö��̣߳�
     * ����StreamGobbler�������д����Է���ס��
     *
     * ����Runtime.getRuntime().exec()���������ʱ��׽���̵������
     * �ᵼ��JAVA��ס�����Ʊ����ý���û�˳������ԣ�����취�ǣ�
     * �������̺�����������JAVA�̼߳�ʱ�İѱ����ý��̵�����ػ�
     * �ο���https://blog.csdn.net/chengly0129/article/details/49865297
     * @Param [arguments��String���飬��ʾִ�����õ�ȫ������
     * ��shell��ʹ�ÿո��Էָ�������˴�������ָ�]
     * @Param  charsetName:�ַ�����
     * @Return void
     */
    public void executeCommand(String[] arguments, String charsetName){
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            /*
             * վ�������Լ�д�ĳ���Ƕ����룬������ǵĳ�����Ҫ����ĳ����������ݣ�
             * ��ô�������ǳ���Ҫ����԰ɣ�����������Ҫ��ȡ�����Ҳ���ǵ���
             * getOutputStream��������ô���������������Ҫ��ȡ��ĳ���������
             * ���ݣ��������ǳ�����˵Ҳ������������ݣ���������Ҫ��ȡ��������
             * ����getInputStream������from:https://zhuanlan.zhihu.com/p/44957705
             * */
            /*
             * SequenceInputStream��һ�����������ܹ������������������
             * ͨ���ö���Ϳ��Խ�
             * getInputStream������getErrorStream����
             * ��ȡ������һ����в鿴�ˣ���ȻҲ���Ե�������
             * �����˷�����׳��Ƿ�ѣ�������
             * */
//            SequenceInputStream sequenceInputStream = new SequenceInputStream(
//                    process.getInputStream(),process.getErrorStream()
//            );
//            process.getOutputStream().close();
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), charsetName);
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), charsetName);
            errorGobbler.start();
            outputGobbler.start();

            /*
             * ��������һֱ�ȴ�Process��������
             * �����˳���ŷ���ֵ�����ҵ��ñ�������
             * ���̽���һֱ�����ȴ�����ֵ
             * */
            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * function: ����
     * @Param [arguments]
     * @Return void
     */
    public void executeCommand(String[] arguments){
        this.executeCommand(arguments, "GBK");
    }
}
