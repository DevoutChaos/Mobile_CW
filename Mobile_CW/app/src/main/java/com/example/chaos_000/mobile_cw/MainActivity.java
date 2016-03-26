package com.example.chaos_000.mobile_cw;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chaos_000 on 25/02/2016.
 * Liam Faulds S1306716
 * <p/>
 * Tutorials Used:
 * http://www.tutorialspoint.com/android/android_xml_parsers.htm
 * ^ First Accessed 18/02/2016, used as an initial guide to parsing
 * http://www.technotalkative.com/android-listview-2-custom-listview/
 * ^ First Accessed 24/02/2016, used to group a pair of TextViews into a ListView
 *
 * GOOGLE VIEWSWITCHER!!!
 */

public class MainActivity extends Activity implements OnItemClickListener{
    Button but1, but2;
    ListView lstVw1;
    ListViewAdapter lstVwAda;
    TextView tit, desc;
    private ViewSwitcher switcher;
    private static final int REFRESH_SCREEN = 1;

    private String rdWrks = "http://trafficscotland.org/rss/feeds/roadworks.aspx";
    private HandleXML xmlObj;

    String[] titleArr;
    String[] descArr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switcher = (ViewSwitcher) findViewById(R.id.ViewSwitcher);
        but1 = (Button) findViewById(R.id.button);
        but2 = (Button) findViewById(R.id.button2);
        tit = (TextView) findViewById(R.id.titTxtView);
        desc = (TextView) findViewById(R.id.descTxtView);
        lstVw1 = (ListView) findViewById(R.id.mainListView);

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xmlObj = new HandleXML(rdWrks);
                xmlObj.fetchXML();
                while (xmlObj.parsingComplete) ;
                ImportantThing();
            }
        });

        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnotherImportantThing();
            }
        });
    }

    public void ImportantThing() {
        titleArr = HandleXML.titleLst.toArray(new String[0]);
        descArr = HandleXML.descLst.toArray(new String[0]);
        lstVwAda = new ListViewAdapter(this, titleArr, descArr);
        lstVw1.setAdapter(lstVwAda);
        lstVw1.setOnItemClickListener(this);
        System.out.println("adapter => " + lstVwAda.getCount());
    }

    public void AnotherImportantThing()
    {
        switcher.showPrevious();
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
    {
        //Toast.makeText(this, "Title: " + titleArr[position] + "\n Description: \n" + descArr[position], Toast.LENGTH_LONG).show();
        tit.setText(titleArr[position]);
        desc.setText(descArr[position]);

        new Thread()
        {
            public void run()
            {
                try
                {
                    Refresh.sendEmptyMessage(REFRESH_SCREEN);
                }
                catch(Exception e)
                {

                }
            }
        }.start();
    }

    Handler Refresh = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case REFRESH_SCREEN:
                    switcher.showNext();
                    break;

                default:
                    break;
            }
        }
    };
}
