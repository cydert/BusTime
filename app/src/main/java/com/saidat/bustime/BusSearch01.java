package com.saidat.bustime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kurokuro on 2015/07/07.
 */

public class BusSearch01 extends Activity {
    static Button btBusCompany[] = new Button[50];
    static String tmp;
    static String tmpCompany[] = new String[50];
    static int j = 0;
    static String tmps0;
    static String page2;
    static int iMv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_search);
        //bus_company = "Boucho";

        try {
            try {
                String fileName = "";
                // assetsフォルダ内の list.txt(バス会社名) をオープンする
                //openFileInputはパス指定不可
                for (int i = 0; i < 10; i++) {
                    String stList[] = {"A", "K", "S", "T", "N", "H", "M", "Y", "R", "W"};
                    FileInputStream fileIN = new FileInputStream("/data/data/" + this.getPackageName() + "/files/BusCompany/" + stList[i] + ".txt");
                    BufferedReader br = new BufferedReader(new InputStreamReader(fileIN, "Unicode"));

                    int viewId = getResources().getIdentifier("ll" + stList[i], "id", getPackageName());
                    String[] tmpId = new String[50];
                    //list.txtにある数だけボタン作成
                    while ((tmp = br.readLine()) != null) {
                        String bus_company[] = tmp.split(",");
                        btBusCompany[j] = new Button(this);
                        btBusCompany[j].setText(bus_company[0]);
                        btBusCompany[j].setTextSize(25);
                        LinearLayout linear = (LinearLayout) findViewById(viewId);
                        linear.addView(btBusCompany[j]);


                        //会社名(ファイル名)代入後同じ数値のタグ
                        tmpCompany[j] = bus_company[1];
                        btBusCompany[j].setTag(j);

                        btBusCompany[j].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int tab = Integer.parseInt(v.getTag().toString());

                                String stTmp;
                                //会社名(stTmp)は番号(tmp)に対応
                                for (int i = 0; ; i++) {
                                    if (i == tab) {
                                        //tmpCompanyはファイル名
                                        stTmp = tmpCompany[i];
                                        break;
                                    }
                                }

                                //会社（ファイル名)を別クラスに送信
                                SearchList001 sl001 = new SearchList001();
                                goto_tokuyamaeki_01 gt01 = new goto_tokuyamaeki_01();
                                sl001.BusCompany(stTmp);
                                Intent intent = new Intent(BusSearch01.this, SearchList001.class);
                                startActivity(intent);
                            }
                        });
                        j++;
                    }
                }

            } catch (IOException e) {
            }
        } catch (IOError e) {
        }

        String makeCompanyBtName = "その他";
        int viewId = getResources().getIdentifier("llW", "id", getPackageName());
        btBusCompany[j] = new Button(this);
        btBusCompany[j].setText(makeCompanyBtName);
        btBusCompany[j].setTextSize(25);
        LinearLayout linear = (LinearLayout) findViewById(viewId);
        linear.addView(btBusCompany[j]);
        btBusCompany[j].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int tab = Integer.parseInt(v.getTag().toString());
                //会社（ファイル名)を別クラスに送信
                SearchList001 sl001 = new SearchList001();
                sl001.BusCompany("makeCompany01"); //会社名(ただし自作なので...
                Intent intent = new Intent(BusSearch01.this, SearchList001.class);
                startActivity(intent);
            }
        });

        TextView txA = (TextView) findViewById(R.id.txA);
        TextView txK = (TextView) findViewById(R.id.txK);
        TextView txS = (TextView) findViewById(R.id.txS);
        TextView txT = (TextView) findViewById(R.id.txT);
        TextView txN = (TextView) findViewById(R.id.txN);
        TextView txH = (TextView) findViewById(R.id.txH);
        TextView txM = (TextView) findViewById(R.id.txM);
        TextView txR = (TextView) findViewById(R.id.txR);
        TextView txY = (TextView) findViewById(R.id.txY);
        TextView txW = (TextView) findViewById(R.id.txW);


