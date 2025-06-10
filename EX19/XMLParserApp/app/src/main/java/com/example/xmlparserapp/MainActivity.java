package com.example.xmlparserapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnParse;
    ListView lv1;
    ArrayList<String> employeeDataList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnParse = findViewById(R.id.btnParse);
        lv1 = findViewById(R.id.lv1);
        employeeDataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, employeeDataList);
        lv1.setAdapter(adapter);

        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseXMLData();
            }
        });
    }

    private void parseXMLData() {
        employeeDataList.clear(); // Xóa dữ liệu cũ trước khi parse
        XmlPullParserFactory parserFactory;
        XmlPullParser parser;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            parser = parserFactory.newPullParser();
            InputStream inputStream = getAssets().open("employees.xml"); // Đảm bảo file này có trong thư mục assets
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            processParsing(parser);
            inputStream.close(); // Đóng InputStream sau khi sử dụng
            adapter.notifyDataSetChanged(); // Cập nhật ListView

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi parse XML: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException {
        int eventType = parser.getEventType();
        String currentEmployeeId = null;
        String currentEmployeeTitle = null;
        String currentEmployeeName = null;
        String currentEmployeePhone = null;
        String currentTag = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    currentTag = parser.getName();
                    if ("employee".equalsIgnoreCase(currentTag)) {
                        // Lấy thuộc tính 'id' và 'title' từ thẻ 'employee'
                        currentEmployeeId = parser.getAttributeValue(null, "id");
                        currentEmployeeTitle = parser.getAttributeValue(null, "title");
                        // Reset các trường khác cho employee mới
                        currentEmployeeName = null;
                        currentEmployeePhone = null;
                    }
                    break;

                case XmlPullParser.TEXT:
                    String text = parser.getText();
                    if (text == null || text.trim().isEmpty()) {
                        break; // Bỏ qua nếu text rỗng hoặc chỉ chứa khoảng trắng
                    }
                    if ("name".equalsIgnoreCase(currentTag) && currentEmployeeName == null) { // Chỉ lấy text đầu tiên cho name
                        currentEmployeeName = text.trim();
                    } else if ("phone".equalsIgnoreCase(currentTag) && currentEmployeePhone == null) { // Chỉ lấy text đầu tiên cho phone
                        currentEmployeePhone = text.trim();
                    }
                    break;

                case XmlPullParser.END_TAG:
                    String tagName = parser.getName();
                    if ("employee".equalsIgnoreCase(tagName)) {
                        // Khi kết thúc thẻ employee, thêm dữ liệu vào danh sách
                        if (currentEmployeeId != null && currentEmployeeTitle != null && currentEmployeeName != null && currentEmployeePhone != null) {
                            String employeeInfo = currentEmployeeId + "-" +
                                    currentEmployeeTitle + "-" +
                                    currentEmployeeName + "-" +
                                    currentEmployeePhone;
                            employeeDataList.add(employeeInfo);
                        }
                        // Reset các biến cho employee tiếp theo
                        currentEmployeeId = null;
                        currentEmployeeTitle = null;
                        currentEmployeeName = null;
                        currentEmployeePhone = null;
                    }
                    currentTag = null; // Reset currentTag khi gặp END_TAG
                    break;
            }
            eventType = parser.next();
        }
    }
}