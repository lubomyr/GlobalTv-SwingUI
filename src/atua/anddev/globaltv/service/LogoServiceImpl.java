package atua.anddev.globaltv.service;

import atua.anddev.globaltv.entity.Channel;
import atua.anddev.globaltv.entity.Logo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogoServiceImpl implements LogoService {
    private boolean logoLoaded;

    @Override
    public void setupLogos() {
        new Thread(new ParseTask()).start();
    }

    @Override
    public String getLogoByName(String str) {
        if (logoLoaded && (logoList.size() != 0)) {
            for (Logo logo : logoList) {
                if (str.equals(logo.getName())) {
                    return logo.getUrl() + logo.getIcon();
                }
            }
        }
        return null;
    }

    @Override
    public ImageIcon getIcon(Channel channel) {
        ImageIcon imageIcon = null;
        try {
            String urlPath;
            if ((channel.getIcon() != null) && (!channel.getIcon().isEmpty()))
                urlPath = channel.getIcon();
            else
                urlPath = getLogoByName(channel.getName());
            if (urlPath != null) {
                URL url = new URL(urlPath);
                BufferedImage image = ImageIO.read(url);
                try {
                    imageIcon = new ImageIcon(image);
                    imageIcon = scaleImage(imageIcon, 100, 25);
                } catch (Exception e) {
                }
            }
        } catch (IOException ignored) {
        }
        return imageIcon;
    }

    private ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if (icon.getIconWidth() > w) {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if (nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
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

                reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

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
                logoLoaded = true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
