package com.oldbox.blockpro.ct.ui;

import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.oldbox.blockpro.ct.MainActivity;
import com.oldbox.blockpro.ct.R;
import com.oldbox.blockpro.ct.util.CodeUtils;
import com.oldbox.blockpro.ct.util.FileUtil;

import java.util.ArrayList;

public class InstallerActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1000;
    public final int REQ_CD_FILEPICKER_1 = 101;
    public final int REQ_CD_FILEPICKER_2 = 102;
    public final int REQ_CD_FILEPICKER_3 = 103;
    public final int REQ_CD_FILEPICKER_4 = 104;
    public final int REQ_CD_FILEPICKER_5 = 105;
    private Switch switch_install_from_zip;
    private CardView card_options_installer, card_options_zip ,card_options_blocks, card_options_component;
    private EditText edt_pathzip, edt_pathpalettes, edt_pathblocks, edt_pathcomponent, edt_pathevents;
    private ImageView btn_file_1, btn_file_2, btn_file_3, btn_file_4, btn_file_5;
    private Button btn_auto_install, btn_install_files;

    private Intent filePicker_1 = new Intent(Intent.ACTION_GET_CONTENT);
    private Intent filePicker_2 = new Intent(Intent.ACTION_GET_CONTENT);
    private Intent filePicker_3 = new Intent(Intent.ACTION_GET_CONTENT);
    private Intent filePicker_4 = new Intent(Intent.ACTION_GET_CONTENT);
    private Intent filePicker_5 = new Intent(Intent.ACTION_GET_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installer);
        initViews(savedInstanceState);

        if (MainActivity.checkPermission(getApplicationContext())) {
            initializeLogic();
        } else {
            showPermissionDialog();
        }
    }

    private void initViews(Bundle _savedInstanceState) {

        card_options_installer = findViewById(R.id.card_options_installer);
        switch_install_from_zip = findViewById(R.id.switch1);

        //Card zip
        card_options_zip =  findViewById(R.id.card_options_zip);
        edt_pathzip = findViewById(R.id.edt_zip_path);
        btn_file_1 = findViewById(R.id.imageView1);

        //Card blocks
        card_options_blocks = findViewById(R.id.card_options_blocks);
        edt_pathpalettes = findViewById(R.id.edittext1);
        btn_file_2 = findViewById(R.id.imageView2);
        edt_pathblocks = findViewById(R.id.edittext2);
        btn_file_3 = findViewById(R.id.imageView3);

        //Card components
        card_options_component = findViewById(R.id.card_options_component);
        edt_pathcomponent = findViewById(R.id.edittext3);
        btn_file_4 = findViewById(R.id.imageView4);
        edt_pathevents = findViewById(R.id.edittext4);
        btn_file_5 = findViewById(R.id.imageView5);
        /*
         * botones
         */
        btn_auto_install = findViewById(R.id.button1);
        btn_install_files = findViewById(R.id.button2);

        filePicker_1.setType("*/*");
        filePicker_1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        filePicker_2.setType("text/*");
        filePicker_2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        filePicker_3.setType("text/*");
        filePicker_3.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        filePicker_4.setType("text/*");
        filePicker_4.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        filePicker_5.setType("text/*");
        filePicker_5.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        card_options_zip.setVisibility(View.GONE);

        switch_install_from_zip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _param1, boolean _param2) {
                final boolean _isChecked = _param2;
                switch_install_from_zip.setText(_isChecked != false ? getResources().getString(R.string.card_title_install_from_zip_on) : getResources().getString(R.string.card_title_install_from_zip_off));
                card_options_zip.setVisibility(_isChecked != false ? View.VISIBLE : View.GONE);
                card_options_blocks.setVisibility(_isChecked != true ? View.VISIBLE : View.GONE);
                card_options_component.setVisibility(_isChecked != true ? View.VISIBLE : View.GONE);
            }
        });

        btn_file_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(filePicker_1, REQ_CD_FILEPICKER_1);


            }
        });


    }

    private void initializeLogic(){

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2000){
            initializeLogic();
        } else if (requestCode == 333){
            initializeLogic();

        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    initializeLogic();
                } else {
                }
            }
        } else if (requestCode == 333){
            initializeLogic();
        }
    }

    */
    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, @Nullable Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);
        switch (_requestCode){
            case REQ_CD_FILEPICKER_1:
                if (_resultCode == Activity.RESULT_OK) {
                    ArrayList<String> _filePath = new ArrayList<>();
                    if (_data != null) {
                        if (_data.getClipData() != null) {
                            for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
                                ClipData.Item _item = _data.getClipData().getItemAt(_index);
                                _filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
                            }

                        }
                        else {
                            _filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
                        }
                    }
                    try {
                        edt_pathzip.setText(_filePath.get((int)(0)));
                    } catch (Exception _e){
                        Toast.makeText(getApplicationContext(),
                                _e.getMessage().toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.i("Errros de archivo",_e.getMessage().toString());
                    }
                }
                else {

                }
                break;
            case REQ_CD_FILEPICKER_2:{
                break;
            }
            case REQ_CD_FILEPICKER_3:{
                break;
            }
            case REQ_CD_FILEPICKER_4:{
                break;
            }
            case REQ_CD_FILEPICKER_5:{
                break;
            }
            default: {
                break;
            }
        }
    }

    public void showPermissionDialog() {
        if(SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            try {
                Intent int_permission = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                int_permission.addCategory("android.intent.category.DEFAULT");
                int_permission.setData(Uri.parse(String.format("packagge:%", new Object[]{getApplicationContext().getPackageName()})));
                startActivityForResult(int_permission, 2000);
            } catch (Exception _e){
                Intent int_permission = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(int_permission, 2000);
            }
        } else {
            ActivityCompat.requestPermissions(InstallerActivity.this, CodeUtils.permissions(), 333);
        }
    }
}