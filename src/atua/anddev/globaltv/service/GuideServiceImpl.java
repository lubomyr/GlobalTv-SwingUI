package atua.anddev.globaltv.service;

import atua.anddev.globaltv.Global;
import atua.anddev.globaltv.entity.ChannelGuide;
import atua.anddev.globaltv.entity.GuideProv;
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
    private static final DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss Z");
    private final String ukrEpg = " (на укр.)";
    private Calendar currentTime;

    public boolean checkForUpdate() {
        boolean result = false;
        if (!isGuideExist()) {
            result = true;
        } else {
            currentTime = Calendar.getInstance();
            parseGuide();
            result = checkGuideDates();
        }
        return result;
    }

    private boolean isGuideExist() {
        boolean result;
        GuideProv guideProv = guideProvList.get(Global.selectedGuideProv);
        File file = new File(myPath + guideProv.getFile());
        result = file.exists();
        return result;
    }

    public void parseGuide() {
        System.out.println("parsing program guide...");
        Global.guideLoaded = false;
        channelGuideList.clear();
        programmeList.clear();
        try {
            File fXmlFile;
            GuideProv guideProv = guideProvList.get(Global.selectedGuideProv);
            fXmlFile = new File(myPath + guideProv.getFile());
            InputStream is = new FileInputStream(fXmlFile);
            GZIPInputStream gzipIs = new GZIPInputStream(is);
            Scanner sc = new Scanner(gzipIs, "UTF8").useDelimiter("[\n]");
            String id = null, lang = null, displayName =  null;
            String start = null, stop = null, channel = null;
            String title = null, desc = null, category = null;
            boolean descHasNext = false;
            while(sc.hasNext()) {
                String lineStr = sc.next();
                if (lineStr.contains("<channel")) {
                    int idIndex = -1;
                    if (lineStr.contains("id="))
                        idIndex = lineStr.indexOf("id=");
                        id = lineStr.substring(idIndex + 4, lineStr.lastIndexOf("\""));
                }
                if (lineStr.contains("<display-name")) {
                    int langIndex = -1;
                    if (lineStr.contains("lang=")) {
                        langIndex = lineStr.indexOf("lang=");
                        lang = lineStr.substring(langIndex + 6, langIndex + 8);
                    }
                    displayName = lineStr.substring(lineStr.indexOf(">") + 1, lineStr.indexOf("</display-name>"));

                }
                if (lineStr.contains("</channel>")) {
                    ChannelGuide channelGuide = new ChannelGuide(id, lang, displayName);
                    channelGuideList.add(channelGuide);
                    id = null;
                    lang = null;
                    displayName = null;
                }
                if (lineStr.contains("<programme")) {
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
                if (lineStr.contains("<title")) {
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
                if (lineStr.contains("<desc")) {
                    if (lineStr.contains("</desc>")) {
                        desc = lineStr.substring(lineStr.indexOf(">") + 1, lineStr.indexOf("</desc>"));
                        descHasNext = false;
                    }
                    else {
                        desc = lineStr.substring(lineStr.indexOf(">") + 1, lineStr.length()) + "\n";
                        descHasNext = true;
                    }
                }
                if (lineStr.contains("<category")) {
                    category = lineStr.substring(lineStr.indexOf(">") + 1, lineStr.indexOf("</category>"));
                }

                if (lineStr.contains("</programme>")) {
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
            Global.guideLoaded = true;
            System.out.println("parsing finished");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getProgramTitle(String chName) {
        currentTime = Calendar.getInstance();
        String id = getIdByChannelName(chName + ukrEpg);
        if (id == null)
            id = getIdByChannelName(chName);
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
        List<String> dateList = new ArrayList<>();
        if (channelGuideList.size() > 0) {
            String chId = channelGuideList.get(0).getId();
            for (Programme programme : programmeList) {
                if (programme.getChannel().equals(chId)) {
                    dateList.add(programme.getStart());
                }
            }
            Calendar startDate = decodeDateTime(dateList.get(0));
            Calendar endDate = decodeDateTime(dateList.get(dateList.size() - 1));
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
                Calendar startTime = decodeDateTime(programme.getStart());
                Calendar stopTime = decodeDateTime(programme.getStop());
                if (currentTime.after(startTime) && currentTime.before(stopTime)) {
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
                Calendar startTime = decodeDateTime(programme.getStart());
                Calendar stopTime = decodeDateTime(programme.getStop());
                if (currentTime.after(startTime) && currentTime.before(stopTime)) {
                    result = programme.getDesc();
                    result = decodeSymbols(result);
                }
            }
        }
        return result;
    }

    public Calendar decodeDateTime(String str) {
        Calendar result = Calendar.getInstance();
        try {
            if (!str.isEmpty())
                result.setTime(sdf.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Programme> getChannelGuide(String chName) {
        List<Programme> result = new ArrayList<>();
        String id = getIdByChannelName(chName + ukrEpg);
        if (id == null)
            id = getIdByChannelName(chName);
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

    public void setupGuideProvList() {
        guideProvList.add(new GuideProv("Epg.in.ua", "http://epg.in.ua/epg/tvprogram_ua_ru.gz",
                "epginua.xml.gz"));
        guideProvList.add(new GuideProv("TeleGuide.info", "http://www.teleguide.info/download/new3/xmltv.xml.gz",
                "teleguide.xml.gz"));
        guideProvList.add(new GuideProv("Torrent-tv.ru", "http://api.torrent-tv.ru/ttv.xmltv.xml.gz",
                "ttvru.xml.gz"));
    }

    public String getTotalTimePeriod() {
        String result = null;
        final DateFormat totalSdf = new SimpleDateFormat("dd.MM");
        List<String> dateList = new ArrayList<>();
        if (channelGuideList.size() > 0) {
            String chId = channelGuideList.get(0).getId();
            for (Programme programme : programmeList) {
                if (programme.getChannel().equals(chId)) {
                    dateList.add(programme.getStart());
                }
            }
            Calendar startDate = decodeDateTime(dateList.get(0));
            Calendar endDate = decodeDateTime(dateList.get(dateList.size() - 1));
            result = totalSdf.format(startDate.getTime()) + " - " + totalSdf.format(endDate.getTime());
        }
        return result;
    }

    public String getChannelNameById(String id) {
        for (ChannelGuide cg : channelGuideList) {
            if (id.equals(cg.getId()))
                return cg.getDisplayName();
        }
        return null;
    }

    public List<Programme> searchAllPeriod(String str) {
        List<Programme> list = new ArrayList<>();
        for (Programme p : programmeList) {
            if (p.getTitle().toLowerCase().contains(str.toLowerCase()))
                list.add(p);
        }
        return list;
    }

    public List<Programme> searchAfterMoment(String str) {
        List<Programme> list = new ArrayList<>();
        for (Programme p : programmeList) {
            if (p.getTitle().toLowerCase().contains(str.toLowerCase())) {
                Calendar stopTime = decodeDateTime(p.getStop());
                if (currentTime.before(stopTime))
                    list.add(p);
            }
        }
        return list;
    }

    public List<Programme> searchToday(String str) {
        List<Programme> list = new ArrayList<>();
        for (Programme p : programmeList) {
            if (p.getTitle().toLowerCase().contains(str.toLowerCase())) {
                Calendar startTime = decodeDateTime(p.getStart());
                Calendar stopTime = decodeDateTime(p.getStop());
                if ((currentTime.get(Calendar.MONTH) == startTime.get(Calendar.MONTH)) &&
                        (currentTime.get(Calendar.DAY_OF_MONTH) == startTime.get(Calendar.DAY_OF_MONTH)))
                    list.add(p);
            }
        }
        return list;
    }

    public List<Programme> searchCurrentMoment(String str) {
        List<Programme> list = new ArrayList<>();
        for (Programme p : programmeList) {
            if (p.getTitle().toLowerCase().contains(str.toLowerCase())) {
                Calendar startTime = decodeDateTime(p.getStart());
                Calendar stopTime = decodeDateTime(p.getStop());
                if (currentTime.after(startTime) && currentTime.before(stopTime))
                    list.add(p);
            }
        }
        return list;
    }

}
