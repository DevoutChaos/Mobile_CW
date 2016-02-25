package com.example.chaos_000.mobile_cw;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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

public class HandleXML {
    private String title = "title";
    private String desc = "desc";
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    public static List<String> titleLst = new ArrayList<>();
    public static List<String> descLst = new ArrayList<>();

    public HandleXML(String url) {
        this.urlString = url;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text = null;
        titleLst.clear();
        descLst.clear();

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (name.equals("title")) {
                            title = text;
                            titleLst.add(title);
                        } else if (name.equals("description")) {
                            desc = text;
                            descLst.add(desc);
                        } else {
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(100000);
                    conn.setConnectTimeout(150000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream stream = conn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXMLAndStoreIt(myparser);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
