package XMLParsing.StAX;

import Utils.DataTypeConverter;
import dto.Frame;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * StAX解析XML
 * StAX与SAX一样是基于XML事件的解析方式，
 * 它们都不会一次性加载整个XML文件。但是它们之间也有很大
 * 的不同。
 *
 * StAX和SAX的区别——拉式解析器和推式解析器的区别
 * StAX在在处理XML事件的方式上使得应用程序更接近底层，
 * 所以在效率上比SAX更优秀。
 *
 * SAX：应用程序是被动于解析器的。
 * 应用程序只能被动的等待解析器将XML事件推送给自己处理，
 * 对于每一种事件都需要在解析开始之前就做好准备。
 * 这种方式被称为“推（push）”
 * StAX：采用一种“拉（pull）”的方式，
 * 由应用程序主动从解析器中获取当前XML事件然后
 * 根据需求处理（保存或者忽略）。StAX使得应用程序掌握了
 * 主动权，可以简化调用代码来准确地处理它预期的内容，或者
 * 发生意外时停止解析。此外，由于该方法不基于处理程序回调，
 * 应用程序不需要像使用 SAX 那样模拟解析器的状态。
 * （摘自：https://blog.csdn.net/HD243608836/article/details/87571886）
 * errorCodePrefix 3
 * @author AricSun
 * @date 2020.08.13 17:14
 */
public class ParserXML {
    private Frame frame;

    /*public List<Frame> getFrameList() {
        return frameList;
    }*/

    private List<Frame> frameList;

