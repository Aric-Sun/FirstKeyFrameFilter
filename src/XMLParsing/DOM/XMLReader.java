package XMLParsing.DOM;

import Utils.DataTypeConverter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * DOM读取XML
 * 由于其实现方法是将整个XML文件
 * 读入内存中并构建结构树，
 * 对CPU短时占用飙升，
 * 并不符合我只解析不修改的目的
 * 且耗时也较长，得不偿失。
 * 故弃之不用
 * @author AricSun
 * @date 2020.08.11 23:11
 */
public class XMLReader {
    public static void main(String[] args) {
        // 固定套路：工厂->解析器->解析加载
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("xml/747.xml");
            /*
                这边不应该把文件参数在此处写死，应该作为函数参数，从外部传入，
                不过此方法只是试验阶段，目前已被弃用，所以没有继续优化的必要，
                只作记录而已
             */
            NodeList tagList = document.getElementsByTagName("Tag");  // 只需要获取此节点
//            System.out.println("一共有"+tagList.getLength()+"个节点");
            int CntKeyFrame = 0;
            /* 用作关键帧计数，遍历到第二个关键帧时结束遍历，结果已经找到。
                此处有一个问题，如果两个关键帧相邻，并且时间一样
                这通常是因为切割造成的产物，正常的flv是不会出现这种问题
                但是其实是有适配的必要的，工具应该具有容错性
                我只想要跨越几秒后，起码是1s后的第二个关键帧
                因为1s以内的音画不同步是可以被容忍的，
                可能有点长，最后具体落实到代码里会有浮动
                不过根据我的经验，缺帧要么6/7/8/9s，要么就不缺
                此方法已经停产，这里只作想法记录
             */

            // 遍历每一个tag，即帧
            for (int i=0; i<tagList.getLength(); ++i){
//                System.out.println("=================下面开始遍历第" + (i + 1) + "本书的内容=================");
                Node tag = tagList.item(i);  // get当前节点
                /*
                    此处有更简单的方案，替代属性循环
                    即将Node强制转换为Element
                    然后就可以通过元素节点API操作属性和文本内容
                    |--getAttribute  获得属性值
                    |--getTextContent 获得元素内部文本内容
                 */
                NamedNodeMap attrs = tag.getAttributes();  // 获取属性集合
                String timeStamp = "NULL"; // 时间值，形如【PT8.333S】，
                /* 考虑本项目只切去第一个帧间隔的内容
                    所以不会有【分钟】这个单位出现，这在xml中表现为M，
                    例如【PT16M6.681S】
                 */

//                System.out.println("第 " + (i + 1) + "本书共有" + attrs.getLength() + "个属性");
                // 遍历一帧的所有属性
                for (int j=0; j<attrs.getLength(); ++j){
                    Node attr = attrs.item(j);  // get当前属性
                    if ("TimeStamp".equals(attr.getNodeName())){
                        timeStamp = attr.getNodeValue();
                    }
                    // 默认先记录下时间，如果当前帧为关键帧，则计数+1
                    if ("Keyframe".equals(attr.getNodeName()) && "true".equals(attr.getNodeValue())){
                        CntKeyFrame++;
                    }
                }
                /*
                当计数器到达2时，即表示来到第二个关键帧位置
                此时记录下关键帧即可，此处为打印出来
                至于为什么是3，是因为上面提到的
                【两个关键帧相邻，时间一样】的错误情况
                此处因废用而不再修复
                 */
                if (CntKeyFrame == 3){
                    System.out.println(DataTypeConverter.TimeStamp2Double(timeStamp));
                    break;
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
