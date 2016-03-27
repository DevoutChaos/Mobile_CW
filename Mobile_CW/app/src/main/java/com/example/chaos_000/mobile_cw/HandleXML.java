package com.example.chaos_000.mobile_cw;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

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
    private String title, description, link, geo;
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;
    private boolean firstDesc = false;
    private boolean firstTit = false;
    private boolean firstLink = false;

    public static List<String> titleLst = new ArrayList<>();
    public static List<String> descLst = new ArrayList<>();
    public static List<String> linkLst = new ArrayList<>();
    public static List<String> geoLst = new ArrayList<>();
    public static List<Date> startDate = new ArrayList<>();
    public static List<Date> endDate = new ArrayList<>();

    public HandleXML(String url) {
        this.urlString = url;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text = null;
        titleLst.clear();
        descLst.clear();
        linkLst.clear();
        geoLst.clear();
        startDate.clear();
        endDate.clear();

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
                        //Add all the main items to the lists - This does NOT handle the actual output
                        if (name.equals("title")) {
                            if (firstTit == true) {
                                title = text;
                                titleLst.add(title);
                            } else {
                                firstTit = true;
                            }
                        } else if (name.equals("description")) {
                            if (firstDesc == true) {

                                //Sets the description
                                description = text;

                                //Splits around the break tag to let us get the date
                                String[] retval = description.split("<br />", 0);

                                //Removes the break tag for the short view
                                description = description.replaceAll("<br />", "\n");

                                //Jiggery pokery to get the date in a usable format
                                retval[0] = retval[0].replaceAll("Start Date: ", "");
                                retval[0] = retval[0].replaceAll(" - 00:00", "");
                                retval[1] = retval[1].replaceAll("End Date: ", "");
                                retval[1] = retval[1].replaceAll(" - 00:00", "");
                                DateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH);
                                Date sDate = format.parse(retval[0]);
                                Date eDate = format.parse(retval[1]);

                                //Putting everything into the list it belongs to
                                startDate.add(sDate);
                                endDate.add(eDate);
                                descLst.add(description);
                            } else {
                                firstDesc = true;
                            }
                        } else if (name.equals("link")) {
                            if (firstLink == true) {
                                link = text;
                                linkLst.add(link);
                            } else {
                                firstLink = true;
                            }
                        } else if (name.equals("georss:point")) {
                            geo = text;
                            geoLst.add(geo);
                        } else {

                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
        } catch (
                Exception e
                )

        {
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
