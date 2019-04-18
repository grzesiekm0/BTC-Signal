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

    TextView mPhoneNumber;
    TextView mCourse;
    TextView mEnableAlarm;

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



        mPhoneNumber = (TextView)listItemView.findViewById(R.id.phoneNumber);
        mCourse = (TextView)listItemView.findViewById(R.id.course);
        mEnableAlarm = (TextView)listItemView.findViewById(R.id.enableAlarm);

        //Set the text of the meal, amount and quantity
        mPhoneNumber.setText(currentAlarm.getPhoneNumber());
        mCourse.setText(currentAlarm.getCourse());
        mEnableAlarm.setText("x "+ currentAlarm.getEnableAlarm());


        //OnClick listeners for all the buttons on the ListView Item
        mEnableAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mEnableAlarm.setText("00");
                //quantityText.setText("x "+ currentFood.getmQuantity());
                //currentCost.setText("GH"+"\u20B5"+" "+ (currentFood.getmAmount() * currentFood.getmQuantity()));
                currentAlarm.setEnableAlarm(4);
                notifyDataSetChanged();
            }
        });

        mPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //currentFood.removeFromQuantity();

                mEnableAlarm.setText("xxx!");
                //currentCost.setText("GH"+"\u20B5"+" "+ (currentFood.getmAmount() * currentFood.getmQuantity()));
                notifyDataSetChanged();
            }
        });

        mPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.remove(position);
                notifyDataSetChanged();
            }
        });

        return listItemView;
    }

}
