package com.Codeminers.allInOne.free.videodownloader.statussaver.utility;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;


import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.Codeminers.allInOne.free.videodownloader.statussaver.app.App;

import org.apache.commons.io.FileUtils;

import java.io.File;

import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.shareVideoToOtherApps;
import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.shareVideoWhatsApp;
import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.sharetext;


public class WhatsAppStatusHandler {
    private static final String TAG = "WhatsAppStatusHandler";

    private static class CopyStatusTask extends AsyncTask<Void, Void, Boolean> {

        String filename;
        String from;
        String is;
        Activity activity;


        private CopyStatusTask(String str, String from, String is, Activity activity) {
            Log.e(TAG, "CopyStatusTask: " + from);
            this.filename = str;
            this.from = from;
            this.is = is;
            this.activity = activity;
        }


        protected Boolean doInBackground(Void... voidArr) {
            return WhatsAppStatusHandler.copyFileInSavedDir(this.filename, from);
        }


        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if (bool && is.equals("d")) {
                Toast.makeText(App.getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
            } else if (bool && is.equals("w")) {
                File to = new File(getSavedDir(from), new File(filename).getName().replace("_transcode", ""));
                shareVideoWhatsApp(App.getContext(), sharetext, Uri.fromFile(to));
            } else if (bool && is.equals("s")) {
                File to = new File(getSavedDir(from), new File(filename).getName().replace("_transcode", ""));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    shareVideoToOtherApps(activity, sharetext, Uri.fromFile(to));
                }

            }
        }
    }


    @Nullable
    public static File getSavedDir(String path) {
        String stringBuilder = Utility.STORAGEDIR + path;
        File file = new File(stringBuilder);
        return (file.exists() || file.isDirectory() || file.mkdirs()) ? file : null;
    }

    public static boolean isFileExistInSavedDire(String fileName, String str) {
        try {
            String stringBuilder = getSavedDir(str) + File.separator + fileName;
            File file = new File(stringBuilder);
            Log.d("myFile", file.getAbsolutePath());
            return file.exists();
        } catch (Exception unused) {
            return false;
        }
    }

    private static boolean copyFileInSavedDir(String str, String path) {
        try {
            if (isFileExistInSavedDire(new File(str).getName().replace("_transcode", ""), path)) {
                return true;
            }
            FileUtils.copyFileToDirectory(new File(str), getSavedDir(path));
            if (str.contains("_transcode")) {
                File from = new File(getSavedDir(path), new File(str).getName());
                File to = new File(getSavedDir(path), new File(str).getName().replace("_transcode", ""));
                from.renameTo(to);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void copyStatusInSavedDirectory(String str, String from, String is, Activity activity) {
        new CopyStatusTask(str, from, is,activity).execute();
    }
}
