package com.oldbox.blockpro.ct.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oldbox.blockpro.ct.MainActivity;
import com.oldbox.blockpro.ct.R;
import com.oldbox.blockpro.ct.adapter.PaletteListAdapter;
import com.oldbox.blockpro.ct.dialog.CreatePaletteDialog;
import com.oldbox.blockpro.ct.util.BlockUtil;
import com.oldbox.blockpro.ct.util.CodeUtils;
import com.oldbox.blockpro.ct.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PalettesActivity extends AppCompatActivity implements View.OnClickListener {


    private static Dialog dCrea_Edit;
    private LinearLayout lin_1, lin_2, lin_3, lin_6, lin_bttn_back;

    private TextView textView11;
    private ListView list_palettes;
    private Button btn_add;

    private HashMap<String,Object> cacheMap = new HashMap<>();
    private ArrayList<HashMap<String, Object>> lmPalettes = new ArrayList<>();
    public static  ArrayList<HashMap<String, Object>> lmBlocks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palettes);

        initViews();

        if (MainActivity.checkPermission(getApplicationContext())) {
            initLogic();
        }
    }

    private void initViews() {

        lin_bttn_back = findViewById(R.id.lin_bttn_back);
        lin_bttn_back.setVisibility(View.GONE);
        lin_3 = findViewById(R.id.lin_3);
        lin_3.setOnClickListener(this);
        lin_6 = findViewById(R.id.lin_6);
        lin_6.setOnClickListener(this);
        list_palettes = findViewById(R.id.list_palettes);
        textView11 = findViewById(R.id.textView11);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        list_palettes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int _position, long l) {
                setBlocksActivity(_position + 9,lmPalettes.get(_position).get("color").toString());
            }
        });
        list_palettes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
    }
    private void initLogic() {
        lmPalettes.clear();
        lmBlocks.clear();
        cacheMap = SettingsActivity.readSettingsFile(getApplicationContext());
        if (!FileUtil.isExistFile(getPalettePath().getAbsolutePath())) {
            list_palettes.setVisibility(View.GONE);

        } else if (!FileUtil.readFile(getPalettePath().getAbsolutePath()).equals("")){
            list_palettes.setVisibility(View.VISIBLE);
            textView11.setVisibility(View.GONE);

            lmPalettes = new Gson().fromJson(FileUtil.readFile(getPalettePath().getAbsolutePath()), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
            list_palettes.setAdapter(new PaletteListAdapter(this, lmPalettes));
        }
        if (!FileUtil.isExistFile(getPalettePath().getAbsolutePath())){
            CodeUtils.showMessage(getApplicationContext(), "Invalid block file");
        } else if (!FileUtil.readFile(getBlocksPath().getAbsolutePath()).equals("")){
            lmBlocks = new Gson().fromJson(FileUtil.readFile(getBlocksPath().getAbsolutePath()), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
        }
    }


    @Override
    public void onClick(View _view) {
        if (_view.getId() == R.id.lin_3) {
            setActyvity(SettingsActivity.class);
        } else if (_view.getId() == R.id.btn_add){
            //showDialogPalett("","",true);
            CreatePaletteDialog _cpd = new CreatePaletteDialog(this);
            _cpd.setSavedButton(getString(R.string.dialog_btn_save), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    crearPaletta(_cpd.getNamePalette(), _cpd.getColorPalette());
                    _cpd.dismiss();
                }
            });
            _cpd.setCancelButton(getString(R.string.dialog_btn_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _cpd.dismiss();
                }
            });
            _cpd.show();
        } else if (_view.getId() == R.id.lin_6) {
            setBlocksActivity(-1, "#616161");
        }
    }

    private void crearPaletta(String namePalette, String colorPalette) {
        HashMap<String, Object> cachePalette = new HashMap<>();
        cachePalette.put("name", namePalette);
        cachePalette.put("color", colorPalette);
        lmPalettes.add(cachePalette);
        FileUtil.writeFile(getPalettePath().getAbsolutePath(), new Gson().toJson(lmPalettes));
        CodeUtils.showMessage(this, getString(R.string.dialog_btn_save));
        cachePalette.clear();
        initLogic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLogic();
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

    public String getKeyMap(String _key) {
        return cacheMap.get(_key).toString();
    }

    public File getBlocksPath(){
        return new File(FileUtil.getExternalStorageDir() + getKeyMap(SettingsActivity.SETTING_BLOCKMANAGER_DIRECTORY_BLOCK_FILE_PATH));
    }

    public File getPalettePath(){
        return new File(FileUtil.getExternalStorageDir() + getKeyMap(SettingsActivity.SETTING_BLOCKMANAGER_DIRECTORY_PALETTE_FILE_PATH));
    }

    public static int getCountBlocks(double _p) {
        int cBlocks = 0;
        for (int i = 0; i < lmBlocks.size(); i++) {
            if (lmBlocks.get(i).get("palette").toString().equals(String.valueOf((long) _p))) {
                cBlocks++;
            }
        }
        return cBlocks;
    }

    public void setBlocksActivity(int _num, String _color){
        Intent acBlocks = new Intent(this, BlocksActivity.class);
        acBlocks.putExtra("bPos", _num);
        acBlocks.putExtra("color", _color);
        startActivity(acBlocks);
    }
    private void setActyvity(Class _class){
        Intent int_act = new Intent(PalettesActivity.this, _class);
        startActivity(int_act);
    }
}