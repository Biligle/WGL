package com.shanshan.housekeeper.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.shanshan.housekeeper.Help.base.BaseActivity;
import com.shanshan.housekeeper.Help.utils.CommonUtil;
import com.shanshan.housekeeper.Help.utils.LogCatUtil;
import com.shanshan.housekeeper.Help.utils.MyToastView;
import com.shanshan.housekeeper.picture.PPictureActivity;
import com.shanshan.housekeeper.R;
import com.shanshan.housekeeper.interfaces.IResponse;
import com.wgl.mvp.headerPicture.HeaderPicture;
import com.wgl.mvp.model.IModel;
import com.wgl.mvp.slideholder.LayoutRelative;
import com.wgl.mvp.slideholder.SlideHolder;

import java.io.File;

/**
 * Created by wgl.
 * P层（将原来的activity加以改造）
 */
public class PLoginActivity extends BaseActivity<VLogin> implements IResponse {

    HeaderPicture headerPicture = new HeaderPicture(PLoginActivity.this);
    private int animMoveClass = 0;

    /**
     * 实例化View
     * @return
     */
    @Override
    protected Class<VLogin> getDelegateClass() {
        return VLogin.class;
    }

    @Override
    protected void setNormal() {
        super.setNormal();
        baseView.slideHolder.setEnabled(false);
//        baseView.slideHolder.setDirection(-1);//右边滑出
        baseView.slideHolder.setOnSlideListener(new SlideHolder.OnSlideListener() {
            @Override
            public void onSlideCompleted(boolean opened) {
                baseView.slideHolder.setEnabled(false);
            }
        });
    }

