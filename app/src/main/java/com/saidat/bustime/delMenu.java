package com.saidat.bustime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by kurokuro on 2015/08/09.
 */
public class delMenu extends Activity {
    public String CompanyName;
    public String delCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.del_menu);
        delCompany = "";
        final ArrayList<String> TxCompany = new ArrayList<String>();
        Button btCP = (Button) findViewById(R.id.btCP);
        Button btRmCP = (Button) findViewById(R.id.btRmCP);
        Button btRmAll = (Button) findViewById(R.id.btRmAll);
        final TextView txCP = (TextView) findViewById(R.id.CPname);
        String tmp;
        final String[] tmp2 = new String[100];
        int i = 0;  int k = 0;
        final String CompanyList[] ={"A","K","S","T","N","H","M","Y","R","W"};
        for (int j = 0; j < 10; j++) {

            try {
                FileInputStream fileIn = new FileInputStream("/data/data/" + this.getPackageName() + "/files/BusCompany/" + CompanyList[j] + ".txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(fileIn, "Unicode"));
                while ((tmp = br.readLine()) != null) {
                    String[] company = tmp.split(",");
                    TxCompany.add(new String(company[0]));
                    tmp2[k] = company[1];
                    k++;
                }

            } catch (java.io.IOException e) {
                //Toast.makeText(delMenu.this, "ファイルが存在しません", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
                                //全て削除
            btRmAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDig = new AlertDialog.Builder(delMenu.this);
                    alertDig.setTitle("本当に削除しますか？");
                    alertDig.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    File file = new File("/data/data/" + delMenu.this.getPackageName() + "/files");
                                    boolean tmps = delFile(file);
                                    if (tmps == true) {
                                        Toast.makeText(delMenu.this, "削除しました", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    alertDig.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    alertDig.create().show();


                }
            });
                    //選択中の会社を削除
        btRmCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // delCompany
                AlertDialog.Builder alertDig = new AlertDialog.Builder(delMenu.this);
                alertDig.setTitle("本当に削除しますか？");
                alertDig.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean tmps = false;   boolean tmps2 = false;
                                String delTmp;
                                PrintWriter pw = null;
                                ArrayList<String> tmpWrite; //書き込み一覧
                                File file = new File(getFilesDir() +"/" + delCompany);
                                tmps2 = delFile(file);//フォルダ削除
                                try {//文を削除
                                    for (int i = 0; i < 10; i++) {
                                        tmpWrite = new ArrayList<String>();
                                        File filePath = new File (getFilesDir() + "/BusCompany/" + CompanyList[i] + ".txt");

                                        FileInputStream fileIn = new FileInputStream(filePath);

                                        BufferedReader br = new BufferedReader(new InputStreamReader(fileIn, "Unicode"));
                                        while ((delTmp = br.readLine()) != null) {

                                            String[] company = delTmp.split(",");
                                            if(!(company[1].equals(delCompany))){
                                                tmpWrite.add(delTmp);
                                            }
                                        }
                                        tmps = filePath.delete();  //一度削除
                                        FileOutputStream outputStream = new FileOutputStream(getFilesDir() + "/BusCompany/" + CompanyList[i] +".txt");
                                        OutputStreamWriter writer = new OutputStreamWriter(outputStream,"Unicode");
                                        if(tmpWrite.size() != 0) {
                                            for (int a = 0; a < tmpWrite.size(); a++) {
                                                writer.write(tmpWrite.get(a));
                                                writer.flush();
                                                writer.close();
                                            }
                                        }
                                    }
                                    if(tmps && tmps2)    Toast.makeText(delMenu.this,"削除しました",Toast.LENGTH_SHORT).show();
                                }catch (IOException e){
                                    Toast.makeText(delMenu.this,"err;"+e,Toast.LENGTH_LONG).show();
                                }finally {
                                    if(pw != null){
                                        pw.close();
                                    }
                                }
                            }
                        });
                alertDig.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDig.create().show();
            }
        });

            //会社名変更ボタン選択時
            final String finalTmp[] = tmp2;
            btCP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //会社選択リスト表示
                    final CharSequence[] items = new CharSequence[TxCompany.size()];//会社名
                    for (int i = 0; i < TxCompany.size(); i++) {
                        items[i] = TxCompany.get(i);
                    }
                    AlertDialog.Builder listDg = new AlertDialog.Builder(delMenu.this);
                    listDg.setTitle("会社名を選択してください");
                    listDg.setItems(items,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    txCP.setText(TxCompany.get(which));
                                    CompanyName = TxCompany.get(which);
                                    List(finalTmp[which]);  //ListViewに表示
                                    delCompany = finalTmp[which];   //ファイル名
                                }
                            });
                    listDg.create().show();
                }
            });

    }

    public void List(String company) {
        try {
            ArrayList<String> tmp = new ArrayList<String>();
            String[] stTmp;
            String stList[] ={"A","K","S","T","N","H","M","Y","R","W"};
            for (int i = 0; i < 10; i++) {
                FileInputStream fl = new FileInputStream("/data/data/" + this.getPackageName() + "/files/" + company + "/Name_list/" + stList[i] + ".txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(fl, "Unicode"));
                String stTmp1;
                while ((stTmp1 = br.readLine()) != null) {
                    // stTmp = stTmp1.split(",");//０が表示用、1がファイル用
                    tmp.add(stTmp1);
                }
            }
            String[] items = new String[tmp.size()];
            String[] file__items = new String[tmp.size()];
            for (int i = 0; i < tmp.size(); i++) {
                stTmp = (tmp.get(i)).split(",");//０が表示用、1がファイル用
                items[i] = stTmp[0];
                file__items[i] = stTmp[1];
            }
            ListView lv = (ListView) findViewById(R.id.listView);
            //ListView内容作成
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
            lv.setAdapter(adapter);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public boolean delFile(File f) {
        if (f.exists() == false) {//存在チェック
            Toast.makeText(delMenu.this, "存在なし", Toast.LENGTH_SHORT).show();
            return false;
        } else if (f.isFile()) {//ファイルかどうか
            f.delete();
        } else if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                delFile(files[i]);//繰り返す
            }
            f.delete();
        } else {
            Toast.makeText(delMenu.this, "err", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
