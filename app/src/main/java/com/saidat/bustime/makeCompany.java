package com.saidat.bustime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by kurokuro on 2016/02/08.
 */
public class makeCompany extends AppCompatActivity {
    private static final int MENU_ID_A = 1,MENU_ID_B = 2,MENU_ID_C = 3,Menu_ID_D=4;


    static int listJp = 0;  //A~Wのどれか
    static String listJpSt[] = {"あ行", "か行", "さ行", "た行", "な行", "は行", "ま行", "や行", "ら行", "わ行"};
    boolean readMode = false;//ファイルを開いているか
    int pageCnt = 0;
    String allTmpSt[] = new String[2];  //駅名、注意事項
    String tmpStAr[][] = new String[100][5];    //その他、時間など
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.saidat.bustime.R.layout.make_company);
        //setTitle("新規ファイル");
        Toolbar toolbar = (Toolbar) findViewById(com.saidat.bustime.R.id.toolbarEdCom);
        setSupportActionBar(toolbar);

        //toolbar.setTitleTextColor(Color.parseColor("white"));
        toolbar.setTitle("新規ファイル");


        //toolbar.inflateMenu(R.menu.menu_main);


        listJp = 0;
        Button nextBt = (Button) findViewById(com.saidat.bustime.R.id.nextBtMc);
        Button backBt = (Button) findViewById(com.saidat.bustime.R.id.backBtMc);
        Button listBt = (Button) findViewById(com.saidat.bustime.R.id.listJPBt);
        final Button saveBt = (Button) findViewById(R.id.saveBrMc);

        Button allDelBt = (Button) findViewById(com.saidat.bustime.R.id.allDel);


        allDelBt.setOnClickListener(new View.OnClickListener() {    //全データ削除
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(makeCompany.this)
                        .setTitle("本当に削除しますか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(getFilesDir()+"/makeCompany01");
                                data.delFile(file);
                                file.mkdirs();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });



        //ファイル読み込み
        Button readBt = (Button) findViewById(com.saidat.bustime.R.id.readBt);
        readBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    final String stList[] = {"A", "K", "S", "T", "N", "H", "M", "Y", "R", "W"};
                    String itemTmp="";  final int listCnt[] = {0,0,0,0,0,0,0,0,0,0};//その行からいくつ読み込んだか(10あり
                    for(int i=0; i<10; i++) {
                        File file = new File(getFilesDir() + "/makeCompany01/Name_list/" + stList[i] + ".txt");
                        if(!file.createNewFile()) {//既にあったら読み込み開始
                            FileInputStream fs = new FileInputStream(file);
                            BufferedReader bfr = new BufferedReader(new InputStreamReader(fs, "Unicode"));
                            String tmp = "";
                            while ((tmp = bfr.readLine()) != null) {
                                itemTmp += tmp.split(",")[1] + ",";
                                listCnt[i]++;
                            }
                            bfr.close();
                        }
                    }

                    final String[] item = itemTmp.split(",");
                    new AlertDialog.Builder(makeCompany.this)
                            .setTitle("選択してください")
                            .setItems(item, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pageCnt = 0;//ページ数リセット
                                    clearEd();//画面
                                    for(int i=0,tmp=0;i<10;i++){
                                        tmp +=listCnt[i];
                                        if(tmp > which){
                                            listJp = i; //何行目か
                                            break;
                                        }
                                    }

                                    //ファイル読み取り
                                    File file = new File(getFilesDir() + "/makeCompany01/BusTime/" + item[which] + ".csv");
                                    Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbarEdCom);
                                    toolbar2.setTitle(""+item[which]);
                                    try {
                                        BufferedReader bfr2 = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Unicode"));
                                        String tmp;
                                        String tmpAr[];
                                        int i = 0;
                                        while ((tmp = bfr2.readLine()) != null) {

                                            tmpAr = tmp.split(",", -1);
                                            if (tmpAr[0].equals("//"))
                                                allTmpSt[1] = tmpAr[1];
                                            else {
                                                if (i == 0)
                                                    allTmpSt[0] = tmpAr[4];
                                                tmpStAr[i][2] = tmpAr[0];
                                                tmpStAr[i][3] = tmpAr[1];
                                                tmpStAr[i][0] = tmpAr[2];
                                                tmpStAr[i][1] = tmpAr[3];
                                                if (tmpAr.length > 5)
                                                    tmpStAr[i][4] = tmpAr[5];
                                                i++;
                                            }
                                        }

                                    } catch (FileNotFoundException e) {
                                        Toast.makeText(makeCompany.this, "ファイルパスエラー(お問い合わせください)", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    setData();  //画面セット
                                    ((EditText)findViewById(com.saidat.bustime.R.id.stationEdMc)).setText(allTmpSt[0]);
                                    ((EditText)findViewById(com.saidat.bustime.R.id.attantionEdMc)).setText(allTmpSt[1]);

                                    readMode = true;

                                    ((Button)findViewById(com.saidat.bustime.R.id.readBt)).setText("閉じる");//開くボタン→閉じる
                                    //(findViewById(com.saidat.bustime.R.id.stationEdMc)).setFocusable(false);//編集不可
                                }
                            }).show();
                }catch (IOException e){
                    e.printStackTrace();

                }
            }
        });

        listBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(makeCompany.this)
                        .setTitle("選択してください")
                        .setItems(listJpSt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((TextView)findViewById(com.saidat.bustime.R.id.listJPTx)).setText(listJpSt[which]);
                                listJp = which;
                            }
                        })
                        .show();
            }
        });



        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageCnt <100) {
                    saveData();
                    clearEd();
                    pageCnt++;
                    setData();
                }
            }
        });

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageCnt != 0) {
                    saveData();
                    pageCnt--;
                    clearEd();
                    setData();
                }
            }
        });

        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                if(allTmpSt[0].equals("")){
                    Toast.makeText(makeCompany.this, "停留所名が未入力です", Toast.LENGTH_SHORT).show();
                }else {
                    String stList[] = {"A", "K", "S", "T", "N", "H", "M", "Y", "R", "W"};
                    //本来はダイアログでA~Wのどこかを決めさせる
                    File file = new File(getFilesDir() + "/makeCompany01/Name_list/");
                    file.mkdirs();
                    file = new File(getFilesDir() + "/makeCompany01/BusTime/");
                    file.mkdirs();


                    try {
                        file = new File(getFilesDir() + "/makeCompany01/Name_list/" + stList[listJp] + ".txt");
                        file.createNewFile();
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(file),"Unicode"));
                        String tmp; boolean flag = false;
                        while((tmp = bfr.readLine()) != null){
                            if(allTmpSt[0].equals(tmp.split(",")[1])){
                                flag = true;
                                break;
                            }
                        }
                        if(flag && (!readMode)) {
                            Toast.makeText(makeCompany.this, "同名のファイルが存在しています", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "Unicode"));
                        bw.write(allTmpSt[0] + "," + allTmpSt[0] + ",");
                        bw.newLine();
                        bw.flush();
                        bw.close();

                        file = new File(getFilesDir() + "/makeCompany01/BusTime/" + allTmpSt[0] + ".csv");
                        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "Unicode"));
                        for (int i = 0; i <= pageCnt; i++) {
                            bw.write(tmpStAr[i][2] + "," + tmpStAr[i][3] + "," + tmpStAr[i][0] + "," + tmpStAr[i][1] + "," + allTmpSt[0] + "," + tmpStAr[i][4]);
                            bw.newLine();
                        }
                        if (!allTmpSt[1].equals("")) {
                            bw.write("//," + allTmpSt[1]);
                            bw.newLine();
                        }
                        bw.flush();
                        bw.close();
                        ((Toolbar)findViewById(R.id.toolbarEdCom)).setTitle(""+allTmpSt[0]);
                        Toast.makeText(makeCompany.this, "保存完了しました", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        toolbar.inflateMenu(R.menu.menu_edit_company);
        //toolbar.setBackgroundColor(Color.parseColor("black"));;
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case (R.id.setting):
                        Intent intent = new Intent(makeCompany.this,MyPreferenceActivity.class);
                        startActivity(intent);
                        break;
                    case (R.id.screenClear):
                        clearEd();
                        break;

                }
                return true;
            }
        });

    }



    void clearEd(){     //editTextを空白に

        ((EditText)findViewById(com.saidat.bustime.R.id.keiyuEdMc)).setText("");
        ((EditText)findViewById(com.saidat.bustime.R.id.mokutekiEdMc)).setText("");
        ((EditText)findViewById(com.saidat.bustime.R.id.fsTimeEdMc)).setText(null);
        ((EditText)findViewById(com.saidat.bustime.R.id.fsTime2EdMc)).setText(null);
        ((EditText)findViewById(com.saidat.bustime.R.id.markEdMc)).setText("");

    }
    void saveData(){
        allTmpSt[0] = ((EditText)(findViewById(com.saidat.bustime.R.id.stationEdMc))).getText().toString();    //駅名
        allTmpSt[1] = ((EditText)findViewById(com.saidat.bustime.R.id.attantionEdMc)).getText().toString();    //注意書き
        tmpStAr[pageCnt][0] =  ((EditText)findViewById(com.saidat.bustime.R.id.keiyuEdMc)).getText().toString();
        tmpStAr[pageCnt][1] =  ((EditText)findViewById(com.saidat.bustime.R.id.mokutekiEdMc)).getText().toString();
        tmpStAr[pageCnt][2] =  ((EditText)findViewById(com.saidat.bustime.R.id.fsTimeEdMc)).getText().toString();
        tmpStAr[pageCnt][3] =  ((EditText)findViewById(com.saidat.bustime.R.id.fsTime2EdMc)).getText().toString();
        tmpStAr[pageCnt][4] =  ((EditText)findViewById(com.saidat.bustime.R.id.markEdMc)).getText().toString();
    }
    void setData(){
        if(tmpStAr[pageCnt][0] != null)
            ((EditText) findViewById(com.saidat.bustime.R.id.keiyuEdMc)).setText(tmpStAr[pageCnt][0]);
        if(tmpStAr[pageCnt][1] != null)
            ((EditText) findViewById(com.saidat.bustime.R.id.mokutekiEdMc)).setText(tmpStAr[pageCnt][1]);
        if(tmpStAr[pageCnt][2] != null) {
            if (tmpStAr[pageCnt][2].equals(""))
                ((EditText) findViewById(com.saidat.bustime.R.id.fsTimeEdMc)).setText(null);
            else
                ((EditText) findViewById(com.saidat.bustime.R.id.fsTimeEdMc)).setText(tmpStAr[pageCnt][2]);
        }
        if(tmpStAr[pageCnt][3] != null) {
            if (tmpStAr[pageCnt][3].equals(""))
                ((EditText) findViewById(com.saidat.bustime.R.id.fsTime2EdMc)).setText(null);
            else
                ((EditText) findViewById(com.saidat.bustime.R.id.fsTime2EdMc)).setText(tmpStAr[pageCnt][3]);
        }
        if(tmpStAr[pageCnt][4] != null)
            ((EditText) findViewById(com.saidat.bustime.R.id.markEdMc)).setText(tmpStAr[pageCnt][4]);

        ((TextView) findViewById(com.saidat.bustime.R.id.page)).setText(pageCnt + 1 + "");//ページ数
        ((TextView)findViewById(com.saidat.bustime.R.id.listJPTx)).setText(listJpSt[listJp]);//行(あ~


    }

    boolean setDialog2cnt(String title,String okMassage,String noMassage){
        final boolean[] tmp = new boolean[1];
        new AlertDialog.Builder(makeCompany.this)
                .setTitle(title)
                .setPositiveButton(okMassage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp[0] = true;
                    }
                })
                .setNegativeButton(noMassage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tmp[0] = false;
                    }
                })
                .show();
        return tmp[0];
    }

    void chengeMode(){
        (findViewById(com.saidat.bustime.R.id.stationEdMc)).setFocusable(false);//編集不可
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_company, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // いつものUPナビゲーションの処理
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
