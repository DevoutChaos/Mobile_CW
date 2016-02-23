package com.example.chaos_000.mobile_cw;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by chaos_000 on 23/02/2016.
 */
public class MainActivity extends ActionBarActivity{
    EditText ed1, ed2, ed3, ed4;
    private String currInc = "http://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String rdWrks = "http://trafficscotland.org/rss/feeds/roadworks.aspx";

    private HandleXML xmlObj;
    Button but1;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        but1=(Button)findViewById(R.id.button);

        ed1=(EditText)findViewById(R.id.editText1);
        ed2=(EditText)findViewById(R.id.editText2);
        ed3=(EditText)findViewById(R.id.editText3);
        ed4=(EditText)findViewById(R.id.editText4);

        but1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                xmlObj = new HandleXML(rdWrks);
                xmlObj.fetchXML();

                while(xmlObj.parsingComplete);
                ed1.setText(xmlObj.getTitle());
                ed2.setText(xmlObj.getDesc());
                ed3.append(xmlObj.getTitle());
                ed4.append(xmlObj.getDesc());
            }
        });
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem menItem)
    {
        int id = menItem.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(menItem);
    }

}
