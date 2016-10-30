package com.saidat.bustime;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

//import java.util.logging.Handler;


public class MainActivity extends ActionBarActivity {

    private static final int MENU_ID_A = 0;
    private static final int MENU_ID_B = 1;
    private static final int MENU_ID_C = 2;
    private static final int MENU_ID_D = 3;

    int nowMonth;
    int nowDay;
    int now_Week;
    int nowHour;
    int nowMin;
    int nowSec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView txLoading = (TextView) findViewById(R.id.txLoading);//読み込み中の表示
        txLoading.setVisibility(View.GONE);


        //日付書き込み場所の取得
        TextView txMonth = (TextView) findViewById(R.id.txtMonth);
        TextView txDay = (TextView) findViewById(R.id.txtDay);
        TextView txWeek = (TextView) findViewById(R.id.txtWeek);
        TextView txHour = (TextView) findViewById(R.id.txthour);
        TextView txMin = (TextView) findViewById(R.id.txtmin);
        TextView txSec = (TextView) findViewById(R.id.txtsec);
        Button reBtn = (Button) findViewById(R.id.rere);
        Button btnList001 = (Button) findViewById(R.id.btnBus);

        //現在時刻取得
        String[] nowWeek = {"曜日不明(err", "日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"};

        Calendar cal = Calendar.getInstance();
        nowMonth = cal.get(Calendar.MONTH);
        nowDay = cal.get(Calendar.DATE);
        now_Week = cal.get(Calendar.DAY_OF_WEEK);
        nowMonth = nowMonth + 1;
        nowHour = cal.get(Calendar.HOUR_OF_DAY);
        nowMin = cal.get(Calendar.MINUTE);
        nowSec = cal.get(Calendar.SECOND);


        //曜日出力
        txMonth.setText(Integer.toString(nowMonth));
        txDay.setText(Integer.toString(nowDay));
        txWeek.setText(nowWeek[now_Week]);
        txHour.setText(Integer.toString(nowHour));
        txMin.setText(Integer.toString(nowMin));
        txSec.setText(Integer.toString(nowSec));

        reBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//日付書き込み場所の取得
                TextView txMonth = (TextView) findViewById(R.id.txtMonth);
                TextView txDay = (TextView) findViewById(R.id.txtDay);
                TextView txWeek = (TextView) findViewById(R.id.txtWeek);
                TextView txHour = (TextView) findViewById(R.id.txthour);
                TextView txMin = (TextView) findViewById(R.id.txtmin);
                TextView txSec = (TextView) findViewById(R.id.txtsec);
                Button reBtn = (Button) findViewById(R.id.rere);
                Button btnList001 = (Button) findViewById(R.id.btnBus);

                //現在時刻取得
                String[] nowWeek = {"曜日不明(err", "日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"};

                Calendar cal = Calendar.getInstance();
                nowMonth = cal.get(Calendar.MONTH);
                nowDay = cal.get(Calendar.DATE);
                now_Week = cal.get(Calendar.DAY_OF_WEEK);
                nowMonth = nowMonth + 1;
                nowHour = cal.get(Calendar.HOUR_OF_DAY);
                nowMin = cal.get(Calendar.MINUTE);
                nowSec = cal.get(Calendar.SECOND);


