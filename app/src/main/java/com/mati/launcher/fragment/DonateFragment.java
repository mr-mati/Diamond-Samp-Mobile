package com.mati.launcher.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mati.game.R;

public class DonateFragment extends Fragment {
	
	Animation animation;
	
	public Dialog dialog;
	
	public EditText nik;
    public EditText sum;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_donate, container, false);
		
		TextView textView = (TextView) inflate.findViewById(R.id.deposit);
		
		Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click);
		
		nik = inflate.findViewById(R.id.nik);
        sum = inflate.findViewById(R.id.sum);
		
		textView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                v.startAnimation(animation);
				onClickDeposit();
            }
        });
		
        return inflate;
    }
	
	public void onClickDeposit() {
        if (nik.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "نام مستعار را وارد کنید!", Toast.LENGTH_LONG).show();
        } else if (!nik.getText().toString().contains("_")) {
            Toast.makeText(getActivity(),"نیک باید داشته باشد \"_\"!", Toast.LENGTH_LONG).show();
        } else if (nik.getText().toString().length() < 4) {
            Toast.makeText(getActivity(), "حداقل طول نام مستعار 4 کاراکتر است!", Toast.LENGTH_LONG).show();
        } else if (this.sum.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "مقدار را وارد کنید!", Toast.LENGTH_LONG).show();
        } else {

        }
    }
}
