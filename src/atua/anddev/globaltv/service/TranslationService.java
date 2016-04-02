package atua.anddev.globaltv.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TranslationService {
    Map<String, String> localMap = new HashMap<String, String>();
    List<String> origNames = new ArrayList<String>();
    List<String> tranNames = new ArrayList<String>();

    void getTranslationData(String path);

    String local(String input);

    String translateCategory(String input);
}