//------------------ショートカットをおしたとき   （１～４まであり）
        Button myList;
        myList = (Button) findViewById(R.id.myFavorite01);
        myList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyList(1);
            }
        });

        myList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences pref = getSharedPreferences("favorite", MODE_PRIVATE);
                String page = pref.getString("my01_busName", null);
                String tmp[];
                if (page == null)
                    Toast.makeText(BusSearch01.this, "設定されていません", Toast.LENGTH_SHORT).show();
                else {
                    tmp = page.split(",", -1);
                    Toast.makeText(BusSearch01.this, "" + tmp[tmp.length-1], Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });

        myList = (Button) findViewById(R.id.myFavorite02);
        myList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyList(2);
            }
        });
        myList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences pref = getSharedPreferences("favorite", MODE_PRIVATE);
                String page = pref.getString("my02_busName", null);
                String tmp[];
                if (page == null)
                    Toast.makeText(BusSearch01.this, "設定されていません", Toast.LENGTH_SHORT).show();
                else {
                    tmp = page.split(",", -1);
                    Toast.makeText(BusSearch01.this, "" + tmp[tmp.length-1], Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });


        myList = (Button) findViewById(R.id.myFavorite03);
        myList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyList(3);
            }
        });
        myList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences pref = getSharedPreferences("favorite", MODE_PRIVATE);
                String page = pref.getString("my03_busName", null);
                String tmp[];
                if (page == null)
                    Toast.makeText(BusSearch01.this, "設定されていません", Toast.LENGTH_SHORT).show();
                else {
                    tmp = page.split(",", -1);
                    Toast.makeText(BusSearch01.this, "" + tmp[tmp.length-1], Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });


        myList = (Button) findViewById(R.id.myFavorite04);
        myList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyList(4);
            }
        });
        myList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences pref = getSharedPreferences("favorite", MODE_PRIVATE);
                String page = pref.getString("my04_busName", null);
                String tmp[];
                if (page == null)
                    Toast.makeText(BusSearch01.this, "設定されていません", Toast.LENGTH_SHORT).show();
                else {
                    tmp = page.split(",", -1);
                    Toast.makeText(BusSearch01.this, "" + tmp[tmp.length-1], Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });

        final ScrollView sv = (ScrollView) findViewById(R.id.scv);
        Button btMv = (Button) findViewById(R.id.btMv);
        Button btUp = (Button) findViewById(R.id.btUp);
        Button btDown = (Button) findViewById(R.id.btDown);

        btMv.setOnClickListener(new View.OnClickListener() {
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
        btUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.smoothScrollBy(0, -2000);
            }
        });
        btDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.smoothScrollBy(0, 2000);
            }
        });


    }



    public void MyList(final int tmp) {    //tmpは番号

        SharedPreferences pref = getSharedPreferences("favorite", MODE_PRIVATE);
        String page = pref.getString("my0" + tmp + "_busName", null);
        page2 = pref.getString("my0" + tmp + "_company", null);
        if (page == null) {
            Toast.makeText(this, "設定されていません", Toast.LENGTH_SHORT).show();
        } else {
            String tmps[] = page.split(",", -1); //tmps[0]はファイル名、[1]は乗り場の数
            if (tmps[1].equals("")) {
                goto_tokuyamaeki_01 gt01 = new goto_tokuyamaeki_01();
                gt01.readName(tmps[0]);
                SearchList001 sl01 = new SearchList001();
                sl01.BusCompany(page2);
                busTimeList btl = new busTimeList();
                btl.OldRead(tmps[0]);
                Intent intent = new Intent(BusSearch01.this, goto_tokuyamaeki_01.class);
                startActivity(intent);
            } else {
                //tmp.length - 2  //これが乗り場数
                String items[] = new String[tmps.length - 2];
                for (int k = 0; k < tmps.length - 2; k++) {
                    items[k] = tmps[k + 1];
                }

                tmps0 = tmps[0];
                new AlertDialog.Builder(BusSearch01.this)
                        .setTitle("行先が複数ありますので指定してください")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int number) {
                                //押した時の処理

                                goto_tokuyamaeki_01 gt01 = new goto_tokuyamaeki_01();
                                gt01.readName(("" + (1 + number)) + "_" + tmps0);
                                SearchList001 sl01 = new SearchList001();
                                sl01.BusCompany(page2);
                                busTimeList btl = new busTimeList();
                                btl.OldRead(("" + (1 + number)) + "_" + tmps0);
                                Intent intent = new Intent(BusSearch01.this, goto_tokuyamaeki_01.class);
                                startActivity(intent);
                            }
                        })
                        .show();

            }
        }
    }
}
