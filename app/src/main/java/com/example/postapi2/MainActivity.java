package com.example.postapi2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.postapi2.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public EditText etEmail, etPass;
    public Button btLogin;
    public final String link = "https://reqres.in/api/users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLogin = findViewById(R.id.btPost);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etEmail.getText().toString();
                String job = etPass.getText().toString();

                User user = new User(name, job);
                //convert => json
                JSONObject jUser = new JSONObject();
                JSONArray jUserArray = new JSONArray();
                try {
                    jUser.put("name", user.getEmail());
                    jUser.put("job", user.getPass());

                    Log.d("TAG", "convert to json: " + jUser.toString());

                    jUserArray.put(jUser);
                    Log.d("TAG", "convert from json to array: " + jUserArray.toString());
                    String jsonData = jUser.toString();
                    new DoCreateUserLogin().execute(jsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    class DoCreateUserLogin extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpsURLConnection httpsURLConnection = null;
            String jsonData = params[0];
            try {
                URL url = new URL(link);
                httpsURLConnection= (HttpsURLConnection) url.openConnection();

                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setChunkedStreamingMode(0);
                httpsURLConnection.connect();

                OutputStream outputStream = new BufferedOutputStream(httpsURLConnection.getOutputStream());
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                bufferedWriter.write(jsonData.toString());
                bufferedWriter.flush();

                int code = httpsURLConnection.getResponseCode();
                if(code !=201 ){
                    throw new IOException("Invalid response from server: " + code);
                }



//                outputStream.write(jsonData.getBytes() );
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

//                InputStream inputStream = httpsURLConnection.getInputStream();
                String result = "";
                while ((result = bufferedReader.readLine()) != null) {
                    //result += (char) byeCharacter;

                    Log.i("data",result);
                }
                Log.d("TAG", "do inbackground return: " + result.toString());
                httpsURLConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(httpsURLConnection != null)
                    httpsURLConnection.disconnect();
            }
            return null;
        }

    }

}
