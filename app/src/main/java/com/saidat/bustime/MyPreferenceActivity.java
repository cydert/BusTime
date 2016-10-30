package com.saidat.bustime;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

/**
 * Created by kurokuro on 2015/07/31.
 */
public class MyPreferenceActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        try {
            PackageManager pm = this.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            PreferenceScreen pfVer = (PreferenceScreen) findPreference("version");
            pfVer.setSummary("VersionName:" + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }




        PreferenceScreen pfHP = (PreferenceScreen) findPreference("toHP");
        pfHP.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse("http://saidat.web.fc2.com/BusTime/BusTime.html");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return false;
            }
        });



/*
        ListPreference lp1 = (ListPreference) getPreferenceScreen().findPreference("reNew");
        if (lp1.getValue() != null) {
            switch (lp1.getValue()) {
                case ("1"):
                    lp1.setSummary("更新があれば全データ更新");
                    break;
                case ("2"):
                    lp1.setSummary("所持データ更新があればそのデータのみ更新");
                    break;
                case ("3"):
                    lp1.setSummary("未所持データがあればデータ取得");
                    break;
                case ("4"):
                    lp1.setSummary("更新確認のみ");
                    break;
            }
        }
*/


        /*ps.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse("http://saidat.web.fc2.com/BusTime/BusTime.html");
                Intent intent1 = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent1);
                return false;
            }
        });
*/
    }


}
