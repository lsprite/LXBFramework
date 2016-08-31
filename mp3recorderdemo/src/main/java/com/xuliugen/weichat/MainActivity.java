package com.xuliugen.weichat;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.buihha.audiorecorder.AudioRecorderButton;
import com.buihha.audiorecorder.AudioRecorderButton.AudioFinishRecorderListener;
import com.buihha.audiorecorder.MediaManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
	private ListView mListView;

	private ArrayAdapter<Recorder> mAdapter;
	private List<Recorder> mDatas = new ArrayList<Recorder>();

	private AudioRecorderButton mAudioRecorderButton;

	private View animView;
	private boolean isPlaying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mListView = (ListView) findViewById(R.id.id_listview);
		mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.id_recorder_button);
		// 设置录音完成时的操作
		mAudioRecorderButton
				.setAudioFinishRecorderListener(new AudioFinishRecorderListener() {

					public void onFinish(float seconds, String filePath) {
						System.out.println("filePath:" + filePath);
						Recorder recorder = new Recorder(seconds, filePath);
						mDatas.add(recorder);
						mAdapter.notifyDataSetChanged(); // 通知更新的内容
						mListView.setSelection(mDatas.size() - 1); // 将lisview设置为最后一个
					}
				});

		mAdapter = new RecoderAdapter(this, mDatas);
		mListView.setAdapter(mAdapter);

		// listView的item点击事件
		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				if (isPlaying) {
					isPlaying = false;
					animView.clearAnimation();
					animView.setBackgroundResource(R.drawable.adj);
					MediaManager.release();
				} else {
					isPlaying = true;
					// 播放动画（帧动画）
					if (animView != null) {
						animView.setBackgroundResource(R.drawable.adj);
						animView = null;
					}
					animView = view.findViewById(R.id.id_recoder_anim);
					animView.setBackgroundResource(R.drawable.play_anim);
					AnimationDrawable animation = (AnimationDrawable) animView
							.getBackground();
					animation.start();
					System.out.println(mDatas.get(position).filePath);
					// 播放录音
					MediaManager.playSound(mDatas.get(position).filePath,
							new MediaPlayer.OnCompletionListener() {
								// 播放完成后的操作
								public void onCompletion(MediaPlayer mp) {
									animView.setBackgroundResource(R.drawable.adj);
								}
							});
				}

			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		MediaManager.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MediaManager.resume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MediaManager.release();
	}

	class Recorder {
		float time;
		String filePath;

		public Recorder(float time, String filePath) {
			super();
			this.time = time;
			this.filePath = filePath;
		}

		public float getTime() {
			return time;
		}

		public void setTime(float time) {
			this.time = time;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

	}

}