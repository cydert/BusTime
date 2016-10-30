package com.saidat.bustime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kurokuro on 2015/07/06.
 */
public class goto_tokuyamaeki_01 extends Activity {
    static String read;
    static boolean flag = true;
    static int x;
    static String list[];
    static Boolean massages = true;
    static String titles = "";
    static String file_name = "";
    static boolean tmpflag = false;
    static String lastStation[];
    static String tmps = "";
    static boolean weekSa = false;
    static boolean weekSu = false;
    static boolean weekSaSu = false;
    static String path;
    static int timeNow[] = new int[8];
    static String[] OldList;
    private float downX = 0,upX = 0;//スワイプ時
    public float limit= 500;//スワイプ感度

    //csvファイル名を転送してもらう（SearchList001より
    //0の場合送信（ファイル名)
    static String readName(String tmp) {
        if(!(tmp.equals("0")))
            read = tmp;

        return read;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        // ディスプレイのインスタンス生成
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(goto_tokuyamaeki_01.this);
        int setSwipe = Integer.parseInt(sharedPreferences.getString("setSwipe", "2"));//感度
        switch (setSwipe){
            case 1:
                limit = size.x/2;
                break;
            case 2:
                limit = size.x/3;
                break;
            case 3:
                limit = size.x/4;
                break;
        }


        lastStation = new String[300];
        x = 1;
        list = new String[100];
        list[0] = "test";
        //レイアウト表示
        setContentView(R.layout.goto_bus_station);
        //アンダーボタン非表示
        Button btNext = (Button) findViewById(R.id.btNext);
        Button btBack = (Button) findViewById(R.id.btBack);
        Button btLastStation = (Button) findViewById(R.id.btLastStation);
        Button btLister = (Button) findViewById(R.id.btLister);
        LinearLayout lStationLister = (LinearLayout)findViewById(R.id.lStationLister);
        lStationLister.setVisibility(View.GONE);

        String tmp;
        int tmpList1, tmpList2, i;

        String monthNow[] = new String[8];
        String weekNow;
        i = 1;
        int j =0;
        setCal();//時間取得
        FileInputStream input;

        FileInputStream fl = null;
        BufferedReader br = null;
        String text = "";
        try {
            try {

                //ファイル名取得
                SearchList001 sl01 = new SearchList001();
                file_name = sl01.BusCompany("0");
                path ="/data/data/"+ this.getPackageName() +"/files/" + file_name  + "/BusTime/" + read;


                File file1 = new File(path + "_1.csv" );//土曜専用ファイルの存在チェック
                weekSa = file1.exists(); //本来false
                File file2 = new File(path + "_2.csv");//日曜
                weekSu = file2.exists();
                File file3 = new File(path + "_3.csv");//土、日両方
                weekSaSu = file3.exists();

                if(timeNow[7] == 7 && weekSa)   read += "_1";
                else if(timeNow[7] == 1 && weekSu)   read += "_2";
                else if((timeNow[7] == 7 || timeNow[7] == 1) && weekSaSu) read += "_3";

                fl = new FileInputStream(getFilesDir() + "/"+file_name + "/BusTime/" + read + ".csv");
                br = new BufferedReader(new InputStreamReader(fl, "Unicode"));
                // １行ずつ読み込み、改行を付加する
                String str;
                while ((str = br.readLine()) != null) {
                    String tmpList[] = str.split(",");

                    if(tmpList[0].equals("//")){    //特殊運行読み込み
                        int tmp2 = tmpList.length;
                        String stInfo = "";
                        for(int k=1;k < tmp2;k++){
                            if(k != 1)  stInfo += "\n";
                            stInfo += tmpList[k];
                        }
                        TextView txInfo = (TextView) findViewById(R.id.txInfo);
                        txInfo.setText(stInfo);
                        break;
                    }

                    tmpList1 = Integer.parseInt(tmpList[0]);
                    tmpList2 = Integer.parseInt(tmpList[1]);
                    titles = tmpList[4];

                    //対象の時刻表読みだし
                    if (tmpList1 == timeNow[1]) {
                        if (tmpList2 >= timeNow[2]) {
                            list[i] = str;
                            i++;
                            lastStation[j] = tmpList[3];
                            j++;
                        }
                    } else if (tmpList1 > timeNow[1]) {
                        list[i] = str;
                        i++;
                        lastStation[j] = tmpList[3];
                        j++;
                    }
                    if(i == 1)  tmpflag = true;
                }
            } finally {
                if (fl != null) fl.close();
                if (br != null) br.close();
            }
        } catch (Exception e) {
            // エラー発生時の処理
        }
        OldList = list;
        TextView Busname = (TextView) findViewById(R.id.BusName01);
        Busname.setText(titles);
        busTimeList btl = new busTimeList();
        btl.StationName(titles);
        SetBusTime();

        TextView txWeek = (TextView) findViewById(R.id.txWeek01);
        if(timeNow[7] == 1) txWeek.setText("日");
        else if(timeNow[7] == 7)txWeek.setText("土");
        else txWeek.setText("平日");
        btl.week(timeNow[7]);



                //進む
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list[x] == null) {
                    if (massages == true)
                        Toast.makeText(getApplicationContext(), "この時間以降はありません", Toast.LENGTH_SHORT).show();
                    massages = false;
                } else {
                    massages = true;
                    SetBusTime();
                }
            }
        });

        //戻る
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (x <= 3) {
                    if (massages == true)
                        Toast.makeText(getApplicationContext(), "この時間より前にはない、もしくは時刻が過ぎています", Toast.LENGTH_LONG).show();
                    massages = false;
                } else {
                    massages = true;
                    x -= 4;
                    SetBusTime();
                }
            }
        });

        //スワイプするかどうか
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean chkBox = sharedPreferences.getBoolean("swipe",false);
        if(chkBox) {
            /*      スワイプ移動      */
            View view = findViewById(R.id.allView);
            ScrollView scroll1 = (ScrollView) findViewById(R.id.ScrollView1);
            ScrollView scroll2 = (ScrollView) findViewById(R.id.ScrollView2);
            ScrollView scroll3 = (ScrollView) findViewById(R.id.ScrollView3);
            HorizontalScrollView hlScroll1 = (HorizontalScrollView) findViewById(R.id.hlScrollView1);
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return swipe(v, event);
                }
            });
            scroll1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    swipe(v, event);
                    return false;
                }
            });
            scroll2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    swipe(v, event);
                    return false;
                }
            });
            scroll3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    swipe(v, event);
                    return false;
                }
            });
            hlScroll1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    swipe(v, event);
                    return false;
                }
            });
                    /*    スワイプ移動        */

        }
        flag = true;




        //切り替えボタン
        Button btChange = (Button) findViewById(R.id.btChange);
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout backNext = (LinearLayout) findViewById(R.id.backNext);
                LinearLayout lStationLister = (LinearLayout) findViewById(R.id.lStationLister);
                Button btLastStation = (Button) findViewById(R.id.btLastStation);
                Button btLister = (Button) findViewById(R.id.btLister);
                if (flag == true) {
                    flag = false;//前後ボタン削除
                    backNext.setVisibility(View.GONE);
                    lStationLister.setVisibility(View.VISIBLE);
                } else {
                    flag = true;//前後ボタン表示
                    backNext.setVisibility(View.VISIBLE);
                    lStationLister.setVisibility(View.GONE);
                }
            }
        });

        tmps = "";

        //フィルター一覧作成中↓
        for (int k =0;!(lastStation[k] == null);k++) {
            if(k >= 1) tmps += ",";
            tmps += lastStation[k];
        }

       // Busname.setText(tmps);
        btLastStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String items[]  = tmps.split(",");
                ArrayList<String> tmps = new ArrayList<String>();
                for(int m=0; m < items.length; m++){          //重複してないなら代入
                    boolean tmpFlag = false;
                    if(tmps.size() == 0)    tmps.add(items[m]);
                    else {
                        for (int l = 0; l < tmps.size(); l++) {
                            if (items[m].equals(tmps.get(l))) {
                                tmpFlag = true;
                                break;
                            }
                        }
                        if(tmpFlag == false){
                            tmps.add(items[m]);
                        }
                    }
                }

                if(tmps.size() != 0) {
                    tmps.add("選択解除");
                    final String[] finalItem = (String[])tmps.toArray(new String[0]);
                    new AlertDialog.Builder(goto_tokuyamaeki_01.this)
                            .setTitle("終点駅を指定してください")
                            .setItems(finalItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int number) {
                                    //押した時の処理
                                    if (finalItem.length - 1 == number) {
                                        fillter("0");
                                        x = 1;
                                        SetBusTime();
                                    } else {
                                        fillter(finalItem[number]);
                                        x = 1;
                                        SetBusTime();
                                    }
                                }
                            })
                            .show();
                }

            }
        });

        btLister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(goto_tokuyamaeki_01.this,busTimeList.class);
                startActivity(intent);
            }
        });

    }
    boolean swipe(View v, MotionEvent event){
        switch (event.getAction()){
            case    MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                if(limit + downX < upX){
                    //戻る
                    if (x <= 3) {
                        if (massages == true)
                            Toast.makeText(getApplicationContext(), "この時間より前にはない、もしくは時刻が過ぎています", Toast.LENGTH_LONG).show();
                        massages = false;
                    } else {
                        massages = true;
                        x -= 4;
                        SetBusTime();
                    }
                }else if( upX+limit < downX){
                    //進む
                    if (list[x] == null) {
                        if (massages == true)
                            Toast.makeText(getApplicationContext(), "この時間以降はありません", Toast.LENGTH_SHORT).show();
                        massages = false;
                    } else {
                        massages = true;
                        SetBusTime();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                upX = event.getX();
                if(downX + limit < upX){
                    if (x <= 3) {
                        if (massages == true)
                            Toast.makeText(getApplicationContext(), "この時間より前にはない、もしくは時刻が過ぎています", Toast.LENGTH_LONG).show();
                        massages = false;
                    } else {
                        massages = true;
                        x -= 4;
                        SetBusTime();
                    }
                }else if(upX+limit < downX ){
                    if (list[x] == null) {
                        if (massages == true)
                            Toast.makeText(getApplicationContext(), "この時間以降はありません", Toast.LENGTH_SHORT).show();
                        massages = false;
                    } else {
                        massages = true;
                        SetBusTime();
                    }
                }
                break;
        }
        return true;
    }

    void setCal(){
        Calendar cal = Calendar.getInstance();
        timeNow[1] = cal.get(Calendar.HOUR_OF_DAY);
        timeNow[2] = cal.get(Calendar.MINUTE);
        timeNow[3] = cal.get(Calendar.SECOND);
        timeNow[4] = cal.get(Calendar.YEAR);
        timeNow[5] = cal.get(Calendar.MONTH);
        timeNow[6] = cal.get(Calendar.DATE);
        timeNow[7] = cal.get(Calendar.DAY_OF_WEEK);
    }

/*
    //土曜、日曜専用ファイルが存在するか:存在すればtrue------------
    public void Weeks_Sa(boolean tmp){
        weekSa = tmp;
    }
    public void Weeks_Su(boolean tmp){
        weekSu = tmp;
    }
    //-----------------------------------------------
*/
    // ファイルを書き込むメソッド
    public void FileWrite(String tmp) {
        String s = "text1";
        try {//
            OutputStream out = openFileOutput(tmp, MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.append(s);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetBusTime(){
        String stTime = ("時");
        String stMin = ("分");
        String stKeiyu = ("経由");
        String stIki = ("行き");
        TextView txBhour = (TextView) findViewById(R.id.Bhour01);
        TextView txBtime = (TextView) findViewById(R.id.Btime01);
        TextView txBhour2 = (TextView) findViewById(R.id.Bhour02);
        TextView txBtime2 = (TextView) findViewById(R.id.Btime02);
        TextView txBikisaki01 = (TextView) findViewById(R.id.Ikisaki01);
        TextView txBikisaki02 = (TextView) findViewById(R.id.Ikisaki02);
        TextView txBkeiyu01 = (TextView) findViewById(R.id.Keiyu01);
        TextView txBkeiyu02 = (TextView) findViewById(R.id.Keiyu02);

        TextView stNEXTBUS01 = (TextView) findViewById(R.id.stNEXTBUS01);
        TextView stNEXTBUS02 = (TextView) findViewById(R.id.stNEXTBUS02);
        TextView stIKISAKI01 = (TextView) findViewById(R.id.stIKISAKI01);
        TextView stIKISAKI02 = (TextView) findViewById(R.id.stIKISAKI02);
        TextView stKEIYU01 = (TextView) findViewById(R.id.stKEIYU01);
        TextView stKEIYU02 = (TextView) findViewById(R.id.stKEIYU02);
        TextView stMIN01 = (TextView) findViewById(R.id.stMIN01);
        TextView stMIN02 = (TextView) findViewById(R.id.stMIN02);
        TextView stTIME01 = (TextView) findViewById(R.id.stTIME01);
        TextView stTIME02 = (TextView) findViewById(R.id.stTIME02);
        TextView mark2 = (TextView) findViewById(R.id.Mark2);

        for (int a = 1; a <= 2; a++, x++) {
            if (list[x] != null) {
                String outList[] = list[x].split(",",-1);
                switch (a) {
                    case (1):
                        txBhour.setText(outList[0]);
                        txBtime.setText(outList[1]);
                        txBkeiyu01.setText(outList[2]);
                        txBikisaki01.setText(outList[3]);

                        stNEXTBUS01.setText("次のバスは");
                        stTIME01.setText(stTime);
                        stMIN01.setText(stMin);
                        stKEIYU01.setText(stKeiyu);
                        stIKISAKI01.setText(stIki);
                        TextView mark = (TextView) findViewById(R.id.Mark1);
                        mark.setText(outList[5]);
                        break;
                    case (2):

                        txBhour2.setText(outList[0]);
                        txBtime2.setText(outList[1]);
                        txBkeiyu02.setText(outList[2]);
                        txBikisaki02.setText(outList[3]);

                        stNEXTBUS02.setText("その次のバスは");
                        stTIME02.setText(stTime);
                        stMIN02.setText(stMin);
                        stKEIYU02.setText(stKeiyu);
                        stIKISAKI02.setText(stIki);
                        mark2.setText(outList[5]);
                        break;
                }
            }else{
                mark2.setText(null);
                txBhour2.setText(null);
                txBtime2.setText(null);
                txBkeiyu02.setText(null);
                txBikisaki02.setText(null);

                stNEXTBUS02.setText(null);
                stTIME02.setText(null);
                stMIN02.setText(null);
                stKEIYU02.setText(null);
                stIKISAKI02.setText(null);
            }
        }
    }
    public void fillter(String tmp){
        TextView txFill = (TextView) findViewById(R.id.txFill);
        if(tmp.equals("0")){
            list = OldList;
            txFill.setText(null);
        }else {
            try {
                txFill.setText(tmp+"を指定中");
                txFill.setTextColor(Color.parseColor("#2F4F4F"));
                txFill.setBackgroundColor(Color.parseColor("#FFFFFF"));
                String tmpLists[] = new String[100];
                int j = 0;
                int tmpList1, tmpList2, i;
                i = 1;
                String str;
                FileInputStream fl = new FileInputStream("/data/data/" + this.getPackageName() + "/files/" + file_name + "/BusTime/" + read + ".csv");
                BufferedReader br = new BufferedReader(new InputStreamReader(fl, "Unicode"));

                while ((str = br.readLine()) != null) {
                    String tmpList[] = str.split(",");

                    if(tmpList[0].equals("//")) break;
                    tmpList1 = Integer.parseInt(tmpList[0]);
                    tmpList2 = Integer.parseInt(tmpList[1]);

                    //対象の時刻表読みだし
                    if (tmpList1 == timeNow[1] && tmpList[3].equals(tmp)) {
                        if (tmpList2 >= timeNow[2]) {
                            tmpLists[i] = str;
                            i++;
                            lastStation[j] = tmpList[3];
                            j++;
                        }
                    } else if (tmpList1 > timeNow[1] && tmpList[3].equals(tmp)) {
                        tmpLists[i] = str;
                        i++;
                        lastStation[j] = tmpList[3];
                        j++;
                    }
                    if (i == 1) tmpflag = true;
                }
                list = tmpLists;
            } catch (IOException e) {
            }
        }

    }
 }

