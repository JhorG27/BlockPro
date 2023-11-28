package com.oldbox.blockpro.ct.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.oldbox.blockpro.ct.R;

public class ListColorsDialog extends Dialog {

    private LinearLayout lin_cancel;
    private TextView tv_cancel;
    private View.OnClickListener btCancelListener=null;

    private String str_cancel;

    public ListColorsDialog(@NonNull Context context) {
        super(context);
    }

    public ListColorsDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ListColorsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_color_list);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        tv_cancel = findViewById(R.id.textView13);

        initLogicDialog();
    }

    private void initLogicDialog(){
        tv_cancel.setText(str_cancel);

        tv_cancel.setOnClickListener(btCancelListener);
    }

    public void setCancelButton(String _cancel, View.OnClickListener onClickListener) {
        dismiss();
        this.str_cancel = _cancel;
        this.btCancelListener = onClickListener;
    }
}
