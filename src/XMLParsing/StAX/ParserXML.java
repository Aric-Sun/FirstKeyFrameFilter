package XMLParsing.StAX;

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

import static XMLParsing.DOM.XMLReader.TimeStamp2Double;

/**
 * StAX����XML
 * StAX��SAXһ���ǻ���XML�¼��Ľ�����ʽ��
 * ���Ƕ�����һ���Լ�������XML�ļ�����������֮��Ҳ�кܴ�
 * �Ĳ�ͬ��
 *
 * StAX��SAX�����𡪡���ʽ����������ʽ������������
 * StAX���ڴ���XML�¼��ķ�ʽ��ʹ��Ӧ�ó�����ӽ��ײ㣬
 * ������Ч���ϱ�SAX�����㡣
 *
 * SAX��Ӧ�ó����Ǳ����ڽ������ġ�
 * Ӧ�ó���ֻ�ܱ����ĵȴ���������XML�¼����͸��Լ�����
 * ����ÿһ���¼�����Ҫ�ڽ�����ʼ֮ǰ������׼����
 * ���ַ�ʽ����Ϊ���ƣ�push����
 * StAX������һ�֡�����pull�����ķ�ʽ��
 * ��Ӧ�ó��������ӽ������л�ȡ��ǰXML�¼�Ȼ��
 * ����������������ߺ��ԣ���StAXʹ��Ӧ�ó���������
 * ����Ȩ�����Լ򻯵��ô�����׼ȷ�ش�����Ԥ�ڵ����ݣ�����
 * ��������ʱֹͣ���������⣬���ڸ÷��������ڴ������ص���
 * Ӧ�ó�����Ҫ��ʹ�� SAX ����ģ���������״̬��
 * ��ժ�ԣ�https://blog.csdn.net/HD243608836/article/details/87571886��
 * errorCodePrefix 3
 * @author AricSun
 * @date 2020.08.13 17:14
 */
public class ParserXML {
    private Frame frame;

    public List<Frame> getFrameList() {
        return frameList;
    }

    private List<Frame> frameList;

