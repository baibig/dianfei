package com.pierce.dianfei;

import android.util.Log;

import com.pierce.dianfei.model.Building;
import com.pierce.dianfei.model.Dianfei;
import com.pierce.dianfei.model.Flow;
import com.pierce.dianfei.model.ReturnInfo;
import com.pierce.dianfei.model.Section;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author: pierce
 * Date: 2015/8/17
 */
public class ParseTask {
    private final String TAG="parsetask";
    public ReturnInfo processInfo(ReturnInfo info) {
        Document doc= Jsoup.parse(info.getHtml());
        Element body=doc.body();
        Element viewState=body.getElementById("__VIEWSTATE");
        Element eventValidation=body.getElementById("__EVENTVALIDATION");
        info.setViewstate(viewState.val());
        info.setEventvalidation(eventValidation.val());
        return info;

    }
    public List<Section> getSections(ReturnInfo info){
        Document doc= Jsoup.parse(info.getHtml());
        Element body=doc.body();
        Elements sections = body.select("#programId > option");
        List<Section> list=new ArrayList<Section>();
        for(Element element : sections)
        {

            if (!(element.text().equals("-请选择-"))){
                Section sec=new Section();
                sec.setSection(element.text());
                //powerDB.saveSection(sec);
                list.add(sec);

                //Log.i("section", element.text());
            }

        }
        return list;

    }
    public List<Building> getBuildings(ReturnInfo info){
        Document doc= Jsoup.parse(info.getHtml());
        Element body=doc.body();
        Elements buildings=body.select("#txtyq>option");
        List<Building> list=new ArrayList<Building>();
        for (Element e:buildings){

            if (!(e.text().equals("-请选择-"))){
                Building bld=new Building();
                bld.setBuilding(e.text());
                //bld.setSectionId(information.getId());
                //powerDB.saveBuilding(bld);
                list.add(bld);


                //Log.i("building",e.text());
            }

        }
        return list;
    }
    public List<Flow> getFlows(ReturnInfo info){
        Document doc= Jsoup.parse(info.getHtml());
        Element body=doc.body();
        Elements flows=body.select("#txtld>option");
        List<Flow> list=new ArrayList<Flow>();
        for (Element e:flows){

            if (!(e.text().equals("-请选择-"))){
                Flow fl=new Flow();
                fl.setFlow(e.text());
                //fl.setBuildingId(currentBuilding.getId());
                //Log.i("flow",e.text());
                //powerDB.saveFlow(fl);
                list.add(fl);
            }
        }
        return list;
    }
    public List<Dianfei> getDianfei(ReturnInfo info){
        Document doc= Jsoup.parse(info.getHtml());
        Element body=doc.body();
        /*Elements d=body.getElementsByClass("style1");
        for (Element s:d){
            Log.i(TAG,s.toString());
        }*/
        Element table=body.getElementById("GridView2");

        Elements rows=table.select("tr");
        List<Dianfei> list=new ArrayList<>();
        for (int i=0;i<rows.size();i++){
            Element row=rows.get(i);
            Elements cols=row.select("td");
            if (cols.size()>0){
                Dianfei item=new Dianfei();
                item.setPower(cols.get(0).text());
                item.setTime(cols.get(1).text());
                list.add(item);
                //Log.i("power",checkData.get(i-1).getPower()+"--"+checkData.get(i-1).getTime());
            }
        }
        return list;

    }
}
