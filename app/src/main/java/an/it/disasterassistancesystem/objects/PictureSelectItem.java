package an.it.disasterassistancesystem.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author AN
 *
 */
public class PictureSelectItem implements Parcelable {
	/*
	 * fields
	 */
	private String linkSDCard;
	private String name;
	//private boolean type;
	private boolean checked;
	
	
	/*
	 * constructor
	 */
	
	public String getLinkSDCard() {
		return linkSDCard;
	}
	public PictureSelectItem(String linkSDCard, String name, boolean checked) {
		super();
		this.linkSDCard = linkSDCard;
		this.name = name;
		this.checked = checked;
	}
	public void setLinkSDCard(String linkSDCard) {
		this.linkSDCard = linkSDCard;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/*public boolean isType() {
		return type;
	}
	public void setType(boolean type) {
		this.type = type;
	}*/
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(linkSDCard);
		out.writeString(name);
//		out.writeInt(type? 1:0);
		out.writeInt(checked? 1:0);
	}
	private void readFromParcel(Parcel in) {
		linkSDCard = in.readString();
		name=in.readString();
	//	type=in.readInt()==1? true:false;
		checked=in.readInt()==1? true:false;
	}
	public PictureSelectItem(Parcel in) {
		readFromParcel(in);
	}
 
	public static final Creator CREATOR =
	    	new Creator() {
	            public PictureSelectItem createFromParcel(Parcel in) {
	                return new PictureSelectItem(in);
	            }
	 
	            public PictureSelectItem[] newArray(int size) {
	                return new PictureSelectItem[size];
	            }
        };
	
	
	
}
