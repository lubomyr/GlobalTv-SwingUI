package atua.anddev.globaltv.service;

import atua.anddev.globaltv.Services;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class TranslationServiceImpl implements TranslationService, Services {


    @Override
    public void getTranslationData(String translationFile) {
        String result = null, name = null;
        localMap.clear();
        origNames.clear();
        tranNames.clear();
        try {
            File fXmlFile;
            fXmlFile = new File(myPath + translationFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nRes = doc.getElementsByTagName("resources");
            Element eStr = (Element) nRes.item(0);
            NodeList nlStr = eStr.getElementsByTagName("string");
            for (int i = 0; i < nlStr.getLength(); i++) {
                Node nStr = nlStr.item(i);
                result = nStr.getTextContent();
                Element eNam = (Element) nlStr.item(i);
                name = eNam.getAttribute("name");
                localMap.put(name, result);
            }
            NodeList nlStArr = eStr.getElementsByTagName("string-array");
            Element eStArr = (Element) nRes.item(0);
            NodeList nlItm = eStr.getElementsByTagName("item");
            for (int i = 0; i < nlItm.getLength(); i++) {
                Node nItm = nlItm.item(i);
                result = nItm.getTextContent();
                if (i < nlItm.getLength() / 2) {
                    origNames.add(result);
                } else {
                    tranNames.add(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String local(String input) {
        return localMap.get(input);
    }

    @Override
    public String translateCategory(String input) {
        String output;
        output = input;

        if (origNames.size() > 0) {
            for (int i = 0; i < origNames.size(); i++) {
                if (origNames.get(i).equalsIgnoreCase(input)) {
                    output = tranNames.get(i);
                    break;
                }
            }
        }
        return output;
    }


}
