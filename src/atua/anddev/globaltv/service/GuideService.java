package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.ChannelGuide;
import atua.anddev.globaltv.entity.Programme;

import java.util.ArrayList;
import java.util.List;

public interface GuideService {
   String guideUrl = "http://api.torrent-tv.ru/ttv.xmltv.xml.gz";
    List<ChannelGuide> channelGuideList = new ArrayList<ChannelGuide>();
    List<Programme> programmeList = new ArrayList<Programme>();

    boolean checkForUpdate();
}
