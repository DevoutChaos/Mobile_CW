package com.example.chaos_000.mobile_cw;

import java.lang.Object;

import android.app.Activity;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Date;

/**
 * Created by chaos_000 on 25/02/2016.
 * Liam Faulds S1306716
 * <p/>
 * Tutorials Used:
 * http://www.tutorialspoint.com/android/android_xml_parsers.htm
 * ^ First Accessed 18/02/2016, used as an initial guide to parsing
 * http://www.technotalkative.com/android-listview-2-custom-listview/
 * ^ First Accessed 24/02/2016, used to group a pair of TextViews into a ListView
 */

public class ListViewAdapter extends BaseAdapter {

    Activity context;
    String title[];
    String duration[];
    Date startDate[];
    Date endDate[];

    public ListViewAdapter(Activity context, String[] title, Date[] startDate, Date[] endDate, String[] duration) {
        super();
        this.context = context;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    public int getCount() {
        return title.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewStartDate;
        TextView txtViewEndDate;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtVwTitle);
            holder.txtViewStartDate = (TextView) convertView.findViewById(R.id.txtVwSDate);
            holder.txtViewEndDate = (TextView) convertView.findViewById(R.id.txtVwEDate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewTitle.setText(title[position]);
        holder.txtViewStartDate.setText("Begins: " + startDate[position].toString());
        holder.txtViewEndDate.setText("Ends: " + endDate[position].toString());

        if (duration[position] == "Long") {
            holder.txtViewTitle.setBackgroundColor(Color.RED);
            holder.txtViewStartDate.setBackgroundColor(Color.RED);
            holder.txtViewEndDate.setBackgroundColor(Color.RED);
        } else if (duration[position] == "Med") {
            holder.txtViewTitle.setBackgroundColor(Color.YELLOW);
            holder.txtViewStartDate.setBackgroundColor(Color.YELLOW);
            holder.txtViewEndDate.setBackgroundColor(Color.YELLOW);
        } else if (duration[position] == "Short") {
            holder.txtViewTitle.setBackgroundColor(Color.GREEN);
            holder.txtViewStartDate.setBackgroundColor(Color.GREEN);
            holder.txtViewEndDate.setBackgroundColor(Color.GREEN);
        } else {
            holder.txtViewTitle.setBackgroundColor(Color.DKGRAY);
            holder.txtViewStartDate.setBackgroundColor(Color.DKGRAY);
            holder.txtViewEndDate.setBackgroundColor(Color.DKGRAY);
        }

        return convertView;
    }
}
