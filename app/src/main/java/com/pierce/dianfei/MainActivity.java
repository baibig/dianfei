package com.pierce.dianfei;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Parcelable;
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
import android.widget.EditText;
import android.widget.Spinner;
import com.devspark.appmsg.AppMsg;
import com.pierce.dianfei.model.Building;
import com.pierce.dianfei.model.Dianfei;
import com.pierce.dianfei.model.Flow;
import com.pierce.dianfei.model.ReturnInfo;
import com.pierce.dianfei.model.Section;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    int p1=0;
    int p2=0;
    int p3=0;
    String room;
    List<Dianfei> dianfeiList=new ArrayList<>();

    boolean isQueryClicked=false;
    int repeatRequestTimes=2;


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences=getSharedPreferences("userinfo",MODE_PRIVATE);
        if (preferences.getBoolean("binded",false)){
            p1=preferences.getInt("section",0);
            p2=preferences.getInt("building",0);
            p3=preferences.getInt("flow",0);
            room=preferences.getString("room", "adv");
            String viewstate=preferences.getString("viewstate","");
            String eventvalidation=preferences.getString("eventvalidation","");

           /* FileInputStream in=null;
            ObjectInputStream ois=null;
            try {
                ois=new ObjectInputStream(openFileInput("section.dat"));
                Section s= (Section) ois.readObject();
                sectionList.clear();
                buildingList.clear();
                flowList.clear();
                while (s!=null){
                    sectionList.add(s);
                    Log.i(TAg, s.toString());
                    s= (Section) ois.readObject();

                }
                ois.close();
                ois=new ObjectInputStream(openFileInput("building.dat"));
                Building b=(Building) ois.readObject();
                while (b!=null){
                    buildingList.add(b);
                    Log.i(TAg, b.toString());
                    b=(Building) ois.readObject();

                }
                ois.close();
                ois=new ObjectInputStream(openFileInput("flow.dat"));
                Flow f= (Flow) ois.readObject();
                while (f!=null){
                    flowList.add(f);
                    Log.i(TAg, f.toString());
                    f= (Flow) ois.readObject();

                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally {
                if (in!=null){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (ois!=null){
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDialog=new ProgressDialog(this);
        mDialog.setMessage("Loading.......");
        task=new DianfeiTask();
        task.setCallback(this);
        task.query(info);
        sectionAdapter=new SpinnerAdapter(this, R.layout.spinner_item, R.id.spinnerItem, sectionList);
        buildingAdapter=new SpinnerAdapter(this, R.layout.spinner_item, R.id.spinnerItem, buildingList);
        flowAdapter=new SpinnerAdapter(this, R.layout.spinner_item, R.id.spinnerItem, flowList);
        spnSection.setAdapter(sectionAdapter);
        spnSection.setOnItemSelectedListener(this);
        spnBuilding.setAdapter(buildingAdapter);
        spnBuilding.setOnItemSelectedListener(this);
        spnFlow.setAdapter(flowAdapter);
        spnFlow.setOnItemSelectedListener(this);

            btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setRoom(edtRoom.getText().toString());
                if (!TextUtils.isEmpty(info.getSection())&&!TextUtils.isEmpty(info.getBuilding())
                        &&!TextUtils.isEmpty(info.getFlow())&&!TextUtils.isEmpty(info.getRoom())){
                    isQueryClicked=true;
                    task.query(info);
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
                p1=position;

                task.query(info);
                break;
            case R.id.spn_building:
                info.setBuilding(parent.getItemAtPosition(position).toString());
                info.setFlow("");
                info.setRoom("");
                info.setState(2);
                task.query(info);
                edtRoom.setText("");
                p2=position;
                break;
            case R.id.spn_flow:
                info.setFlow( parent.getItemAtPosition(position).toString());
                info.setState(3);
                info.setRoom("");
                edtRoom.setText("");
                p3=position;
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
                spnSection.setSelection(p1);
                break;
            case 1:
                buildingList.clear();
                flowList.clear();
                buildingList.addAll(parseTask.getBuildings(info));
                buildingAdapter.notifyDataSetChanged();
                flowAdapter.notifyDataSetChanged();
                spnBuilding.setSelection(p2);
                break;
            case 2:
                flowList.clear();
                flowList.addAll(parseTask.getFlows(info));
                flowAdapter.notifyDataSetChanged();
                spnFlow.setSelection(p3);
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
        editor.putBoolean("binded",true);
        editor.putInt("section", p1);
        editor.putInt("building", p2);
        editor.putInt("flow",p3);
        editor.putString("room", info.getRoom());
        editor.putString("viewstate", info.getViewstate());
        editor.putString("eventvalidation", info.getEventvalidation());
        editor.commit();
        ObjectOutputStream oos=null;
        FileOutputStream out = null;
        try {
            out = openFileOutput("section.dat",MODE_PRIVATE);
            oos=new ObjectOutputStream(out);
            for (Section s:sectionList){
                oos.writeObject(s);
            }
            oos.writeObject(null);
            oos.close();
            oos=new ObjectOutputStream(openFileOutput("building.dat",MODE_PRIVATE));
            for (Building b:buildingList){
                oos.writeObject(b);
            }
            oos.writeObject(null);
            oos.close();
            oos=new ObjectOutputStream(openFileOutput("flow.dat",MODE_PRIVATE));
            for (Flow f:flowList){
                oos.writeObject(f);
            }
            oos.writeObject(null);
            Log.i(TAg,"success");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAg,"error");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAg, "error2");
        }finally {
            if (oos!=null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }if (out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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
