package com.example.ncu_app;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStreamReader;
import java.util.ArrayList;



import util.AutoAdaptImage;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ListItem> listItems = new ArrayList<ListItem>();
    private String userName;

    public RecyclerViewAdapter(Context context, ArrayList<ListItem> listItems, String userName){
        this.context = context;
        this.listItems = listItems;
        this.userName = userName;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_recycler_listitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder)holder;

        final ListItem listItem = listItems.get(position);
        Log.i("---position---", String.valueOf(position));
        myViewHolder.tv_objectName.setText(listItem.getObjectName());
        myViewHolder.tv_objectDate.setText(listItem.getBuildDate());
        setBackground(myViewHolder.btn_delete, R.drawable.icon_delete);
        myViewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("---position---", String.valueOf(position));
                removeItem(position);
                //不能直接使用position,因為可能還沒更新好
                listItems.remove(myViewHolder.getLayoutPosition());
                if(position == listItems.size()){
                    Log.i("---position---", String.valueOf(position) + "the last object");
                }else {
                    //從刪除的地方至最後都要更新position
                    notifyItemRangeChanged(position, listItems.size()-1);
                }
                saveData();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listItems != null){
            return listItems.size();
        }else {
            return 0;
        }

    }

    public void addItem(ListItem listItem){
        listItems.add(listItem);
        saveData();
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeItem(int position){
        notifyItemRemoved(position);
//        notifyDataSetChanged();
    }

    private JSONArray parseObjectsToJSON(){
        if(listItems != null){
            JSONArray jsonArray = new JSONArray();
            for(int i=0; i<listItems.size(); i++){
                JSONObject tempObject = new JSONObject();
                try {
                    tempObject.put("objectName", listItems.get(i).getObjectName());
                    tempObject.put("buildDate", listItems.get(i).getBuildDate());
                    jsonArray.put(tempObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.i("---json---", jsonArray.toString());
//            System.out.println("List to JSON: " + jsonArray.toString());
            return jsonArray;
        }
        return null;
    }

    private void saveData(){
        JSONArray jsonArray = parseObjectsToJSON();
        String fileName = userName + ".json";
        File file = new File(context.getFilesDir(), fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(jsonArray.toString().getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView riv_image_head;
        private TextView tv_objectName, tv_objectDate;
        private Button btn_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            riv_image_head = itemView.findViewById(R.id.riv_img_head);
            tv_objectName = itemView.findViewById(R.id.tv_objectName);
            tv_objectDate = itemView.findViewById(R.id.tv_objectDate);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    private void setBackground(final View view, final int drawable){
        view.post(new Runnable() {
            @Override
            public void run() {
                int reqWidth = view.getWidth();
                int reqHeight = view.getHeight();
                Drawable db = new BitmapDrawable(
                        context.getResources(),
                        AutoAdaptImage.decodeSampledBitmapFromResource(context.getResources(), drawable
                                , reqWidth, reqHeight)
                );
                view.setBackground(db);
            }
        });
    }
}
