package com.example.chaos_000.mobile_cw;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

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
 */

public class MainActivity extends Activity implements OnItemClickListener {
    //Interface Declarations
    Button but1, but2, but3, but4, but5, but6;
    ListView lstVw1;
    ListViewAdapter lstVwAda;
    RelativeLayout ambLayout;
    TextView tit, desc, link, geo, ambi;
    EditText searchInput;
    private ViewFlipper switcher;
    private static final int REFRESH_SCREEN = 1;

    //Parsing Declarations
    private String rdWrks = "http://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String pRdWrks = "http://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private HandleXML xmlObj;
    private boolean showingAll = false;
    int count;

    //Main arrays for data
    String[] titleArr;
    String[] descArr;
    String[] linkArr;
    String[] geoArr;
    String[] durationArr;
    Date[] sDArr;
    Date[] eDArr;

    //Arrays for search function
    Date[][] searchArr;
    int[] foundArr;
    String searchTerm;
    String[] titleArrAlt;
    String[] descArrAlt;
    String[] durationArrAlt;
    Date dateToFind;
    Date[] sDArrAlt;
    Date[] eDArrAlt;
    public static List<String> titleLstAlt = new ArrayList<>();
    public static List<String> descLstAlt = new ArrayList<>();
    public static List<Date> startDateAlt = new ArrayList<>();
    public static List<Date> endDateAlt = new ArrayList<>();
    public static List<String> duration = new ArrayList<>();
    public static List<String> durationAlt = new ArrayList<>();
    int searchCount;

    //Declarations for Ambient Mode
    public int randomColour;
    int r;
    int g;
    int b;
    int minSleep = 5000;
    int trueSleep;
    boolean ambient = false;
    boolean runningThread = false;
    Timer timer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switcher = (ViewFlipper) findViewById(R.id.ViewFlipper);
        but1 = (Button) findViewById(R.id.button);
        but2 = (Button) findViewById(R.id.button2);
        but3 = (Button) findViewById(R.id.button3);
        but4 = (Button) findViewById(R.id.button4);
        but5 = (Button) findViewById(R.id.button5);
        but6 = (Button) findViewById(R.id.button6);
        tit = (TextView) findViewById(R.id.titTxtView);
        desc = (TextView) findViewById(R.id.descTxtView);
        link = (TextView) findViewById(R.id.linkTxtView);
        geo = (TextView) findViewById(R.id.geoTxtView);
        ambi = (TextView) findViewById(R.id.countOfIncs);
        lstVw1 = (ListView) findViewById(R.id.mainListView);
        searchInput = (EditText) findViewById(R.id.editText);
        ambLayout = (RelativeLayout) findViewById(R.id.ambRelLay);

