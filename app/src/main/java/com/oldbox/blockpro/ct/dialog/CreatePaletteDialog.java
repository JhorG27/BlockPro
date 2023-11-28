package com.oldbox.blockpro.ct.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.oldbox.blockpro.ct.R;
import com.oldbox.blockpro.ct.util.CodeUtils;

public class CreatePaletteDialog extends Dialog {

    private EditText edt_name;
    private EditText edt_color;
    private ImageView img_color;
    private TextView tv_delete;
    private TextView tv_cancel;
    private TextView tv_save;

    private String str_save;
    private String str_cancel;
    private View.OnClickListener btSaveListener = null;
    private View.OnClickListener btCancelListener = null;

    private Context _context;

    public CreatePaletteDialog(@NonNull Context context) {
        super(context);
        _context = context;
    }

    public CreatePaletteDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CreatePaletteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_data_palette);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        edt_name = findViewById(R.id.edt_name);
        edt_color = findViewById(R.id.edt_color);
        img_color = findViewById(R.id.imageView13);

        tv_delete = findViewById(R.id.tv_delete);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_save = findViewById(R.id.tv_save);

        edt_color.setText("#1976D2");
        tv_delete.setVisibility(View.INVISIBLE);
        tv_save.setEnabled(false);
        tv_save.setAlpha(0.5f);
        tv_save.setText(str_save);
        tv_cancel.setText(str_cancel);

        tv_cancel.setOnClickListener(btCancelListener);

        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String hexCode = edt_color.getText().toString().trim();
                boolean isHexCode = CodeUtils.isValidHexCode(hexCode);

                tv_save.setEnabled(charSequence.length() > 0 && isHexCode != false ? true : false);
                tv_save.setAlpha(charSequence.length() > 0 && isHexCode != false ? 1.0f : 0.5f);
                tv_save.setOnClickListener(charSequence.length() > 0 && isHexCode != false ? btSaveListener : null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_color.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String hexCode = edt_color.getText().toString().trim();
                boolean isHexCode = CodeUtils.isValidHexCode(hexCode);

                tv_save.setEnabled(isHexCode != false && edt_name.getText().toString().length() > 0 ? true : false);
                tv_save.setAlpha(isHexCode != false && edt_name.getText().toString().length() > 0 ? 1.0f : 0.5f);
                tv_save.setOnClickListener(isHexCode != false && edt_name.getText().toString().length() > 0 ? btSaveListener : null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        img_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListColorsDialog _lcd = new ListColorsDialog(_context);
                _lcd.setCancelButton(_context.getString(R.string.dialog_btn_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        _lcd.dismiss();
                    }
                });
                _lcd.show();
            }
        });

    }

    public String getNamePalette() {
        return edt_name.getText().toString();
    }

    public String getColorPalette() {
        return edt_color.getText().toString();
    }

    public void setSavedButton(String _save, View.OnClickListener onClickListener) {
        dismiss();
        this.str_save = _save;
        this.btSaveListener = onClickListener;
    }

    public void setCancelButton(String _cancel, View.OnClickListener onClickListener) {
        dismiss();
        this.str_cancel = _cancel;
        this.btCancelListener = onClickListener;
    }

}
