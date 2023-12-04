package com.mati.launcher.activity;

import static com.mati.game.core.Config.VERSION_CODE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mati.game.R;
import com.mati.game.core.GTASA;
import com.mati.launcher.model.Update;
import com.mati.launcher.other.Interface;
import com.mati.weikton.reg.Preferences;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
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
        CheckNewUpdate();


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
                if (info_nick.getVisibility() == View.INVISIBLE) {
                    info_nick.setVisibility(View.VISIBLE);
                    logo.setVisibility(View.INVISIBLE);
                } else {
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
                        Intent telegram = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://t.me/Persian_Criminal")
                        );

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


    public void CheckNewUpdate() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://vbd.fdv.dd/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

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
                tost("Error -> " + t.getMessage());
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
                Intent telegram = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://t.me/Mati_Source")
                );

                telegram.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                telegram.setPackage("org.telegram.messenger");
                startActivity(telegram);
            }
        });

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
        Toast.makeText(this, pon, Toast.LENGTH_LONG).show();
    }
}