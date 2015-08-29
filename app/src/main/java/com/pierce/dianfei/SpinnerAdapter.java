package com.pierce.dianfei;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HJ on 2015/4/27.
 */
public class SpinnerAdapter extends ArrayAdapter {
    int resource;
    public SpinnerAdapter(Context context, int resource, int textViewId, List objects) {
        super(context, resource, textViewId,objects);
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object object=getItem(position);
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(resource,null);
            viewHolder.spinnerItem= (TextView) convertView.findViewById(R.id.spinnerItem);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.spinnerItem.setText(object.toString());
        return convertView;
    }
    class ViewHolder{
        TextView spinnerItem;
    }
}
