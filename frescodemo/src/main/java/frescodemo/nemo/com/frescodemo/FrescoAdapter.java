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

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.fresco.library.configs.ConfigConstants;
import com.facebook.fresco.library.instrumentation.Instrumented;
import com.facebook.fresco.library.instrumentation.InstrumentedDraweeView;
import com.facebook.fresco.library.instrumentation.PerfListener;
import com.facebook.imagepipeline.request.ImageRequest;

import java.util.ArrayList;

/** Populate the list view with images using the Fresco image pipeline. */
public class FrescoAdapter<V extends View & Instrumented> extends ArrayAdapter<String> {
	private final PerfListener mPerfListener;
	public FrescoAdapter(Context context, int resourceId,ArrayList<String> mImageUrls, PerfListener perfListener) {
		super(context, resourceId,mImageUrls);
		mPerfListener = perfListener;
	}

	protected InstrumentedDraweeView createView() {
		GenericDraweeHierarchy gdh = ConfigConstants.getGenericDraweeHierarchy(getContext());
		return new InstrumentedDraweeView(getContext(), gdh);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		InstrumentedDraweeView view = InstrumentedDraweeView.class.isInstance(convertView) ? (InstrumentedDraweeView) convertView : createView();

		int size = calcDesiredSize(parent.getWidth(), parent.getHeight());
		updateViewLayoutParams(view, size, size);

		String uri = getItem(position);
		view.initInstrumentation(uri, mPerfListener);
		ImageRequest imageRequest = ConfigConstants.getImageRequest(view,uri);
		DraweeController draweeController = ConfigConstants.getDraweeController(imageRequest, view);
		view.setController(draweeController);
		return view;
	}

	//横竖屏计算图片宽高
	private int calcDesiredSize(int parentWidth, int parentHeight) {
		int orientation = getContext().getResources().getConfiguration().orientation;
		int desiredSize = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? parentHeight / 2 : parentHeight / 3;
		return Math.min(desiredSize, parentWidth);
	}
	//横竖屏设置图片宽高
	private static void updateViewLayoutParams(View view, int width, int height) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		if (layoutParams == null || layoutParams.height != width || layoutParams.width != height) {
			layoutParams = new AbsListView.LayoutParams(width, height);
			view.setLayoutParams(layoutParams);
		}
	}

}
