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
import an.it.disasterassistancesystem.objects.Employer;
import an.it.disasterassistancesystem.objects.Recruitment;

public class ListEmployerAdapter extends BaseAdapter {
    private Context cxt;
    private ArrayList<Employer> lst;
    private LayoutInflater inflater;

    public ListEmployerAdapter(Context cxt, ArrayList<Employer> lst) {
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
    public Employer getItem(int position) {
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup vGroup) {
        // TODO Auto-generated method stub
        Employer item = getItem(position);
        View rowView = convertView;
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.item_employer, null);
        }
        ImageView image = (ImageView) rowView.findViewById(R.id.imv_employer);
        TextView name = (TextView) rowView.findViewById(R.id.tv_employer_name);
        TextView address = (TextView) rowView.findViewById(R.id.tv_employer_address);
        TextView recruitment = (TextView) rowView.findViewById(R.id.tv_employer_recruitment);
        name.setSelected(true);
        //appname
        name.setText(item.getFullName());
        //
        address.setText(item.getAddress());
        //owner
        String textRecruitment="";
        if (item.getRecruitments() != null && item.getRecruitments().size() > 0) {
            for (int i=0;i<item.getRecruitments().size();i++) {
                Recruitment rc=item.getRecruitments().get(i);
                textRecruitment+=rc.getLicenseName()+": "+rc.getNumberDriver();
                if(rc.getNote()!=null&&rc.getNote().length()>0)textRecruitment+=(" ("+rc.getNote()+")");
                if(i<item.getRecruitments().size()-1) textRecruitment+="\n ";
            }
        }
        recruitment.setText(textRecruitment);
        //rating
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading().cacheOnDisc().cacheInMemory()
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showStubImage(R.drawable.ic_no_image)
                .showImageOnFail(R.drawable.ic_no_image)
                .showImageForEmptyUri(R.drawable.ic_no_image)
                .displayer(new SimpleBitmapDisplayer()).build();
        ImageLoader.getInstance().displayImage(item.getAvatarURL(), image, options);
        return rowView;
    }

}
