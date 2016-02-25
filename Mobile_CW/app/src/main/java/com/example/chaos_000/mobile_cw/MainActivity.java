package com.example.chaos_000.mobile_cw;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;

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
 */

public class MainActivity extends ActionBarActivity {
    Button but1;
    ListView lstVw1;
    ListViewAdapter lstVwAda;

    private String rdWrks = "http://trafficscotland.org/rss/feeds/roadworks.aspx";
    private HandleXML xmlObj;
    private ArrayAdapter<String> titAdptr;
    private ArrayAdapter<String> desAdptr;

    String[] titleArr;
    String[] descArr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        but1 = (Button) findViewById(R.id.button);
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
    }

    public void ImportantThing() {
        titleArr = HandleXML.titleLst.toArray(new String[0]);
        descArr = HandleXML.descLst.toArray(new String[0]);
        lstVwAda = new ListViewAdapter(this, titleArr, descArr);
        //lstVwAda = new ListViewAdapter(this, month, number);
        lstVw1.setAdapter(lstVwAda);
        System.out.println("adapter => " + lstVwAda.getCount());
    }
}