        but4.setEnabled(false);
        but5.setEnabled(false);
        searchInput.setEnabled(false);

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showingAll = true;
                xmlObj = new HandleXML(rdWrks);
                xmlObj.fetchXML();
                while (xmlObj.parsingComplete) ;
                ImportantThing();
                but4.setEnabled(true);
                but5.setEnabled(true);
                searchInput.setEnabled(true);
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
                showingAll = true;
                xmlObj = new HandleXML(pRdWrks);
                xmlObj.fetchXML();
                while (xmlObj.parsingComplete) ;
                ImportantThing();
                but4.setEnabled(true);
                but5.setEnabled(true);
                searchInput.setEnabled(true);
            }
        });

        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showingAll = false;
                titleLstAlt.clear();
                startDateAlt.clear();
                endDateAlt.clear();
                descLstAlt.clear();
                durationAlt.clear();
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
                        descLstAlt.add(descArr[z]);
                        startDateAlt.add(sDArr[z]);
                        endDateAlt.add(eDArr[z]);
                        durationAlt.add(durationArr[z]);
                    }
                }
                titleArrAlt = titleLstAlt.toArray(new String[0]);
                descArrAlt = descLstAlt.toArray(new String[0]);
                durationArrAlt = durationAlt.toArray(new String[0]);
                sDArrAlt = startDateAlt.toArray(new Date[0]);
                eDArrAlt = endDateAlt.toArray(new Date[0]);
                ShowingTheSearch();
            }
        });

        but5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambi.setText("Current Incidents: " + count);
                SwitchToAmbient();
            }
        });

        but6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YetAnotherImportantThing();
                ambient = false;
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
        count = titleArr.length;

        SearchPrep();
        DateCompare();

        lstVwAda = new ListViewAdapter(this, titleArr, sDArr, eDArr, durationArr);
        lstVw1.setAdapter(lstVwAda);
        lstVw1.setOnItemClickListener(this);
        System.out.println("adapter => " + count);
        Toast.makeText(this, "Roadworks found: " + count, Toast.LENGTH_SHORT).show();
    }

    public void AnotherImportantThing() {
        switcher.showPrevious();
    }

    public void YetAnotherImportantThing() {
        switcher.showNext();
        runningThread = false;
        timer.cancel();
    }

    public void SwitchToAmbient() {
        new Thread() {
            public void run() {
                try {
                    Refresh.sendEmptyMessage(REFRESH_SCREEN);
                } catch (Exception e) {

                }
            }
        }.start();

        ambient = true;

        if (count > 0) {
            trueSleep = ((minSleep / (count / 2)) * 10);
        } else {
            trueSleep = (minSleep / 1);
        }

            timer  = new Timer();
            timer.schedule(new AmbientTask(), 100, (trueSleep));

    }

    public void ShowingTheSearch() {
        lstVwAda = new ListViewAdapter(this, titleArrAlt, sDArrAlt, eDArrAlt, durationArrAlt);
        lstVw1.setAdapter(lstVwAda);
        lstVw1.setOnItemClickListener(this);
        searchCount = lstVwAda.getCount();
        System.out.println("adapter => " + searchCount);
        Toast.makeText(this, "New roadworks found: " + searchCount, Toast.LENGTH_SHORT).show();
    }


    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        //We need to separate the search list from the regular list
        if (showingAll == true) {
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
        } else {
            tit.setText(titleArrAlt[position]);
            desc.setText(descArrAlt[position]);
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
    }

    Handler Refresh = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_SCREEN:
                    if (ambient == false) {
                        switcher.showNext();
                    }
                    else
                    {
                        if (runningThread == true) {
                            ambLayout.setBackgroundColor(randomColour);
                        }
                        else
                        {
                            switcher.showPrevious();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void SearchPrep() {
        searchArr = new Date[count][3];
        //Adds the start date to an array so that we can use it later for functions and searching more easily
        for (int i = 0; i < count; i++) {
            searchArr[i][0] = sDArr[i];
        }
        //Adds the end date to an array so that we can use it later for functions and searching more easily
        for (int x = 0; x < count; x++) {
            searchArr[x][1] = eDArr[x];
        }
    }

    public void DateCompare() {
        if (showingAll == true) {
            duration.clear();
            for (int i = 0; i < count; i++) {
                long temp = Math.abs(searchArr[i][1].getTime() - searchArr[i][0].getTime());
                long tempDays = temp / (24 * 60 * 60 * 1000);
                if (tempDays > 30) {
                    duration.add("Long");
                } else if (tempDays > 10) {
                    duration.add("Med");
                } else {
                    duration.add("Short");
                }
            }
            durationArr = duration.toArray(new String[0]);
        }
    }

    class AmbientTask extends TimerTask {
        public void run() {
            try {
                Refresh.sendEmptyMessage(REFRESH_SCREEN);
                GetRandom();
            } catch (Exception e) {

            }
        }

        public void GetRandom()
        {
            runningThread = true;
            r = (int) (Math.random() * 256);
            g = (int) (Math.random() * 256);
            b = (int) (Math.random() * 256);
            randomColour = Color.rgb(r, g, b);
            System.out.println("Wait = " + trueSleep + "     " + randomColour);
        }
    }
}
