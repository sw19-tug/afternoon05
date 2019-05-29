package com.example.afternoon5.HelperClasses;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.example.afternoon5.HelperClasses.Note;
import com.example.afternoon5.R;

import java.util.ArrayList;
import java.util.Arrays;

public class list_adapter extends BaseAdapter {

    private Activity context_1;

    private ArrayList<Note> pairs;

    public list_adapter(Activity context,
                        ArrayList<Note> pairs) {
        context_1 = context;
        this.pairs = pairs;
    }

    @Override
    public int getCount() {
        return pairs.size();
    }

    @Override
    public Object getItem(int position) {
        return pairs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context_1).inflate(
                    R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.note_title = (TextView) convertView
                    .findViewById(R.id.note_title);
            viewHolder.note_text = (TextView) convertView
                    .findViewById(R.id.note_text);
            viewHolder.note_tags = (TextView) convertView
                    .findViewById(R.id.note_tags);
            viewHolder.checkBox2 = (CheckBox) convertView
                    .findViewById(R.id.checkBox2);

            convertView.setTag(viewHolder);
        } else {


            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.note_text.setText(pairs.get(position).getText());

        viewHolder.note_title.setText(pairs.get(position).getTitle());

        viewHolder.note_tags.setText(pairs.get(position).getTagsAsString());

        viewHolder.checkBox2.setChecked(pairs.get(position).getPinn());


        return convertView;
    }



    public class ViewHolder {
        public TextView note_title;
        public TextView note_text;
        public TextView note_tags;
        public CheckBox checkBox2; //

    }
}