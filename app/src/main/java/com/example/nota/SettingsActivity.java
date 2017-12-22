package com.example.nota;

import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences compartilhandoPreferences;
    private SharedPreferences.Editor editor;
    private boolean tocaSound;
    public static final int FAST = 0;
    public static final int SLOW = 1;
    public static final int NONE = 2;
    private int tocaAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        CheckBox cbSound = (CheckBox) findViewById(R.id.cbSound);
        RadioGroup rGroup = (RadioGroup) findViewById(R.id.rGroup);

        compartilhandoPreferences = getSharedPreferences("Nota Si Bro", MODE_PRIVATE);
        editor = compartilhandoPreferences.edit();

        tocaSound = compartilhandoPreferences.getBoolean("sound", true);
        if(tocaSound){
            cbSound.setChecked(true);
        } else{
            cbSound.setChecked(false);
        }

        tocaAnimation = compartilhandoPreferences.getInt("anim option", SLOW);
        rGroup.clearCheck();
        switch (tocaAnimation){
            case FAST: rGroup.check(R.id.rbFast); break;
            case SLOW: rGroup.check(R.id.rbSlow); break;
            case NONE: rGroup.check(R.id.rbNone); break;
        }

        cbSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("sound = ", ""+ tocaSound);
                Log.i("isChecked = ", ""+isChecked);

                tocaSound = !tocaSound;
                editor.putBoolean("sound", tocaSound);
            }
        });

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rbTerpilih = (RadioButton) group.findViewById(checkedId);
                if (null != rbTerpilih && checkedId > -1){
                    switch (rbTerpilih.getId()){
                        case R.id.rbFast: tocaAnimation = FAST; break;
                        case R.id.rbSlow: tocaAnimation = SLOW; break;
                        case R.id.rbNone: tocaAnimation = NONE; break;
                    }
                    editor.putInt("anim option", tocaAnimation);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.commit();
    }
}
