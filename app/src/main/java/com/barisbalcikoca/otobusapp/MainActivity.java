package com.barisbalcikoca.otobusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView otobusNumara, otobusGuzergah;

    ArrayList<String> otobusNumaraList; //LİSTELENEN otobüs no İÇİN
    ArrayList<String> otobusGuzergahList; //LİSTELENEN otobüs no İÇİN

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        registerEventHandlers();
        new otobus_numara().start();
        new otobus_guzergah().start();


    }

    private void initComponents() {
        otobusNumara = findViewById(R.id.txtNumaralar);
        otobusGuzergah = findViewById(R.id.txtGuzergahlar);

        otobusNumaraList = new ArrayList<>();
        otobusGuzergahList = new ArrayList<>();

    }

    private void registerEventHandlers() {
        otobusNumara.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Seçilen veriyi alın
                String selectedValue = (String) parent.getItemAtPosition(position);

                // Alınan değeri NumaraActivity'e iletmek için bir Intent oluşturun
                Intent intent = new Intent(MainActivity.this, NumaraActivity.class);
                intent.putExtra("selectedValue", selectedValue); // Veriyi ekleyin
                // NumaraActivity'i başlatın
                startActivity(intent);
            }
        });

        otobusGuzergah.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Seçilen veriyi alın
                String selectedValue = (String) parent.getItemAtPosition(position);

                // Alınan değeri NumaraActivity'e iletmek için bir Intent oluşturun
                Intent intent = new Intent(MainActivity.this, GuzergahActivity.class);
                intent.putExtra("selectedValue", selectedValue); // Veriyi ekleyin
                // NumaraActivity'i başlatın
                startActivity(intent);
            }
        });

    }
    private class otobus_numara extends Thread {
        String data ="";
        @Override
        public void run(){
            {
                otobusNumaraList.clear();
                try {
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("islem", "otobus_numara");

                    JSONArray parametreler = new JSONArray();
                    parametreler.put("");
                    jsonData.put("parametreler", parametreler);

                    URL url = new URL("https://orakoglu.net/staj1/ayarlar1.php");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(("jsonData=" + URLEncoder.encode(jsonData.toString(), "UTF-8")).getBytes());
                    outputStream.flush();
                    outputStream.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        data = data+line;
                    }
                    if(!data.isEmpty()){
                        JSONObject obj = new JSONObject(data);

                        JSONArray otobusNoArray = obj.getJSONArray("result");
                        //Toast.makeText(ShowActivity.this, cityArray2,Toast.LENGTH_SHORT).show(); Bu kullanılamıyor alt tarafta yazdığım kod güvenli olması için yazıldı
                        for (int i = 0; i < otobusNoArray.length(); i++) {
                            String otobus_no = otobusNoArray.getString(i);
                            otobusNumaraList.add(otobus_no);
                        }

                        // AutoCompleteTextView için Adapter'ı güncelleyin
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, otobusNumaraList);
                                otobusNumara.setAdapter(adapter);
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class otobus_guzergah extends Thread {
        String data ="";
        @Override
        public void run(){
            {
                otobusGuzergahList.clear();
                try {
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("islem", "otobus_guzergah");

                    JSONArray parametreler = new JSONArray();
                    parametreler.put("");
                    jsonData.put("parametreler", parametreler);

                    URL url = new URL("https://orakoglu.net/staj1/ayarlar1.php");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(("jsonData=" + URLEncoder.encode(jsonData.toString(), "UTF-8")).getBytes());
                    outputStream.flush();
                    outputStream.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        data = data+line;
                    }
                    if(!data.isEmpty()){
                        JSONObject obj = new JSONObject(data);

                        JSONArray otobusGuzergahArray = obj.getJSONArray("result");
                        //Toast.makeText(ShowActivity.this, cityArray2,Toast.LENGTH_SHORT).show(); Bu kullanılamıyor alt tarafta yazdığım kod güvenli olması için yazıldı
                        for (int i = 0; i < otobusGuzergahArray.length(); i++) {
                            String otobus_guzergah = otobusGuzergahArray.getString(i);
                            otobusGuzergahList.add(otobus_guzergah);
                        }

                        // AutoCompleteTextView için Adapter'ı güncelleyin
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, otobusGuzergahList);
                                otobusGuzergah.setAdapter(adapter);
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}