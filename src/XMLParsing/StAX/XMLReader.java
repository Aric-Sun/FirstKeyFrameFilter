package XMLParsing.StAX;

/**
 * 调用StAX读取xml
 * errorCodePrefix 4
 * @author AricSun
 * @date 2020.08.13 17:59
 */
public class XMLReader {
    public double getSecondKeyFrameTime(String xmlPath) {
//        long startTime = System.currentTimeMillis();
        if (null == xmlPath){
            return -45.0;
        }
        ParserXML parserXML = new ParserXML();
//        Path path = Paths.get("xml/959-TypicalCase.xml");
        double secondKeyFrameTime = parserXML.readXMLByCursor(xmlPath);
//        List<Frame> frameList = parserXML.getFrameList();
//        System.out.println(frameList);
        System.out.println("第二个关键帧的时间戳为：" + secondKeyFrameTime + "s");

        return secondKeyFrameTime;
//        long endTime = System.currentTimeMillis();
//        System.out.println(endTime - startTime + "ms");
    }
}
