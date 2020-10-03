package Utils.RuntimeUtils;

import java.io.*;

/**
 * Stream����
 * �ο���https://blog.csdn.net/chengly0129/article/details/49865297
 * ����ʱ�汾��https://www.jianshu.com/p/af4b3264bc5d
 * @author AricSun
 * @date 2020.09.03 22:07
 */
public class StreamGobbler extends Thread{
    private InputStream inputStream;

    /*
    * ����ffmpegĬ�������ERROR���ͣ�Ӱ��ʹ��Ч����
    * �ʷ���������������
    * */
//    private String streamType;
    private String charsetName;  // �ַ�������
//    private StringBuilder buf;
    // ����װ�����е�������ݣ�����Ҫ�ɿ���

    /**
     * @Param inputStream   the InputStream to be consumed,
     * @Param streamType    (stop using)the stream type (should be OUTPUT or ERROR)
     * @Param charsetName   Stream���ַ�����
     */
    public StreamGobbler(final InputStream inputStream,
                         final String charsetName){
        this.inputStream = inputStream;
        this.charsetName = charsetName;
//        this.buf = new StringBuilder();
    }

    /**
     * function: ȱʡ�ַ�����
     * @Param [inputStream, streamType]
     */
    public StreamGobbler(final InputStream inputStream){
        this.inputStream = inputStream;
        this.charsetName = "GBK";  // ȱʡֵΪGBK
    }

    /**
     * function:Consumes the output from the input stream
     * and displays the lines consumed
     * if configured to do so.
     */
    @Override
    public void run() {
        // InputStreamReader��charsetNameĬ��ΪUTF-8
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line=bufferedReader.readLine()) != null){
//                this.buf.append(line).append("\n");
                System.out.println(line);
//                if ("ERROR".equals(streamType.toUpperCase())){
//                    System.out.println("err:"+line);
//                } else if ("OUTPUT".equals(streamType.toUpperCase())){
//                    System.out.println("out:"+line);
//                } else {
//                    System.out.println("oth"+line);
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
