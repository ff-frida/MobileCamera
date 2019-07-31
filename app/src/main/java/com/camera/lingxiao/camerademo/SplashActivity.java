package com.camera.lingxiao.camerademo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.camera.lingxiao.camerademo.audio.AudioActivity;
import com.camera.lingxiao.camerademo.crash.ContentValue;
import com.camera.lingxiao.camerademo.video.Camera2Activity;
import com.camera.lingxiao.camerademo.video.MainActivity;
import com.camera.lingxiao.camerademo.video.MediaExtractActivity;
import com.camera.lingxiao.camerademo.video.PlayActivity;
import com.camera.lingxiao.camerademo.video.VideoListActivity;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends BaseActivity{

    @BindView(R.id.button_extractor)
    Button mButtonExtractor;
    @BindView(R.id.button_camera)
    Button mButtonCamera;
    @BindView(R.id.button_audio)
    Button mButtonAudio;

    public static final int RC_CAMERA_AND_LOCATION = 1;

    private String[] perms = {Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO};

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        methodRequiresTwoPermission();
    }

    @OnClick({R.id.button_extractor,R.id.button_camera,R.id.button_audio,R.id.button_system_camera,R.id.button_camera2})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.button_extractor:
                startActivity(new Intent(getApplicationContext(), MediaExtractActivity.class));
                break;
            case R.id.button_camera:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.button_audio:
                startActivity(new Intent(getApplicationContext(), AudioActivity.class));
                break;
            case R.id.button_system_camera:
                startActivity(new Intent(getApplicationContext(), SystemCameraActivity.class));
                break;
            case R.id.button_camera2:
                startActivity(new Intent(getApplicationContext(), Camera2Activity.class));
                break;
        }
    }

    @OnClick(R.id.button_player)
    public void onGetPlayList(View v){
        Intent intent = new Intent(getApplicationContext(),VideoListActivity.class);
        startActivity(intent);

        /*showProgressDialog();
        Observable.create(new ObservableOnSubscribe<String[]>() {
            @Override
            public void subscribe(ObservableEmitter<String[]> emitter) {
                List<String> pcmList = FileUtil.getFileList(ContentValue.MAIN_PATH);
                String[] files = pcmList.toArray(new String[pcmList.size()]);
                emitter.onNext(files);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String[]>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(String[] files) {
                        cancelProgressDialog();
                        showDialog(files);
                        mDisposable.dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });*/
    }

    private void showDialog(final String[] files) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择播放文件");
        builder.setItems(files, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*AudioDecoder.getInstance()
                        .startDecodeFromMPEG_4(files[i]);*/
                Intent intent = new Intent(getApplicationContext(),PlayActivity.class);
                intent.putExtra("path",files[i]);
                startActivity(intent);
            }
        });
        builder.show();
    }
    private void methodRequiresTwoPermission() {
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "需要同意权限",
                    RC_CAMERA_AND_LOCATION, perms);
        } else {
            createPath();
        }
    }

    private void createPath() {
        File rootDir = new File(ContentValue.MAIN_PATH);
        if (rootDir.exists()) {
            if (rootDir.isFile()) {
                rootDir.delete();
                rootDir.mkdirs();
            }
        } else {
            rootDir.mkdirs();
        }
    }

}