                //曜日出力
                txMonth.setText(Integer.toString(nowMonth));
                txDay.setText(Integer.toString(nowDay));
                txWeek.setText(nowWeek[now_Week]);
                txHour.setText(Integer.toString(nowHour));
                txMin.setText(Integer.toString(nowMin));
                txSec.setText(Integer.toString(nowSec));
            }
        });


        //画面移動
        btnList001.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BusSearch01.class);
                startActivity(intent);
            }
        });

        final Handler handler = new Handler();

        //データ更新
        final Button btData = (Button) findViewById(R.id.dataCheck);
        btData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              reNew();
            }
        });
        //起動時に更新確認するか
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean checkReNew = sharedPreferences.getBoolean("firstReNew",false);
        if(checkReNew==true) {
            reNew();
        }

    }


    @Override   //メニュー画面
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(Menu.NONE, MENU_ID_A, Menu.NONE, "設定");
        menu.add(Menu.NONE, MENU_ID_B, Menu.NONE, "データ管理");
        menu.add(Menu.NONE,MENU_ID_C, Menu.NONE, "マイリスト一括削除");
        menu.add(Menu.NONE,MENU_ID_D,Menu.NONE,"公式サイトに接続");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case(MENU_ID_A):
                Intent intent = new Intent(this,MyPreferenceActivity.class);
                startActivity(intent);
                return  true;
            case(MENU_ID_B):
                Intent it2 = new Intent(this,data.class);
                startActivity(it2);
                return true;
            case(MENU_ID_C):
                SharedPreferences perf = getSharedPreferences("favorite", MODE_PRIVATE);
                SharedPreferences.Editor edit = perf.edit();
                edit.clear().commit();
                Toast.makeText(this,"削除しました。",Toast.LENGTH_SHORT).show();
                return true;
            case (MENU_ID_D):
                Uri uri = Uri.parse("http://saidat.web.fc2.com/BusTime/BusTime.html");
                Intent it = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean extractDownload(String filename, String folder) {
        String path;
        try {
            File file = new File(filename);
            FileInputStream fs = new FileInputStream(file);

            ZipInputStream zis = new ZipInputStream(fs);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                path = getFilesDir() + "/" + folder + "/" + ze.getName();
                FileOutputStream fos = new FileOutputStream(path, false);
                byte[] buf = new byte[1024];
                int size = 0;
                while ((size = zis.read(buf, 0, buf.length)) > -1) {
                    fos.write(buf, 0, size);
                }
                fos.close();
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            zis.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean delFile(File f) {
        if (f.exists() == false) {//存在チェック
            return false;
        } else if (f.isFile()) {//ファイルかどうか
            f.delete();
        } else if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                delFile(files[i]);//繰り返す
            }
            f.delete();
        }
        return true;
    }
    void reNew() {
        final Handler handler = new Handler();
        final TextView txLoading = (TextView) findViewById(R.id.txLoading);//読み込み中の表示
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        int checkNum = Integer.parseInt(sharedPreferences.getString("reNew", "4"));
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {//ネットに接続してるか
            final String path = new String(getFilesDir() + "/tmp/BusCompany.zip");
            final File cacheFile = new File(getFilesDir() + "/tmp");
            final File companyPath = new File(getFilesDir() + "/tmp/BusCompany");
            final String urlSt = "http://saidat.web.fc2.com/data/BusTime/BusCompany.zip";//会社ファイルのある場所(net上)
            switch (checkNum) {
                case (1)://更新があれば全データ更新
                    Toast.makeText(MainActivity.this, "未実装です。設定で更新確認のみに変更してください。\nデータ更新はデータ管理の全データ一括更新を押してください", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        txLoading.setVisibility(View.VISIBLE);//更新中の表示
                                    }
                                });
                                companyPath.mkdirs();
                                URL url = new URL(urlSt);
                                data.dataDL(url,path);
                                extractDownload(path, "tmp/BusCompany");
                                String stList[] = {"A", "K", "S", "T", "N", "H", "M", "Y", "R", "W"};
                                boolean fileOk = false;
                                for(int i=0;i<10;i++) {//A~Wの会社ファイル読み込み
                                    File tmpFile = new File(getFilesDir() + "/tmp/BusCompany/" + stList[i] + ".txt");
                                    File tmpFile2 = new File(getFilesDir() + "/BusCompany/" + stList[i] + ".txt");
                                    BufferedReader bf1New = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFile),"Unicode"));
                                    BufferedReader bf2Old = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFile2),"Unicode"));
                                    String oldCompanyName;  String newCompanyName;
                                    while (((newCompanyName = bf1New.readLine()) != null)) {
                                        fileOk = false;
                                        while ((oldCompanyName = bf2Old.readLine()) != null) {
                                            if (newCompanyName.split(",")[0].equals(oldCompanyName.split(",")[0])) {//同じ会社名のものがあるか、
                                                if (newCompanyName.split(",")[2].equals(oldCompanyName.split(",")[2])) {   //ver比較
                                                    fileOk = true;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                        //DLファイル削除

                                        if(!fileOk){//更新有
                                            //ここにDLプログラム
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), "データ更新があります。\nトップ画面にて更新するか選択してください。", Toast.LENGTH_SHORT).show();
                                                    new AlertDialog.Builder(MainActivity.this)
                                                            .setTitle("更新中は時刻表を表示できません\n更新しますか？")
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
                                                                    pDialog.setTitle("更新中");
                                                                    pDialog.setMessage("少々お待ちください");
                                                                    pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                                    pDialog.setCancelable(false);//キャンセル不可
                                                                    pDialog.show();
                                                                    NetworkInfo nInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                                                                    if(nInfo.isConnected() && nInfo != null) {
                                                                        new Thread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                //DL
                                                                               // "/data/data/" + getPackageName() + "/files/tmp/BusCompany.zip"

                                                                                pDialog.dismiss();
                                                                            }
                                                                        }).start();
                                                                    }else{
                                                                        handler.post(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                Toast.makeText(getApplicationContext(), "ネットワークに接続できません", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                        dialog.dismiss();
                                                                    }
                                                                }
                                                            })
                                                            .setNegativeButton("Cansel", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            }).show();
                                                }
                                            });
                                            break;
                                        }

                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        txLoading.setVisibility(View.GONE);//更新中の表示
                                    }
                                });
                            }catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    break;
                case (4)://更新確認のみ                              -------------------------------
                    final ArrayList<String> newData = new ArrayList<String>();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean flagHave = false;
                            boolean flagNew = false;
                            try {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        txLoading.setVisibility(View.VISIBLE);
                                    }
                                });
                                File zipFile = new File(path);
                                cacheFile.mkdirs();
                                File companyPath = new File(getFilesDir() + "/tmp/BusCompany");
                                //URL url = new URL("http://saidat.web.fc2.com/data/BusTime/BusCompany.zip");
                                URL url = new URL(urlSt);
                                data.dataDL(url, path);
                                companyPath.mkdirs();
                                extractDownload(path, "tmp/BusCompany");


                                String stList[] = {"A", "K", "S", "T", "N", "H", "M", "Y", "R", "W"};
                                FileInputStream fis1;
                                BufferedReader bfr1;
                                FileInputStream fis2;
                                BufferedReader bfr2;
                                lb:
                                //所持データの更新確認
                                for (int i = 0; i < 10; i++) {
                                    File tmpFile = new File(getFilesDir() + "/tmp/BusCompany/" + stList[i] + ".txt");
                                    File tmpFile2 = new File(getFilesDir() + "/BusCompany/" + stList[i] + ".txt");
                                    fis1 = new FileInputStream(tmpFile);
                                    bfr1 = new BufferedReader(new InputStreamReader(fis1, "Unicode"));
                                    fis2 = new FileInputStream(tmpFile2);
                                    bfr2 = new BufferedReader(new InputStreamReader(fis2, "Unicode"));
                                    String[] newSt;
                                    String[] oldSt;
                                    String tmp;
                                    while ((tmp = bfr1.readLine()) != null) {   //St[0]は日本語,St[1]はファイル名,St[2]はver
                                        newSt = tmp.split(",");
                                        while ((tmp = bfr2.readLine()) != null) {
                                            oldSt = tmp.split(",");
                                            if (oldSt[0].equals(newSt[0])) {
                                                if (!(oldSt[2].equals(newSt[2]))) {//verが異なるか
                                                    //newData.add(oldSt[0]);
                                                    flagHave = true;
                                                    break lb;
                                                }
                                            }
                                        }
                                    }
                                }
                                lb2:
                                for (int i = 0; i < 10; i++) {
                                    File tmpFile = new File(getFilesDir() + "/tmp/BusCompany/" + stList[i] + ".txt");
                                    File tmpFile2 = new File(getFilesDir() + "/BusCompany/" + stList[i] + ".txt");
                                    fis1 = new FileInputStream(tmpFile);
                                    bfr1 = new BufferedReader(new InputStreamReader(fis1, "Unicode"));
                                    fis2 = new FileInputStream(tmpFile2);
                                    bfr2 = new BufferedReader(new InputStreamReader(fis2, "Unicode"));
                                    String[] newSt;
                                    String[] oldSt;
                                    String tmp;
                                    String tmp2;
                                    while ((tmp = bfr1.readLine()) != null) {
                                        newSt = tmp.split(",");
                                        while ((tmp2 = bfr2.readLine()) != null) {
                                            oldSt = tmp2.split(",");
                                            if (!(newSt[0].equals(oldSt[0]))) {
                                                flagNew = true;
                                                break lb2;
                                            }
                                        }
                                    }
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                                flagNew = true;
                            } finally {
                                delFile(cacheFile);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        txLoading.setVisibility(View.GONE);
                                    }
                                });

                                Looper.myLooper().prepare();
                                if (flagHave) {
                                    Toast.makeText(MainActivity.this, "所持データの更新があります", Toast.LENGTH_SHORT).show();
                                }
                                if (flagNew) {
                                    Toast.makeText(MainActivity.this, "未所持データの更新が存在します", Toast.LENGTH_SHORT).show();
                                } else if (flagHave == false && flagNew == false) {

                                    Toast.makeText(MainActivity.this, "更新はありません", Toast.LENGTH_SHORT).show();
                                }
                                Looper.myLooper().loop();
                                Looper.myLooper().quit();
                            }
                        }
                    }).start();


                    break;
                default:
                    Toast.makeText(MainActivity.this, "内部エラー:0\n設定で更新確認のみに変更してください。", Toast.LENGTH_SHORT).show();
            }
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "ネットワークに接続できません", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    void newCheck1(){

    }
}