    /*
     * function: 指针读取XML
     * 基于指针（Cursor）的 API （XMLStreamReader）
     * 允许应用程序把 XML
     * 作为一个标记（或事件）流来处理。在这里，解析器
     * 就像一跟指针一样在文件流上前进，应用程序可以
     * 跟随解析器从文件的开头一直处理到结尾。这是一种
     * 低层 API，尽管效率高，但是没有提供底层 XML
     * 结构的抽象。
     *
     * 还有一种基于迭代器的API（XMLEventReader），
     * 具体不作阐述，
     * 关于为什么只选择了游标API，详见：
     * https://docs.oracle.com/javase/tutorial/jaxp/stax/api.html
     * 此处截取一句话：如果将性能放在首位
     * （例如，在创建低级库或基础结构时），
     * 则游标API效率更高。
     * 但另外写在此处，如没有强烈偏好，建议使用
     * 迭代器API，因为更灵活和可扩展。
     * @Param: [xml文件路径：String]
     * @Return: 第二个关键帧的时间：double
     */
    public double readXMLByCursor(String XMLFilePath){
        if (!Files.exists(Paths.get(XMLFilePath))){
            System.err.println("路径在文件系统中不存在！");
            return -36.0;
        }
        try (InputStream is = new FileInputStream(XMLFilePath)){
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader streamReader = factory.createXMLStreamReader(is);
//            boolean loopFlag = false;

            while (streamReader.hasNext()){
//                if (loopFlag){
//                    break;
//                }
//                System.out.println("=======start reading XML file======");

                int eventType = streamReader.getEventType();
                switch (eventType){
                    case XMLStreamConstants.START_DOCUMENT:
//                        System.out.println("=====start document=====");
                        frameList = new ArrayList<>();  // 初始化
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        if (streamReader.getLocalName().equals("Tag")){
                            frame = new Frame();
                            int cntAttr = streamReader.getAttributeCount();
                            if (cntAttr > 0){
                                String namespaceURI = streamReader.getNamespaceURI();
//                                frame.setIndex(streamReader.getAttributeValue(1));  // Index
                                frame.setTimeStamp(streamReader.getAttributeValue(2));  // TimeStamp
//                                frame.setType(streamReader.getAttributeValue(3));  // Type
                                // 取只需要的值。
                                // timestamp属于已知序号，keyframe的序号有时候是header。故取值方式不一样
                                frame.setKeyframe(streamReader.getAttributeValue(namespaceURI,"Keyframe"));
//                                frame.setHeader(streamReader.getAttributeValue(namespaceURI,"Header"));

                                /*
                                * 以下代码（for+switch）被废弃，其原因为，经过20次运行时间采集，
                                * 发现上方代码在平均值(525.75<566.4)和中位数(506<570.5)上
                                * 都小于下方的switch方式，且拥有更低的最小值(498<504)奇迹
                                * 通过图表可以直观了解到，虽说上方代码运行时间的
                                * 最大值(626ms)超过了下方代码的最小值(504ms)，
                                * 但也未超过witch的最大值(637)，且整体上上方代码的时间效率
                                * 还是优于下方的。而且代码更加精简
                                *    getWithString  getWithIndex(switch)
                                * average	525.75  566.4
                                * 标准差	    43.94	35.95
                                * 中位数	    506	    570.5
                                * max	    626	    637
                                * min	    468	    504
                                * */
//                                for (int i=0; i<cntAttr; ++i){
//                                    String attrName = streamReader.getAttributeLocalName(i);
//                                    switch (attrName){
//                                        case "Index":
//                                            frame.setIndex(streamReader.getAttributeValue(i));
//                                            break;
//                                        case "TimeStamp":
//                                            frame.setTimeStamp(streamReader.getAttributeValue(i));
//                                            break;
//                                        case "Type":
//                                            frame.setType(streamReader.getAttributeValue(i));
//                                            break;
//                                        case "Keyframe":
//                                            frame.setKeyframe(streamReader.getAttributeValue(i));
//                                            break;
//                                        case "Header":
//                                            frame.setHeader(streamReader.getAttributeValue(i));
//                                            break;
//                                        default:
//                                            break;
//                                    }
//
//                                    /*
//                                        以下代码被废弃，其原因为，每次遍历会逐行执行以下代码，造成资源浪费，
//                                        遂选择上面的switch方式，制造分支，减少代码被无效执行的次数。
//                                     */
////                                    frame.setIndex(attrName.equals("Index")?streamReader.getAttributeValue(i): frame.getIndex());
////                                    frame.setTimeStamp(attrName.equals("TimeStamp")?streamReader.getAttributeValue(i): frame.getTimeStamp());
////                                    frame.setType(attrName.equals("Type")?streamReader.getAttributeValue(i): frame.getType());
////                                    frame.setKeyframe(attrName.equals("Keyframe")?streamReader.getAttributeValue(i): frame.getKeyframe());
////                                    frame.setHeader(attrName.equals("Header")?streamReader.getAttributeValue(i): frame.getHeader());
//                                }
                            }
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        // 第一遍CHARACTERS事件，返回的“空白”应该
                        // 是<Tags>与<Tag>之间的
                        // “换行”与“四个空格或Tab”
                        // 开发时候一般用下面这几行来跳过“空白”
//                        if (streamReader.isWhiteSpace()){
//                            break;
//                        }
//                        System.out.println(streamReader.getText());
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (streamReader.getLocalName().equals("Tag")){
                            if (frame.getKeyframe().equals("true")){
                                frameList.add(frame);
                            }
                        }
                        /*
                        * 当读到第二个关键帧的时候，停止读取
                        * 如果开头两个关键帧相邻，且时间戳一样
                        * （即都为PT0S），则继续循环一次
                        * 具体实现方式为判断时间是否不小于1s
                        * */
                        int cntKeyFrame = frameList.size();
                        if (cntKeyFrame >= 2){
                            double secondKeyFrameTime = DataTypeConverter.TimeStamp2Double(
                                    frameList.get(cntKeyFrame-1).getTimeStamp()
                            );
                            /* 2020年10月4日
                            * 由于发现了存在0.15s这种不常见的KetFrame，遂修改。
                            * （20201002-213047-素颜~！就今天！！！-856.flv）
                            * 当初设定的初衷是防止切割到0s，
                            * 因为两个0s的KeyFrame经常会一起出现，
                            * 而设置到>=1s的原因是，当时认为1s内的音画不同步可以被接受
                            * 事实上还是估算失误。即使是0.15s，也会被辨认出来。
                            * 以B站播放器的尿性，这0.15s足以被放大成影响观看体验的地步
                            * 遂修改为不为0的正数。
                            * */
                            if (secondKeyFrameTime > 0){
                                return secondKeyFrameTime;
                            }
                        }
                        break;
                    case XMLStreamConstants.END_DOCUMENT:  // 读不到这个位置
                        System.out.println("=====end document=====");
                        break;
                    default:
                        break;
                }
                streamReader.next();
                /*
                    网上的代码基本都把此行代码放在hasNext()判断后，
                    当hasNext被执行后，此时指针位置处于开头，
                    也就是　START_DOCUMENT　的位置，
                    此时执行　getEventType，便会读到该位置
                    网上的基本操作是直接跳过文档头，
                    开始进行下面的START_ELEMENT，
                    由于我要在文档开始时进行ArrayList的初始化，
                    所以得读到这个位置，
                    所以将此行代码放置最后，不过这也有个坏处，
                    就是文档尾会被跳过，
                    因为当读到最后一个的时候，hasNext为假，
                    是不会进入循环判断的，
                    解决方法是在循环体下面加入判断逻辑
                    不过目前对我来说无用，所以就不加了。
                 */
            }
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
        return -37.0;
    }
}
