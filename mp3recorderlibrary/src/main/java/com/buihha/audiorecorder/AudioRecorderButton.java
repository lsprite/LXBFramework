package com.buihha.audiorecorder;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.buihha.audiorecorder.AudioManager.AudioStateListener;
import com.czt.mp3recorder.R;

public class AudioRecorderButton extends Button {
    private static final int STATE_NORMAL = 1;// 默认的状态
    private static final int STATE_RECORDING = 2;// 正在录音
    private static final int STATE_WANT_TO_CANCEL = 3;// 希望取消

    private int mCurrentState = STATE_NORMAL; // 当前的状态
    private boolean isRecording = false;// 已经开始录音

    private static final int DISTANCE_Y_CANCEL = 50;

    private DialogManager mDialogManager;
    private AudioManager mAudioManager;

    private float mTime;
    // 是否触发longClick
    private boolean mReady;

    private static final int MSG_AUDIO_PREPARED = 0x110;
    private static final int MSG_VOICE_CHANGED = 0x111;
    private static final int MSG_DIALOG_DIMISS = 0x112;

    /*
     * 获取音量大小的线程
     */
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    // 显示對話框在开始录音以后
                    mDialogManager.showRecordingDialog();
                    isRecording = true;
                    // 开启一个线程
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;

                case MSG_VOICE_CHANGED:
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                    break;

                case MSG_DIALOG_DIMISS:
                    mDialogManager.dimissDialog();
                    break;

            }

            super.handleMessage(msg);
        }
    };

    /**
     * 以下2个方法是构造方法
     */
    public AudioRecorderButton(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mDialogManager = new DialogManager(context);
        // String dir = "/storage/sdcard0/my_weixin";
        String dir = Environment.getExternalStorageDirectory()
                + "/Android/data/" + context.getPackageName() + "/files/mp3";
        System.out.println("语音存储路径:" + dir);
        mAudioManager = AudioManager.getInstance(dir);
        mAudioManager.setOnAudioStateListener(new AudioStateListener() {

            public void wellPrepared() {
                mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
            }
        });

        // 由于这个类是button所以在构造方法中添加监听事件
        setOnLongClickListener(new OnLongClickListener() {

            public boolean onLongClick(View v) {
                boolean sdCardExist = Environment.getExternalStorageState()
                        .equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
                if (sdCardExist) {
                    mReady = true;
                    mAudioManager.prepareAudio();
                } else {
                    Toast.makeText(context, "SD卡未存在,暂时无法使用语音功能",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public AudioRecorderButton(Context context) {
        this(context, null);
    }

    /**
     * 录音完成后的回调
     */
    public interface AudioFinishRecorderListener {
        void onFinish(float seconds, String filePath);
    }

    private AudioFinishRecorderListener audioFinishRecorderListener;

    public void setAudioFinishRecorderListener(
            AudioFinishRecorderListener listener) {
        audioFinishRecorderListener = listener;
    }

    /**
     * 屏幕的触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();// 获得x轴坐标
        int y = (int) event.getY();// 获得y轴坐标

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:

                if (isRecording) {
                    // 如果想要取消，根据x,y的坐标看是否需要取消
                    if (wantToCancle(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording || mTime < 0.6f) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1000);// 延迟显示对话框
                } else if (mCurrentState == STATE_RECORDING) { // 正在录音的时候，结束
                    mDialogManager.dimissDialog();
                    mAudioManager.release();
                    if (audioFinishRecorderListener != null) {
                        // audioFinishRecorderListener.onFinish(mTime,
                        // mAudioManager.getCurrentFilePath());
                        // 取mp3文件
                        audioFinishRecorderListener.onFinish(mTime,
                                mAudioManager.getCurrentFilePath());
                    }

                } else if (mCurrentState == STATE_WANT_TO_CANCEL) { // 想要取消
                    mDialogManager.dimissDialog();
                    mAudioManager.cancel();
                }
                reset();
                break;

        }
        return super.onTouchEvent(event);
    }

    /**
     * 恢复状态及标志位
     */
    private void reset() {
        isRecording = false;
        mTime = 0;
        mReady = false;
        changeState(STATE_NORMAL);
    }

    private boolean wantToCancle(int x, int y) {
        if (x < 0 || x > getWidth()) { // 超过按钮的宽度
            return true;
        }
        // 超过按钮的高度
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }

        return false;
    }

    /**
     * 改变
     */
    private void changeState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (state) {
                case STATE_NORMAL:
//				setBackgroundResource(R.drawable.fromclient_edit);
                    setText(R.string.str_recorder_normal);
                    break;

                case STATE_RECORDING:
//				setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
                    setText(R.string.str_recorder_recording);
                    if (isRecording) {
                        mDialogManager.recording();
                    }
                    break;

                case STATE_WANT_TO_CANCEL:
//				setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
                    setText(R.string.str_recorder_want_cancel);

                    mDialogManager.wantToCancel();
                    break;
            }
        }
    }
}
