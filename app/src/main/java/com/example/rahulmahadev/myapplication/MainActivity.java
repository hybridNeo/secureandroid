package com.example.rahulmahadev.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    EditText answer_elem;
    TextView question_elem;
    Button submit;
    class BackgroundQuiz extends AsyncTask<Void,Void,Void>{

        String line;
        protected Void doInBackground(Void... params) {
            try{
                while(true){
                    System.out.println("Here\n");
                    FileInputStream fin = new FileInputStream("/data/data/com.example.rahulmahadev.myapplication/files/appin");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
                    line = reader.readLine();
                    System.out.println("Question "+ line);
                    fin.close();
                    if(line != null)
                        break;
                    Thread.sleep(1000);
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            question_elem.setText(line);

        }
    }
    protected void createFiles(){
        try{
            FileOutputStream fOut1 = openFileOutput("appout",
                    MODE_WORLD_READABLE);
            FileOutputStream fOut2 = openFileOutput("appin",MODE_WORLD_WRITEABLE);
            OutputStreamWriter osw_appout = new OutputStreamWriter(fOut1);
            osw_appout.write("1");
            osw_appout.flush();
            osw_appout.close();
            OutputStreamWriter osw_appin = new OutputStreamWriter(fOut2);
            osw_appin.write("");
            osw_appin.flush();
            osw_appin.close();
            System.out.println("Hello I'm here world");

        }catch(IOException e){
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createFiles();
        question_elem = (TextView)findViewById(R.id.question);
        answer_elem = (EditText)findViewById(R.id.answer);
        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer;
                answer = answer_elem.getText().toString();
                System.out.println("Answer is "+ answer);
                try{
                    FileOutputStream fOut1 = openFileOutput("appout",
                            MODE_WORLD_READABLE);
                    OutputStreamWriter osw_appout = new OutputStreamWriter(fOut1);
                    osw_appout.write("2\n"+ answer);
                    osw_appout.flush();
                    osw_appout.close();
                    Thread.sleep(100);
                    FileInputStream fin = new FileInputStream("/data/data/com.example.rahulmahadev.myapplication/files/appin");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fin));

                    String line = reader.readLine();
                    fin.close();
                    if(line == "4"){
                        Toast.makeText(getApplicationContext(),"Over",Toast.LENGTH_LONG).show();

                    }else{
                        createFiles();
                        BackgroundQuiz back_act = new BackgroundQuiz();

                        back_act.execute();
                    }



                }catch (IOException e){
                    e.printStackTrace();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {

                }


            }
        });
        BackgroundQuiz back_act = new BackgroundQuiz();

        back_act.execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
