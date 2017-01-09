package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.ChannelGuide;
import atua.anddev.globaltv.entity.GuideProv;
import atua.anddev.globaltv.entity.Programme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public interface GuideService {
    List<GuideProv> guideProvList = new ArrayList<>();
    List<ChannelGuide> channelGuideList = new ArrayList<>();
    List<Programme> programmeList = new ArrayList<>();

    boolean checkForUpdate();

    void parseGuide();

    String getProgramTitle(String chName);

    String getProgramDesc(String chName);

    List<Programme> getChannelGuide(String chName);

    String decodeSymbols(String str);

    void setupGuideProvList();

    String getTotalTimePeriod();

    Calendar decodeDateTime(String str);
}
