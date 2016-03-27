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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Exchanger;

/**
 * Created by chaos_000 on 25/02/2016.
 * Liam Faulds S1306716
 * <p/>
 * Tutorials Used:
 * http://www.tutorialspoint.com/android/android_xml_parsers.htm
 * ^ First Accessed 18/02/2016, used as an initial guide to parsing
 * http://www.technotalkative.com/android-listview-2-custom-listview/
 * ^ First Accessed 24/02/2016, used to group a pair of TextViews into a ListView
 * <p/>
 * GOOGLE VIEWSWITCHER!!!
 */

public class MainActivity extends Activity implements OnItemClickListener {
    Button but1, but2, but3, but4;
    ListView lstVw1;
    ListViewAdapter lstVwAda;
    TextView tit, desc, link, geo;
    EditText searchInput;
    private ViewSwitcher switcher;
    private static final int REFRESH_SCREEN = 1;

    private String rdWrks = "http://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String pRdWrks = "http://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private HandleXML xmlObj;
    int count;

    String[] titleArr;
    String[] descArr;
    String[] linkArr;
    String[] geoArr;
    Date[] sDArr;
    Date[] eDArr;

    Date[][] searchArr;
    int[] foundArr;
    String searchTerm;
    String[] titleArrAlt;
    Date dateToFind;
    Date[] sDArrAlt;
    Date[] eDArrAlt;
    public static List<String> titleLstAlt = new ArrayList<>();
    public static List<Date> startDateAlt = new ArrayList<>();
    public static List<Date> endDateAlt = new ArrayList<>();
    int searchCount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switcher = (ViewSwitcher) findViewById(R.id.ViewSwitcher);
        but1 = (Button) findViewById(R.id.button);
        but2 = (Button) findViewById(R.id.button2);
        but3 = (Button) findViewById(R.id.button3);
        but4 = (Button) findViewById(R.id.button4);
        tit = (TextView) findViewById(R.id.titTxtView);
        desc = (TextView) findViewById(R.id.descTxtView);
        link = (TextView) findViewById(R.id.linkTxtView);
        geo = (TextView) findViewById(R.id.geoTxtView);
        lstVw1 = (ListView) findViewById(R.id.mainListView);
        searchInput = (EditText) findViewById(R.id.editText);

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

        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xmlObj = new HandleXML(pRdWrks);
                xmlObj.fetchXML();
                while (xmlObj.parsingComplete) ;
                ImportantThing();
            }
        });

        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleLstAlt.clear();
                startDateAlt.clear();
                endDateAlt.clear();
                searchTerm = searchInput.getText().toString();
                DateFormat format = new SimpleDateFormat("dd-MM-yy", Locale.ENGLISH);
                try {
                    dateToFind = format.parse(searchTerm);
                } catch (Exception e) {
                    System.out.println("#Panicification");
                }
                foundArr = new int[count];
                for (int y = 0; y < count; y++) {
                    searchArr[y][2] = dateToFind;
                }

                for (int z = 0; z < count; z++) {
                    if (((searchArr[z][2].after(searchArr[z][0])) || (searchArr[z][2].equals(searchArr[z][0]))) && ((searchArr[z][2].before(searchArr[z][1])) || (searchArr[z][2].equals(searchArr[z][1])))) {
                        titleLstAlt.add(titleArr[z]);
                        startDateAlt.add(sDArr[z]);
                        endDateAlt.add(eDArr[z]);
                    }
                }
                titleArrAlt = titleLstAlt.toArray(new String[0]);
                sDArrAlt = startDateAlt.toArray(new Date[0]);
                eDArrAlt = endDateAlt.toArray(new Date[0]);
                ShowingTheSearch();
            }
        });
    }

    public void ImportantThing() {
        titleArr = HandleXML.titleLst.toArray(new String[0]);
        descArr = HandleXML.descLst.toArray(new String[0]);
        linkArr = HandleXML.linkLst.toArray(new String[0]);
        geoArr = HandleXML.geoLst.toArray(new String[0]);
        sDArr = HandleXML.startDate.toArray(new Date[0]);
        eDArr = HandleXML.endDate.toArray(new Date[0]);

        lstVwAda = new ListViewAdapter(this, titleArr, sDArr, eDArr);
        lstVw1.setAdapter(lstVwAda);
        lstVw1.setOnItemClickListener(this);
        count = lstVwAda.getCount();
        System.out.println("adapter => " + count);
        Toast.makeText(this, "Roadworks found: " + count, Toast.LENGTH_SHORT).show();
        searchArr = new Date[count][3];
        for (int i = 0; i < count; i++) {
            searchArr[i][0] = sDArr[i];
        }
        for (int x = 0; x < count; x++) {
            searchArr[x][1] = eDArr[x];
        }
    }

    public void AnotherImportantThing() {
        switcher.showPrevious();
    }

    public void ShowingTheSearch() {
        lstVwAda = new ListViewAdapter(this, titleArrAlt, sDArrAlt, eDArrAlt);
        lstVw1.setAdapter(lstVwAda);
        lstVw1.setOnItemClickListener(this);
        searchCount = lstVwAda.getCount();
        System.out.println("adapter => " + searchCount);
        Toast.makeText(this, "New roadworks found: " + searchCount, Toast.LENGTH_SHORT).show();
    }


    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        tit.setText(titleArr[position]);
        desc.setText(descArr[position]);
        link.setText("For more information, go to: \n" + linkArr[position]);
        geo.setText("Coordinates: \n" + geoArr[position]);

        new Thread() {
            public void run() {
                try {
                    Refresh.sendEmptyMessage(REFRESH_SCREEN);
                } catch (Exception e) {

                }
            }
        }.start();
    }

    Handler Refresh = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_SCREEN:
                    switcher.showNext();
                    break;

                default:
                    break;
            }
        }
    };
}
