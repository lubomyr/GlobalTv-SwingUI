package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.ChannelGuide;
import atua.anddev.globaltv.entity.Programme;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

import static atua.anddev.globaltv.Services.myPath;

public class GuideServiceImpl implements GuideService {
    private static final DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private Calendar currentTime;

    public boolean checkForUpdate() {
        boolean result = false;
        if (!isGuideExist()) {
            result = true;
        } else {
            currentTime = Calendar.getInstance();
            parseGuide();
            result = checkGuideDates();
            //Thread thread = new Thread(parseGuideRunnable);
            //thread.start();
        }
        return result;
    }

    private boolean isGuideExist() {
        boolean result;
        File file = new File(myPath + "ttv.xmltv.xml.gz");
        result = file.exists();
        return result;
    }

    private Runnable parseGuideRunnable = new Runnable() {

        @Override
        public void run() {
            parseGuide();
            checkGuideDates();
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

    @Override
    public String getProgramTitle(String chName) {
        currentTime = Calendar.getInstance();
        String id = getIdByChannelName(chName);
        return getProgramTitlebyId(id);
    }

    @Override
    public String getProgramDesc(String chName) {
        currentTime = Calendar.getInstance();
        String id = getIdByChannelName(chName);
        return getProgramDescbyId(id);
    }

    private boolean checkGuideDates() {
        boolean result = false;
        List<String> dateList = new ArrayList<String>();
        if (channelGuideList.size() > 0) {
            String chId = channelGuideList.get(0).getId();
            for (Programme programme : programmeList) {
                if (programme.getChannel().equals(chId)) {
                    dateList.add(programme.getStart());
                }
            }
            String startDateStr = dateList.get(0);
            String endDateStr = dateList.get(dateList.size() - 1);
            Calendar startDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            try {
                startDate.setTime(sdf.parse(startDateStr));
                endDate.setTime(sdf.parse(endDateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            result = !(currentTime.after(startDate) && currentTime.before(endDate));
        }
        return result;
    }

    private String getIdByChannelName(String chName) {
        String result = null;
        for (ChannelGuide channelGuide : channelGuideList) {
            if (chName.equals(channelGuide.getDisplayName()))
                result = channelGuide.getId();
        }
        return result;
    }

    private String getProgramTitlebyId(String id) {
        String result = null;
        for (Programme programme : programmeList) {
            if (programme.getChannel().equals(id)) {
                String startDateStr = programme.getStart();
                String endDateStr = programme.getStop();
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                try {
                    startDate.setTime(sdf.parse(startDateStr));
                    endDate.setTime(sdf.parse(endDateStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // convert time to Ukrainian time zone
                startDate.add(Calendar.HOUR,-1);
                endDate.add(Calendar.HOUR,-1);
                if (currentTime.after(startDate) && currentTime.before(endDate)) {
                    result = programme.getTitle();
                    result = decodeSymbols(result);
                }
            }
        }
        return result;
    }

    private String getProgramDescbyId(String id) {
        String result = null;
        for (Programme programme : programmeList) {
            if (programme.getChannel().equals(id)) {
                String startDateStr = programme.getStart();
                String endDateStr = programme.getStop();
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();
                try {
                    if (!startDateStr.isEmpty())
                        startDate.setTime(sdf.parse(startDateStr));
                    if (!endDateStr.isEmpty())
                        endDate.setTime(sdf.parse(endDateStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // convert time to Ukrainian time zone
                startDate.add(Calendar.HOUR,-1);
                endDate.add(Calendar.HOUR,-1);
                if (currentTime.after(startDate) && currentTime.before(endDate)) {
                    result = programme.getDesc();
                    result = decodeSymbols(result);
                }
            }
        }
        return result;
    }

    public List<Programme> getChannelGuide(String chName) {
        List<Programme> result = new ArrayList<Programme>();
        String id = getIdByChannelName(chName);
        for (Programme programme : programmeList) {
            if (programme.getChannel().equals(id)) {
                result.add(programme);
            }
        }
        return result;
    }

    public String decodeSymbols(String str) {
        String res = str;
        if ((res != null) && !res.isEmpty()) {
            if (res.contains("&amp;quot;"))
                res = res.replace("&amp;quot;", "\"");
            if (res.contains("&amp;apos;"))
                res = res.replace("&amp;apos;", "'");
        } else
            res = "";
        return res;
    }

}
