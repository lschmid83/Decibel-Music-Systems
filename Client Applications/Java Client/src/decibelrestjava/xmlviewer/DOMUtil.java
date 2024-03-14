package decibelrestjava.xmlviewer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * @author Santhosh Kumar T - santhosh@in.fiorano.com
 * (http://www.javalobby.org/java/forums/t19666.html)
 */
public class DOMUtil {

    public static Document createDocument(InputSource is) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(is);
    }
}