    /*
     * function: ָ���ȡXML
     * ����ָ�루Cursor���� API ��XMLStreamReader��
     * ����Ӧ�ó���� XML
     * ��Ϊһ����ǣ����¼����������������������
     * ����һ��ָ��һ�����ļ�����ǰ����Ӧ�ó������
     * ������������ļ��Ŀ�ͷһֱ������β������һ��
     * �Ͳ� API������Ч�ʸߣ�����û���ṩ�ײ� XML
     * �ṹ�ĳ���
     *
     * ����һ�ֻ��ڵ�������API��XMLEventReader����
     * ���岻��������
     * ����Ϊʲôֻѡ�����α�API�������
     * https://docs.oracle.com/javase/tutorial/jaxp/stax/api.html
     * �˴���ȡһ�仰����������ܷ�����λ
     * �����磬�ڴ����ͼ��������ṹʱ����
     * ���α�APIЧ�ʸ��ߡ�
     * ������д�ڴ˴�����û��ǿ��ƫ�ã�����ʹ��
     * ������API����Ϊ�����Ϳ���չ��
     * @Param: [xml�ļ�·����String]
     * @Return: �ڶ����ؼ�֡��ʱ�䣺double
     */
    public double readXMLByCursor(String XMLFilePath){
        if (!Files.exists(Paths.get(XMLFilePath))){
            System.err.println("·�����ļ�ϵͳ�в����ڣ�");
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
                        frameList = new ArrayList<>();  // ��ʼ��
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
                                // ȡֻ��Ҫ��ֵ��
                                // timestamp������֪��ţ�keyframe�������ʱ����header����ȡֵ��ʽ��һ��
                                frame.setKeyframe(streamReader.getAttributeValue(namespaceURI,"Keyframe"));
//                                frame.setHeader(streamReader.getAttributeValue(namespaceURI,"Header"));

                                /*
                                * ���´��루for+switch������������ԭ��Ϊ������20������ʱ��ɼ���
                                * �����Ϸ�������ƽ��ֵ(525.75<566.4)����λ��(506<570.5)��
                                * ��С���·���switch��ʽ����ӵ�и��͵���Сֵ(498<504)�漣
                                * ͨ��ͼ�����ֱ���˽⵽����˵�Ϸ���������ʱ���
                                * ���ֵ(626ms)�������·��������Сֵ(504ms)��
                                * ��Ҳδ����witch�����ֵ(637)�����������Ϸ������ʱ��Ч��
                                * ���������·��ġ����Ҵ�����Ӿ���
                                *    getWithString  getWithIndex(switch)
                                * average	525.75  566.4
                                * ��׼��	    43.94	35.95
                                * ��λ��	    506	    570.5
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
//                                        ���´��뱻��������ԭ��Ϊ��ÿ�α���������ִ�����´��룬�����Դ�˷ѣ�
//                                        ��ѡ�������switch��ʽ�������֧�����ٴ��뱻��Чִ�еĴ�����
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
                        // ��һ��CHARACTERS�¼������صġ��հס�Ӧ��
                        // ��<Tags>��<Tag>֮���
                        // �����С��롰�ĸ��ո��Tab��
                        // ����ʱ��һ���������⼸�����������հס�
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
                        * �������ڶ����ؼ�֡��ʱ��ֹͣ��ȡ
                        * �����ͷ�����ؼ�֡���ڣ���ʱ���һ��
                        * ������ΪPT0S���������ѭ��һ��
                        * ����ʵ�ַ�ʽΪ�ж�ʱ���Ƿ�С��1s
                        * */
                        int cntKeyFrame = frameList.size();
                        if (cntKeyFrame >= 2){
                            double secondKeyFrameTime = TimeStamp2Double(
                                    frameList.get(cntKeyFrame-1).getTimeStamp()
                            );
                            /* 2020��10��4��
                            * ���ڷ����˴���0.15s���ֲ�������KetFrame�����޸ġ�
                            * ��20201002-213047-����~���ͽ��죡����-856.flv��
                            * �����趨�ĳ����Ƿ�ֹ�и0s��
                            * ��Ϊ����0s��KeyFrame������һ����֣�
                            * �����õ�>=1s��ԭ���ǣ���ʱ��Ϊ1s�ڵ�������ͬ�����Ա�����
                            * ��ʵ�ϻ��ǹ���ʧ�󡣼�ʹ��0.15s��Ҳ�ᱻ���ϳ�����
                            * ��Bվ�����������ԣ���0.15s���Ա��Ŵ��Ӱ��ۿ�����ĵز�
                            * ���޸�Ϊ��Ϊ0��������
                            * */
                            if (secondKeyFrameTime > 0){
                                return secondKeyFrameTime;
                            }
                        }
                        break;
                    case XMLStreamConstants.END_DOCUMENT:  // ���������λ��
                        System.out.println("=====end document=====");
                        break;
                    default:
                        break;
                }
                streamReader.next();
                /*
                    ���ϵĴ���������Ѵ��д������hasNext()�жϺ�
                    ��hasNext��ִ�к󣬴�ʱָ��λ�ô��ڿ�ͷ��
                    Ҳ���ǡ�START_DOCUMENT����λ�ã�
                    ��ʱִ�С�getEventType����������λ��
                    ���ϵĻ���������ֱ�������ĵ�ͷ��
                    ��ʼ���������START_ELEMENT��
                    ������Ҫ���ĵ���ʼʱ����ArrayList�ĳ�ʼ����
                    ���Եö������λ�ã�
                    ���Խ����д��������󣬲�����Ҳ�и�������
                    �����ĵ�β�ᱻ������
                    ��Ϊ���������һ����ʱ��hasNextΪ�٣�
                    �ǲ������ѭ���жϵģ�
                    �����������ѭ������������ж��߼�
                    ����Ŀǰ������˵���ã����ԾͲ����ˡ�
                 */
            }
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
        return -37.0;
    }
}
