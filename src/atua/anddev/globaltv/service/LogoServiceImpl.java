package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Logo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogoServiceImpl implements LogoService {

    @Override
    public void setupLogos() {
        new Thread(new ParseTask()).start();
    }

    @Override
    public String getLogoByName(String str) {
        if (logoList.size() != 0) {
            for (Logo logo : logoList) {
                if (str.equals(logo.getName())) {
                    return logo.getUrl() + logo.getIcon();
                }
            }
        }
        return null;
    }

    private void addToLogoList(String name, String icon, String url) {
        Logo logo = new Logo(name, icon, url);
        logoList.add(logo);
    }

    private class ParseTask extends Thread {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        public void run() {
            String json = doInBackground();
            onPostExecute(json);
        }

        String doInBackground(Void... params) {
            try {
                URL url = new URL("https://dl.dropboxusercontent.com/u/47797448/playlist/icons.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        void onPostExecute(String strJson) {
            JSONObject dataJsonObj = null;

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONArray json = dataJsonObj.getJSONArray("channel");
                for (int i = 0; i < json.length(); i++) {
                    JSONObject el = json.getJSONObject(i);
                    String name = el.getString("name");
                    String icon = el.getString("icon");
                    String url = el.getString("url");
                    addToLogoList(name, icon, url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
