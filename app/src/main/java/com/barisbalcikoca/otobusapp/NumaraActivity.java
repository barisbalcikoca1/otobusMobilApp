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
import java.util.List;

public class NumaraActivity extends AppCompatActivity {
    private TextView txtHatNo,txtSeferler;
    private ListView listView;
    Intent gelenIntent;

    ArrayList<String> seferSaatListesi = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numara);


        initComponents();
        registerEventHandlers();

        // Intent'ten gönderilen veriyi alın
        gelenIntent= getIntent();
        if (gelenIntent != null) {
            String selectedValue = gelenIntent.getStringExtra("selectedValue");

            // Alınan veriyi txtHatNo TextView'ine yerleştirin
            if (selectedValue != null) {
                txtHatNo.setText(selectedValue);
            }
        }


        new otobus_numara_sefer().start();
        new otobus_numara_sefer_saat().start();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, seferSaatListesi);
        listView.setAdapter(adapter);
    }


    private void initComponents() {
        txtHatNo = findViewById(R.id.txtHatNo);
        txtSeferler = findViewById(R.id.txtSeferler);
        listView = findViewById(R.id.listSeferSaat);
}

    private void registerEventHandlers() {

    }

    private class otobus_numara_sefer extends Thread {
        String data ="";
        @Override
        public void run(){
            {
                try {
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("islem", "guzergah_aciklama_getir");

                    JSONArray parametreler = new JSONArray();
                    parametreler.put(txtHatNo.getText());
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
                            String numara_guzergah_seferler = otobusGuzergahArray.getString(i);
                            txtSeferler.setText(numara_guzergah_seferler);
                        }

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

    private class otobus_numara_sefer_saat extends Thread {
        String data ="";
        @Override
        public void run(){
            {
                try {
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("islem", "kalkis_saati_getir");

                    JSONArray parametreler = new JSONArray();
                    parametreler.put(txtHatNo.getText());
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

                        JSONArray otobusSeferSaat = obj.getJSONArray("result");
                        //Toast.makeText(ShowActivity.this, cityArray2,Toast.LENGTH_SHORT).show(); Bu kullanılamıyor alt tarafta yazdığım kod güvenli olması için yazıldı
                        for (int i = 0; i < otobusSeferSaat.length(); i++) {
                            String numara_guzergah_sefer_saat = otobusSeferSaat.getString(i);
                            seferSaatListesi.add(numara_guzergah_sefer_saat);
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