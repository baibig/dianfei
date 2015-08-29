package com.pierce.dianfei;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pierce.dianfei.model.Dianfei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author: pierce
 * Date: 2015/8/15
 */
public class ResultDialog extends DialogFragment {
    private final String  TAG="resultDialog";

    ListView mListView;

    private List<Dianfei> dianfeis=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.result_dialog,container);
        mListView= (ListView) view.findViewById(R.id.result_list);
        Bundle bundle=getArguments();
        dianfeis.clear();
        dianfeis.addAll((List<Dianfei>) bundle.getSerializable("data"));
        Log.i(TAG, dianfeis.toString());
        ListAdapter adapter=new ListAdapter(getActivity(),R.layout.list_item,dianfeis);
        mListView.setAdapter(adapter);
        getDialog().setTitle("最近使用电量情况");
        return view;
    }
}
