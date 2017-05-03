package an.it.disasterassistancesystem.applications;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import an.it.disasterassistancesystem.objects.User;

public class DASApplication extends Application {
	private User user;
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader();
	}

	@SuppressWarnings("deprecation")
	private void initImageLoader() {
		WindowManager windowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.memoryCacheExtraOptions(screenWidth, screenHeight)
				.discCacheExtraOptions(screenWidth, screenHeight,
						CompressFormat.PNG, 75)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
				.offOutOfMemoryHandling()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 768))
				.memoryCacheSize(2 * 1024 * 1024)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileCount(1000)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.imageDownloader(
						new BaseImageDownloader(getApplicationContext())) // default
				.tasksProcessingOrder(QueueProcessingType.FIFO) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.enableLogging().build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onLowMemory() {
		ImageLoader.getInstance().clearMemoryCache();
		super.onLowMemory();
	}

	public User getUser() {
		return new User("hoanganf", 1, "https://i.stack.imgur.com/hGvbT.jpg?s=328&g=1","Hoang An", "02/11/1990","Japan", "hoangnaf@gmail.com","07013194329") ;
		//return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
