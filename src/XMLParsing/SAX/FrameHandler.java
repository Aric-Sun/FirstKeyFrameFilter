package XMLParsing.SAX;

import dto.Frame;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * SAX从头至尾逐行逐个读取，
 * 虽不易修改，但很契合本项目需求：
 * 只读&大文档
 * 采用事件驱动，不可回退
 * 占用资源少，内存消耗少
 * @author AricSun
 * @date 2020.08.13 2:15
 */
public class FrameHandler extends DefaultHandler {
    private Frame frame;

    public List<Frame> getFrameList() {
        return frameList;
    }
    // 对外接口
    private List<Frame> frameList;
    private String currentTag;

    /*
     * function: 遇到文档开头触发，主要目的是对List初始化
     * @Param: []
     * @Return: void
     */
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
//        System.out.println("=======start reading XML file======");
        frameList = new ArrayList<>();
    }

    /*
     * function: 当遇到元素时触发
     * @Param: [uri名称空间,
     *      localName包含名称空间的标签，如果没有名称空间，则为空,
     *      qName不包含名称空间的标签,
     *      attributes属性集合]
     * @Return: void
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        currentTag = qName;
        if (currentTag.equals("Tag")){
            frame = new Frame();
            if (attributes.getLength() != 0){
                frame.setIndex(attributes.getValue("Index"));
                frame.setTimeStamp(attributes.getValue("TimeStamp"));
                frame.setType(attributes.getValue("Type"));
                frame.setKeyframe(attributes.getValue("Keyframe"));
                frame.setHeader(attributes.getValue("Header"));
            }
        }
    }

    /*
     * function: 主要处理被标签包裹的文字信息，形如<title>书名</title>
     * @Param: [ch传回来的字符数组，其包含元素内容,
     *      start开始位置, length截取长度]
     * @Return: void
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        // currentTag在这里要用。
//        if (currentTag.equals("")){
//
//        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        currentTag = "";
        // 有不相干的节点结束会触发事件，所以要加限定
        if (qName.equals("Tag")){
            if (frame.getKeyframe().equals("true")){
                frameList.add(frame);
            }
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
//        System.out.println("=======finish reading XML file======");
    }
}

// end of FrameHandler.java