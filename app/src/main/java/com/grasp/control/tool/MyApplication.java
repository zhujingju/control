package com.grasp.control.tool;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.Player.Core.PlayerClient;
import com.map.LogcatHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.videogo.openapi.EZOpenSDK;

import org.litepal.LitePalApplication;

/**
 * Created by zhujingju on 2017/8/17.
 */

public class MyApplication extends LitePalApplication {

    // 上下文菜单
    private Context mContext;
    public static Context applicationContext;
    public static Application application;
    private PlayerClient playerclient;
    public final  static String NAME_TX="zjj_Name_TX";

    @Override
    public void onCreate() {

        playerclient = new PlayerClient();
        WriteLogThread writeLogThread = new WriteLogThread(playerclient);
        writeLogThread.start();
        super.onCreate();
        application=this;
        mContext = this;
        applicationContext = getApplicationContext();
        initImageLoader(mContext);
        initSDK();
        /** sxg 监测Logcat 此方法可以抓取所有logcat start */
        LogcatHelper.getInstance(this).start();
        /** sxg 监测Logcat end */
    }

    /**
     * ImageLoader 图片组件初始化
     *
     * @param context
     */
    public static DisplayImageOptions options;
    public static DisplayImageOptions options2;
    // 使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader-1.8.6-with-sources.jar
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    //    public static ImageLoadingListener animateFirstListener = (ImageLoadingListener) new AnimateFirstDisplayListener();
    public static ImageLoaderConfiguration iconfig;


    public static void initImageLoader(Context context) {
        iconfig = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(1000, 1000) // max width, max height，即保存的每个缓存文件的最大长宽
//    			    .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
//    			    .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建


        options = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.camera)
                //.showImageForEmptyUri(R.drawable.camera)
                //.showImageOnFail(R.drawable.camera)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
                // 		.delayBeforeLoading(500)   //int delayInMillis为你设置的下载前的延迟时间
// 		  .displayer(new FadeInBitmapDisplayer(300))
                .displayer(new RoundedBitmapDisplayer(0))//是否设置为圆角，弧度为多少
                .build();

        options2 = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.camera)
                //.showImageForEmptyUri(R.drawable.camera)
                //.showImageOnFail(R.drawable.camera)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
                // 		.delayBeforeLoading(500)   //int delayInMillis为你设置的下载前的延迟时间
// 		  .displayer(new FadeInBitmapDisplayer(300))
                .displayer(new RoundedBitmapDisplayer(180))//是否设置为圆角，弧度为多少
                .build();

        imageLoader = ImageLoader.getInstance();
        //imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.init(iconfig);
    }

    //开发者需要填入自己申请的appkey
    public static String AppKey = "272a51a679e34ffba3542ed5b8c5c2de";

    public static EZOpenSDK getOpenSDK() {
        return EZOpenSDK.getInstance();
    }

    private void initSDK() {
        {
            /**
             * sdk日志开关，正式发布需要去掉
             */
            EZOpenSDK.showSDKLog(false);

            /**
             * 设置是否支持P2P取流,详见api
             */
            EZOpenSDK.enableP2P(false);

            /**
             * APP_KEY请替换成自己申请的
             */
            EZOpenSDK.initLib(this, AppKey, "");

        }
    }




    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub

        super.onTerminate();
    }

    public synchronized PlayerClient getPlayerclient() {
        return playerclient;
    }

    public synchronized void releaseClient() {
        playerclient = null;
    }

    public synchronized void setPlayerclient(PlayerClient playerclient) {
        this.playerclient = playerclient;
    }
}
