package Utils.RuntimeUtils;

import java.io.IOException;

/**
 * 本地命令执行类
 * 代码复用，精简入口
 * @author AricSun
 * @date 2020.09.04 0:06
 */
public class LocalCmdExecutor {
    /**
     * function:执行cmd/shell命令，启用多线程，
     * 调用StreamGobbler对流进行处理，以防挂住。
     *
     * 调用Runtime.getRuntime().exec()后，如果不及时捕捉进程的输出，
     * 会导致JAVA挂住，看似被调用进程没退出。所以，解决办法是，
     * 启动进程后，再启动两个JAVA线程及时的把被调用进程的输出截获。
     * 参考：https://blog.csdn.net/chengly0129/article/details/49865297
     * @Param [arguments：String数组，表示执行所用的全部命令
     * 在shell中使用空格以分割参数，此处用数组分割]
     * @Param  charsetName:字符编码
     * @Return void
     */
    public void executeCommand(String[] arguments, String charsetName){
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            /*
             * 站在我们自己写的程序角度来想，如果我们的程序想要往别的程序输入数据，
             * 那么就是我们程序要输出对吧，所以我们需要获取输出流也就是调用
             * getOutputStream方法，那么反过来，如果我们要获取别的程序的输出的
             * 数据，对于我们程序来说也就是输入的数据，所以我们要获取输入流，
             * 调用getInputStream方法。from:https://zhuanlan.zhihu.com/p/44957705
             * */
            /*
             * SequenceInputStream是一个串联流，能够把两个流结合起来，
             * 通过该对象就可以将
             * getInputStream方法和getErrorStream方法
             * 获取到的流一起进行查看了，当然也可以单独操作
             * 不过此方法健壮性欠佳，已弃用
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
             * 本方法会一直等待Process对象代表的
             * 进程退出后才返回值，而且调用本方法的
             * 进程将会一直堵塞等待返回值
             * */
            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * function: 重载
     * @Param [arguments]
     * @Return void
     */
    public void executeCommand(String[] arguments){
        this.executeCommand(arguments, "GBK");
    }
}
