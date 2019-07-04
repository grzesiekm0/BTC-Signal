package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import static com.yourstronghelper.grzegorzmacko.btcsignal.MainActivity.updateRowsDb;

public class AlertAdapter extends ArrayAdapter<Alert> {
    private List<Alert> mList;
    private Context mContext;

    TextView mExchange;
    TextView mCourse;
    Switch mEnableAlarm;
    TextView mCurrency;

    public AlertAdapter(Context context, List<Alert> myOrders) {
        super(context, 0, myOrders);
        this.mList = myOrders;
        this.mContext = context;
    }


    public View getView(final int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_listview,parent,false
            );
        }

        final Alert currentAlarm = getItem(position);


        mExchange = (TextView)listItemView.findViewById(R.id.exchange);
        mCourse = (TextView)listItemView.findViewById(R.id.course);
        mCurrency = (TextView)listItemView.findViewById(R.id.currency);
        mEnableAlarm = (Switch)listItemView.findViewById(R.id.enableAlarm);


        //Set the text of the meal, amount and quantity
        mExchange.setText(currentAlarm.getExchange());
        mCourse.setText(currentAlarm.getCourse());
        mCurrency.setText("x "+ currentAlarm.getCurrency());
        if(currentAlarm.getEnableAlarm()==1){
            mEnableAlarm.setChecked(true);
        }
        else if(currentAlarm.getEnableAlarm()==0){
            mEnableAlarm.setChecked(false);
        }

        //OnClick listeners for all the buttons on the ListView Item
        mExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mEnableAlarm.setText("00");
                //quantityText.setText("x "+ currentFood.getmQuantity());
                //currentCost.setText("GH"+"\u20B5"+" "+ (currentFood.getmAmount() * currentFood.getmQuantity()));
                currentAlarm.setExchange("Bitmex");
                notifyDataSetChanged();
            }
        });

        mCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //currentFood.removeFromQuantity();

                mCurrency.setText("xxx!");
                //currentCost.setText("GH"+"\u20B5"+" "+ (currentFood.getmAmount() * currentFood.getmQuantity()));
                notifyDataSetChanged();
            }
        });

        mEnableAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true){
                    updateRowsDb(currentAlarm.getAlertId(), 1);
                    currentAlarm.getAlertId();
                    System.out.println("Tak!");
                }
                else if(isChecked == false){
                    updateRowsDb(currentAlarm.getAlertId(), 0);
                    System.out.println("Nie!");
                }
            }
        });

        /*mExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.remove(position);
                notifyDataSetChanged();
            }
        });*/

        return listItemView;
    }

}
