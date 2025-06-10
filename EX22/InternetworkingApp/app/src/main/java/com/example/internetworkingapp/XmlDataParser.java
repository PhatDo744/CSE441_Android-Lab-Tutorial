package com.example.internetworkingapp;

import android.os.Build;
import android.text.Html;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class XmlDataParser {

    private XmlPullParser parser;

    public ArrayList<String> parseXml(InputStream inputStream) {
        ArrayList<String> results = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // factory.setNamespaceAware(true); // Bật nếu XML của bạn sử dụng namespaces phức tạp
            parser = factory.newPullParser();
            parser.setInput(inputStream, null); // null for auto-detect encoding

            int eventType = parser.getEventType();
            String currentTag = null;
            String title = "", link = "", description = "", pubDate = ""; // Các trường dữ liệu mới

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = tagName;
                        if ("item".equalsIgnoreCase(currentTag)) { // Thường thì các mục tin tức nằm trong thẻ <item>
                            // Reset cho item mới
                            title = ""; link = ""; description = ""; pubDate = "";
                        }
                        break;
                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        if (text == null || text.trim().isEmpty()) break;
                        text = text.trim();

                        if ("title".equalsIgnoreCase(currentTag)) {
                            title = text;
                        } else if ("link".equalsIgnoreCase(currentTag)) {
                            link = text;
                        } else if ("description".equalsIgnoreCase(currentTag)) {
                            // Sử dụng Html.fromHtml với cờ phù hợp
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                description = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString();
                            } else {
                                //noinspection deprecation
                                description = Html.fromHtml(text).toString();
                            }
                        } else if ("pubDate".equalsIgnoreCase(currentTag)) {
                            pubDate = text;
                        }
                        // Thêm các trường khác nếu cần từ API (ví dụ: category, guid, ...)
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equalsIgnoreCase(tagName)) {
                            results.add("Tiêu đề: " + title + "\nMiêu tả: " + description + "\nNgày đăng: " + pubDate + "\nLink: " + link);
                        }
                        currentTag = null; // Rất quan trọng để reset currentTag
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e("XmlDataParser", "Lỗi khi parse XML: " + e.getMessage(), e);
            // Có thể thêm một thông báo lỗi vào results nếu muốn hiển thị trên UI
            // results.add("Lỗi phân tích XML: " + e.getMessage());
            return results; // Trả về danh sách (có thể rỗng hoặc chứa lỗi)
        }
        return results;
    }
}