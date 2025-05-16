package com.example.userinformationregistration;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText edtHoTen, edtCMND, edtBoSung;
    CheckBox chkDocBao, chkDocSach, chkDocCoding;
    RadioGroup group;
    Button btnGuiTT;

    public void doShowInfo(){
        String hoTen = edtHoTen.getText().toString();
        hoTen = hoTen.trim();
        if (hoTen.length() < 3) {
            edtHoTen.setError("Họ tên phải lớn hơn 3 ký tự");
            return;
        }

        String cmnd = edtCMND.getText().toString();
        cmnd = cmnd.trim();
        if (cmnd.length() != 9) {
            edtCMND.setError("CMND phải đúng 9 ký tự");
            return;
        }

        String bangCap = "";
        group = findViewById(R.id.radGroup);
        int id = group.getCheckedRadioButtonId();
        if (id == -1) {
            Toast.makeText(this, "Phải chọn bằng cấp", Toast.LENGTH_SHORT).show();
        }
        RadioButton rad = findViewById(id);
        bangCap = rad.getText().toString();

        String soThich = "";
        if (chkDocBao.isChecked()) {
            soThich += chkDocBao.getText().toString() + "\n";
        }
        if (chkDocSach.isChecked()){
            soThich += chkDocSach.getText().toString() + "\n";
        }
        if (chkDocCoding.isChecked()){
            soThich += chkDocCoding.getText().toString() + "\n";
        }

        String boSung = edtBoSung.getText().toString() + "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông tin cá nhân");
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        String msg = "Họ tên: " + hoTen + "\n" +
                "CMND: " + cmnd + "\n" +
                "Bằng cấp: " + bangCap + "\n" +
                "Sở thích: " + soThich + "\n" +
                "Thông tin bổ sung: " + boSung;
        builder.setMessage(msg);
        builder.create().show();
    }
    @Override
    public void onBackPressed() {
        if (!isFinishing()) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Question");
            b.setMessage("Are you sure you want to exit?");
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = b.create();
            dialog.show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edtHoTen = findViewById(R.id.edtHoTen);
        edtCMND = findViewById(R.id.edtCMND);
        chkDocBao = findViewById(R.id.chkDocBao);
        chkDocSach = findViewById(R.id.chkDocSach);
        chkDocCoding = findViewById(R.id.chkDocCoding);
        edtBoSung = findViewById(R.id.edtBoSung);
        btnGuiTT = findViewById(R.id.btnGuiTT);
        btnGuiTT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                doShowInfo();
            }
        });
        // Handle Enter key press
        edtBoSung.setImeOptions(EditorInfo.IME_ACTION_NONE);
        edtBoSung.setSingleLine(false);
        edtBoSung.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NONE) {
                // Prevent default behavior
                return true;
            }
            return false;
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}