package com.Codeminers.allInOne.free.videodownloader.statussaver.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Codeminers.allInOne.free.videodownloader.statussaver.fragments.DashboardFragment;
import com.Codeminers.allInOne.free.videodownloader.statussaver.fragments.DownloadsFragment;
import com.Codeminers.allInOne.free.videodownloader.statussaver.fragments.StoryFragment;
import com.Codeminers.allInOne.free.videodownloader.statussaver.utility.LocaleManager;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Objects;


import com.Codeminers.allInOne.free.videodownloader.statussaver.utility.DialogUtils;
import com.Codeminers.allInOne.free.videodownloader.statussaver.adapter.MenuAdapter;
import com.Codeminers.allInOne.free.videodownloader.statussaver.adapter.ViewPagerAdapter;
import com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility;

import me.ibrahimsn.lib.OnItemReselectedListener;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

import com.Codeminers.allInOne.free.videodownloader.statussaver.utility.PrefManager;

import videodownload.com.newmusically.R;

import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.LANG_AR;
import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.LANG_EN;
import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.LANG_HI;
import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.LANG_IN;
import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.LANG_IT;
import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.LANG_KO;
import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.LANG_RU;
import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.RateApp;
import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.ShareApp;
import static com.Codeminers.allInOne.free.videodownloader.statussaver.utility.Utility.handledarkmode;

