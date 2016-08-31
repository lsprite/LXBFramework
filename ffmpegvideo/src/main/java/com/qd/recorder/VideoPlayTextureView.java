package com.qd.recorder;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

public class VideoPlayTextureView extends TextureView implements
		TextureView.SurfaceTextureListener, OnPreparedListener,
		OnCompletionListener {

	private MediaPlayer mediaPlayer;
	private Surface surface;
	private MediaStateLitenser mediaStateLitenser;
	private MediaState currentMediaState = MediaState.RESET;
	private boolean isChange = true;//����������Ƶ�ļ�ʱ���жϵ�ǰSurfaceView�Ƿ���֮ǰ��SurfaceView
	
	public void setChange(boolean change){
		isChange = change;
	}
	
	public boolean isChange(){
		return isChange;
	}

	public VideoPlayTextureView(Context context) {
		super(context);
		init(context);
	}

	public VideoPlayTextureView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		init(context);
	}

	public VideoPlayTextureView(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		init(paramContext);
	}

	/**
	 * ��ʼ�����
	 * @param context
	 */
	private void init(Context context) {
		mediaPlayer = new MediaPlayer();
		setSurfaceTextureListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnPreparedListener(this);
	}

	protected void onMeasure(int paramInt1, int paramInt2) {
		int i = MeasureSpec.getSize(paramInt1);
		setMeasuredDimension(i, i);
	}

	/**
	 * ���ŵ�ǰ����Ƶ������Ѿ��ڲ�������ͣ�����򶼿�ʼ����
	 */
	public void play() {
		if(currentMediaState ==  MediaState.PLAY){
			currentMediaState = MediaState.PAUSE;
			if (mediaPlayer != null)
				mediaPlayer.pause();
			if(mediaStateLitenser != null)
				mediaStateLitenser.OnPauseListener();
		}else{
			currentMediaState = MediaState.PLAY;
			if(mediaStateLitenser != null)
				mediaStateLitenser.OnPlayListener();
			if (mediaPlayer != null && !mediaPlayer.isPlaying())
				mediaPlayer.start();
		}
	}

	/**
	 * ��ͣ����
	 */
	public void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()){
			currentMediaState = MediaState.PAUSE;
			mediaPlayer.pause();
			if(mediaStateLitenser != null)
				mediaStateLitenser.OnPauseListener();
		}
	}

	/**
	 * ֹͣ���ţ���ʱûʹ��
	 */
	public void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			currentMediaState = MediaState.RESET;
			mediaPlayer.stop();
			mediaPlayer.release();
		}
	}
	
	/**
	 * ���õ�ǰmediaPlyer����listView���øÿؼ�ʱ����
	 */
	public void reset(){
		currentMediaState = MediaState.RESET;
		mediaPlayer.reset();
	}

	/**
	 * ׼������֮ǰ��׼��������ͬ�����ã��ʺ����ļ�
	 * �������������asyncPrepare()
	 * @param path
	 */
	public void prepare(String path) {
		try {
			currentMediaState = MediaState.PREPARE;
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(path);
			mediaPlayer.setSurface(surface);
			mediaPlayer.prepare();
		} catch (Exception e) {
		}
	}
	
	public MediaState getMediaState(){
		return currentMediaState;
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,
			int arg2) {
		surface = new Surface(arg0);
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
		return false;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
			int arg2) {

	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture arg0) {

	}

	/**
	 * ���ò���״̬�ļ���
	 * @param mediaStateLitenser
	 */
	public void setMediaStateLitenser(MediaStateLitenser mediaStateLitenser) {
		this.mediaStateLitenser = mediaStateLitenser;
	}

	/**
	 * ����״̬
	 * @author QD
	 *
	 */
	public enum MediaState {
		RESET(0x5),PREPARE(0x1), COMPLETE(0x2), PLAY(0x3), PAUSE(0x4);
		static MediaState mapIntToValue(final int stateInt) {
			for (MediaState value : MediaState.values()) {
				if (stateInt == value.getIntValue()) {
					return value;
				}
			}
			return RESET;
		}

		private int mIntValue;

		MediaState(int intValue) {
			mIntValue = intValue;
		}

		int getIntValue() {
			return mIntValue;
		}
	}
	
	/**
	 * ��ʼ��Ƶ�ļ������ӵ���
	 */
	public void OnDownLoadingListener(){
		if (mediaStateLitenser != null)
			mediaStateLitenser.OnDownLoadingListener();
	}

	public interface MediaStateLitenser {
		public void OnCompletionListener();

		public void OnPrepareListener();

		public void OnPauseListener();

		public void OnPlayListener();
		
		public void OnDownLoadingListener();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (mediaStateLitenser != null)
			mediaStateLitenser.OnCompletionListener();
		currentMediaState = MediaState.COMPLETE;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		if (mediaStateLitenser != null)
			mediaStateLitenser.OnPrepareListener();
	}
}
