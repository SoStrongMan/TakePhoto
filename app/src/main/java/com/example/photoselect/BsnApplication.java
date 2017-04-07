package com.example.photoselect;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

public class BsnApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initGalleryFinal();
    }

    /**
     * 初始化GalleryFinal
     */
    private void initGalleryFinal() {
        //配置主题
        ThemeConfig theme = new ThemeConfig.Builder()
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true) //开启相机功能
                .setEnableEdit(false) //开启编辑功能
                .setEnableCrop(true) //开启裁剪功能
                .setEnableRotate(true) //开启选择功能
                .setCropSquare(true) //裁剪正方形
                .setEnablePreview(false) //开启预览功能
                .setCropWidth(200)//裁剪宽度
                .setCropHeight(200)//裁剪高度
                .setForceCrop(true) //启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
                .build();
        //设置核心配置信息
        CoreConfig coreConfig = new CoreConfig.Builder(this, new GlideImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setEditPhotoCacheFolder(new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + "test")) //编辑后的照片保存路径
//                .setTakePhotoFolder(new File(Environment.getExternalStorageDirectory().getAbsolutePath()
//                        + File.separator + "test", System.currentTimeMillis() + ".jpg")) //拍的照片保存路径
                .build();
        GalleryFinal.init(coreConfig);
    }
}