public class DashboardActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {
    private static final String TAG = "DashboardActivity";
    private ArrayList<String> mTitles = new ArrayList<>();
    private SmoothBottomBar bottomBar;
    private ViewPager viewPager;
    private PrefManager prefManager;
    private ArrayList<Drawable> mDrawbles = new ArrayList<>();
    private ViewHolder mViewHolder;
    private MenuAdapter mMenuAdapter;
    private DashboardFragment callsFragment;
    private DownloadsFragment callsFragment1;
    private StoryFragment callsFragment2;
    public ConstraintLayout constraint;
    public Fragment curFragment = null;
    private int checkPermition = 1001;
    public Snackbar snackbar = null;
    private int requestPermissionSetting = 1002;
    private String copyKey = "";
    private String copyValue = "";
    private RelativeLayout adView;
    private ClipboardManager clipBoard;
    private DashboardActivity activity;
    private boolean isexit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handledarkmode(this);
        FirebaseApp.initializeApp(this);
        activity = this;
        LocaleManager.setLocale(activity);
        setContentView(R.layout.activity_dashboard);
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        getintentdata();
        initdrawer();
        initbottombar();
        initpref();
        initdialog();
        bindviews();
        handlenotification();

    }

    private void handlenotification() {
        if (clipBoard != null) {
            clipBoard.addPrimaryClipChangedListener(() -> {
                try {
                    showNotification(Objects.requireNonNull(clipBoard.getPrimaryClip().getItemAt(0).getText()).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public void showNotification(String Text) {
        if (iscontains(Text) && !prefManager.getServiceStatus()) {
            Intent intent = new Intent(activity, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", Text);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(activity, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(getResources().getColor(R.color.blck))
                    .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(),
                            R.mipmap.ic_launcher))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle(getResources().getString(R.string.copiedlink))
                    .setContentText(Text)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    private boolean iscontains(String text) {
        for (String s : DashboardFragment.FORLIST) {
            if (text.contains(s)) {
                return true;
            }

        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
    }


    private void getintentdata() {
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                copyKey = key;

                if (copyKey.equals("android.intent.extra.TEXT")) {
                    copyValue = getIntent().getExtras().getString(copyKey);
                } else {
                    copyValue = "";
                }
            }
        }
    }


    private void bindviews() {
        constraint = (ConstraintLayout) findViewById(R.id.constraint);
        adView = findViewById(R.id.adView);

    }

    private void initdialog() {
        DialogUtils.with(this)
                .setRateDialogImageRes(R.drawable.rate_image)
                .setTitleAndDescTextColors(ContextCompat.getColor(getApplicationContext(), R.color.setTitleAndDescTextColors), ContextCompat.getColor(getApplicationContext(), R.color.setTitleAndDescTextColors))
                .setButtonColors(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)).setButtonTextColor(ContextCompat.getColor(getApplicationContext(), R.color.setButtonTextColor));
        DialogUtils.showAppRaterIfNeeded(this);
    }

    private void initpref() {
        prefManager = new PrefManager(this);
    }

    private void initbottombar() {
        bottomBar = findViewById(R.id.bottomBar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewHolder = new ViewHolder();
        setupToolbar();
        handleDrawer();
        handleMenu();
        setupViewPager(viewPager);
        bottomBar.setOnItemReselectedListener(new OnItemReselectedListener() {
            @Override
            public void onItemReselect(int i) {
                if (mViewHolder.mDuoDrawerLayout.isDrawerOpen()) {
                    mViewHolder.mDuoDrawerLayout.closeDrawer();
                }
            }
        });
        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if (mViewHolder.mDuoDrawerLayout.isDrawerOpen()) {
                    mViewHolder.mDuoDrawerLayout.closeDrawer();
                }
                viewPager.setCurrentItem(i);
                curFragment = adapter.getItem(i);
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                bottomBar.setActiveItem(position);
                curFragment = adapter.getItem(position);
                switch (position) {
                    case 0:
                        setTitle(getResources().getString(R.string.app_name));
                        break;
                    case 1:
                        setTitle(getResources().getString(R.string.downloadstab));
                        break;
                    case 2:
                        setTitle(getResources().getString(R.string.stories));
                        break;
                    default:
                        setTitle(getResources().getString(R.string.app_name));
                        break;
                }
                if (mViewHolder.mDuoDrawerLayout.isDrawerOpen()) {
                    mViewHolder.mDuoDrawerLayout.closeDrawer();
                }
            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.setActiveItem(position);
                switch (position) {
                    case 0:
                        setTitle(getResources().getString(R.string.app_name));
                        break;
                    case 1:
                        setTitle(getResources().getString(R.string.downloadstab));
                        break;
                    case 2:
                        setTitle(getResources().getString(R.string.stories));
                        break;
                    default:
                        setTitle(getResources().getString(R.string.app_name));
                        break;
                }
                if (mViewHolder.mDuoDrawerLayout.isDrawerOpen()) {
                    mViewHolder.mDuoDrawerLayout.closeDrawer();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mViewHolder.mDuoDrawerLayout.isDrawerOpen()) {
                    mViewHolder.mDuoDrawerLayout.closeDrawer();
                }
            }
        });


    }

    private void initdrawer() {

        mDrawbles.add(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_star_black_24dp, null));
        mDrawbles.add(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_share_black_24dp, null));
        mDrawbles.add(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_lock_black_24dp, null));
        mDrawbles.add(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_round_settings_24, null));

        mTitles = new ArrayList<>();
        mTitles.add(getResources().getString(R.string.rateus));
        mTitles.add(getResources().getString(R.string.shareapp));
        mTitles.add(getResources().getString(R.string.privacy));
        mTitles.add(getResources().getString(R.string.settings));

    }

    private void setupToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    @Override
    public void onFooterClicked() {

    }

    @Override
    public void onHeaderClicked() {

    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        if (mViewHolder.mDuoDrawerLayout.isDrawerOpen()) {
            mViewHolder.mDuoDrawerLayout.closeDrawer();
        }
        switch (position) {
            case 0:
                RateApp(this);
                break;
            case 1:
                ShareApp(this);
                break;
            case 2:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utility.privacyurl));
                startActivity(browserIntent);
                break;
            case 3:
                darkmodedialog();
                break;
        }
    }


    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();
        mViewHolder.mToolbar.setNavigationIcon(R.drawable.ic_round_menu_24);

    }

    private void handleMenu() {
        try {
            mMenuAdapter = new MenuAdapter(mTitles, mDrawbles);
            mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
            mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ViewPagerAdapter adapter;

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        callsFragment = new DashboardFragment(getIntent());
        callsFragment1 = new DownloadsFragment();
        callsFragment2 = new StoryFragment();

        adapter.addFragment(callsFragment);
        adapter.addFragment(callsFragment1);
        adapter.addFragment(callsFragment2);
        viewPager.setAdapter(adapter);
        viewPager.setHorizontalScrollBarEnabled(false);


    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == 0) {
            if (curFragment != null && curFragment instanceof DashboardFragment) {
                ((DashboardFragment) curFragment).checkDownloadVideo();
            }

        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, checkPermition);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getResources().getString(R.string.storageheader));

            builder.setMessage(getResources().getString(R.string.storagedescription));

            builder.setPositiveButton(getResources().getString(R.string.grantpermission), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, checkPermition);
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.cancelpermission), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == checkPermition) {
            String str = strArr[0];
            if (iArr.length <= 0 || iArr[0] != 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, str)) {
                    Snackbar make = Snackbar.make(constraint, (CharSequence) getResources().getString(R.string.allowpermission), BaseTransientBottomBar.LENGTH_SHORT);
                    ((TextView) make.getView().findViewById(R.id.snackbar_text)).setTextColor(-1);
                    make.show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.storageheader));
                builder.setMessage(getResources().getString(R.string.storagedescription));
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivityForResult(intent, requestPermissionSetting);
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancelpermission), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            } else if (Build.VERSION.SDK_INT >= 23) {
                if (curFragment != null && curFragment instanceof DashboardFragment) {
                    ((DashboardFragment) curFragment).checkDownloadVideo();
                }

            }
        } else if (i == requestPermissionSetting) {
            checkPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (mViewHolder.mDuoDrawerLayout.isDrawerOpen()) {
            mViewHolder.mDuoDrawerLayout.closeDrawer();
        } else if (isexit) {
            super.onBackPressed();
        } else {
            isexit = true;
            Toast.makeText(activity, "Press back again to Exit", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    isexit = false;
                }
            }, 3000);
        }
    }


    public void darkmodedialog() {
        RadioGroup group;
        RadioButton defaultTheme, darkTheme, lightheme, auto;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_back);
        dialog.setContentView(R.layout.appdialog_mode);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.gravity = 17;
        dialog.getWindow().setAttributes(layoutParams);
        group = (RadioGroup) dialog.findViewById(R.id.group);
        defaultTheme = (RadioButton) dialog.findViewById(R.id.defaultTheme);
        darkTheme = (RadioButton) dialog.findViewById(R.id.darkTheme);
        lightheme = (RadioButton) dialog.findViewById(R.id.lightheme);
        auto = (RadioButton) dialog.findViewById(R.id.auto);

        SharedPreferences sharedPreferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);
        String[] darkModeValues = getResources().getStringArray(R.array.dark_mode_values);
        String string = sharedPreferences.getString(getString(R.string.dark_mode), getString(R.string.dark_mode_def_value));
        if (string != null) {
            if (string.equalsIgnoreCase(darkModeValues[0])) {
                defaultTheme.setChecked(true);
            } else if (string.equalsIgnoreCase(darkModeValues[1])) {
                lightheme.setChecked(true);
            } else if (string.equalsIgnoreCase(darkModeValues[2])) {
                darkTheme.setChecked(true);
            } else if (string.equalsIgnoreCase(darkModeValues[3])) {
                auto.setChecked(true);
            }
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String[] darkModeValues = getResources().getStringArray(R.array.dark_mode_values);
                if (checkedId == R.id.defaultTheme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    applyTheme(darkModeValues[0]);
                } else if (checkedId == R.id.darkTheme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    applyTheme(darkModeValues[2]);
                } else if (checkedId == R.id.lightheme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    applyTheme(darkModeValues[1]);
                } else if (checkedId == R.id.auto) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                    applyTheme(darkModeValues[3]);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {

                dialog.dismiss();
            }
        });
    }


    public void applyTheme(String name) {
        Log.e(TAG, "applyTheme: called:" + name);
        // Create preference to store theme name
        SharedPreferences preferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.dark_mode), name);
        editor.apply();
        startActivity(new Intent(DashboardActivity.this, SplashActivity.class));
        finish();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.local_english:
                setNewLocale(this, LANG_EN);
                return true;
            case R.id.local_hindi:
                setNewLocale(this, LANG_HI);
                return true;
            case R.id.local_arabic:
                setNewLocale(this, LANG_AR);
                return true;
            case R.id.local_indo:
                setNewLocale(this, LANG_IN);
                return true;
            case R.id.local_korea:
                setNewLocale(this, LANG_KO);
                return true;
            case R.id.local_itali:
                setNewLocale(this, LANG_IT);
                return true;
            case R.id.local_Russian:
                setNewLocale(this, LANG_RU);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}