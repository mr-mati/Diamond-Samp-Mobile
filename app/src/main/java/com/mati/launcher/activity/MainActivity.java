package com.mati.launcher.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mati.game.R;
import com.mati.game.core.GTASA;
import com.mati.weikton.reg.Preferences;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import soup.neumorphism.NeumorphCardView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    EditText nickname;
    ImageButton ib_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.button_click);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }

        InitLogic();
        LoadNick();

        String LOGO_SERVER = "https://s2.uupload.ir/files/sp_logo_dztq.png";
        ImageView logo = findViewById(R.id.logo);
        Glide
                .with(this)
                .load(LOGO_SERVER)
                .into(logo);

        if (IsGameInstalled()) {

            TextView install = findViewById(R.id.install);
            install.setText("بازسازی داده ها");

            TextView textStatus = findViewById(R.id.text_status);
            textStatus.setText(getString(R.string.installation_data));
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
                if (info_nick.getVisibility() == View.INVISIBLE) {
                    info_nick.setVisibility(View.VISIBLE);
                    logo.setVisibility(View.INVISIBLE);
                }
                else{
                    logo.setVisibility(View.VISIBLE);
                    info_nick.setVisibility(View.INVISIBLE);
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
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://t.me/PRP_CRMP")));
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


        ((EditText) nickname)
                .setOnEditorActionListener(
                        new EditText.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(
                                    TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_SEARCH
                                        || actionId == EditorInfo.IME_ACTION_DONE
                                        || event.getAction() == KeyEvent.ACTION_DOWN
                                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                    try {
                                        File f =
                                                new File(
                                                        Environment.getExternalStorageDirectory()
                                                                + "/PersianRp/SAMP/settings.ini");
                                        if (!f.exists()) {
                                            f.createNewFile();
                                            f.mkdirs();
                                        }
                                        Wini w =
                                                new Wini(
                                                        new File(
                                                                Environment.getExternalStorageDirectory()
                                                                        + "/PersianRp/SAMP/settings.ini"));
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
                    File f = new File(Environment.getExternalStorageDirectory() + "/PersianRp/SAMP/settings.ini");
                    if (!f.exists()) {
                        f.createNewFile();
                        f.mkdirs();
                    }
                    Wini w = new Wini(new File(Environment.getExternalStorageDirectory() + "/PersianRp/SAMP/settings.ini"));
                    if (checkValidNick()) {
                        w.put("client", "name", nickname.getText().toString());
                    } else {
                        checkValidNick();
                    }
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
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }
    }

    public void onClickPlay() {
        if (IsGameInstalled()) {
            if (IsUpdateInstalled()) {
                startActivity(new Intent(this, GTASA.class));
            } else {
                ToUpdate();
            }
        } else {
            ToLoad();
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
            Wini w = new Wini(new File(Environment.getExternalStorageDirectory() + "/PersianRp/SAMP/settings.ini"));
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
            Wini w = new Wini(new File(Environment.getExternalStorageDirectory() + "/PersianRp/SAMP/settings.ini"));
            Preferences.setNick(w.get("client", "name"));
            w.store();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tost(String pon) {
        Toast.makeText(this, pon, Toast.LENGTH_SHORT).show();
    }
} 