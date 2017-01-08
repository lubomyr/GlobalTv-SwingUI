package atua.anddev.globaltv.service;

import atua.anddev.globaltv.MainActivity;
import atua.anddev.globaltv.Services;
import atua.anddev.globaltv.entity.Favorites;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteServiceImpl implements FavoriteService, Services {

    @Override
    public int indexOfFavoriteByNameAndProv(String name, String prov) {
        int result = -1;
        for (int i = 0; i < sizeOfFavoriteList(); i++) {
            if (name.equals(favorites.get(i).getName()) && prov.equals(favorites.get(i).getProv())) {
                result = i;
            }
        }
        return result;
    }

    @Override
    public List<String> getFavoriteListForSelProv() {
        List<String> arr = new ArrayList<String>();
        for (int i = 0; i < sizeOfFavoriteList(); i++) {
            for (int j = 0; j < channelService.sizeOfChannelList(); j++) {
                if (getFavoriteById(i).getName().equals(channelService.getChannelById(j).getName()) && !arr.contains(getFavoriteById(i).getName())
                        && getFavoriteById(i).getProv().equals(playlistService.getActivePlaylistById(MainActivity.selectedProvider).getName())) {
                    arr.add(getFavoriteById(i).getName());
                }
            }
        }
        return arr;
    }

    @Override
    public void deleteFromFavoritesById(int id) {
        favorites.remove(id);
    }

    @Override
    public void addToFavoriteList(String name, String prov) {
        favorites.add(new Favorites(name, prov));
    }

    @Override
    public Favorites getFavoriteById(int id) {
        return favorites.get(id);
    }

    @Override
    public void clearAllFavorites() {
        favorites.clear();
    }

    @Override
    public int sizeOfFavoriteList() {
        return favorites.size();
    }

    @Override
    public void saveFavorites() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            for (int j = 0; j < sizeOfFavoriteList(); j++) {
                // favorites elements
                Element favorites = doc.createElement("favorites");
                rootElement.appendChild(favorites);

                // channel elements
                Element channel = doc.createElement("channel");
                channel.appendChild(doc.createTextNode(getFavoriteById(j).getName()));
                favorites.appendChild(channel);

                // playlist elements
                Element playlist = doc.createElement("playlist");
                playlist.appendChild(doc.createTextNode(getFavoriteById(j).getProv()));
                favorites.appendChild(playlist);
            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("favorites.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            // System.out.println("File saved!");

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    @Override
    public void loadFavorites() throws IOException {
        String channel = null, playlist = null;

        try {
            File fXmlFile;
            fXmlFile = new File(myPath + "favorites.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            // optional, but recommended
            // read this -
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("favorites");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    channel = eElement.getElementsByTagName("channel").item(0).getTextContent();
                    playlist = eElement.getElementsByTagName("playlist").item(0).getTextContent();

                    addToFavoriteList(channel, playlist);
                }
            }
        } catch (Exception ignored) {
        }
    }
}
