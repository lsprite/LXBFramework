package com.buihha.audiorecorder;

import com.czt.mp3recorder.MP3Recorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AudioManager {
	// private MediaRecorder mMediaRecorder;
	/**
	 * 采样频率
	 */
	MP3Recorder recorder;
	private String mDir;
	private String mCurrentFilePath;
	private static AudioManager mInstance;

	private boolean isPrepare;

	private AudioManager(String dir) {
		mDir = dir;
	}

	public static AudioManager getInstance(String dir) {
		if (mInstance == null) {
			synchronized (AudioManager.class) {
				if (mInstance == null) {
					mInstance = new AudioManager(dir);
				}
			}
		}
		return mInstance;
	}

	/**
	 * 使用接口 用于回调
	 */
	public interface AudioStateListener {
		void wellPrepared();
	}

	public AudioStateListener mAudioStateListener;

	/**
	 * 回调方法
	 */
	public void setOnAudioStateListener(AudioStateListener listener) {
		mAudioStateListener = listener;
	}

	// 去准备
	public void prepareAudio() {
		try {
			isPrepare = false;
			File dir = new File(mDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String fileName = generateFileName() + ".mp3";
			File file = new File(dir, fileName);
			mCurrentFilePath = file.getAbsolutePath();
			recorder = new MP3Recorder(file);
			recorder.start();
			// mMediaRecorder = new MediaRecorder();
			// // 设置MediaRecorder的音频源为麦克风
			// mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// // 设置音频格式
			// mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			// // 设置音频编码
			// mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			// // 设置输出文件
			// mMediaRecorder.setOutputFile(file.getAbsolutePath());
			// // 准备录音
			// mMediaRecorder.prepare();
			// // 开始
			// mMediaRecorder.start();
			// 准备结束
			isPrepare = true;
			if (mAudioStateListener != null) {
				mAudioStateListener.wellPrepared();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
			System.out.println("IllegalStateException");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 随机生成文件的名称
	 */
	private String generateFileName() {
		return UUID.randomUUID().toString();
		// return UUID.randomUUID().toString() + ".raw";
	}

	public int getVoiceLevel(int maxlevel) {
		return recorder.getVoiceLevel(maxlevel);
	}

	/**
	 * 释放资源
	 */
	public void release() {
		try {
			recorder.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mMediaRecorder.stop();
		// mMediaRecorder.reset();
		// mMediaRecorder = null;
		// 开始转换
		// System.out.println("开始转换");
		// FLameUtils lameUtils = new FLameUtils(1, SAMPLE_RATE, 96);
		// String mp3_path = mCurrentFilePath.replace(".raw", ".mp3");
		// boolean convertOk = lameUtils.raw2mp3(mCurrentFilePath, mp3_path);
		// System.out.println(convertOk + "转换完成");
		// File file = new File(mCurrentFilePath);
		// if (file.exists()) {
		// file.delete();
		// file = null;
		// }
		// 删掉.raw文件
		// mRecorder.stop();
		// isPrepare = false;
		// mRecorder = null;
	}

	/**
	 * 取消录音
	 */
	public void cancel() {
		release();
		if (mCurrentFilePath != null) {
			File file = new File(mCurrentFilePath);
			file.delete();
			mCurrentFilePath = null;
		}

	}

	public String getCurrentFilePath() {

		return mCurrentFilePath;
	}

}
