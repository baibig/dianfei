package com.pierce.dianfei;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.devspark.appmsg.AppMsg;
import com.pierce.dianfei.model.Building;
import com.pierce.dianfei.model.Dianfei;
import com.pierce.dianfei.model.Flow;
import com.pierce.dianfei.model.ReturnInfo;
import com.pierce.dianfei.model.Section;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, InfoProcess{

    private final static String TAg="MainActivity";
    private final static String ERROR_MESSAGE="不存在该仪表信息";
    @Bind(R.id.spn_section)
    Spinner spnSection;
    @Bind(R.id.spn_building)
    Spinner spnBuilding;
    @Bind(R.id.spn_flow)
    Spinner spnFlow;
    @Bind(R.id.et_room)
    EditText edtRoom;
    @Bind(R.id.cbx_bind)
    CheckBox cbxBind;
    @Bind(R.id.btn_query)
    FloatingActionButton btnQuery;
    /*@Bind(R.id.toolbar)
    Toolbar toolbar;*/

    ProgressDialog mDialog;

    ReturnInfo info=new ReturnInfo();

    DianfeiTask task;
    ParseTask parseTask;

    SpinnerAdapter sectionAdapter;
    SpinnerAdapter buildingAdapter;
    SpinnerAdapter flowAdapter;

    List<Section> sectionList=new ArrayList<>();
    List<Building> buildingList=new ArrayList<>();
    List<Flow> flowList=new ArrayList<>();
    List<Dianfei> dianfeiList=new ArrayList<>();

    boolean isQueryClicked=false;
    int repeatRequestTimes=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //setSupportActionBar(toolbar);

        mDialog=new ProgressDialog(this);
        mDialog.setMessage("Loading.......");

        task=new DianfeiTask();
        task.setCallback(this);
        task.query(info);

        //task.execute(info);
        sectionAdapter=new SpinnerAdapter(this, R.layout.spinner_item, R.id.spinnerItem, sectionList);
        buildingAdapter=new SpinnerAdapter(this, R.layout.spinner_item, R.id.spinnerItem, buildingList);
        flowAdapter=new SpinnerAdapter(this, R.layout.spinner_item, R.id.spinnerItem, flowList);
        spnSection.setAdapter(sectionAdapter);
        spnSection.setOnItemSelectedListener(this);
        spnBuilding.setAdapter(buildingAdapter);
        spnBuilding.setOnItemSelectedListener(this);
        spnFlow.setAdapter(flowAdapter);
        spnFlow.setOnItemSelectedListener(this);

        /*SharedPreferences sharedPreferences=getSharedPreferences("userinfo",MODE_PRIVATE);
        if (sharedPreferences.getString("binded","error").equals("error")){
            task.query(info);
        }
        else{
            //spnSection.setSelection();
        }*/

            btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setRoom(edtRoom.getText().toString());
                if (!TextUtils.isEmpty(info.getSection())&&!TextUtils.isEmpty(info.getBuilding())
                        &&!TextUtils.isEmpty(info.getFlow())&&!TextUtils.isEmpty(info.getRoom())){
                    isQueryClicked=true;
                    task.query(info);
                    //task.execute(info);
                    Log.i(TAg, info.toString());
                    mDialog.show();


                }
                else
                    showInfo("请先输入完整信息再查询");
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spn_section:
                info.setSection(parent.getItemAtPosition(position).toString());
                info.setState(1);
                info.setBuilding("");
                info.setFlow("");
                info.setRoom("");
                edtRoom.setText("");

                task.query(info);
                break;
            case R.id.spn_building:
                info.setBuilding(parent.getItemAtPosition(position).toString());
                info.setFlow("");
                info.setRoom("");
                info.setState(2);
                task.query(info);
                edtRoom.setText("");
                break;
            case R.id.spn_flow:
                info.setFlow( parent.getItemAtPosition(position).toString());
                info.setState(3);
                info.setRoom("");
                edtRoom.setText("");
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void callback(String s) {

        if (!s.contains(ERROR_MESSAGE)) {
            process(s);
            repeatRequestTimes=2;
        }

        else {
            showInfo("请输入正确的信息！");
            mDialog.dismiss();
        }
    }

    @Override
    public void error() {
        if (repeatRequestTimes>0){
            task.query(info);
            repeatRequestTimes--;
        }else {
            showInfo("由于网络原因数据未加载，请重启应用再试！");
        }
    }
    public void process(String s){
        parseTask=new ParseTask();
        //Log.i(TAg,s);
        info.setHtml(s);
        Log.i(TAg, info.toString());

        info.setInfo(parseTask.processInfo(info));
        switch (info.getState()){
            case 0:
                sectionList.clear();
                buildingList.clear();
                flowList.clear();
                sectionList.addAll(parseTask.getSections(info));
                sectionAdapter.notifyDataSetChanged();
                buildingAdapter.notifyDataSetChanged();
                flowAdapter.notifyDataSetChanged();

                break;
            case 1:
                buildingList.clear();
                flowList.clear();
                buildingList.addAll(parseTask.getBuildings(info));
                buildingAdapter.notifyDataSetChanged();
                flowAdapter.notifyDataSetChanged();
                break;
            case 2:
                flowList.clear();
                flowList.addAll(parseTask.getFlows(info));
                flowAdapter.notifyDataSetChanged();
                break;
            case 3:
                dianfeiList.clear();
                dianfeiList.addAll(parseTask.getDianfei(info));
                mDialog.dismiss();
                if (isQueryClicked){
                    ResultDialog dialog=new ResultDialog();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("data", (Serializable) dianfeiList);
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), "resultdialog");
                }
                isQueryClicked=false;
                if (cbxBind.isChecked()){
                    bind();
                }

                break;
        }
    }

    public void bind(){
        SharedPreferences sharedPreferences=getSharedPreferences("userinfo",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.putString("binded","bind");
        editor.putString("section",info.getSection());
        editor.putString("building",info.getBuilding());
        editor.putString("flow",info.getFlow());
        editor.putString("room",info.getRoom());
        editor.putString("viewstate", info.getViewstate());
        editor.putString("eventvalidation", info.getEventvalidation());
        editor.commit();
    }

    public void showInfo(String message) {
        AppMsg.Style style = new AppMsg.Style(1500, R.color.register);
        AppMsg appMsg = AppMsg.makeText(this, message, style);
        appMsg.setAnimation(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        appMsg.setLayoutGravity(Gravity.CENTER);
        appMsg.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
