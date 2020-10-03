package Utils.RuntimeUtils;

import java.io.*;

/**
 * Stream管理
 * 参考：https://blog.csdn.net/chengly0129/article/details/49865297
 * 防超时版本：https://www.jianshu.com/p/af4b3264bc5d
 * @author AricSun
 * @date 2020.09.03 22:07
 */
public class StreamGobbler extends Thread{
    private InputStream inputStream;

    /*
    * 由于ffmpeg默认输出是ERROR类型，影响使用效果，
    * 故放弃区分流的类型
    * */
//    private String streamType;
    private String charsetName;  // 字符集编码
//    private StringBuilder buf;
    // 用来装载所有的输出内容，有需要可开启

    /**
     * @Param inputStream   the InputStream to be consumed,
     * @Param streamType    (stop using)the stream type (should be OUTPUT or ERROR)
     * @Param charsetName   Stream的字符编码
     */
    public StreamGobbler(final InputStream inputStream,
                         final String charsetName){
        this.inputStream = inputStream;
        this.charsetName = charsetName;
//        this.buf = new StringBuilder();
    }

    /**
     * function: 缺省字符编码
     * @Param [inputStream, streamType]
     */
    public StreamGobbler(final InputStream inputStream){
        this.inputStream = inputStream;
        this.charsetName = "GBK";  // 缺省值为GBK
    }

    /**
     * function:Consumes the output from the input stream
     * and displays the lines consumed
     * if configured to do so.
     */
    @Override
    public void run() {
        // InputStreamReader的charsetName默认为UTF-8
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
