package com.zhan.spider_alert;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private ListView listview;

    private AlertAdapter adapter;

    private BroadcastReceiver broadcastReceiver;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        PushAgent.getInstance(getApplicationContext()).onAppStart();

        adapter = new AlertAdapter(this, new ArrayList<JSONObject>());
        listview.setAdapter(adapter);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                reSetData();
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(AlertApplication.ACTION_REFRESH));
        reSetData();


        listview.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                ListView lr = (ListView) v;
                menu.add(0, 1, 1, "上报");
                menu.add(0, 2, 2, "刪除");
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();  //获得AdapterContextMenuInfo,以此来获得选择的listview项目
//        Toast.makeText(getApplicationContext(), adapter.getItem(menuInfo.position)+"", 0).show();

        JSONObject currentItem = adapter.getItem(menuInfo.position);

        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, currentItem.optString("title"));
                intent.putExtra(Intent.EXTRA_TEXT, currentItem.toString());
                startActivity(Intent.createChooser(intent, getTitle()));
                break;
            case 2:
                getSharedPreferences("alert", Context.MODE_APPEND).edit().remove(currentItem.optString("key")).commit();
                adapter.remove(currentItem);
                break;
        }
        return false;
    }

    private void reSetData() {
        new Thread() {

            @Override
            public void run() {
                Map<String, ?> allData = getSharedPreferences("alert", Context.MODE_APPEND).getAll();
                if (allData.size() > 0) {
                    allData = SortUtil.sortMapByKey(allData);
                    final List<JSONObject> allItems = new ArrayList<JSONObject>();
                    for (String key : allData.keySet()) {
                        try {
                            JSONObject object = new JSONObject(allData.get(key).toString());
                            object.put("key", key);
                            allItems.add(object);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.addItems(allItems);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();

    }
}
