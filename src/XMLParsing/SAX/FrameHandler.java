package XMLParsing.SAX;

import dto.Frame;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * SAX��ͷ��β���������ȡ��
 * �䲻���޸ģ��������ϱ���Ŀ����
 * ֻ��&���ĵ�
 * �����¼����������ɻ���
 * ռ����Դ�٣��ڴ�������
 * @author AricSun
 * @date 2020.08.13 2:15
 */
public class FrameHandler extends DefaultHandler {
    private Frame frame;

    public List<Frame> getFrameList() {
        return frameList;
    }
    // ����ӿ�
    private List<Frame> frameList;
    private String currentTag;

    /*
     * function: �����ĵ���ͷ��������ҪĿ���Ƕ�List��ʼ��
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
     * function: ������Ԫ��ʱ����
     * @Param: [uri���ƿռ�,
     *      localName�������ƿռ�ı�ǩ�����û�����ƿռ䣬��Ϊ��,
     *      qName���������ƿռ�ı�ǩ,
     *      attributes���Լ���]
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
     * function: ��Ҫ������ǩ������������Ϣ������<title>����</title>
     * @Param: [ch���������ַ����飬�����Ԫ������,
     *      start��ʼλ��, length��ȡ����]
     * @Return: void
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        // currentTag������Ҫ�á�
//        if (currentTag.equals("")){
//
//        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        currentTag = "";
        // �в���ɵĽڵ�����ᴥ���¼�������Ҫ���޶�
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