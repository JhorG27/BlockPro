package com.oldbox.blockpro.ct;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.oldbox.blockpro.ct.ui.InstallerActivity;
import com.oldbox.blockpro.ct.ui.PalettesActivity;
import com.oldbox.blockpro.ct.util.CodeUtils;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Timer timer1 = new Timer();
    private TimerTask timerTC;

    private int num1 = 0;
    private int num2 = 0;

    private LinearLayout card_permission;
    private CardView card_btn_accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(0xFFFFFFFF);

        card_permission = findViewById(R.id.lin_req_permission);
        card_btn_accept = findViewById(R.id.card_btn_accept);
        card_btn_accept.setOnClickListener(this);

        if (checkPermission(getApplicationContext())) {
            initLogic();
            Log.i("Informacion", String.format("%s", checkPermission(getApplicationContext())));
        }

        //initLogic();

    }

    private void initLogic(){
        card_permission.setVisibility(View.GONE);
        card_btn_accept.setVisibility(View.GONE);
        num1 = CodeUtils.getRandom(1, 5);
        num2 = num1 * 1000;
        timerTC = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent int_palettes_class = new Intent(MainActivity.this, PalettesActivity.class);
                        startActivity(int_palettes_class);
                        finish();
                    }
                });
            }
        };
        timer1.schedule(timerTC, num2);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2000){
            initLogic();
        } else if (requestCode == 333){
            initLogic();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    initLogic();
                } else {
                    CodeUtils.showMessage(this, "rrrrr" + Environment.isExternalStorageManager());
                }
            }
        } else if (requestCode == 333){
            initLogic();
        }
    }

    @Override
    public void onClick(View view) {
        int _view = view.getId();
        if(_view == R.id.card_btn_accept){
            showPermissionDialog();
        }
    }

    public static boolean checkPermission(Context _getAppCont) {
        int version = Build.VERSION.SDK_INT;
        if( version <= 32 ) {
            boolean isAllowPermissionApi28 = ActivityCompat.checkSelfPermission(_getAppCont, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ;
            Log.i("general_em","isGrantedPermissionWRITE_EXTERNAL_STORAGE() - isAllowPermissionApi28: " + isAllowPermissionApi28);
            return  isAllowPermissionApi28;
        } else {
            boolean isAllowPermissionApi33 = Environment.isExternalStorageManager();
            Log.i("general_em","isGrantedPermissionWRITE_EXTERNAL_STORAGE() - isAllowPermissionApi33: " + isAllowPermissionApi33);
            return isAllowPermissionApi33;
        }
        /*/
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Environment.isExternalStorageManager();
        } else {
            int write = ContextCompat.checkSelfPermission(_getAppCont,
                    WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(_getAppCont,
                    READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED &&
                    read == PackageManager.PERMISSION_GRANTED;
        }*/
    }
    public void showPermissionDialog() {
        /*if (Build.VERSION.SDK_INT > 29) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivityForResult(intent, 2000);
                } catch (ActivityNotFoundException e) {
                    Log.e("MainActivity", "Activity to manage apps' all files access permission not found!");
                }
            }
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, CodeUtils.permissions(), 333);
            throw new AssertionError("Not on an API level 30 or higher device!");
        }
        */
        if(SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            try {
                Intent int_permission = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                int_permission.addCategory("android.intent.category.DEFAULT");
                int_permission.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                int_permission.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(int_permission, 2000);
                Log.i("showPermissionDialog", int_permission.toString());
            } catch (Exception _e){
                Intent int_permission = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(int_permission, 2000);
                Log.i("showPermissionDialog_Exception", _e.getMessage().toString());
            }
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, CodeUtils.permissions(), 333);
        }
    }
}