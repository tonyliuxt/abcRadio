package com.baby.play.abc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainAdapter extends BaseAdapter{

	private List<MainVO> mData;
    private LayoutInflater mInflater;
    
	public MainAdapter(Context context, List<MainVO> data){
		mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}
	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return mData.get(position).getContentId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		viewHolder = new ViewHolder();
		
		convertView = mInflater.inflate(R.layout.grid_item, parent, false);
		
		viewHolder.mainBackground = (ImageView)convertView.findViewById(R.id.gridViewMainBackground);
		
		viewHolder.mainBackground.setBackgroundResource(mData.get(position).getBackgroundSourceId());
		
		viewHolder.nameText = (TextView)convertView.findViewById(R.id.gridViewPersonName);
		
		if(mData.get(position) != null && mData.get(position).getName() != null){
			viewHolder.nameText.setText(mData.get(position).getName());
		}

		return convertView;
	}
	
	public  class ViewHolder{
		private ImageView mainBackground;
		private TextView nameText;
	}
}
