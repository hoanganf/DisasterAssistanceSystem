package an.it.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class AViewPager extends ViewPager{
	private boolean isBlockLeftSwipe=false;
	private boolean isBlockRightSwipe=false;
	private boolean isSwipeLeft=false;
	private Toast toast;
	private String blockMessage;
	private float oldX;

    public AViewPager(Context context) {
        super(context);
    }
    public AViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setBlockSwipe(boolean value){
    	this.isBlockLeftSwipe=value;
    	this.isBlockRightSwipe=value;
    }
    public boolean isBlockLeftSwipe() {
		return isBlockLeftSwipe;
	}

	public void setBlockLeftSwipe(boolean isBlockLeftSwipe) {
		setBlockSwipe(false);
		this.isBlockLeftSwipe = isBlockLeftSwipe;
	}

	public boolean isBlockRightSwipe() {
		return isBlockRightSwipe;
	}

	public void setBlockRightSwipe(boolean isBlockRightSwipe) {
		setBlockSwipe(false);
		this.isBlockRightSwipe = isBlockRightSwipe;
	}
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		//Log.e("aaaa", arg0.getRawX()+"|"+arg0.getAction());
		if(arg0.getAction()==MotionEvent.ACTION_DOWN){
			oldX=arg0.getX();

		}else if(arg0.getAction()==MotionEvent.ACTION_MOVE){
			float distance=getDistance(oldX,arg0);
			if(distance<0){//is Right
				isSwipeLeft=false;
				if (isBlockRightSwipe){
					if(distance<-50){
						//Log.e("DISTANCE",getDistance(oldX,arg0)+"");
						if(blockMessage!=null && blockMessage.length()>0) showAToast(blockMessage);
					}
		            return false;
		        }
			}else{//is Left
				isSwipeLeft=true;
				if (isBlockLeftSwipe){
					if(distance>50){
						//Log.e("DISTANCE",getDistance(oldX,arg0)+"");
						if(blockMessage!=null && blockMessage.length()>0) showAToast(blockMessage);
					}
		            return false;
		        }
			}

		}
	    return super.onTouchEvent(arg0);

	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		//Log.e("aaaa", arg0.getRawX()+"|"+arg0.getAction());
		if(arg0.getAction()==MotionEvent.ACTION_DOWN){
			oldX=arg0.getX();

		}else if(arg0.getAction()==MotionEvent.ACTION_MOVE){
			float distance=getDistance(oldX,arg0);
			if(distance<0){//is Right
				isSwipeLeft=false;
				if (isBlockRightSwipe){
					if(distance<-50){
						//Log.e("DISTANCE",getDistance(oldX,arg0)+"");
						if(blockMessage!=null && blockMessage.length()>0) showAToast(blockMessage);
					}
		            return false;
		        }
			}else{//is Left
				isSwipeLeft=true;
				if (isBlockLeftSwipe){
					if(distance>50){
						Log.e("DISTANCE",getDistance(oldX,arg0)+"");
						if(blockMessage!=null && blockMessage.length()>0) showAToast(blockMessage);
					}
		            return false;
		        }
			}

		}
        return super.onInterceptTouchEvent(arg0);
	}

	public String getBlockMessage() {
		return blockMessage;
	}

	public void setBlockMessage(String blockMessage) {
		this.blockMessage = blockMessage;
	}
	public void showAToast (String st){ //"Toast toast" is declared in the class
        try{
        	toast.getView().isShown();     // true if visible
            toast.setText(st);
        } catch (Exception e) {         // invisible if exception
            toast = Toast.makeText(getContext(), st, Toast.LENGTH_SHORT);
        }
        toast.show();  //finally display it
    }
	public float getDistance(float startX, float startY, MotionEvent ev) {
	     float distanceSum = 0;
	     final int historySize = ev.getHistorySize();
	     for (int h = 0; h < historySize; h++) {
	         // historical point
	         float hx = ev.getHistoricalX(0, h);
	         float hy = ev.getHistoricalY(0, h);
	         // distance between startX,startY and historical point
	         float dx = (hx-startX);
	         float dy = (hy-startY);
	         distanceSum += Math.sqrt(dx*dx+dy*dy);
	         // make historical point the start point for next loop iteration
	         startX = hx;
	         startY = hy;
	     }
	     // add distance from last historical point to event's point
	     float dx = (ev.getX(0)-startX);
	     float dy = (ev.getY(0)-startY);
	     distanceSum += Math.sqrt(dx*dx+dy*dy);
	     return distanceSum;
	 }
	public float getDistance(float startX, MotionEvent ev) {
	     return ev.getX()-startX;
	 }
	public boolean isSwipeLeft() {
		return isSwipeLeft;
	}
	public void setSwipeLeft(boolean isSwipeLeft) {
		this.isSwipeLeft = isSwipeLeft;
	}

}
