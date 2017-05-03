package an.it.disasterassistancesystem.adapters;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.objects.PictureSelectItem;
import an.it.utils.FileManager;

public class GalleryAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater inflater;
	private ArrayList<PictureSelectItem> list;
	private boolean isChoose=false;
	public GalleryAdapter(Context c,ArrayList<PictureSelectItem> list) {
		mContext = c;
		this.list=list;
		inflater=(LayoutInflater)mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		PictureSelectItem item=getItem(position);
		ImageView imageView;
		if (convertView == null) {
			imageView = (ImageView) inflater.inflate(R.layout.item_grid_image, parent, false);
		} else {
			imageView = (ImageView) convertView;
		}
	    imageView.setImageBitmap(FileManager.resizeImageBitmap(item.getLinkSDCard(), 128, 128));
	    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	    if(item.isChecked()) imageView.setBackgroundResource(R.drawable.take_photo_border);
	    else imageView.setBackgroundResource(R.color.bk_main);
	    
	    return imageView;
	}
	@Override
	public int getCount() {
		if(list==null) return 0;
		else return list.size();
	}
	@Override
	public PictureSelectItem getItem(int pos) {
		if(list==null) return null;
		else return list.get(pos);
	}
	@Override
	public long getItemId(int pos) {
		return pos;
	}
	public void setList(ArrayList<PictureSelectItem> list){
		this.list=list;
		this.notifyDataSetInvalidated();
	}
	public void showFullImage(int pos){
		Dialog dialog = new Dialog(mContext,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
		dialog.setContentView(R.layout.content_description_image);
	    ImageViewerAdapter adapter = new ImageViewerAdapter(mContext,list);
	    ViewPager myPager=(ViewPager)dialog.findViewById(R.id.viewpager);
	    myPager.setAdapter(adapter);
	    myPager.setCurrentItem(pos);
	    dialog.show();
	}
	public boolean isChoose() {
		return isChoose;
	}
	public void setChoose(boolean isChoose) {
		this.isChoose = isChoose;
	}
	
}
