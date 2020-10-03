package XMLParsing.DOM;

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
 * DOM��ȡXML
 * ������ʵ�ַ����ǽ�����XML�ļ�
 * �����ڴ��в������ṹ����
 * ��CPU��ʱռ�������
 * ����������ֻ�������޸ĵ�Ŀ��
 * �Һ�ʱҲ�ϳ����ò���ʧ��
 * ����֮����
 * @author AricSun
 * @date 2020.08.11 23:11
 */
public class XMLReader {
    public static void main(String[] args) {
        // �̶���·������->������->��������
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("xml/747.xml");
            /*
                ��߲�Ӧ�ð��ļ������ڴ˴�д����Ӧ����Ϊ�������������ⲿ���룬
                �����˷���ֻ������׶Σ�Ŀǰ�ѱ����ã�����û�м����Ż��ı�Ҫ��
                ֻ����¼����
             */
            NodeList tagList = document.getElementsByTagName("Tag");  // ֻ��Ҫ��ȡ�˽ڵ�
//            System.out.println("һ����"+tagList.getLength()+"���ڵ�");
            int CntKeyFrame = 0;
            /* �����ؼ�֡�������������ڶ����ؼ�֡ʱ��������������Ѿ��ҵ���
                �˴���һ�����⣬��������ؼ�֡���ڣ�����ʱ��һ��
                ��ͨ������Ϊ�и���ɵĲ��������flv�ǲ��������������
                ������ʵ��������ı�Ҫ�ģ�����Ӧ�þ����ݴ���
                ��ֻ��Ҫ��Խ�����������1s��ĵڶ����ؼ�֡
                ��Ϊ1s���ڵ�������ͬ���ǿ��Ա����̵ģ�
                �����е㳤����������ʵ����������и���
                ���������ҵľ��飬ȱ֡Ҫô6/7/8/9s��Ҫô�Ͳ�ȱ
                �˷����Ѿ�ͣ��������ֻ���뷨��¼
             */

            // ����ÿһ��tag����֡
            for (int i=0; i<tagList.getLength(); ++i){
//                System.out.println("=================���濪ʼ������" + (i + 1) + "���������=================");
                Node tag = tagList.item(i);  // get��ǰ�ڵ�
                /*
                    �˴��и��򵥵ķ������������ѭ��
                    ����Nodeǿ��ת��ΪElement
                    Ȼ��Ϳ���ͨ��Ԫ�ؽڵ�API�������Ժ��ı�����
                    |--getAttribute  �������ֵ
                    |--getTextContent ���Ԫ���ڲ��ı�����
                 */
                NamedNodeMap attrs = tag.getAttributes();  // ��ȡ���Լ���
                String timeStamp = "NULL"; // ʱ��ֵ�����硾PT8.333S����
                /* ���Ǳ���Ŀֻ��ȥ��һ��֡���������
                    ���Բ����С����ӡ������λ���֣�����xml�б���ΪM��
                    ���硾PT16M6.681S��
                 */

//                System.out.println("�� " + (i + 1) + "���鹲��" + attrs.getLength() + "������");
                // ����һ֡����������
                for (int j=0; j<attrs.getLength(); ++j){
                    Node attr = attrs.item(j);  // get��ǰ����
                    if ("TimeStamp".equals(attr.getNodeName())){
                        timeStamp = attr.getNodeValue();
                    }
                    // Ĭ���ȼ�¼��ʱ�䣬�����ǰ֡Ϊ�ؼ�֡�������+1
                    if ("Keyframe".equals(attr.getNodeName()) && "true".equals(attr.getNodeValue())){
                        CntKeyFrame++;
                    }
                }
                /*
                ������������2ʱ������ʾ�����ڶ����ؼ�֡λ��
                ��ʱ��¼�¹ؼ�֡���ɣ��˴�Ϊ��ӡ����
                ����Ϊʲô��3������Ϊ�����ᵽ��
                �������ؼ�֡���ڣ�ʱ��һ�����Ĵ������
                �˴�����ö������޸�
                 */
                if (CntKeyFrame == 3){
                    System.out.println(TimeStamp2Double(timeStamp));
                    break;
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * function: ��timestamp��PTxx.xxSͨ������ƥ�������������
     * @Param: [timeStampʱ���]
     * @Return: java.lang.Double
     */
    public static Double TimeStamp2Double(String timeStamp){
        return Double.parseDouble(timeStamp.replaceAll("[^\\d.]", ""));
    }
}
