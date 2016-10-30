package com.saidat.bustime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by kurokuro on 2015/08/08.
 */
public class data extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);

        Button bt1 = (Button) findViewById(R.id.bt1);
        Button bt2 = (Button) findViewById(R.id.bt2);
        Button btrm = (Button) findViewById(R.id.btrm);
        Button allDL = (Button) findViewById(R.id.AllDL);

        //ネットに接続
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button btReadPublic = (Button) findViewById(R.id.btReadPublic);
        btReadPublic.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                boolean flag1 = false;
                File pathExternalPublicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file1 = new File(pathExternalPublicDir + "/BusCompany.zip");
                if (!(file1.exists()))
                    Toast.makeText(data.this, "ファイル(BusCompany.zip)が存在していません", Toast.LENGTH_LONG).show();
                else {
                    try {
                        String tmp;
                        ArrayList<String> companyTmp = new ArrayList<String>();
                        File tmpFile = new File(getFilesDir().toString() + "/tmp");
                        tmpFile.mkdir();
                        extractDownload(pathExternalPublicDir + "/BusCompany.zip", "tmp");
                        String stList[] = {"A", "K", "S", "T", "N", "H", "M", "Y", "R", "W"};
                        for (int i = 0; i < 10; i++) {
                            boolean flag = false;
                            tmpFile = new File("/data/data/" + data.this.getPackageName() + "/files/tmp/" + stList[i] + ".txt");
                            if (tmpFile.exists()) { //～.txtが存在すれば続行
                                FileInputStream fileIN = new FileInputStream("/data/data/" + data.this.getPackageName() + "/files/tmp/" + stList[i] + ".txt");
                                BufferedReader br = new BufferedReader(new InputStreamReader(fileIN, "Unicode"));
                                while ((tmp = br.readLine()) != null) {
                                    String tmps[] = tmp.split(",");
                                    File file2 = new File(pathExternalPublicDir + "/" + tmps[1]);
                                    if (file2.exists()) {
                                        companyTmp.add(tmps[1]);
                                        flag = true;
                                    }
                                }
                                if (flag == true) {
                                    flag = false;
                                    for (int j = 0; j < companyTmp.size(); j++) {
                                        File fileTmp = new File(pathExternalPublicDir + "/" + companyTmp.get(j) + "/BusTime.zip");
                                        if (!(fileTmp.exists())) {
                                            Toast.makeText(data.this, companyTmp.get(j) + "にBusTime.zipが存在していません", Toast.LENGTH_LONG).show();
                                        } else {
                                            fileTmp = new File(pathExternalPublicDir + "/" + companyTmp.get(j) + "/Name_list.zip");
                                            if (!(fileTmp.exists())) {
                                                Toast.makeText(data.this, companyTmp.get(j) + "にName_list.zipが存在していません", Toast.LENGTH_LONG).show();
                                            } else {
                                                File newFile1 = new File("/data/data/" + getPackageName() + "/files/" + companyTmp.get(j) + "/BusTime");
                                                File newFile2 = new File("/data/data/" + getPackageName() + "/files/" + companyTmp.get(j) + "/Name_list");
                                                newFile1.mkdirs();
                                                newFile2.mkdirs();
                                                extractDownload(pathExternalPublicDir + "/" + companyTmp.get(j) + "/BusTime.zip", companyTmp.get(j) + "/BusTime");
                                                extractDownload(pathExternalPublicDir + "/" + companyTmp.get(j) + "/Name_list.zip", companyTmp.get(j) + "/Name_list");
                                                fileTmp = new File(getFilesDir() + "/BusCompany");
                                                if (fileTmp.exists()) {       //既に会社が存在してる場合
                                                    String tmpTx;
                                                    ArrayList<String> tmpWrite;
                                                    for (i = 0; i < 10; i++) {
                                                        fileIN = new FileInputStream("/data/data/" + data.this.getPackageName() + "/files/tmp/" + stList[i] + ".txt");
                                                        br = new BufferedReader(new InputStreamReader(fileIN, "Unicode"));
                                                        File path = new File(getFilesDir() + "/BusCompany/" + stList[i] + ".txt");
                                                        FileInputStream fileOld = new FileInputStream(path);
                                                        BufferedReader brOld = new BufferedReader(new InputStreamReader(fileOld, "Unicode"));
                                                        tmpWrite = new ArrayList<String>();
                                                        String tmpTxs;
                                                        while ((tmpTxs = brOld.readLine()) != null) {
                                                            tmpWrite.add(tmpTxs);
                                                        }
                                                        if (tmpWrite.size() != 0) {
                                                            path.delete();//一度txt削除
                                                            FileOutputStream outputStream = new FileOutputStream(path);
                                                            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "Unicode");
                                                            BufferedWriter bw = new BufferedWriter(writer);
                                                            for (int a = 0; a < tmpWrite.size(); a++) {//既存テキスト書き込み
                                                                bw.write(tmpWrite.get(a));
                                                                bw.newLine();
                                                            }
                                                            while ((tmpTx = br.readLine()) != null) {//newテキスト書き込み
                                                                bw.write(tmpTx);
                                                                bw.newLine();
                                                            }


                                                            bw.flush();
                                                            bw.close();
                                                            writer.close();
                                                            //この時点で重複あり


                                                            flag1 = true;
                                                        }
                                                    }
                                                } else {
                                                    fileTmp.mkdir();
                                                    extractDownload(pathExternalPublicDir + "/BusCompany.zip", "BusCompany");
                                                    delMenu del = new delMenu();
                                                    fileTmp = new File(getFilesDir() + "/tmp");
                                                    del.delFile(fileTmp);

                                                    flag1 = true;
                                                }
                                            }
                                        }
                                    }
                                    File fileTmp = new File(getFilesDir() + "/tmp");
                                    if (fileTmp.exists()) {
                                        delMenu del = new delMenu();
                                        del.delFile(fileTmp);


                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        Toast.makeText(data.this, "err;" + e, Toast.LENGTH_LONG);
                    }
                }
                if (flag1 == true)
                    Toast.makeText(data.this, "完了しました", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(data.this, "エラーが発生しました。ファイル名、内容を確認してください", Toast.LENGTH_LONG).show();

            }
        });

        allDL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(data.this)
                        .setTitle("データがある場合、一度データを削除してから更新します。")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        int msg;
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                                if (nInfo != null && nInfo.isConnected()) { //ネット接続可能か
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {
                                                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                                                NetworkInfo nInfo = cm.getActiveNetworkInfo();
                                                String path = new String("/data/data/" + getPackageName() + "/files/tmp/BusCompany.zip");
                                                File cacheFile = new File(getFilesDir() + "/tmp");
                                                cacheFile.mkdirs();
                                                File companyPath = new File(getFilesDir() + "/BusCompany");
                                                URL url = new URL("http://saidat.web.fc2.com/data/BusTime/BusCompany.zip");
                                                if (nInfo != null && nInfo.isConnected()) {//端末接続情報
                                                    //データ削除準備
                                                    if (dataDL(url, path)) {
                                                        File fileTmp = new File(getFilesDir() + "/tmp/BusCompany.zip");
                                                        if (fileTmp.exists()) {    //無事にDLできたか

                                                            if (companyPath.exists())
                                                                delFile(companyPath);//既にフォルダがあれば削除
                                                            companyPath.mkdirs();
                                                            extractDownload(path, "BusCompany");
                                                            //データに存在する会社へリンク
                                                            String stList[] = {"A", "K", "S", "T", "N", "H", "M", "Y", "R", "W"};
                                                            for (int i = 0; i < 10; i++) {
                                                                FileInputStream fl = new FileInputStream(getFilesDir() + "/" + "BusCompany/" + stList[i] + ".txt");
                                                                BufferedReader brNameList = new BufferedReader(new InputStreamReader(fl, "Unicode"));
                                                                String stTmp1;
                                                                URL url2;
                                                                String path2;
                                                                while ((stTmp1 = brNameList.readLine()) != null) {
                                                                    String[] fileName = stTmp1.split(",");
                                                                    url = new URL("http://saidat.web.fc2.com/data/BusTime/BusCompany_" + stList[i] + "/" + fileName[1] + "/BusTime.zip");
                                                                    url2 = new URL("http://saidat.web.fc2.com/data/BusTime/BusCompany_" + stList[i] + "/" + fileName[1] + "/Name_list.zip");
                                                                    File data = new File(getFilesDir() + "/" + fileName[1]);
                                                                    path = new String("/data/data/" + getPackageName() + "/files/tmp/" + fileName[1]);
                                                                    File newFile = new File(path);
                                                                    newFile.mkdirs();
                                                                    path2 = path;
                                                                    path += "/BusTime.zip";
                                                                    path2 += "/Name_list.zip";
                                                                    if (dataDL(url, path) && dataDL(url2, path2)) {

                                                                        if (data.exists())//ファイルが存在していたら
                                                                            delFile(data);
                                                                        data = new File(getFilesDir() + "/" + fileName[1] + "/BusTime");
                                                                        data.mkdirs();  //会社名フォルダ作成
                                                                        data = new File(getFilesDir() + "/" + fileName[1] + "/Name_list");
                                                                        data.mkdirs();

                                                                        extractDownload(path, fileName[1] + "/BusTime");
                                                                        extractDownload(path2, fileName[1] + "/Name_list");
                                                                        fileTmp = new File(getFilesDir() + "/tmp");
                                                                        delFile(fileTmp);
                                                                        Looper.myLooper().prepare();
                                                                        Toast.makeText(data.this, "完了", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }

                                                        } else {
                                                            msg = 1;    //err
                                                            Looper.myLooper().prepare();
                                                            Toast.makeText(data.this,"サーバーに接続できませんでした。\n再度時間をあけて接続してみてください。\n" +
                                                                    "それでも接続できない場合はお問い合わせをしてください", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        msg = 4;    //err
                                                        Looper.myLooper().prepare();
                                                        Toast.makeText(data.this, "エラーが発生しました。お問い合わせをしてください", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    msg = 2;    //err
                                                    Looper.myLooper().prepare();
                                                    Toast.makeText(data.this, "ネットワークに接続できません。\n" +
                                                            "接続が可能な状態にしてください", Toast.LENGTH_SHORT).show();
                                                }
                                                Looper.myLooper().loop();
                                                Looper.myLooper().quit();

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                    /*
                                    String massegeText;
                                    switch (msg) {
                                        case 1:
                                            massegeText = "サーバーに接続できませんでした。\n再度時間をあけて接続してみてください。\n" +
                                                    "それでも接続できない場合はお問い合わせをしてください";
                                            break;
                                        case 2:
                                            massegeText = "ネットワークに接続できません。\n接続が可能な状態にしてください";
                                            break;
                                        case 3:
                                            massegeText = "ネットワークが途中で切断されたかサーバーに接続できません。\nネットワークにつながっている場合お問い合わせください";
                                            break;
                                        case 4:
                                            massegeText = "エラーが発生しました。お問い合わせをしてください";
                                            break;
                                        default:
                                            massegeText = "完了";
                                            break;
                                    }
                                    Toast.makeText(data.this, massegeText, Toast.LENGTH_LONG).show();
                                    */

                                } else {
                                    Toast.makeText(data.this, "ネットワークに接続できません。\n接続が可能な状態にしてください", Toast.LENGTH_LONG).show();
                                }
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


        //デバック専用　=========================================================================================
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File newFile3 = new File("/data/data/" + getPackageName() + "/files/BusCompany");//会社名収納ファイル作成
                newFile3.mkdirs();
                extract("BusCompany.zip", "BusCompany");
                String tmp;
                try {
                    for (int i = 0; i < 10; i++) {
                        String stList[] = {"A", "K", "S", "T", "N", "H", "M", "Y", "R", "W"};
                        FileInputStream fileIN = new FileInputStream("/data/data/" + data.this.getPackageName() + "/files/BusCompany/" + stList[i] + ".txt");
                        BufferedReader br = new BufferedReader(new InputStreamReader(fileIN, "Unicode"));
                        while ((tmp = br.readLine()) != null) {
                            String tmp2[] = tmp.split(",");
                            File newFile1 = new File("/data/data/" + getPackageName() + "/files/" + tmp2[1] + "/BusTime");
                            File newFile2 = new File("/data/data/" + getPackageName() + "/files/" + tmp2[1] + "/Name_list");
                            newFile1.mkdirs();
                            newFile2.mkdirs();

                            extract(tmp2[1] + "/BusTime.zip", tmp2[1] + "/BusTime");
                            extract(tmp2[1] + "/Name_list.zip", tmp2[1] + "/Name_list");
                        }
                    }
                } catch (IOException e) {
                }
                Toast.makeText(getApplicationContext(), "完了", Toast.LENGTH_SHORT).show();
            }
        });
        //-==============================================================================================

        btrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(data.this, delMenu.class);
                startActivity(intent);
            }
        });
    }

    //データDL
    public static boolean dataDL(URL url,String out) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);//タイムアウト10秒
            conn.setConnectTimeout(10000);//タイムアウト
            conn.setAllowUserInteraction(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            conn.connect();
            int httpStatusCode = conn.getResponseCode();
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception();
            }
            // Input Stream
            DataInputStream dataInStream = new DataInputStream(conn.getInputStream());
            // Output Stream
            DataOutputStream dataOutStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(out)));
            // Read Data
            byte[] b = new byte[1024];
            int readByte = 0;
            while (-1 != (readByte = dataInStream.read(b))) {
                dataOutStream.write(b, 0, readByte);
            } // Close Stream
            dataInStream.close();
            dataOutStream.close();
            return true;    //完了
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (SocketTimeoutException e){
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //コピー
    private void copy2Local() throws IOException {
        // assetsから読み込み、出力する
        String[] fileList = getResources().getAssets().list("resource");
        if (fileList == null || fileList.length == 0) {
            return;
        }
        AssetManager as = getResources().getAssets();
        InputStream input = null;
        FileOutputStream output = null;

        for (String file : fileList) {
            input = as.open("resource" + "/" + file);
            output = openFileOutput(file, Context.MODE_WORLD_READABLE);

            int DEFAULT_BUFFER_SIZE = 1024 * 4;

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            output.close();
            input.close();
            Toast.makeText(getApplicationContext(), "完了", Toast.LENGTH_SHORT).show();
        }
    }

    //Assetフォルダ内から解凍
    public void extract(String filename, String folder) {
        String path;
        try {
            AssetManager am = getResources().getAssets();
            InputStream is = am.open(filename, AssetManager.ACCESS_STREAMING);
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                path = getFilesDir().toString() + "/" + folder + "/" + ze.getName();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //元パス指定で解凍
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

    public void twiceDel(File fileAllPath) {
        try {
            String tmp;
            ArrayList<String> list = new ArrayList<String>();
            FileInputStream is = new FileInputStream(fileAllPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "Unicode"));
            while ((tmp = br.readLine()) != null) {
                list.add(tmp);
            }
            HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(list);
            //FileWriter fw = new FileWriter(fileAllPath);
            FileOutputStream fo = new FileOutputStream(fileAllPath);
            OutputStreamWriter fw = new OutputStreamWriter(fo, "Unicode");
            PrintWriter pw = new PrintWriter(fw);

            tmp = hashSet.toString();
            String[] tmp2 = tmp.split(",");
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                pw.println(it.next());
            }
            fw.close();
            fo.close();
            pw.close();
        } catch (IOException e) {
        }
    }
    public static boolean delFile(File f) {
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
}

