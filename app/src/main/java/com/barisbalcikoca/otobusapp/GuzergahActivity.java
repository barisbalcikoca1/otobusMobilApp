package com.barisbalcikoca.otobusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class GuzergahActivity extends AppCompatActivity {

    private TextView txtGuzergahAdi;
    ListView listView;
    Intent gelenIntent;

    ArrayList<String> guzergahAdinaGoreNumara = new ArrayList<>();
    ArrayList<String> guzergahAdinaGoreNumaraList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String selectedValue = "";

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = guzergahAdinaGoreNumaraList.get(position);

                // Create an intent to start the next activity
                Intent intent = new Intent(GuzergahActivity.this, NumaraActivity.class);

                // Pass the selected item as extra data to the next activity
                intent.putExtra("selectedValue", selectedValue);

                // Start the next activity
                startActivity(intent);
            }
        });


       // new guzergah_adina_gore_numara().start();
        new GuzergahAdinaGoreNumaraTask().execute();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, guzergahAdinaGoreNumara);
        listView.setAdapter(adapter);

    }

    private void initComponents() {
        txtGuzergahAdi = findViewById(R.id.txtGuzergahAdi);
        listView = findViewById(R.id.listOtobusNo);
    }

    private void registerEventHandlers() {

    }

    //2 KULLANIM DA VAR HANGİSİNİ İSTERSEM ONU KULLANICAMM..
    public class GuzergahAdinaGoreNumaraTask extends AsyncTask<String, Void, ArrayList<String>> {

        String data ="";
        @Override
        protected ArrayList<String> doInBackground(String... params) {

            String guzergahAdi = txtGuzergahAdi.getText().toString();

            guzergahAdinaGoreNumaraList = new ArrayList<>();

            try {
                JSONObject jsonData = new JSONObject();
                jsonData.put("islem", "e_guzergaha_gore_numara");

                JSONArray parametreler = new JSONArray();
                parametreler.put(guzergahAdi);

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
                reader.close();
                if (!data.isEmpty()) {
                    JSONObject obj = new JSONObject(data);
                    JSONArray cityArray = obj.getJSONArray("result");

                    System.out.println(cityArray);

                    for (int i = 0; i < cityArray.length(); i++) {
                        guzergahAdinaGoreNumaraList.add((String) cityArray.get(i));
                        System.out.println(guzergahAdinaGoreNumaraList);
                    }
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return guzergahAdinaGoreNumaraList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> guzergahAdinaGoreNumaraList) {
            if (guzergahAdinaGoreNumaraList != null && !guzergahAdinaGoreNumaraList.isEmpty()) {
                // ListView ve ArrayAdapter oluşturun

                ArrayAdapter<String> adapter = new ArrayAdapter<>(GuzergahActivity.this, android.R.layout.simple_list_item_1, guzergahAdinaGoreNumaraList);

                // ArrayAdapter'ı ListView'a ayarlayın
                listView.setAdapter(adapter);
            }
        }
    }


    private class guzergah_adina_gore_numara extends Thread {
        String data ="";
        String veri = "";
        @Override
        public void run(){
            {
                try {
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("islem", "e_guzergaha_gore_numara");

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