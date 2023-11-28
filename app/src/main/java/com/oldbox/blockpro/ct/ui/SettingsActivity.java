package com.oldbox.blockpro.ct.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.oldbox.blockpro.ct.R;
import com.oldbox.blockpro.ct.util.CodeUtils;
import com.oldbox.blockpro.ct.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout lin_close;
    private LinearLayout lin_backup_dir;
    private LinearLayout lin_filename_format;

    private Switch card_switch1;
    private Switch card_switch2;
    private Switch card_switch3;
    private Switch card_switch4;
    private Switch card_switch5;
    private Switch card_switch6;
    private LinearLayout lin_auto_install;
    private Switch card_switch7;
    private LinearLayout lin_launch_after_installing;
    private Switch card_switch8;

    public static final File SETTINGS_FILE = new File(FileUtil.getExternalStorageDir(), ".sketchware/data/settings.json");
    public static final String SETTING_ALWAYS_SHOW_BLOCKS = "always-show-blocks";
    public static final String SETTING_BACKUP_DIRECTORY = "backup-dir";
    public static final String SETTING_ROOT_AUTO_INSTALL_PROJECTS = "root-auto-install-projects";
    public static final String SETTING_ROOT_AUTO_OPEN_AFTER_INSTALLING = "root-auto-open-after-installing";
    public static final String SETTING_BACKUP_FILENAME = "backup-filename";
    public static final String SETTING_LEGACY_CODE_EDITOR = "legacy-ce";
    public static final String SETTING_SHOW_BUILT_IN_BLOCKS = "built-in-blocks";
    public static final String SETTING_SHOW_EVERY_SINGLE_BLOCK = "show-every-single-block";
    public static final String SETTING_USE_NEW_VERSION_CONTROL = "use-new-version-control";
    public static final String SETTING_USE_ASD_HIGHLIGHTER = "use-asd-highlighter";
    public static final String SETTING_SKIP_MAJOR_CHANGES_REMINDER = "skip-major-changes-reminder";
    public static final String SETTING_BLOCKMANAGER_DIRECTORY_PALETTE_FILE_PATH = "palletteDir";
    public static final String SETTING_BLOCKMANAGER_DIRECTORY_BLOCK_FILE_PATH = "blockDir";

    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#fafafa");
    private HashMap<String, Object> setting_map = new HashMap<>();

    private boolean isCheckRoot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView(savedInstanceState);
        initLogic();
    }

    private void initView(Bundle _savedInstanceState) {
        lin_close = findViewById(R.id.lin_close);

        card_switch1 = findViewById(R.id.card_switch1);
        card_switch2 = findViewById(R.id.card_switch2);
        card_switch3 = findViewById(R.id.card_switch3);

        lin_backup_dir = findViewById(R.id.lin_backup_dir);
        lin_filename_format = findViewById(R.id.lin_filename_format);

        card_switch4 = findViewById(R.id.card_switch4);
        card_switch5 = findViewById(R.id.card_switch5);

        card_switch6 = findViewById(R.id.card_switch6);
        card_switch7 = findViewById(R.id.card_switch7);
        card_switch8 = findViewById(R.id.card_switch8);

        lin_close.setOnClickListener(this);

        card_switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _compoundButton, boolean _isChecked) {
                setSetting(getApplicationContext(), SETTING_SHOW_BUILT_IN_BLOCKS, _isChecked);
            }
        });
        card_switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _compoundButton, boolean _isChecked) {
                setSetting(getApplicationContext(),SETTING_ALWAYS_SHOW_BLOCKS, _isChecked);
            }
        });
        card_switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _compoundButton, boolean _isChecked) {
                setSetting(getApplicationContext(), SETTING_SHOW_EVERY_SINGLE_BLOCK, _isChecked);
            }
        });

        lin_backup_dir.setOnClickListener(this);
        lin_filename_format.setOnClickListener(this);

        card_switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _compoundButton, boolean _isChecked) {
                setSetting(getApplicationContext(), SETTING_LEGACY_CODE_EDITOR, _isChecked);
            }
        });
        card_switch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _compoundButton, boolean _isChecked) {
                setSetting(getApplicationContext(), SETTING_USE_ASD_HIGHLIGHTER, _isChecked);
            }
        });

        card_switch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _compoundButton, boolean _isChecked) {
                setSetting(getApplicationContext(), SETTING_USE_NEW_VERSION_CONTROL, _isChecked);
            }
        });

        isCheckRoot = checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
        card_switch7.setEnabled(isCheckRoot);

        card_switch7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _compoundButton, boolean _isChecked) {
                setSetting(getApplicationContext(), SETTING_ROOT_AUTO_INSTALL_PROJECTS, _isChecked);
            }
        });
        card_switch8.setEnabled(isCheckRoot);
        card_switch8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _compoundButton, boolean _isChecked) {
                setSetting(getApplicationContext(),SETTING_ROOT_AUTO_OPEN_AFTER_INSTALLING, _isChecked);
            }
        });
    }

    private void initLogic() {
        card_switch1.setChecked(_isSettingEnabled(SETTING_SHOW_BUILT_IN_BLOCKS));
        card_switch2.setChecked(_isSettingEnabled(SETTING_ALWAYS_SHOW_BLOCKS));
        card_switch3.setChecked(_isSettingEnabled(SETTING_SHOW_EVERY_SINGLE_BLOCK));

        card_switch4.setChecked(_isLegacyCeEnabled());
        card_switch5.setChecked(_isSettingEnabled(SETTING_USE_ASD_HIGHLIGHTER));

        card_switch6.setChecked(_isSettingEnabled(SETTING_USE_NEW_VERSION_CONTROL));
        card_switch7.setChecked(_isSettingEnabled(SETTING_ROOT_AUTO_INSTALL_PROJECTS));
        card_switch8.setChecked(_isSettingEnabled(SETTING_ROOT_AUTO_OPEN_AFTER_INSTALLING));
    }
    

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.lin_close) {
            finish();
        } else if (view.getId() == R.id.lin_backup_dir) { //Backup dir
            final LinearLayout container = new LinearLayout(this);
            container.setPadding(
                    (int) CodeUtils.getDip(getApplicationContext(),20),
                    (int) CodeUtils.getDip(getApplicationContext(),8),
                    (int) CodeUtils.getDip(getApplicationContext(),20),
                    0);

            final TextInputLayout tilBackupDirectory = new TextInputLayout(this);
            tilBackupDirectory.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tilBackupDirectory.setHint("Backup directory");
            tilBackupDirectory.setHelperText("Directory inside /Internal storage/, e.g. sketchware/backups");
            container.addView(tilBackupDirectory);

            final EditText backupDirectory = new EditText(this);
            backupDirectory.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            backupDirectory.setTextSize(14.0f);
            backupDirectory.setText(getBackupPath());
            tilBackupDirectory.addView(backupDirectory);

            new AlertDialog.Builder(this)
                    .setTitle("Backup directory")
                    .setView(container)
                    .setPositiveButton("Save", (dialogInterface, which) -> {
                        SettingsActivity.setSetting(getApplicationContext(),SETTING_BACKUP_DIRECTORY, backupDirectory.getText().toString());
                        CodeUtils.showMessage(getApplicationContext(),"Saved");
                    })
                    .setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss())
                    .show();

        } else if (view.getId() == R.id.lin_filename_format ) { //Backup file formatName

            final LinearLayout container = new LinearLayout(this);
            container.setPadding(
                    (int) CodeUtils.getDip(getApplicationContext(),20),
                    (int) CodeUtils.getDip(getApplicationContext(),8),
                    (int) CodeUtils.getDip(getApplicationContext(),20),
                    0);

            final TextInputLayout tilBackupFormat = new TextInputLayout(this);
            tilBackupFormat.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tilBackupFormat.setHint("Format");
            tilBackupFormat.setHelperText("This defines how SWB backup files get named.\n" +
                    "Available variables:\n" +
                    " - $projectName - Project name\n" +
                    " - $versionCode - App version code\n" +
                    " - $versionName - App version name\n" +
                    " - $pkgName - App package name\n" +
                    " - $timeInMs - Time during backup in milliseconds\n" +
                    "\n" +
                    "Additionally, you can format your own time like this using Java's date formatter syntax:\n" +
                    "$time(yyyy-MM-dd'T'HHmmss)\n");
            container.addView(tilBackupFormat);

            final EditText backupFilename = new EditText(this);
            backupFilename.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            backupFilename.setTextSize(14.0f);
            backupFilename.setText(getBackupFileName());
            tilBackupFormat.addView(backupFilename);

            new AlertDialog.Builder(this)
                    .setTitle("Backup filename format")
                    .setView(container)
                    .setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss())
                    .setPositiveButton("Save", (dialogInterface, which) -> {
                        setting_map.put(SETTING_BACKUP_FILENAME, backupFilename.getText().toString());
                        FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), new Gson().toJson(setting_map));
                        CodeUtils.showMessage(getApplicationContext(),"Saved");
                    })
                    .setNeutralButton("Reset", (dialogInterface, which) -> {
                        setting_map.remove(SETTING_BACKUP_FILENAME);
                        FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), new Gson().toJson(setting_map));
                        CodeUtils.showMessage(getApplicationContext(),"Reset to default complete.");
                    })
                    .show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initLogic();
    }

    /*
     *Escribimos nuestro codigo
     */
    @NonNull
    public static HashMap<String, Object> readSettingsFile(Context _context){
        HashMap<String, Object> cacheMap;
        if (SETTINGS_FILE.exists()) {
            Exception toLog;
            try {
                cacheMap = new Gson().fromJson(FileUtil.readFile(SETTINGS_FILE.getAbsolutePath()), new TypeToken<HashMap<String,Object>>(){}.getType());
                if (cacheMap != null) {
                    return cacheMap;
                }
                toLog = new NullPointerException("settings == null");
                // fall-through to shared error handler
            } catch (JsonParseException e) {
                toLog = e;
                // fall-through to shared error handler
            }

            toastError(_context,"Couldn't parse Mod Settings! Restoring defaults.", Toast.LENGTH_SHORT);
            Log.e("ConfigActivity", "Failed to parse Mod Settings.", toLog);
        }
        cacheMap = new HashMap<>();
        _restoreDefaultSettings(cacheMap);
        return cacheMap;
    }
    /*
    * Nos basamos en el codigo original de Sketchware pro
    */

    public String getBackupPath() {
        if (FileUtil.isExistFile(SETTINGS_FILE.getAbsolutePath())) {
            HashMap<String, Object> settings = readSettingsFile(getApplicationContext());
            if (settings.containsKey(SETTING_BACKUP_DIRECTORY)) {
                Object value = settings.get(SETTING_BACKUP_DIRECTORY);
                if (value instanceof String) {
                    return (String) value;
                } else {
                    toastError(getApplicationContext(),"Detected invalid preference "
                                    + "for backup directory. Restoring defaults",
                            Toast.LENGTH_LONG);
                    settings.remove(SETTING_BACKUP_DIRECTORY);
                    FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), new Gson().toJson(settings));
                }
            }
        }
        return "/.sketchware/backups/";
    }

    public static String getStringSettingValueOrSetAndGet(Context _context, String settingKey, String toReturnAndSetIfNotFound) {
        HashMap<String, Object> settings = readSettingsFile(_context);
        Object value = settings.get(settingKey);
        if (value instanceof String) {
            return (String) value;
        } else {
            settings.put(settingKey, toReturnAndSetIfNotFound);
            FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), new Gson().toJson(settings));

            return toReturnAndSetIfNotFound;
        }
    }


    public String getBackupFileName() {
        if (FileUtil.isExistFile(SETTINGS_FILE.getAbsolutePath())) {
            HashMap<String, Object> settings = new Gson().fromJson(FileUtil.readFile(SETTINGS_FILE.getAbsolutePath()), new TypeToken<HashMap<String,Object>>(){}.getType());
            if (settings.containsKey(SETTING_BACKUP_FILENAME)) {
                Object value = settings.get(SETTING_BACKUP_FILENAME);
                if (value instanceof String) {
                    return (String) value;
                } else {
                    toastError(getApplicationContext(),"Detected invalid preference "
                                    + "for backup filename. Restoring defaults",
                            Toast.LENGTH_LONG);
                    settings.remove(SETTING_BACKUP_FILENAME);
                    FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), new Gson().toJson(settings));
                }
            }
        }
        return "$projectName v$versionName ($pkgName, $versionCode) $time(yyyy-MM-dd'T'HHmmss)";
    }

    public boolean _isLegacyCeEnabled() {
        /* The legacy Code Editor is specifically opt-in */
        if (!FileUtil.isExistFile(SETTINGS_FILE.getAbsolutePath())) {
            return false;
        }

        HashMap<String, Object> settings = readSettingsFile(getApplicationContext());
        if (settings.containsKey(SETTING_LEGACY_CODE_EDITOR)) {
            Object value = settings.get(SETTING_LEGACY_CODE_EDITOR);
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else {
                toastError(getApplicationContext(),"Detected invalid preference for legacy "
                                + " Code Editor. Restoring defaults",
                        Toast.LENGTH_LONG);
                settings.remove(SETTING_LEGACY_CODE_EDITOR);
                FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), new Gson().toJson(settings));
            }
        }
        return false;
    }

    public boolean _isSettingEnabled(String keyName) {
        if (!FileUtil.isExistFile(SETTINGS_FILE.getAbsolutePath())) {
            return false;
        }

        HashMap<String, Object> settings = readSettingsFile(getApplicationContext());
        if (settings.containsKey(keyName)) {
            Object value = settings.get(keyName);
            if (value instanceof Boolean) {
                return (Boolean) value;
            } else {
                toastError(getApplicationContext(),"Detected invalid preference. Restoring defaults",
                        Toast.LENGTH_LONG);
                settings.remove(keyName);
                FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), new Gson().toJson(settings));
            }
        }
        return false;
    }

    public static void setSetting(Context _context,String key, Object value) {
        HashMap<String, Object> settings = readSettingsFile(_context);
        settings.put(key, value);
        FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), new Gson().toJson(settings));
    }

    public static void _restoreDefaultSettings(HashMap<String, Object> settings) {
        settings.clear();

        List<String> keys = Arrays.asList(SETTING_ALWAYS_SHOW_BLOCKS,
                SETTING_BACKUP_DIRECTORY,
                SETTING_LEGACY_CODE_EDITOR,
                SETTING_ROOT_AUTO_INSTALL_PROJECTS,
                SETTING_ROOT_AUTO_OPEN_AFTER_INSTALLING,
                SETTING_SHOW_BUILT_IN_BLOCKS,
                SETTING_SHOW_EVERY_SINGLE_BLOCK,
                SETTING_USE_NEW_VERSION_CONTROL,
                SETTING_USE_ASD_HIGHLIGHTER,
                SETTING_BLOCKMANAGER_DIRECTORY_PALETTE_FILE_PATH,
                SETTING_BLOCKMANAGER_DIRECTORY_BLOCK_FILE_PATH);

        for (String key : keys) {
            settings.put(key, _getDefaultValue(key));
        }
        FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), new Gson().toJson(settings));
    }

    public static Object _getDefaultValue(String key) {
        switch (key) {
            case SETTING_ALWAYS_SHOW_BLOCKS:
            case SETTING_LEGACY_CODE_EDITOR:
            case SETTING_ROOT_AUTO_INSTALL_PROJECTS:
            case SETTING_SHOW_BUILT_IN_BLOCKS:
            case SETTING_SHOW_EVERY_SINGLE_BLOCK:
            case SETTING_USE_NEW_VERSION_CONTROL:
            case SETTING_USE_ASD_HIGHLIGHTER:
                return false;

            case SETTING_BACKUP_DIRECTORY:
                return "/.sketchware/backups/";

            case SETTING_ROOT_AUTO_OPEN_AFTER_INSTALLING:
                return true;

            case SETTING_BLOCKMANAGER_DIRECTORY_PALETTE_FILE_PATH:
                return "/.sketchware/resources/block/My Block/palette.json";

            case SETTING_BLOCKMANAGER_DIRECTORY_BLOCK_FILE_PATH:
                return "/.sketchware/resources/block/My Block/block.json";

            default:
                throw new IllegalArgumentException("Unknown key '" + key + "'!");
        }
    }

    public static void toastError(Context _CONTEXT, String message, int length) {
        try {
            Toast.makeText(_CONTEXT,message,
                    Toast.LENGTH_LONG).show();
        } catch (RuntimeException e) {
            Log.e("SketchwareUtil", "Failed to toast regular message, " +
                    "Toast's message was: \"" + message + "\"", e);
        }
    }


    /*
     *Verificadores de root
    * en el dispositivo
    * */
    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su" };
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    private static boolean canExecuteSu() {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec("su");
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }
        return executedSuccesfully;
    }
}