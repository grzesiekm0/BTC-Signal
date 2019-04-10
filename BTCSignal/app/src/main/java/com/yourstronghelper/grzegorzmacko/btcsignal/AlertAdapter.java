package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AlertAdapter extends ArrayAdapter<Alert> {
    private List<Alert> list;
    private Context context;

    TextView mExchange,
            mCourse,
            mPerPoints,
            mEnableAlarm;

    public AlertAdapter(Context context, List<Alert> myOrders) {
        super(context, 0, myOrders);
        this.list = myOrders;
        this.context = context;
    }


    public View getView(final int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_listview,parent,false
            );
        }

        final Alert currentAlarm = getItem(position);



        mExchange = (TextView)listItemView.findViewById(R.id.selected_food_name);
        mCourse = (TextView)listItemView.findViewById(R.id.selected_food_amount);
        mPerPoints = (TextView)listItemView.findViewById(R.id.minus_meal);
        mEnableAlarm = (TextView)listItemView.findViewById(R.id.quantity);


        //Set the text of the meal, amount and quantity
        mExchange.setText(currentAlarm.getExchange());
        mCourse.setText(currentAlarm.getCourse());
        mPerPoints.setText("x "+ currentAlarm.getPerPoints());
        mEnableAlarm.setText("x "+ currentAlarm.getEnableAlarm());

        //OnClick listeners for all the buttons on the ListView Item
        mEnableAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* currentFood.addToQuantity();
                quantityText.setText("x "+ currentFood.getmQuantity());
                currentCost.setText("GH"+"\u20B5"+" "+ (currentFood.getmAmount() * currentFood.getmQuantity()));
                notifyDataSetChanged();*/
            }
        });

        mExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* currentFood.removeFromQuantity();
                quantityText.setText("x "+currentFood.getmQuantity());
                currentCost.setText("GH"+"\u20B5"+" "+ (currentFood.getmAmount() * currentFood.getmQuantity()));
                notifyDataSetChanged();*/
            }
        });

        /*removeMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*list.remove(position);
                notifyDataSetChanged();*//*
            }
        });*/

        return listItemView;
    }

}
