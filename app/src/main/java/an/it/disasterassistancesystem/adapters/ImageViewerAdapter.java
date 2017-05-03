package an.it.disasterassistancesystem.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import an.it.disasterassistancesystem.objects.PictureSelectItem;
import an.it.utils.FileManager;

/**
 * 
 * @author AN
 *
 */
public class ImageViewerAdapter extends PagerAdapter {
	private ArrayList<PictureSelectItem> lst;
	Context myContext;
	public ImageViewerAdapter(Context myContex,ArrayList<PictureSelectItem> lst) {
		this.lst=lst;
		this.myContext=myContex;
		// TODO Auto-generated constructor stub
	}
	 public int getCount() {
         return lst.size();
     }

     public Object instantiateItem(View collection, int position) {
        ImageView iv=new ImageView(myContext);
 	    iv.setImageBitmap(FileManager.resizeImageBitmap(lst.get(position).getLinkSDCard(), 1024,768 ));
        ((ViewPager) collection).addView(iv);
        return iv;
     }

     @Override
     public void destroyItem(View arg0, int arg1, Object arg2) {
         ((ViewPager) arg0).removeView((View) arg2);

     }


     @Override
     public boolean isViewFromObject(View arg0, Object arg1) {
         return arg0 == ((View) arg1);

     }

     @Override
     public Parcelable saveState() {
         return null;
     }

}
