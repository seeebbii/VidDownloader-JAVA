package com.Codeminers.allInOne.free.videodownloader.statussaver.utility;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.Codeminers.allInOne.free.videodownloader.statussaver.app.App;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import videodownload.com.newmusically.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Utility {
    private static final String TAG = "Utility";
    public static final String ABSOLUTEPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String STORAGEDIR = getstoragedir();
    public static final String nodatafound = "No data found";
    public static final String nointernet = "Please check your Internet Connection";
    public static final String tryagain = "Try again later";
    public static final String tryagain_login = "Please login with instagram to download private images/videos.";
    public static final String alreadydownloaded = "Video already downloaded";
    public static final String dirforwhatsapp = Environment.getExternalStorageDirectory() + "/WhatsApp/Media/.Statuses";
    public static final String dirforwbussines = Environment.getExternalStorageDirectory() + "/WhatsApp Business/Media/.Statuses";
    public static final String dirformoj = Environment.getExternalStorageDirectory() + "/.Moj/Camera";

    /* whatsapp dir location*/
    public static final String wabasedirname = ABSOLUTEPATH + "/WhatsApp/";
    public static final String databasePath = wabasedirname + "Databases";
    public static final String wallpaperPath = wabasedirname + "Media/WallPaper";
    public static final String imagepathSent = wabasedirname + "Media/WhatsApp Images/Sent";
    public static final String imagepath = wabasedirname + "Media/WhatsApp Images";
    public static final String voicePath = wabasedirname + "Media/WhatsApp Voice Notes";
    public static final String voicePathSent = wabasedirname + "Media/WhatsApp Voice Notes/Sent";
    public static final String audioPath = wabasedirname + "Media/WhatsApp Audio";
    public static final String audioPathSent = wabasedirname + "Media/WhatsApp Audio/Sent";
    public static final String videoPath = wabasedirname + "Media/WhatsApp Video";
    public static final String videoPathSent = wabasedirname + "Media/WhatsApp Video/Sent";
    public static final String profilePath = wabasedirname + "Media/WhatsApp Profile Photos";
    public static final String documentspath = wabasedirname + "Media/WhatsApp Documents";
    public static final String documentspathSent = wabasedirname + "Media/WhatsApp Documents/Sent";
    public static final String stickerpath = wabasedirname + "Media/WhatsApp Stickers";
    public static final String stickerpathSent = wabasedirname + "Media/WhatsApp Stickers/Sent";


    /* whatsapp bussiness dir location*/
    public static final String wabasedirname_wb = ABSOLUTEPATH + "/WhatsApp Business/";
    public static final String databasePath_wb = wabasedirname_wb + "Databases";
    public static final String wallpaperPath_wb = wabasedirname_wb + "Media/WallPaper";
    public static final String imagepathSent_wb = wabasedirname_wb + "Media/WhatsApp Business Images/Sent";
    public static final String imagepath_wb = wabasedirname_wb + "Media/WhatsApp Business Images";
    public static final String voicePath_wb = wabasedirname_wb + "Media/WhatsApp Business Voice Notes";
    public static final String voicePathSent_wb = wabasedirname_wb + "Media/WhatsApp Business Voice Notes/Sent";
    public static final String audioPath_wb = wabasedirname_wb + "Media/WhatsApp Business Audio";
    public static final String audioPathSent_wb = wabasedirname_wb + "Media/WhatsApp Business Audio/Sent";
    public static final String videoPath_wb = wabasedirname_wb + "Media/WhatsApp Business Video";
    public static final String videoPathSent_wb = wabasedirname_wb + "Media/WhatsApp Business Video/Sent";
    public static final String profilePath_wb = wabasedirname_wb + "Media/WhatsApp Business Profile Photos";
    public static final String documentspath_wb = wabasedirname_wb + "Media/WhatsApp Business Documents";
    public static final String documentspathSent_wb = wabasedirname_wb + "Media/WhatsApp Business Documents/Sent";
    public static final String stickerpath_wb = wabasedirname_wb + "Media/WhatsApp Business Stickers";
    public static final String stickerpathSent_wb = wabasedirname_wb + "Media/WhatsApp Business Stickers/Sent";
    // change privacy url here
    public static final String privacyurl = "https://yoursitename.com/privacy.htm";
    public static final String sharetext = "Best " + App.getContext().getResources().getString(R.string.app_name) + " App in Playstore \n Click to Download Now http://play.google.com/store/apps/details?id=" + App.getContext().getPackageName();

    /*
     * user agents
     * */

    public static final String USER_AGENT_1="Mozilla/5.0 (Linux; Android 9; ONEPLUS A5000) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.92 Mobile Safari/537.36";
    public static final String USER_AGENT_2="Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0";

    /*
     *
     * language code*/
    public static final String LANG_EN = "en";
    public static final String LANG_IN = "in";
    public static final String LANG_IT = "it";
    public static final String LANG_KO = "ko";
    public static final String LANG_HI = "hi";
    public static final String LANG_AR = "ar";
    public static final String LANG_RU = "ru";


    private static Toast toast;
    private static final Pattern urlPattern = Pattern.compile("(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)", 42);


    public static int convertDpToPixel(int i, Context context) {
        return (int) (((float) i) * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f));
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static void shareVideoToOtherApps(Activity activity, String str, Uri uri) {
        shareVideoToOtherApps(activity, str, uri, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static void shareVideoToOtherApps(Activity activity, String str, Uri uri, boolean z) {
        Intent intent = new Intent("android.intent.action.SEND");
        if (z) {
            intent.setType("video/*");
        } else {
            intent.setType("image/*");
        }
        intent.putExtra("android.intent.extra.SUBJECT", sharetext);
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT < 22) {
            activity.startActivity(Intent.createChooser(intent, "Choose one..."));
            return;
        }
        activity.startActivity(Intent.createChooser(intent, "Choose one...", PendingIntent.getBroadcast(activity, 0, new Intent(activity, ShareCallBackBroadcastReceiver.class).addFlags(FLAG_ACTIVITY_NEW_TASK), PendingIntent.FLAG_UPDATE_CURRENT).getIntentSender()));
    }

    public static void shareVideoWhatsApp(Context activity, String str, Uri uri) {
        shareVideoWhatsApp(activity, str, uri, true);
    }

    public static void shareVideoWhatsApp(Context activity, String str, Uri uri, boolean z) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setPackage("com.whatsapp");
        if (z) {
            intent.setType("video/*");
        } else {
            intent.setType("image/*");
        }
        intent.putExtra("android.intent.extra.SUBJECT", sharetext);
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(activity, "Install WhatsApp first", Toast.LENGTH_SHORT).show();

        }
    }

    public static void setToast(Context _mContext, String str) {
        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String videoUrl(String str) {
        if (!TextUtils.isEmpty(str) && !str.equals("")) {
            Matcher matcher = urlPattern.matcher(str);
            if (matcher.find()) {
                return str.substring(matcher.start(1), matcher.end());
            }
        }
        return null;
    }


    public static ArrayList<String> patternforsharechat(String str) {
        ArrayList<String> arrayList = new ArrayList<String>();
        Matcher matcher = Pattern.compile("\\(?\\b(http(s?)://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]").matcher(str);
        while (matcher.find()) {
            String group = matcher.group();
            if (group.startsWith("(") && group.endsWith(")")) {
                group = group.substring(1, group.length() - 1);
            }
            arrayList.add(group);
        }
        return arrayList;
    }

    public static void RateApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }

    public static void ShareApp(Context context) {

        Intent sendInt = new Intent(Intent.ACTION_SEND);
        sendInt.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sendInt.putExtra(Intent.EXTRA_TEXT, sharetext);
        sendInt.setType("text/plain");
        context.startActivity(Intent.createChooser(sendInt, "Share"));
    }

    private static String getstoragedir() {
        try {
            String dir;
            /* external write permission revoked so we have moved downloads to app data dir */
            if (Build.VERSION.SDK_INT > 30) {
                File[] externalStorageFiles = ContextCompat.getExternalFilesDirs(App.getContext(), null);
                Log.e(TAG, "chefolder: " + externalStorageFiles[0].getAbsolutePath() + File.separator + App.getContext().getResources().getString(R.string.app_name) + "/");
                dir = externalStorageFiles[0].getAbsolutePath() + File.separator + App.getContext().getResources().getString(R.string.app_name) + "/";
            } else {
                dir = Environment.getExternalStorageDirectory() + "/" + App.getContext().getResources().getString(R.string.app_name) + "/";
            }
            return dir;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void handledarkmode(Activity activity) {

        String[] darkModeValues = activity.getResources().getStringArray(R.array.dark_mode_values);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("Theme", Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(activity.getString(R.string.dark_mode), activity.getString(R.string.dark_mode_def_value));
        if (string != null) {
            if (string.equalsIgnoreCase(darkModeValues[0])) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            } else if (string.equalsIgnoreCase(darkModeValues[1])) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (string.equalsIgnoreCase(darkModeValues[2])) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (string.equalsIgnoreCase(darkModeValues[3])) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
            }
        }


    }
}
