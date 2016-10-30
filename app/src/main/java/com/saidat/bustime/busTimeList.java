package com.saidat.bustime;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kurokuro on 2015/07/13.
 */
public class busTimeList extends Activity {
    static int i;
    static TextView[] txList = new TextView[100];
    static int tmpsize1 = 30;
    static int tmpsize2 = 20;
    static String StationName = "バス停";
    static int week,OldWeek;
    static String read;
    static String OldRead;
    static LinearLayout lay01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_time_list);
        lay01 = (LinearLayout) findViewById(R.id.lay01);

        goto_tokuyamaeki_01 go01 = new goto_tokuyamaeki_01();
        read = go01.readName("0");
        BustimeList();

        //初回のみ本来の曜日を表示
        /*final   Button btWeek = (Button) findViewById(R.id.btWeek);
        if(OldWeek == 1)   btWeek.setText("日");
        else if(OldWeek == 7)  btWeek.setText("土");
        else btWeek.setText("平日");
        */
        week = OldWeek;

        TextView tx = (TextView) findViewById(R.id.busStationName);
        tx.setText(StationName);

        SearchList001 sl01 = new SearchList001();
        String file_name = sl01.BusCompany("0");
        String path ="/data/data/"+ this.getPackageName() +"/files/" + file_name  + "/BusTime/" + OldRead;

        final boolean weekSa,weekSu,weekSaSu;
        File file1 = new File(path + "_1.csv" );//土曜専用ファイルの存在チェック
        weekSa = file1.exists(); //本来false
        File file2 = new File(path + "_2.csv");//日曜
        weekSu = file2.exists();
        File file3 = new File(path + "_3.csv");//土、日両方
        weekSaSu = file3.exists();

        final Button btWeek = (Button) findViewById(R.id.btWeek);
        setBtWeek();//ボタン（曜日）の表示
        btWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                read = null;
                lay01.removeAllViews();
                lay01 = (LinearLayout) findViewById(R.id.lay01);
                switch (week){
                    case(1)://日
                        week = 2;//平日に
                        setBtWeek();
                        read = OldRead;

                        BustimeList();
                        break;

                    case(7)://土
                        week = 1;//日曜
                        setBtWeek();
                        if(weekSu)  read = OldRead + "_2";
                        else if(weekSaSu)   read = OldRead + "_3";
                        else read = OldRead;
                        BustimeList();
                        break;
                    default://平日
                        week = 7;//土曜
                        setBtWeek();
                        if(weekSa)  read = OldRead + "_1";
                        else if(weekSaSu)   read = OldRead + "_3";
                        else read = OldRead;
                        BustimeList();
                        break;
                }
            }
        });

    }

    public void StationName(String tmp){
        StationName = tmp;
    }


    public void BustimeList() {
        int j = 0;

        LinearLayout[] linearList = new LinearLayout[200];
        String time1, time2;

        try {

            FileInputStream fl;
            BufferedReader br;

            //ファイル名取得(0でreturnをもらう
            SearchList001 sl01 = new SearchList001();
            String file_name = sl01.BusCompany("0");
            fl = new FileInputStream(getFilesDir() +"/" + file_name + "/BusTime/" + read + ".csv");
            br = new BufferedReader(new InputStreamReader(fl, "Unicode"));
            String str;

            while ((str = br.readLine()) != null) {

                linearList[j] = new LinearLayout(this);
                linearList[j].setOrientation(LinearLayout.VERTICAL);
                if (j % 2 == 0)
                    linearList[j].setBackgroundColor(Color.parseColor("#fff0f8ff"));

                else
                    linearList[j].setBackgroundColor(Color.parseColor("#DDF4DD"));
                lay01.addView(linearList[j]);
                String tmpList[] = str.split(",",-1);
                if(tmpList[0].equals("//")){    //特殊運行読み込み
                    int tmp2 = tmpList.length;
                    String stInfo = "";
                    for(int k=1;k < tmp2;k++){
                        if(k != 1)  stInfo += "\n";
                        stInfo += tmpList[k];
                    }
                    TextView txInfo;
                    txInfo = new TextView(this);
                    txInfo.setTextSize(21);
                    txInfo.setTextColor(Color.parseColor("#4169E1"));
                    txInfo.setText(stInfo);
                    linearList[j].setBackgroundColor(Color.parseColor("#FFDEAD"));

                    linearList[j].addView(txInfo);

                    break;
                }
                time1 = tmpList[0] + "時" + tmpList[1] + "分";
                time2 = tmpList[2] + "経由　\t\t" + tmpList[3] + "行き" +"\t"+tmpList[5];
                txList[i] = new TextView(this);
                txList[i + 1] = new TextView(this);
                txList[i].setText(time1);
                txList[i + 1].setText(time2);
                txList[i].setTextSize(tmpsize1);
                txList[i + 1].setTextSize(tmpsize2);
                for (int k = i; k <= i + 1; k++)
                    txList[k].setTextColor(Color.parseColor("#000000"));
                linearList[j].addView(txList[i]);
                linearList[j].addView(txList[i + 1]);
                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void week(int tmp){
        week = tmp;
        OldWeek = tmp;
    }
    public void setBtWeek(){
        Button btWeek = (Button) findViewById(R.id.btWeek);
        if(week == 1)   btWeek.setText("日");
        else if(week == 7)  btWeek.setText("土");
        else btWeek.setText("平日");
    }
    public static void  OldRead(String tmp){
        OldRead = tmp;
    }
}