    /**
     * 按钮监听
     */
    @Override
    protected void setListener() {
        super.setListener();
        baseView.etUser.setText("15201163153");
        baseView.etPassword.setText("woaini1314");
        baseView.setOnClickListener(onClickListener,R.id.et_password,R.id.et_user, R.id.button1, R.id.tou,R.id.bt1_touxiang,R.id.bt2_touxiang,R.id.bt3_touxiang,R.id.left,R.id.right,R.id.frame);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.et_password:
                    //点击外边，侧滑消失
                    if (animMoveClass == 1) {
                        setSlidingMenu();
                        return;
                    }
                    break;
                case R.id.et_user:
                    if (animMoveClass == 1) {
                        setSlidingMenu();
                        return;
                    }
                    break;
                case R.id.button1:
                    if (animMoveClass == 1) {
                        setSlidingMenu();
                        return;
                    }
                    //网络层的实现(登录)
                    LogCatUtil.log("打印");
                    NetLogin mPresenter = new NetLogin(PLoginActivity.this, PLoginActivity.this);
                    //getIPublic(true): false:不缓存，true:缓存
                    mPresenter.tonetLogin(getIPublic(false), baseView.etUser.getText().toString(), baseView.etPassword.getText().toString(), true, "正在登陆...");
                    break;
                case R.id.tou:
                    if (animMoveClass == 1) {
                        setSlidingMenu();
                        return;
                    }
                    initPopuWindow(PLoginActivity.this, baseView.popupWindow, baseView.view, 1);
                    break;
                case R.id.bt1_touxiang:
                    headerPicture.camera();
                    baseView.popupWindow.dismiss();
                    break;
                case R.id.bt2_touxiang:
                    headerPicture.gallery();
                    baseView.popupWindow.dismiss();
                    break;
                case R.id.bt3_touxiang:
                    baseView.popupWindow.dismiss();
                    break;
                case R.id.left:
                    if (animMoveClass == 1) {
                        setSlidingMenu();
                        return;
                    }
                    baseView.slideHolder.setEnabled(true);
                    baseView.slideHolder.toggle();
                    baseView.layoutRelativeLeft.setOnScrollListener(new LayoutRelative.OnScrollListener() {
                        @Override
                        public void doOnScrollRight(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                            右边滑出
//                            try {
//                                if(null != baseView.slideHolder){
//                                    if(baseView.slideHolder.isOpened()){
//                                        baseView.slideHolder.close();
//                                        return;
//                                    }
//                                    baseView.slideHolder.setEnabled(true);
//                                    baseView.slideHolder.toggle();
//                                }
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
                        }

                        @Override
                        public void doOnScrollLeft(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                            //左边滑出
                            try {
                                if(null != baseView.slideHolder){
                                    if(baseView.slideHolder.isOpened()){
                                        baseView.slideHolder.close();
                                        return;
                                    }
                                    baseView.slideHolder.setEnabled(true);
                                    baseView.slideHolder.toggle();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                        }}

                        @Override
                        public void doOnRelease() {

                        }
                    });
                    break;
                case R.id.right:
                    setSlidingMenu();
                    baseView.layoutRelativeRight.setOnScrollListener(new LayoutRelative.OnScrollListener() {
                        @Override
                        public void doOnScrollRight(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                            baseView.layoutRelativeRight.startAnimation(baseView.animToRight);
                            baseView.layoutRelativeRight.setVisibility(View.GONE);
                        }

                        @Override
                        public void doOnScrollLeft(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                        }

                        @Override
                        public void doOnRelease() {

                        }
                    });
                    break;
                case R.id.frame:
                    if (animMoveClass == 1) {
                        setSlidingMenu();
                    }
                    break;
            }
        }
    };
    /**
     * 设置侧滑
     */
    public void setSlidingMenu() {
        if (animMoveClass == 0) {//滑出
            baseView.layoutRelativeRight.setVisibility(View.VISIBLE);
            baseView.layoutRelativeRight.startAnimation(baseView.animToLeft);
            baseView.layoutRelativeRight.setFocusableInTouchMode(true);
            animMoveClass = 1;
        } else {//消失
            baseView.layoutRelativeRight.startAnimation(baseView.animToRight);
            baseView.layoutRelativeRight.setVisibility(View.GONE);
            animMoveClass = 0;
            baseView.layoutRelativeRight.setFocusableInTouchMode(false);
        }
    }

    @Override
    public void onSuccess(IModel modle) {
        MyToastView.showToast("登陆成功",this);
        CommonUtil.toActivity(this,PPictureActivity.class);
    }

    @Override
    public void onFailure(String error) {
        MyToastView.showToast(error,this);
        CommonUtil.toActivity(this,PPictureActivity.class);
    }

    /**
     * 弹出自定义PopupWindow
     *
     * @param popupWindow : PopupWindow popuppWindow = new PopupWinsow();
     */
    public static void initPopuWindow(final Context context,
                                       PopupWindow popupWindow, View view1, int n) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view1,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }

        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = 0.4f;// 透明度(0.0-1.0)
        ((Activity) context).getWindow().setAttributes(lp);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 居中显示
        if (n == 1) {
            popupWindow.showAtLocation(view1, Gravity.CENTER
                    | Gravity.CENTER_VERTICAL, 0, 0);
        }
        // 底部显示
        if (n == 2) {
            popupWindow.showAtLocation(view1, Gravity.BOTTOM, 0, 0);
        }

        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = ((Activity) context)
                        .getWindow().getAttributes();
                lp.alpha = 1f;
                ((Activity) context).getWindow().setAttributes(lp);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            MyToastView.showToast("取消",this);
            return;
        }
        switch (resultCode) {
            case RESULT_OK:
                if(requestCode == HeaderPicture.SELECT_PIC_BY_TACK_PHOTO){
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/" + HeaderPicture.IMAGE_FILE_NAME);
                    headerPicture.startPhotoZoom(Uri.fromFile(temp),300,300);
                    return;
                }
                if(requestCode == HeaderPicture.SELECT_PIC_BY_PICK_PHOTO){
                    try {
                        headerPicture.startPhotoZoom(data.getData(),300,300);
                    } catch (NullPointerException e) {
                        e.printStackTrace();//用户点击取消操作
                    }
                    return;
                }
                if(requestCode == HeaderPicture.REQUESTCODE_CUTTING){
                    if (data != null) {
                        String base64 = headerPicture.setPicgetBase64(data,baseView.imageView);
                    }
                }
                break;

        }
    }
}
