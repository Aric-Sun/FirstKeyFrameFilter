package XMLParsing.SAX;

import dto.Frame;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author AricSun
 * @date 2020.08.13 3:09
 */
public class XMLReader {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try {
            // FrameHandler相当于触发器，由main调用
            FrameHandler frameHandler = new FrameHandler();
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(new File("xml/352-TypicalCase.xml"),frameHandler);
            List<Frame> frameList = frameHandler.getFrameList();
//            System.out.println(frameList);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime + "ms");
    }
}
