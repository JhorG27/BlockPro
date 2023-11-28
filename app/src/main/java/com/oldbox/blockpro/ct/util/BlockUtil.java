package com.oldbox.blockpro.ct.util;

import android.graphics.Color;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class BlockUtil {

    private static String PATH_PALETTE_JSON_DIR = "";
    private static String PATH_BLOCKS_JSON_DIR = "";


    public static void setPathPaletteJsonDir(String _valueP){
        PATH_PALETTE_JSON_DIR =  FileUtil.getExternalStorageDir() + _valueP;
    }

    public static void setPathBlocksJsonDir(String _valueB){
        PATH_BLOCKS_JSON_DIR =  FileUtil.getExternalStorageDir() + _valueB;
    }

    public static final String getPathPaletteJsonDir(){
        return PATH_PALETTE_JSON_DIR;
    }

    public static final String getPathBlocksJsonDir(){
        return PATH_BLOCKS_JSON_DIR;
    }

    public static final ArrayList<HashMap<String, Object>> getPalettesList(){
        ArrayList<HashMap<String,Object>> _lmP = new ArrayList<>();
        return _lmP = new Gson().fromJson(FileUtil.readFile(PATH_PALETTE_JSON_DIR), new TypeToken<ArrayList<HashMap<String,Object>>>(){}.getType());
    }

    public static final ArrayList<HashMap<String, Object>> getBlockList(){
        ArrayList<HashMap<String,Object>> _lmB = new ArrayList<>();
        return _lmB = new Gson().fromJson(FileUtil.readFile(PATH_BLOCKS_JSON_DIR), new TypeToken<ArrayList<HashMap<String,Object>>>(){}.getType());
    }

    public static void deletePalette(int _position){
        ArrayList<HashMap<String, Object>> _lmP2 = getPalettesList();
        _lmP2.remove((int)_position);
    }

    public static void savedPalette(String _name, String _color){
        HashMap<String, Object> _tempMap = new HashMap<>();
        _tempMap.put("name", _name);
        _tempMap.put("color", _color);
        FileUtil.writeFile(getPathPaletteJsonDir(), new Gson().toJson(_color));
    }

}
