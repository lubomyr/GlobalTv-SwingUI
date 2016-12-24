package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.ChannelGuide;
import atua.anddev.globaltv.entity.Programme;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import static atua.anddev.globaltv.Services.myPath;

public class GuideServiceImpl implements GuideService {

    public boolean checkForUpdate() {
        boolean result = false;
        if (!isGuideExist()) {
            result = true;
        } else {
            result = false;
            Thread thread = new Thread(parseGuideRunnable);
            thread.start();
        }
        return result;
    }

    private boolean isGuideExist() {
        boolean result;
        File file = new File(myPath + "ttv.xmltv.xml.gz");
        result = file.exists();
        return result;
    }

    Runnable parseGuideRunnable = new Runnable() {

        @Override
        public void run() {
            parseGuide();
        }
    };

    public void parseGuide() {
        System.out.println("parsing...");
        try {
            File fXmlFile;
            fXmlFile = new File(myPath + "ttv.xmltv.xml.gz");
            InputStream is = new FileInputStream(fXmlFile);
            GZIPInputStream gzipIs = new GZIPInputStream(is);
            Scanner sc = new Scanner(gzipIs, "UTF8").useDelimiter("[\n]");
            String id = null, lang = null, displayName =  null;
            String start = null, stop = null, channel = null;
            String title = null, desc = null, category = null;
            boolean descHasNext = false;
            while(sc.hasNext()) {
                String lineStr = sc.next();
                if (lineStr.startsWith("<channel")) {
                    int idIndex = -1;
                    if (lineStr.contains("id="))
                        idIndex = lineStr.indexOf("id=");
                        id = lineStr.substring(idIndex + 4, lineStr.lastIndexOf("\""));
                }
                if (lineStr.startsWith("<display-name")) {
                    int langIndex = -1;
                    if (lineStr.contains("lang=")) {
                        langIndex = lineStr.indexOf("lang=");
                        lang = lineStr.substring(langIndex + 6, langIndex + 8);
                    }
                    displayName = lineStr.substring(lineStr.indexOf(">") + 1, lineStr.indexOf("</display-name>"));

                }
                if (lineStr.startsWith("</channel>")) {
                    ChannelGuide channelGuide = new ChannelGuide(id, lang, displayName);
                    channelGuideList.add(channelGuide);
                    id = null;
                    lang = null;
                    displayName = null;
                }
                if (lineStr.startsWith("<programme")) {
                    int startPos = -1;
                    if (lineStr.contains("start=")) {
                        startPos = lineStr.indexOf("start=");
                        start = lineStr.substring(startPos + 7, startPos + 27);

                    }
                    int stopPos = -1;
                    if (lineStr.contains("stop=")) {
                        stopPos = lineStr.indexOf("stop=");
                        stop = lineStr.substring(stopPos + 6, stopPos + 26);
                    }
                    int channelPos = -1;
                    if (lineStr.contains("channel=")) {
                        channelPos = lineStr.indexOf("channel=");
                        channel = lineStr.substring(channelPos + 9, lineStr.lastIndexOf("\""));
                    }
                }
                if (lineStr.startsWith("<title")) {
                    title = lineStr.substring(lineStr.indexOf(">") + 1, lineStr.indexOf("</title>"));
                }
                if (descHasNext) {
                    if (lineStr.contains("</desc>")) {
                        desc += lineStr.substring(0, lineStr.indexOf("</desc>"));
                        descHasNext = false;
                    } else {
                        desc += lineStr.substring(0, lineStr.length()) + "\n";
                        descHasNext = true;
                    }
                }
                if (lineStr.startsWith("<desc")) {
                    if (lineStr.contains("</desc>")) {
                        desc = lineStr.substring(lineStr.indexOf(">") + 1, lineStr.indexOf("</desc>"));
                        descHasNext = false;
                    }
                    else {
                        desc = lineStr.substring(lineStr.indexOf(">") + 1, lineStr.length()) + "\n";
                        descHasNext = true;
                    }
                }
                if (lineStr.startsWith("<category")) {
                    category = lineStr.substring(lineStr.indexOf(">") + 1, lineStr.indexOf("</category>"));
                }

                if (lineStr.startsWith("</programme>")) {
                    Programme programme = new Programme(start, stop, channel, title, desc, category);
                    programmeList.add(programme);
                    start = null;
                    stop = null;
                    channel = null;
                    title = null;
                    desc = null;
                    category = null;
                    descHasNext = false;
                }
            }
            gzipIs.close();
            is.close();
            System.out.println("succesfuly parsed");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
