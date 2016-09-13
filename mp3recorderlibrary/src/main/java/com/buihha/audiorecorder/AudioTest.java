package com.buihha.audiorecorder;

//  public boolean isFirstApp() {
//  boolean isFirstIn = false;
//  SharedPreferences preferences = getSharedPreferences("first_pref",
//  MODE_PRIVATE);
//  isFirstIn = preferences.getBoolean("isFirstIn", true);
//  Editor editor = preferences.edit();
//  editor.putBoolean("isFirstIn", false);
//  return isFirstIn;
//  }

//  protected void onResume() {
//  super.onResume();
//  if (isFirstApp()) {
//  AudioTest.getInstance().doTest(MyTodoActivity.this);
//  }
//  };

import android.content.Context;
import android.os.Environment;
import android.os.Handler;

public class AudioTest {
    private static AudioTest instance;
    private AudioManager mAudioManager;
    private Handler handler = new Handler() {
    };

    public static AudioTest getInstance() {
        if (instance == null) {
            instance = new AudioTest();
        }
        return instance;
    }

    public void doTest(Context context) {
        initAudio(context);
        startRecode();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                cancelRecode();
            }
        }, 1000);
    }

    private void initAudio(Context context) {
        // TODO Auto-generated method stub
        String dir = Environment.getExternalStorageDirectory()
                + "/Android/data/" + context.getPackageName() + "/files/mp3";
        mAudioManager = AudioManager.getInstance(dir);
    }

    public void startRecode() {
        mAudioManager.prepareAudio();
    }

    public void cancelRecode() {
        mAudioManager.cancel();
        mAudioManager = null;
    }

}
