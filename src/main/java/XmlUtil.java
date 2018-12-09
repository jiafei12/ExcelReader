import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import java.io.InputStream;
import java.util.*;

public class XmlUtil {

    /**
     * 根据文件名字获取当前项目类路径下的输入流
     * @param xmlName
     * @return
     */
    private static InputStream getXmlResource(String xmlName)
    {
        return XmlUtil.class.getResourceAsStream(xmlName+".xml");
    }

    /**
     * 利用SAXReader对象利用流读取对象，将xml数据读入内存中
     * @param xmlName
     */
     public static List<Map<String,String>> parseXml(String xmlName) throws DocumentException {
         SAXReader reader = new SAXReader();
         Document doc  =reader.read(XmlUtil.getXmlResource(xmlName));
         Element root =doc.getRootElement();
         Iterator<Element> it=root.elementIterator();
         List elements=new ArrayList();
         Map  elementMap;
         while(it.hasNext())
         {
             Element e=it.next();
             elementMap=new LinkedHashMap();
             elementMap.put("sheet",e.attribute("sheet").getValue());
             elementMap.put("tableName",e.attribute("tableName").getValue());
             elementMap.put("dateRow",e.attribute("dateRow").getValue());
             elementMap.put("startRow",e.attribute("startRow").getValue());
             elementMap.put("endRow",e.attribute("endRow").getValue());
             elementMap.put("startRow",e.attribute("startRow").getValue());
             elementMap.put("startCol",e.attribute("startCol").getValue());
             elementMap.put("endCol",e.attribute("endCol").getValue());
             elementMap.put("skipRow",e.attribute("skipRow").getValue());
             elements.add(elementMap);
         }
         return elements;

     }
}
