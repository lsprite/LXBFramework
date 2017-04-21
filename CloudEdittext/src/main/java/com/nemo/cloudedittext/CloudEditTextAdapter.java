package com.nemo.cloudedittext;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.nemo.cloudedittext.bean.ContactBean;

public class CloudEditTextAdapter extends BaseAdapter implements Filterable {
	private ArrayFilter mFilter;
	private ArrayList<ContactBean> mList;
	private Context context;
	private ArrayList<ContactBean> mUnfilteredData;

	public ArrayList<ContactBean> getmUnfilteredData() {
		return mUnfilteredData;
	}

	public CloudEditTextAdapter(Context context, ArrayList<ContactBean> mList) {
		this.mList = mList;
		this.context = context;
	}

	@Override
	public int getCount() {

		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.cloud_edittext_actv_item,
					null);

			holder = new ViewHolder();
			holder.t1 = (TextView) view.findViewById(R.id.name);
			holder.t2 = (TextView) view.findViewById(R.id.email);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		ContactBean bean = mList.get(position);
		holder.t1.setText(bean.getName());
		holder.t2.setText(bean.getEmail());

		return view;
	}

	static class ViewHolder {
		public TextView t1;
		public TextView t2;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mUnfilteredData == null) {
				mUnfilteredData = new ArrayList<ContactBean>(mList);
			}

			if (prefix == null || prefix.length() == 0) {
				ArrayList<ContactBean> list = mUnfilteredData;
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<ContactBean> unfilteredValues = mUnfilteredData;
				int count = unfilteredValues.size();

				ArrayList<ContactBean> newValues = new ArrayList<ContactBean>(
						count);

				for (int i = 0; i < count; i++) {
					ContactBean pc = unfilteredValues.get(i);
					if (pc != null) {
						if (pc.getName().indexOf(prefixString.toString()) > -1) {
							newValues.add(pc);
						} else if (pc.getEmail().indexOf(
								prefixString.toString()) > -1) {
							newValues.add(pc);
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// noinspection unchecked
			mList = (ArrayList<ContactBean>) results.values;
			// System.out.println("++++publishResults:" + mList.size());
			// System.out.println("++++publishResults:" +
			// mUnfilteredData.size());
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}

}
