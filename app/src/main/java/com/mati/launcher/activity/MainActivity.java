package com.mati.launcher.activity;

import static com.mati.game.core.Config.BASE_URL;
import static com.mati.game.core.Config.VERSION_CODE;
import static com.mati.game.core.Config.VERSION_CODE_DATA;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mati.game.R;
import com.mati.game.core.GTASA;
import com.mati.launcher.adapter.NewsAdapter;
import com.mati.launcher.model.News;
import com.mati.launcher.model.Update;
import com.mati.launcher.other.Interface;
import com.mati.launcher.other.Lists;
import com.mati.weikton.reg.Preferences;

import org.ini4j.Wini;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import soup.neumorphism.NeumorphCardView;

public class MainActivity extends AppCompatActivity {

    EditText nickname;
    ImageButton ib_info;

    RecyclerView recyclerNews;
    NewsAdapter storiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.button_click);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
            }
        }

        InitLogic();
        LoadNick();
        CheckNewUpdate();
        getStories(this);

        if (CheckFile()) {
            CeloeErrorDialog();
        }

        String LOGO_SERVER = "http://62.3.14.22/dl/logo.png";
        ImageView logo = findViewById(R.id.logo);
        Glide.with(this).load(R.drawable.logo).load(LOGO_SERVER).placeholder(R.drawable.logo).error(R.drawable.logo).into(logo);

        if (IsGameInstalled()) {

            TextView install = findViewById(R.id.install);
            install.setText("بازسازی داده ها");

            TextView textStatus = findViewById(R.id.text_status);
            textStatus.setText(getString(R.string.installation_data));
            textStatus.setTextColor(ContextCompat.getColor(this, R.color.green));

            ImageView imgStatus = findViewById(R.id.img_status);
            imgStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_cycle_green));
        } else if (IsUpdateInstalled()) {
            TextView textStatus = findViewById(R.id.text_status);
            textStatus.setText(getString(R.string.update_data));
            textStatus.setTextColor(ContextCompat.getColor(this, R.color.green));

            ImageView imgStatus = findViewById(R.id.img_status);
            imgStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_cycle_green));
        } else {

            TextView install = findViewById(R.id.install);
            install.setText("نصب بازی");

            TextView textStatus = findViewById(R.id.text_status);
            textStatus.setText(getString(R.string.installation_data));
            textStatus.setTextColor(ContextCompat.getColor(this, R.color.red));

            ImageView imgStatus = findViewById(R.id.img_status);
            imgStatus.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_cycle_green));
        }

        nickname = findViewById(R.id.edit_text_name);
        ib_info = findViewById(R.id.ib_info);

        ((NeumorphCardView) findViewById(R.id.button_play)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animation);
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        onClickPlay();
                    }
                }, 200L);
            }
        });

        ((ImageButton) ib_info).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animation);
                TextView info_nick = findViewById(R.id.text_view_info_about_nickname);
                LinearLayout button_clean_game = findViewById(R.id.button_clean_game);
                if (info_nick.getVisibility() == View.INVISIBLE) {
                    info_nick.setVisibility(View.VISIBLE);
                    button_clean_game.setVisibility(View.INVISIBLE);
                } else {
                    info_nick.setVisibility(View.INVISIBLE);
                    button_clean_game.setVisibility(View.VISIBLE);
                }
            }
        });

        ((NeumorphCardView) findViewById(R.id.button_discord)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animation);
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://discord.gg/")));
                    }
                }, 200L);
            }
        });

        ((NeumorphCardView) findViewById(R.id.button_telegram)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animation);
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/PRPMOBILE"));

                        telegram.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        telegram.setPackage("org.telegram.messenger");
                        startActivity(telegram);
                    }
                }, 200L);
            }
        });

        ((LinearLayout) findViewById(R.id.button_clean_game)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animation);
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ToLoad();
                    }
                }, 200L);
            }
        });


        ((EditText) nickname).setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    try {
                        File f = new File(Environment.getExternalStorageDirectory() + "/PersianRp/persian/settings.ini");
                        if (!f.exists()) {
                            f.createNewFile();
                            f.mkdirs();
                        }
                        Wini w = new Wini(new File(Environment.getExternalStorageDirectory() + "/PersianRp/persian/settings.ini"));
                        if (checkValidNick()) {
                            w.put("client", "name", nickname.getText().toString());
                            tost("نام مستعار جدید شما با موفقیت ذخیره شد");
                            Preferences.setNick(String.valueOf(nickname.getText()));
                        } else {
                            checkValidNick();
                        }
                        w.store();
                    } catch (IOException e) {
                        e.printStackTrace();
                        tost("بازی را نصب کن!");
                    }
                }
                return false;
            }
        });

        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    File f = new File(Environment.getExternalStorageDirectory() + "/PersianRp/persian/settings.ini");
                    if (!f.exists()) {
                        f.createNewFile();
                        f.mkdirs();
                    }
                    Wini w = new Wini(new File(Environment.getExternalStorageDirectory() + "/PersianRp/persian/settings.ini"));
                    w.put("client", "name", nickname.getText().toString());
                    w.store();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 1000) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
            }
        }
    }

    public void onClickPlay() {
        if (CheckFile()) {
            CeloeErrorDialog();
        } else {
            if (IsGameInstalled()) {
                if (IsUpdateInstalled()) {
                    try {
                        Wini w = new Wini(new File(Environment.getExternalStorageDirectory() + "/PersianRp/version.ini"));
                        if (w.get("version", "code").equals(VERSION_CODE_DATA)) {
                            if (checkValidNick()) {
                                startActivity(new Intent(this, GTASA.class));
                            } else {
                                checkValidNick();
                            }
                        } else {
                            ToUpdate();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToUpdate();
                }
            } else {
                ToLoad();
            }
        }
    }

    private boolean IsGameInstalled() {
        String CheckFile = Environment.getExternalStorageDirectory() + "/PersianRp/texdb/gta3.img";
        File file = new File(CheckFile);
        return file.exists();
    }

    private boolean IsUpdateInstalled() {
        String CheckFile = Environment.getExternalStorageDirectory() + "/PersianRp/version.ini";
        File file = new File(CheckFile);
        return file.exists();
    }

    private void ToLoad() {
        startActivity(new Intent(this, LoaderActivity.class));
    }

    private void ToUpdate() {
        startActivity(new Intent(this, UpdateActivity.class));
    }

    private void InitLogic() {
        try {
            Wini w = new Wini(new File(Environment.getExternalStorageDirectory() + "/PersianRp/persian/settings.ini"));
            nickname = findViewById(R.id.edit_text_name);
            nickname.setText(w.get("client", "name"));
            w.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkValidNick() {
        EditText nick = (EditText) findViewById(R.id.edit_text_name);
        if (nick.getText().toString().isEmpty()) {
            tost("نام مستعار را وارد کنید");
            return false;
        }
        if (!(nick.getText().toString().contains("_"))) {
            tost("نام مستعار باید دارای نماد \"_\" باشد ");
            return false;
        }
        if (nick.getText().toString().length() < 4) {
            tost("\n" + "نام مستعار باید حداقل 4 کاراکتر باشد");
            return false;
        }
        return true;
    }


    private void LoadNick() {
        try {
            Wini w = new Wini(new File(Environment.getExternalStorageDirectory() + "/PersianRp/persian/settings.ini"));
            Preferences.setNick(w.get("client", "name"));
            w.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CheckNewUpdate() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Interface sInterface = retrofit.create(Interface.class);
        Call<Update> scall = sInterface.getUpdate();
        scall.enqueue(new Callback<Update>() {
            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {
                List<Update.Value> data = response.body().getValues();

                if (data.get(0) != null) {
                    if (data.get(0).getVersion_code() != VERSION_CODE) {
                        UpdateDialog(data.get(0).getMandatory());
                    }
                } else {
                    tost("خطا در اتصال با سروور");
                }
            }

            @Override
            public void onFailure(Call<Update> call, Throwable t) {
                ErrorDialog();
            }
        });
    }


    public void getStories(Context context) {

        ImageView logo = findViewById(R.id.logo);

        LinearLayout postTitle = findViewById(R.id.post_title);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Interface sInterface = retrofit.create(Interface.class);
        Call<News> scall = sInterface.getStories();
        scall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {

                List<News.Value> data = response.body().getValues();
                if (data != null) {
                    if (Lists.nlist.isEmpty()) {
                        postTitle.setVisibility(View.INVISIBLE);
                        for (News.Value item : data) {
                            if (item.getShow() == 1) {
                                Lists.nlist.add(item);
                                postTitle.setVisibility(View.VISIBLE);
                                logo.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                    logo.setVisibility(View.INVISIBLE);
                } else {
                    logo.setVisibility(View.VISIBLE);
                    postTitle.setVisibility(View.INVISIBLE);
                    recyclerNews.setVisibility(View.INVISIBLE);
                }

                recyclerNews = findViewById(R.id.stories_recycler);
                recyclerNews.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                recyclerNews.setLayoutManager(layoutManager);

                Collections.reverse(Lists.nlist);
                storiesAdapter = new NewsAdapter(getApplicationContext(), Lists.nlist);
                recyclerNews.setAdapter(storiesAdapter);

                PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
                pagerSnapHelper.attachToRecyclerView(recyclerNews);

                recyclerNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            if (recyclerNews.getScrollState() > 3) {
                                tost(String.valueOf(response.body().getValues().size()));
                                recyclerView.scrollToPosition(0);
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                ErrorDialog();
            }

        });
    }

    private void UpdateDialog(Integer visible) {

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setView(getLayoutInflater().inflate(R.layout.update_dialog, null));
        dialog.setCancelable(false);
        dialog.show();

        ConstraintLayout btnCancel = dialog.findViewById(R.id.cancel_btn);
        ConstraintLayout btnUpdate = dialog.findViewById(R.id.Update_Btn);

        if (visible == 0) {
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            btnCancel.setVisibility(View.GONE);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Mati_Source"));

                telegram.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                telegram.setPackage("org.telegram.messenger");
                startActivity(telegram);
            }
        });

    }

    private void ErrorDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setView(getLayoutInflater().inflate(R.layout.error_dialog, null));
        dialog.setCancelable(false);
        dialog.show();

        ConstraintLayout btnUpdate = dialog.findViewById(R.id.Update_Btn);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

    }


    private void CeloeErrorDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setView(getLayoutInflater().inflate(R.layout.cheat_dialog, null));
        dialog.setCancelable(false);
        dialog.show();

        ConstraintLayout btnUpdate = dialog.findViewById(R.id.Update_Btn);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

    }


    public boolean CheckFile() {
        String directoryPath = Environment.getExternalStorageDirectory() + "/PersianRp/";
        String directoryCloe = Environment.getExternalStorageDirectory() + "/PersianRp/cloe";
        String directoryData = Environment.getExternalStorageDirectory() + "/PersianRp/data";
        String directorySamp = Environment.getExternalStorageDirectory() + "/PersianRp/samp";
        String directoryAnim = Environment.getExternalStorageDirectory() + "/PersianRp/anim";
        String directoryPersian = Environment.getExternalStorageDirectory() + "/PersianRp/persian";
        String[] fileExtensions = {"cs", "asi", "csa", "csi", "so"};

        if (hasFiles(directoryAnim, fileExtensions) || hasFiles(directoryPath, fileExtensions) || hasFiles(directoryCloe, fileExtensions) || hasFiles(directoryData, fileExtensions) || hasFiles(directorySamp, fileExtensions) || hasFiles(directoryPersian, fileExtensions)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean hasFiles(String directoryPath, final String[] fileExtensions) {
        File directory = new File(directoryPath);
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                for (String extension : fileExtensions) {
                    if (name.endsWith("." + extension)) {
                        return true;
                    }
                }
                return false;
            }
        };
        File[] files = directory.listFiles(filter);
        return files != null && files.length > 0;
    }

    private void tost(String pon) {
        Toast.makeText(this, pon, Toast.LENGTH_LONG).show();
    }
}