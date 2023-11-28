package com.oldbox.blockpro.ct.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.oldbox.blockpro.ct.R;
import com.oldbox.blockpro.ct.ui.BlocksActivity;
import com.oldbox.blockpro.ct.ui.PalettesActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class PaletteListAdapter extends BaseAdapter {
    ArrayList<HashMap<String, Object>> _data;
    private final Activity _context;

    public PaletteListAdapter(Activity _cont, ArrayList<HashMap<String, Object>> _arr) {
        _context = _cont;
        _data = _arr;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public HashMap<String, Object> getItem(int _index) {
        return _data.get(_index);
    }

    @Override
    public long getItemId(int _index) {
        return _index;
    }

    @Override
    public View getView(final int _position, View _v, ViewGroup _container) {
        LayoutInflater _inflater = _context.getLayoutInflater();
        View _view = _v;
        if (_view == null) {
            _view = _inflater.inflate(R.layout.item_list_palettes, null);
        }

        final CardView card_root = _view.findViewById(R.id.card_root);
        final LinearLayout lin_color = _view.findViewById(R.id.lin_color);
        final TextView tv_name = _view.findViewById(R.id.tv_name);
        final TextView tv_cb = _view.findViewById(R.id.tv_count_blocks);

        tv_name.setText(_data.get(_position).get("name").toString());
        tv_cb.setText("Blocks: " + PalettesActivity.getCountBlocks(9 + _position));
        try {
            lin_color.setBackgroundColor(Color.parseColor(_data.get(_position).get("color").toString()));
        } catch (Exception _MSG){

        }

        return _view;
    }
}
