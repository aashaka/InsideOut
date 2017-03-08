package com.example.root.insideout;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

public class MainActivity extends Activity {
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;

    TableLayout tab;
//    TableLayout tab2;
//    TableLayout tab3;
//    TableLayout tab4;
//    TableLayout tab5;
//    TableLayout tab6;
    Context context;
    String text;


    private class AskWatsonTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... textsToAnalyse) {

            System.out.println(text);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

            AlchemyLanguage service = new AlchemyLanguage();
            service.setApiKey("APIKEY");

            Map<String, Object> params = new HashMap<String, Object>();
            params.put(AlchemyLanguage.TEXT, text);
            DocumentSentiment sentiment = service.getSentiment(params).execute();

            System.out.println(sentiment);

            //passing the result to be displayed at UI in the main tread
            return sentiment.getSentiment().getType().name();
        }

        //setting the value of UI outside of the thread
        @Override
        protected void onPostExecute(String result) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab = (TableLayout)findViewById(R.id.tab);

        context = getApplicationContext();
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        button1 = (Button) findViewById(R.id.button1);


        try {
            FileInputStream fileIn=openFileInput("mytextfile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[100];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
                    TableRow tr = new TableRow(getApplicationContext());
                    tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView textview = new TextView(getApplicationContext());
                    textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
                    textview.setTextSize(20);
                    textview.setTextColor(Color.parseColor("#0B0719"));
                    textview.setText(Html.fromHtml(s));
                    tr.addView(textview);
                    tab.addView(tr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            text = intent.getStringExtra("text");

            MediaPlayer mediaPlayer;
            String emotion ="";

            System.out.println("Logging to the console that the button pressed for the text : " + text);


            AskWatsonTask task = new AskWatsonTask();
            task.execute(new String[]{});

            switch (emotion) {
                case "fear":
                    mediaPlayer = MediaPlayer.create(context, R.raw.alarm_fear);
                    break;
                case "anger":
                    mediaPlayer = MediaPlayer.create(context, R.raw.machinegun_anger);
                    break;
                case "joy":
                    mediaPlayer = MediaPlayer.create(context, R.raw.hway2hell_joy);
                    break;
                case "disgust":
                    mediaPlayer = MediaPlayer.create(context, R.raw.ohmygod_disgust);
                    break;
                case "sad":
                    mediaPlayer = MediaPlayer.create(context, R.raw.lethergo_sad);
                    break;
                default:
                    mediaPlayer = MediaPlayer.create(context, R.raw.nokia_neutral);
            }

            mediaPlayer.start();

            String MY_FILE_NAME = "mytextfile.txt";

            if(emotion.equals("joy")) {
                TableRow tr = new TableRow(getApplicationContext());
                tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#0B0719"));
                textview.setText(Html.fromHtml(" Message from: " + "<b>" + title + " : </b>\n" + text + " <br/>"));
                tr.addView(textview);
                try {
                    FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_APPEND);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                    outputWriter.write(textview.getText().toString());
                    outputWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tab.addView(tr);
            }

        }
    };
}