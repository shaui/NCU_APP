package com.example.ncu_app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private String userName;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private Button btn_add;
    private EditText et_inputObjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        actionBar = getSupportActionBar();

        //get the input and set the Bar's userName
        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("userName");
        actionBar.setTitle(userName);

        //set the recyclerVeiw
        recyclerView = findViewById(R.id.recycleView_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //自定義的ViewHolder
        recyclerViewAdapter = new RecyclerViewAdapter(this, readData(), userName);
        recyclerView.setAdapter(recyclerViewAdapter);
        //add and remove animation
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btn_add = findViewById(R.id.btn_add);
        et_inputObjectName = findViewById(R.id.et_inputObjectName);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_inputObjectName.getText().toString().trim();
                if(!name.isEmpty()){
                    et_inputObjectName.setText("");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(System.currentTimeMillis());
                    String currentDate = simpleDateFormat.format(date);
                    ListItem listItem = new ListItem(name, currentDate);
                    recyclerViewAdapter.addItem(listItem);
                }else {
                    Toast.makeText(ListActivity.this, "please input the name", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private ArrayList<ListItem> readData(){
        JSONArray jsonArray;
        ArrayList<ListItem> listItems = new ArrayList<ListItem>();
        String fileName = userName + ".json";
        File file = new File(getFilesDir(), fileName);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line; //暫時存放每一行的data
            StringBuilder sb = new StringBuilder();
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }
            inputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
            Log.i("---readFile---", sb.toString());
            jsonArray = new JSONArray(sb.toString());
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String objectName = jsonObject.getString("objectName");
                String buildDate = jsonObject.getString("buildDate");
                listItems.add(new ListItem(objectName, buildDate));

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listItems;
    }



}
