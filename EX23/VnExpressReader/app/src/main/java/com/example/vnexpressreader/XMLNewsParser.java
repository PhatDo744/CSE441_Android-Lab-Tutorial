package com.example.vnexpressreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLNewsParser {
    private XmlPullParser parser;

    public ArrayList<NewsItem> parseXml(InputStream inputStream) {
        ArrayList<NewsItem> newsItems = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            NewsItem currentNewsItem = null;
            String currentTag = null;
            StringBuilder descriptionBuilder = new StringBuilder();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = tagName;
                        if ("item".equalsIgnoreCase(currentTag)) {
                            currentNewsItem = new NewsItem();
                            descriptionBuilder.setLength(0);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (text == null || text.trim().isEmpty() || currentNewsItem == null) break;
                        text = text.trim();

                        if ("title".equalsIgnoreCase(currentTag)) {
                            currentNewsItem.setTitle(text);
                        } else if ("link".equalsIgnoreCase(currentTag)) {
                            currentNewsItem.setLink(text);
                        } else if ("description".equalsIgnoreCase(currentTag)) {
                            descriptionBuilder.append(text);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (currentNewsItem != null) {
                            if ("item".equalsIgnoreCase(tagName)) {
                                String fullDescriptionHtml = descriptionBuilder.toString();
                                if (!fullDescriptionHtml.isEmpty()) {
                                    Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                                    Matcher matcher = pattern.matcher(fullDescriptionHtml);
                                    if (matcher.find()) {
                                        currentNewsItem.setImageUrl(matcher.group(1));
                                        try {
                                            URL imgUrl = new URL(currentNewsItem.getImageUrl());
                                            HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
                                            connection.setDoInput(true);
                                            connection.connect();
                                            InputStream imgInputStream = connection.getInputStream();
                                            Bitmap bitmap = BitmapFactory.decodeStream(imgInputStream);
                                            currentNewsItem.setImageBitmap(bitmap);
                                            imgInputStream.close();
                                            connection.disconnect(); // Đóng kết nối
                                        } catch (IOException e) {
                                            Log.e("XMLNewsParser", "Lỗi tải ảnh: " + currentNewsItem.getImageUrl(), e);
                                        }
                                    }

                                    String plainDescription;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        plainDescription = Html.fromHtml(fullDescriptionHtml, Html.FROM_HTML_MODE_LEGACY).toString();
                                    } else {
                                        plainDescription = Html.fromHtml(fullDescriptionHtml).toString();
                                    }
                                    currentNewsItem.setDescription(plainDescription.trim());
                                }
                                newsItems.add(currentNewsItem);
                            }
                        }
                        currentTag = null;
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e("XMLNewsParser", "Lỗi khi parse XML news: " + e.getMessage(), e);
        }
        return newsItems;
    }
}