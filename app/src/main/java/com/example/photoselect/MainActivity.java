package com.example.photoselect;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.iv)
    ImageView ivImg;

    private static final int BAIDU_PERMISSION = 0x10;
    private static final int REQUEST_CAMERA = 0x01;
    private static final int REQUEST_ALBUM = 0x02;
    private static final int REQUEST_CROP = 0x03;
    private static final String IMAGE = "image/*";
    private File mImgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //android6.0后需要动态获取的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        BAIDU_PERMISSION);
            }
        }
    }

    /**
     * 创建文件夹
     */
    private void createFile() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "test");
        if (!file.exists()) {
            file.mkdir();
        }
        mImgFile = new File(file, System.currentTimeMillis() + ".jpg");
    }

    /**
     * 剪切照片
     *
     * @param uri 照片的uri
     */
    private void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImgFile));
        startActivityForResult(intent, REQUEST_CROP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case BAIDU_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //允许后的操作
                } else {
                    //禁止后的操作
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CAMERA:
                cropImage(Uri.fromFile(mImgFile));
                break;
            case REQUEST_ALBUM:
                createFile();
                Uri uri = data.getData();
                if (uri != null) {
                    cropImage(uri);
                }
                ivImg.setImageURI(Uri.fromFile(mImgFile));
                break;
            case REQUEST_CROP:
                ivImg.setImageURI(Uri.fromFile(mImgFile));
                break;
        }
    }

    @OnClick({R.id.btn_1, R.id.btn_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                new AlertDialog.Builder(this)
                        .setItems(new String[]{"拍照", "从相册选择"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: //拍照
                                        createFile();
                                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImgFile));
                                        startActivityForResult(cameraIntent, REQUEST_CAMERA);
                                        break;
                                    case 1: //从相册选择
                                        Intent albumIntent = new Intent(Intent.ACTION_PICK);
                                        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE);
                                        startActivityForResult(albumIntent, REQUEST_ALBUM);
                                        break;
                                }
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.btn_2:
                new AlertDialog.Builder(this)
                        .setItems(new String[]{"拍照", "从相册选择"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: //拍照
                                        GalleryFinal.openCamera(REQUEST_CAMERA, mOnHanlderResultCallback);
                                        break;
                                    case 1: //从相册选择
                                        GalleryFinal.openGalleryMuti(REQUEST_ALBUM, 3, mOnHanlderResultCallback);
                                        break;
                                }
                            }
                        })
                        .create()
                        .show();
                break;
        }
    }

    /**
     * GalleryFinal的回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            for (PhotoInfo info : resultList) {
                switch (reqeustCode) {
                    case REQUEST_CAMERA:
                        ivImg.setImageURI(Uri.fromFile(new File(info.getPhotoPath())));
                        break;
                    case REQUEST_ALBUM:
                        System.out.println("路径为：" + info.getPhotoPath());
                        break;
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };
}
