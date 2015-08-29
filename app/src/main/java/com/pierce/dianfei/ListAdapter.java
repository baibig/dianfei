package com.pierce.dianfei;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pierce.dianfei.model.Dianfei;

import java.util.List;

/**
 * Created by HJ on 2015/4/25.
 */
public class ListAdapter extends ArrayAdapter {
    int resource;

    public ListAdapter(Context context, int resource, List objects) {
        super(context, resource,objects);
        this.resource=resource;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Dianfei item= (Dianfei) getItem(i);
        ViewHolder viewHolder=null;
        if (view==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater= LayoutInflater.from(getContext());
            view=inflater.inflate(resource,null);
            viewHolder.power= (TextView) view.findViewById(R.id.powerText);
            viewHolder.time= (TextView) view.findViewById(R.id.timeText);
            view.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.power.setText(item.getPower());
        viewHolder.time.setText(item.getTime());
        return view;
    }
    public class ViewHolder{
        TextView power;
        TextView time;
    }
}
