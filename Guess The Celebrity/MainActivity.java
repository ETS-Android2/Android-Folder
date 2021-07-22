package com.shahdivya.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ArrayList<String> namesUrl = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();
    int selectUrl ;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    public class downloadSource extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                StringBuilder stringBuilder = new StringBuilder();
                while (data != -1) {
                    char current = (char) data;
                    stringBuilder.append(current);

                    data = reader.read();
                }
                return stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public class downloadImage extends AsyncTask<String,Void,Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... strings)
        {
            URL url;
            HttpURLConnection connection;
            try
            {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(inputStream);
                return image;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        imageView = findViewById(R.id.getImage);
        int k = 0;
        try {
            downloadSource task = new downloadSource();
            String w = task.execute("https://www.imdb.com/list/ls052283250/").get();
            System.out.println(w);
            Pattern p = Pattern.compile("src=\"(.*?).jpg\"");
            Matcher matcher = p.matcher(w);
            while (matcher.find() && k <= 99) {
                k++;
                namesUrl.add(matcher.group(1));
            }
            //System.out.println(namesUrl.toString());
            p = Pattern.compile("img alt=\"(.*?)\"");
            matcher = p.matcher(w);
            while (matcher.find()) {
                k++;
                names.add(matcher.group(1));
            }
            //System.out.println(k);
            //System.out.println(names.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        QuestionGenerator();
    }
    public void QuestionGenerator()
    {
        ArrayList<String> options = new ArrayList<String>();
        Random random = new Random();
        selectUrl = random.nextInt(100);
        downloadImage task = new downloadImage();
        try
        {
            Bitmap bitmap;
            bitmap = task.execute(namesUrl.get(selectUrl)+".jpg").get();
            imageView.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        int position = random.nextInt(4);
        for (int i=0;i<4;i++)
        {
            if (i==position)
            {
                options.add(names.get(selectUrl));
            }
            else
            {
                String wrongAnswer = names.get(random.nextInt(100));
                while (wrongAnswer.compareTo(names.get(selectUrl))==0)
                {
                    wrongAnswer = names.get(random.nextInt());
                }
                options.add(wrongAnswer);
            }
        }
        Log.i("answer",Integer.toString(position));
        button0.setText(options.get(0));
        button1.setText(options.get(1));
        button2.setText(options.get(2));
        button3.setText(options.get(3));
    }
    public void isRight(View view)
    {
        Button userAns = (Button)view;
        String ans = userAns.getText().toString();
        if (ans.compareTo(names.get(selectUrl))==0)
        {
            Toast.makeText(getApplicationContext(),"Correct Answer",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String correctAnswer = names.get(selectUrl);
            //Toast.makeText(getApplicationContext(),"Better Luck Next Time",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),correctAnswer,Toast.LENGTH_SHORT).show();
        }
        QuestionGenerator();
    }
}

