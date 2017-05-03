package an.it.disasterassistancesystem.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

import an.it.disasterassistancesystem.R;
import an.it.disasterassistancesystem.objects.Driver;
import an.it.disasterassistancesystem.objects.License;

public class ListDriverAdapter extends BaseAdapter {
    private Context cxt;
    private ArrayList<Driver> lst;
    private LayoutInflater inflater;

    public ListDriverAdapter(Context cxt, ArrayList<Driver> lst) {
        this.cxt = cxt;
        this.lst = lst;
        this.inflater = (LayoutInflater) cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (lst != null) return lst.size();
        else return 0;
    }

    @Override
    public Driver getItem(int position) {
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup vGroup) {
        // TODO Auto-generated method stub
        Driver item = getItem(position);
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.item_driver, null);
        }
        ImageView image = (ImageView) rowView.findViewById(R.id.imv_driver);
        TextView name = (TextView) rowView.findViewById(R.id.tv_driver_name);
        TextView licenses = (TextView) rowView.findViewById(R.id.tv_driver_licenses);
        TextView rating = (TextView) rowView.findViewById(R.id.tv_driver_rating);
        name.setSelected(true);
        //appname
        name.setText(item.getFullName());
        //owner
        String textLicenses = "";
        if (item.getLicenses() != null && item.getLicenses().size() > 0) {
            for (int i = 0; i < item.getLicenses().size(); i++) {
                License li = item.getLicenses().get(i);
                textLicenses += li.getName();
                if (i < item.getLicenses().size() - 1) textLicenses += ", ";
            }
        }
        licenses.setText(textLicenses);
        //rating
        rating.setText(String.valueOf(5));
        ImageLoader.getInstance().displayImage(item.getAvatarURL(), image, new DisplayImageOptions.Builder()
                .resetViewBeforeLoading().cacheOnDisc().cacheInMemory()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showStubImage(R.drawable.ic_no_image)
                .showImageOnFail(R.drawable.ic_no_image)
                .showImageForEmptyUri(R.drawable.ic_no_image)
                .displayer(new SimpleBitmapDisplayer()).build());
        return rowView;
    }

}
