package com.saidat.bustime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kurokuro on 2015/07/07.
 */
public class SearchList001 extends Activity {
    static String busCompany = "";
    static String[] tmp2 = new String[10];
    static int flag, j;
    static String[] stTmp;
    static String[] tmp;
    static int iMv;
    int[] locationA = new int[2];
    int[] locationK = new int[2];
    int[] locationS = new int[2];
    int[] locationT = new int[2];
    int[] locationN = new int[2];
    int[] locationH = new int[2];
    int[] locationM = new int[2];
    int[] locationR = new int[2];
    int[] locationY = new int[2];
    int[] locationW = new int[2];
    boolean loca;
    //locationA～W[] 宣言
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_list001);
        String[] stList01 = new String[100];

        final String item[] = {"01", "02", "03", "04"};
        SharedPreferences perf = getSharedPreferences("favorite", MODE_PRIVATE);
        SharedPreferences.Editor edit = perf.edit();

        if(busCompany.equals("makeCompany")) {//自作なら
            //itemDi = new String[6];
            //itemDi[0] = "設定"; itemDi[1] = "データ管理"; itemDi[2]="マイリスト一括削除"; itemDi[3]="公式HP接続"; itemDi[4]="データに関するお問い合わせ"; itemDi[5]="データ作成・編集";
            File file = new File(getFilesDir() + "/makeCompany01/");
            if(!file.exists()) {
                    file.mkdirs();
            }
        }

        try {
            try {   //ボタン作成
                Button btList;
                final String stList[] = {"A", "K", "S", "T", "N", "H", "M", "Y", "R", "W"};
                final String stListJP[] = {"あ行", "か行", "さ行", "た行", "な行", "は行", "ま行", "や行", "ら行", "わ行"};
                for (int i = 0; i < 10; i++) {

                    int viewId = getResources().getIdentifier("ll" + stList[i], "id", getPackageName());
                    LinearLayout linear = (LinearLayout) findViewById(viewId);
                    //InputStream isNameList = this.getAssets().open(busCompany + "/Name_list/" + stList + ".txt");
                    FileInputStream fl = new FileInputStream("/data/data/" + this.getPackageName() + "/files/" + busCompany + "/Name_list/" + stList[i] + ".txt");
                    BufferedReader brNameList = new BufferedReader(new InputStreamReader(fl, "Unicode"));
                    String stTmp1;
                    j = 0;
                    while ((stTmp1 = brNameList.readLine()) != null) {
                        stTmp = stTmp1.split(",");
                        btList = new Button(this);

                        btList.setText(stTmp[0]);
                        btList.setTextSize(25);
                        linear.addView(btList);
                        btList.setTag(stTmp1);

                        //乗り場が複数ならリスト表示、1つなら直intent
                        btList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String arrayindex = v.getTag().toString();
                                tmp = null;
                                tmp = arrayindex.split(",", -1);
                                if (!(tmp[2].equals(""))) {
                                    //tmp.length - 2  //これが乗り場数
                                    String items[] = new String[tmp.length - 2];
                                    for (int k = 0; k < tmp.length - 2; k++) {
                                        items[k] = tmp[k + 2];
                                    }
                                    new AlertDialog.Builder(SearchList001.this)
                                            .setTitle("行先が複数ありますので指定してください")
                                            .setItems(items, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int number) {
                                                    //押した時の処理
                                                    goto_tokuyamaeki_01 gt01 = new goto_tokuyamaeki_01();
                                                    busTimeList btl = new busTimeList();
                                                    tmp[1] = (1 + number) + "_" + tmp[1];
                                                    gt01.readName(tmp[1]);
                                                    btl.OldRead(tmp[1]);

                                                    Intent it = new Intent(SearchList001.this, goto_tokuyamaeki_01.class);
                                                    startActivity(it);
                                                }
                                            })
                                            .show();
                                } else {
                                    goto_tokuyamaeki_01 gt01 = new goto_tokuyamaeki_01();
                                    busTimeList btl = new busTimeList();
                                    gt01.readName(tmp[1]);
                                    btl.OldRead(tmp[1]);
                                    Intent it = new Intent(SearchList001.this, goto_tokuyamaeki_01.class);
                                    startActivity(it);
                                }
                            }
                        });

                        //移動
                        Button btMove = (Button) findViewById(R.id.move);
                        final ScrollView sv = (ScrollView) findViewById(R.id.scrollView);
                        btMove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (iMv == 0) {
                                    sv.fullScroll(ScrollView.FOCUS_DOWN);
                                    iMv = 1;
                                }
                                if (iMv == 1) {
                                    sv.fullScroll(ScrollView.FOCUS_UP);
                                    iMv = 0;
                                }
                            }
                        });

                        //指定行まで移動
                        findViewById(R.id.changeMoveBt).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(SearchList001.this)
                                        .setTitle("移動先を指定してください")
                                        .setItems(stListJP, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which){
                                                    case 0:
                                                        sv.smoothScrollTo(locationA[0], locationA[1]-220);
                                                        break;
                                                    case 1:
                                                        sv.smoothScrollTo(locationK[0], locationK[1]-220);
                                                        break;
                                                    case 2:
                                                        sv.smoothScrollTo(locationS[0], locationS[1]-220);
                                                        break;
                                                    case 3:
                                                        sv.smoothScrollTo(locationT[0], locationT[1]-220);
                                                        break;
                                                    case 4:
                                                        sv.smoothScrollTo(locationN[0], locationN[1]-220);
                                                        break;
                                                    case 5:
                                                        sv.smoothScrollTo(locationH[0], locationH[1]-220);
                                                        break;
                                                    case 6:
                                                        sv.smoothScrollTo(locationM[0], locationM[1]-220);
                                                        break;
                                                    case 7:
                                                        sv.smoothScrollTo(locationR[0], locationR[1]-220);
                                                        break;
                                                    case 8:
                                                        sv.smoothScrollTo(locationY[0], locationY[1]-220);
                                                        break;
                                                    case 9:
                                                        sv.smoothScrollTo(locationW[0],locationW[1]-220);
                                                        break;
                                                }
                                            }
                                        }).show();
                            }
                        });

                        //長押ししたときの処理-------------------------------------------
                        btList.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(final View view) {
                                new AlertDialog.Builder(SearchList001.this)
                                        .setTitle("マイリストに登録しますか？")
                                        .setItems(item, new DialogInterface.OnClickListener() {
                                            View v = view;

                                            @Override
                                            public void onClick(DialogInterface dialog, int num) {
                                                String arrayindex = v.getTag().toString();
                                                tmp = null;
                                                tmp = arrayindex.split(",", 3);
                                                SharedPreferences perf = getSharedPreferences("favorite", MODE_PRIVATE);
                                                SharedPreferences.Editor edit = perf.edit();
                                                if (!(tmp[2].equals(""))) {
                                                    edit.putString("my0" + (num + 1) + "_busName", tmp[1] + "," + tmp[2] + "," + tmp[0]).commit();   //バス停名
                                                } else {
                                                    edit.putString("my0" + (num + 1) + "_busName", tmp[1] + ",," + tmp[0]).commit();   //バス停名
                                                }
                                                //my0+(num) に駅名保存

                                                edit.putString("my0" + (num + 1) + "_company", busCompany).commit();    //会社名
                                                Toast.makeText(getApplicationContext(), (num + 1) + "に保存しました。", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .show();

                                return false;
                            }
                        });
                        j++;
                    }
                }
                //---------------------------------------
            } catch (IOException e) {
            }
        } catch (IOError e) {
        }

        Button btMenu = (Button) findViewById(R.id.btMenu);
        final String[] itemDi;
        if(busCompany.equals("makeCompany01"))
            itemDi = new String[]{"設定", "データ管理", "マイリスト一括削除", "公式HP接続", "データに関するお問い合わせ","データ編集"};
        else
            itemDi = new String[]{"設定", "データ管理", "マイリスト一括削除", "公式HP接続", "データに関するお問い合わせ"};
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SearchList001.this)
                        .setTitle("Menu")
                        .setItems(itemDi, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(SearchList001.this, MyPreferenceActivity.class);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        Intent it2 = new Intent(SearchList001.this, data.class);
                                        startActivity(it2);
                                        break;
                                    case 2:
                                        SharedPreferences perf = getSharedPreferences("favorite", MODE_PRIVATE);
                                        SharedPreferences.Editor edit = perf.edit();
                                        edit.clear().commit();
                                        Toast.makeText(SearchList001.this, "削除しました。", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        Uri uri = Uri.parse("http://saidat.web.fc2.com/BusTime/BusTime.html");
                                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(it);
                                        break;
                                    case 4:
                                        break;
                                    case 5:
                                        Intent it3 = new Intent(SearchList001.this,makeCompany.class);
                                        startActivity(it3);
                                        break;
                                }
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(!loca) {
            findViewById(R.id.A).getLocationOnScreen(locationA);
            findViewById(R.id.K).getLocationOnScreen(locationK);
            findViewById(R.id.S).getLocationOnScreen(locationS);
            findViewById(R.id.T).getLocationOnScreen(locationT);
            findViewById(R.id.N).getLocationOnScreen(locationN);
            findViewById(R.id.H).getLocationOnScreen(locationH);
            findViewById(R.id.M).getLocationOnScreen(locationM);
            findViewById(R.id.R).getLocationOnScreen(locationR);
            findViewById(R.id.Y).getLocationOnScreen(locationY);
            findViewById(R.id.W).getLocationOnScreen(locationW);
            loca = true;
        }
    }

    public static String BusCompany(String tmp) {
        if (!(tmp.equals("0")))
            busCompany = tmp;
        return busCompany;
    }

}

