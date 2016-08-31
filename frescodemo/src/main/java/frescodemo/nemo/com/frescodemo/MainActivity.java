/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package frescodemo.nemo.com.frescodemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.fresco.library.configs.ConfigConstants;
import com.facebook.fresco.library.instrumentation.InstrumentedDraweeView;
import com.facebook.fresco.library.instrumentation.PerfListener;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {
	private static final String TAG = "FrescoSample";

	private static final long STATS_CLOCK_INTERVAL_MS = 1000;// 内存状态等刷新延迟
	private static final int DEFAULT_MESSAGE_SIZE = 1024;// 默认StringBuilder的缓存区域
	private static final int BYTES_IN_MEGABYTE = 1024 * 1024;//Byte装MB
	// 实时刷新内存状态等信息
	private Handler mHandler;
	private Runnable mStatsClockTickRunnable;

	private TextView mStatsDisplay;
	private ListView mImageList;
	private ArrayList<String> mImageUrls = new ArrayList<String>();

	private FrescoAdapter<InstrumentedDraweeView> mCurrentAdapter;
	private PerfListener mPerfListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FLog.setMinimumLoggingLevel(FLog.WARN);// 日志打印等级
		ConfigConstants.init(getResources());// 初始化默认图片（占位图，错误图）
		Fresco.initialize(this, ConfigConstants.getImagePipelineConfig(this));// 图片缓存初始化配置
		// 刷新内存占用率等状态
		mHandler = new Handler(Looper.getMainLooper());
		mStatsClockTickRunnable = new Runnable() {
			@Override
			public void run() {
				updateStats();
				scheduleNextStatsClockTick();
			}
		};
		// 布局
		mStatsDisplay = (TextView) findViewById(R.id.stats_display);
		mImageList = (ListView) findViewById(R.id.image_list);

		reloadUrls();// 读取链接地址
		mPerfListener = new PerfListener();// 监听
		mCurrentAdapter = new FrescoAdapter<InstrumentedDraweeView>(this, R.id.image_list, mImageUrls, mPerfListener);
		mImageList.setAdapter(mCurrentAdapter);

		updateStats();
	}

	private void reloadUrls() {
		mImageUrls.add("http://img32.mtime.cn/up/2013/07/20/142428.27146212_500.jpg");
		mImageUrls.add("https://www.google.com/logos/doodles/2015/81st-anniversary-of-the-loch-ness-monsters-most-famous-photograph-4847834381680640-hp.gif");
		mImageUrls.add("http://g.hiphotos.baidu.com/baike/w%3D268/sign=66d17ed667380cd7e61ea5eb9945ad14/e61190ef76c6a7ef18b42940fffaaf51f2de66c2.jpg");
		mImageUrls.add("http://s1.dwstatic.com/group1/M00/B7/A5/9e17c82bae2fc22df427b09ae317eaaa.gif");
		mImageUrls.add("http://img32.mtime.cn/up/2013/07/20/142420.11265268_500.jpg");
		mImageUrls.add("http://img32.mtime.cn/up/2013/07/20/142352.84233298_500.jpg");
		mImageUrls.add("http://img32.mtime.cn/up/2013/07/20/142234.22690934_500.jpg");
		mImageUrls.add("http://img32.mtime.cn/up/2013/07/20/142140.58842929_500.jpg");
		mImageUrls.add("http://img32.mtime.cn/up/2013/07/20/142204.46977964_500.jpg");
		mImageUrls.add("http://img32.mtime.cn/up/2013/07/20/142406.96541771_500.jpg");
		mImageUrls.add("http://img32.mtime.cn/up/2013/07/20/142315.72377310_500.jpg");
		mImageUrls.add("http://img32.mtime.cn/up/2013/07/20/142303.81804449_500.jpg");
		mImageUrls.add("http://img32.mtime.cn/up/2013/07/20/142251.40035406_500.jpg");
		mImageUrls.add("http://img32.mtime.cn/up/2013/07/20/142329.63833494_500.jpg");
	}

	@Override
	protected void onStart() {
		super.onStart();
		updateStats();
		scheduleNextStatsClockTick();
	}

	protected void onStop() {
		super.onStop();
		cancelNextStatsClockTick();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Fresco.shutDown();// 关闭图片缓存～注意：一定要关闭，C++里new的空间
	}

	/**
	 * 延迟刷新当前内存等状态
	 */
	private void scheduleNextStatsClockTick() {
		mHandler.postDelayed(mStatsClockTickRunnable, STATS_CLOCK_INTERVAL_MS);
	}

	/**
	 * 取消刷新内存等状态
	 */
	private void cancelNextStatsClockTick() {
		mHandler.removeCallbacks(mStatsClockTickRunnable);
	}

	private void updateStats() {
		final Runtime runtime = Runtime.getRuntime();
		final long heapMemory = runtime.totalMemory() - runtime.freeMemory();
		final StringBuilder sb = new StringBuilder(DEFAULT_MESSAGE_SIZE);
		appendSize(sb, "Java heap size:          ", heapMemory, "\n");
		appendSize(sb, "Native heap size:        ", Debug.getNativeHeapSize(), "\n");
		appendTime(sb, "Average photo wait time: ", mPerfListener.getAverageWaitTime(), "\n");
		appendNumber(sb, "Outstanding requests:    ", mPerfListener.getOutstandingRequests(), "\n");
		appendNumber(sb, "Cancelled requests:      ", mPerfListener.getCancelledRequests(), "\n");
		final String message = sb.toString();
		mStatsDisplay.setText(message);
		FLog.i(TAG, message);
	}

	private static void appendSize(StringBuilder sb, String prefix, long bytes, String suffix) {
		String value = String.format(Locale.getDefault(), "%.2f", (float) bytes / BYTES_IN_MEGABYTE);
		appendValue(sb, prefix, value + " MB", suffix);
	}

	private static void appendTime(StringBuilder sb, String prefix, long timeMs, String suffix) {
		appendValue(sb, prefix, timeMs + " ms", suffix);
	}

	private static void appendNumber(StringBuilder sb, String prefix, long number, String suffix) {
		appendValue(sb, prefix, number + "", suffix);
	}

	private static void appendValue(StringBuilder sb, String prefix, String value, String suffix) {
		sb.append(prefix).append(value).append(suffix);
	}
}
