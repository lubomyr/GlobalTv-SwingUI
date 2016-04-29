package atua.anddev.globaltv;

import atua.anddev.globaltv.dao.ChannelDb;
import atua.anddev.globaltv.dao.DBHelper;
import atua.anddev.globaltv.dao.FavoriteDb;
import atua.anddev.globaltv.dao.PlaylistDb;
import atua.anddev.globaltv.service.*;

public interface Services {
    PlaylistService playlistService = new PlaylistServiceImpl();
    ChannelService channelService = new ChannelServiceImpl();
    FavoriteService favoriteService = new FavoriteServiceImpl();
    SearchService searchService = new SearchServiceImpl();
    TranslationService tService = new TranslationServiceImpl();
    GuideService guideService = new GuideServiceImpl();
    DBHelper dbHelper = new DBHelper();
    PlaylistDb playlistDb = new PlaylistDb();
    ChannelDb channelDb = new ChannelDb();
    FavoriteDb favoriteDb = new FavoriteDb();
    String myPath = "";
}
