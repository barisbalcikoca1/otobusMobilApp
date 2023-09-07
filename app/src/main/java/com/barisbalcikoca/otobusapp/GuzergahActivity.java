package com.barisbalcikoca.otobusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class GuzergahActivity extends AppCompatActivity {

    private TextView txtGuzergahAdi;
    ListView listView;
    Intent gelenIntent;

    ArrayList<String> guzergahAdinaGoreNumara = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guzergah);

        initComponents();
        registerEventHandlers();

        gelenIntent= getIntent();
        if (gelenIntent != null) {
            String selectedValue = gelenIntent.getStringExtra("selectedValue");

            // Alınan veriyi txtHatNo TextView'ine yerleştirin
            if (selectedValue != null) {
                txtGuzergahAdi.setText(selectedValue);
            }
        }
        new guzergah_adina_gore_numara().start();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, guzergahAdinaGoreNumara);
        listView.setAdapter(adapter);

    }

    private void initComponents() {
        txtGuzergahAdi = findViewById(R.id.txtGuzergahAdi);
        listView = findViewById(R.id.listOtobusNo);
    }

    private void registerEventHandlers() {

    }

    private class guzergah_adina_gore_numara extends Thread {
        String data ="";
        @Override
        public void run(){
            {
                try {
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("islem", "guzergaha_gore_numara");

                    JSONArray parametreler = new JSONArray();
                    parametreler.put(txtGuzergahAdi.getText());
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

                        JSONArray otobusGuzergahAdinaGoreNumaraList= obj.getJSONArray("result");
                        //Toast.makeText(ShowActivity.this, cityArray2,Toast.LENGTH_SHORT).show(); Bu kullanılamıyor alt tarafta yazdığım kod güvenli olması için yazıldı
                        for (int i = 0; i < otobusGuzergahAdinaGoreNumaraList.length(); i++) {
                            String numara_guzergah_sefer_saat = otobusGuzergahAdinaGoreNumaraList.getString(i);
                            guzergahAdinaGoreNumara.add(numara_guzergah_sefer_saat);
                        }

                        //Altta bulunan kod eklenmezse liste güncellenmediği için veriler eklense bile boş görünür.
                        //Verileri listeye ekledikten sonra adapter güncellenir ve bu sayede görünür hale gelr.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
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