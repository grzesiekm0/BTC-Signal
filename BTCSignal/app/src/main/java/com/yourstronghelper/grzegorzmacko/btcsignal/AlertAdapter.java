package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AlertAdapter extends ArrayAdapter<Alert> {
    private List<Alert> mList;
    private Context mContext;

    TextView mExchange;
    TextView mCourse;
    TextView mEnableAlarm;
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

        //Set the text of the meal, amount and quantity
        mExchange.setText(currentAlarm.getExchange());
        mCourse.setText(currentAlarm.getCourse());
        mCurrency.setText("x "+ currentAlarm.getCurrency());


        //OnClick listeners for all the buttons on the ListView Item
        mExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAlarm.setExchange("Bitmex");
                notifyDataSetChanged();
            }
        });

        mCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mCurrency.setText("xxx!");
                notifyDataSetChanged();
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
