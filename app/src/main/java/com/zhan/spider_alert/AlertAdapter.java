package com.zhan.spider_alert;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by zah on 2017/6/19.
 */

public class AlertAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<JSONObject> alertList;
    SimpleDateFormat sdf;

    public AlertAdapter(Context context, List<JSONObject> alerts) {
        this.inflater = LayoutInflater.from(context);
        this.alertList = alerts;
    }

    @Override
    public int getCount() {
        return alertList.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return alertList.get(position);
    }

    public void addItems(List<JSONObject> objects) {
        alertList.clear();
        alertList.addAll(objects);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_alert, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String id = getItem(position).optString("key");
        viewHolder.id.setText(id);
        viewHolder.sipder_name.setText(getItem(position).optString("spiderName"));
        viewHolder.time.setText(praseTime(id));
        viewHolder.content.setText(getItem(position).optString("title"));
        viewHolder.url.setText(getItem(position).optString("url"));
        viewHolder.status.setText(getItem(position).optString("status"));
        viewHolder.content.setText("报告");
        return convertView;
    }

    private String praseTime(String timeLongs) {
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        Date d = new Date(Long.parseLong(timeLongs) * 1000);
        return sdf.format(d);
    }

    public void remove(JSONObject currentItem) {
        if (alertList.contains(currentItem)) {
            alertList.remove(currentItem);
            notifyDataSetChanged();
        }

    }

    static class ViewHolder {
        TextView id;
        TextView sipder_name;
        TextView time;
        TextView content;
        TextView url;
        TextView status;

        public ViewHolder(View view) {
            id = (TextView) view.findViewById(R.id.id);
            sipder_name = (TextView) view.findViewById(R.id.sipder_name);
            time = (TextView) view.findViewById(R.id.time);
            content = (TextView) view.findViewById(R.id.content);
            url = (TextView) view.findViewById(R.id.url);
            status = (TextView) view.findViewById(R.id.status);

        }
    }
}
