package com.example.root.insideout;

        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.app.Activity;
        import android.text.Html;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TableLayout;
        import android.widget.TableRow;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.FileInputStream;
        import java.io.InputStreamReader;

public class NewActivity extends Activity {

    TableLayout tab1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.activityhappy);
        tab1=(TableLayout)findViewById(R.id.tab1);

        Button button1 = (Button) findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(NewActivity.this,
                        MainActivity.class);
                startActivity(myIntent);
                setContentView(R.layout.activity_main);
            }
        });

        try {
            FileInputStream fileIn=openFileInput("mytextfile.txt");
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[1000];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            String[] separated = s.split("$");
            for (String sep : separated) {
                if(sep.toLowerCase().contains("joy")) {
                    TableRow tr = new TableRow(getApplicationContext());
                    tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView textview = new TextView(getApplicationContext());
                    textview.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f));
                    textview.setTextSize(20);
                    textview.setTextColor(Color.parseColor("#0B0719"));
                    textview.setText(Html.fromHtml(sep));
                    tr.addView(textview);
                    tab1.addView(tr);
                    //   Toast.makeText(getBaseContext(), s,Toast.LENGTH_SHORT).show();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
