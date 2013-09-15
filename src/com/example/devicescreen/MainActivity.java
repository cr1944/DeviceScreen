
package com.example.devicescreen;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text2 = (TextView) findViewById(R.id.text2);
        StringBuilder sb = new StringBuilder();
        sb.append("densityDpi:");
        sb.append(getResources().getDisplayMetrics().densityDpi);
        sb.append('\n');
        sb.append("density:");
        sb.append(getResources().getDisplayMetrics().density);
        sb.append('\n');
        sb.append("scaledDensity:");
        sb.append(getResources().getDisplayMetrics().scaledDensity);
        sb.append('\n');
        sb.append("smallestScreenWidthDp:");
        sb.append(getResources().getConfiguration().smallestScreenWidthDp);
        text2.setText(sb);
        final SampleView sv = new SampleView(this);
        ((LinearLayout) findViewById(R.id.layout1)).addView(sv);
        ((CheckBox) findViewById(R.id.modeFullscreen))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int visibility = 0;
                        if (isChecked) {
                            visibility |= View.SYSTEM_UI_FLAG_FULLSCREEN;
                        }
                        sv.setSystemUiVisibility(visibility);
                    }
                });
        ((CheckBox) findViewById(R.id.modeHideNavigation))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int visibility = 0;
                        if (isChecked) {
                            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                        }
                        sv.setSystemUiVisibility(visibility);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private static class SampleView extends View implements View.OnSystemUiVisibilityChangeListener {
        private Paint mLinePaint;
        private Paint mTextPaint;
        private RectF[] mOvals;
        private Path mWPath;
        private Path mHPath;
        private Path mXPath;
        private Path mYPath;
        private Path mZPath;
        private int mHeightPixels;
        private int mWidthPixels;
        private int mScreenHeightDp;
        private int mScreenWidthDp;
        private float mYdpi;
        private float mXdpi;

        private static final int LEFT_TOP_WIDTH = 30;

        public SampleView(Context context) {
            super(context);
            setOnSystemUiVisibilityChangeListener(this);
            init();

            final int centerX = mWidthPixels / 2;
            final int centerY = mHeightPixels / 2;
            mOvals = new RectF[2];
            mOvals[0] = new RectF(LEFT_TOP_WIDTH, LEFT_TOP_WIDTH, LEFT_TOP_WIDTH + centerX,
                    LEFT_TOP_WIDTH + centerY);
            mOvals[1] = new RectF(LEFT_TOP_WIDTH, LEFT_TOP_WIDTH + centerY - mYdpi / 2,
                    LEFT_TOP_WIDTH + mXdpi / 2, LEFT_TOP_WIDTH + centerY);

            mLinePaint = new Paint();
            mLinePaint.setAntiAlias(true);
            mLinePaint.setColor(0xffffffff);
            mLinePaint.setStyle(Paint.Style.STROKE);
            mLinePaint.setStrokeWidth(0);

            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(0xffffffff);
            mTextPaint.setTextSize(24);
            mWPath = new Path();
            mWPath.moveTo(LEFT_TOP_WIDTH, LEFT_TOP_WIDTH);
            mWPath.lineTo(LEFT_TOP_WIDTH + centerX, LEFT_TOP_WIDTH);
            mHPath = new Path();
            mHPath.moveTo(LEFT_TOP_WIDTH + centerX, LEFT_TOP_WIDTH);
            mHPath.lineTo(LEFT_TOP_WIDTH + centerX, LEFT_TOP_WIDTH + centerY);
            mXPath = new Path();
            mXPath.moveTo(LEFT_TOP_WIDTH, LEFT_TOP_WIDTH + centerY - mYdpi / 2);
            mXPath.lineTo(LEFT_TOP_WIDTH + mXdpi / 2, LEFT_TOP_WIDTH + centerY - mYdpi / 2);
            mYPath = new Path();
            mYPath.moveTo(LEFT_TOP_WIDTH + mXdpi / 2, LEFT_TOP_WIDTH + centerY - mYdpi / 2);
            mYPath.lineTo(LEFT_TOP_WIDTH + mXdpi / 2, LEFT_TOP_WIDTH + centerY);
            mZPath = new Path();
            mZPath.moveTo(LEFT_TOP_WIDTH + centerX / 2, LEFT_TOP_WIDTH + centerY / 2);
            mZPath.lineTo(LEFT_TOP_WIDTH + centerX, LEFT_TOP_WIDTH + centerY);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xff242424);
            canvas.drawRect(mOvals[0], mLinePaint);
            canvas.drawRect(mOvals[1], mLinePaint);
            canvas.drawLine(LEFT_TOP_WIDTH, LEFT_TOP_WIDTH, mOvals[0].right, mOvals[0].bottom,
                    mLinePaint);
            canvas.drawTextOnPath("W: " + mWidthPixels + "px / " + mScreenWidthDp + "dp / "
                    + mWidthPixels / mXdpi + "in.", mWPath, 10, 0, mTextPaint);
            canvas.drawTextOnPath("H: " + mHeightPixels + "px / " + mScreenHeightDp + "dp / "
                    + mHeightPixels / mYdpi + "in.", mHPath, 10, 0, mTextPaint);
            canvas.drawTextOnPath("xdpi: " + mXdpi, mXPath, 0, 0, mTextPaint);
            canvas.drawTextOnPath("ydpi: " + mYdpi, mYPath, 0, 0, mTextPaint);
            canvas.drawTextOnPath(Math.sqrt(dist2(mWidthPixels / mXdpi, mHeightPixels / mYdpi))
                    + "in.", mZPath, 0, 0, mTextPaint);
        }

        private void init() {
            mHeightPixels = getResources().getDisplayMetrics().heightPixels;
            mWidthPixels = getResources().getDisplayMetrics().widthPixels;
            mScreenHeightDp = getResources().getConfiguration().screenHeightDp;
            mScreenWidthDp = getResources().getConfiguration().screenWidthDp;
            mYdpi = getResources().getDisplayMetrics().ydpi;
            mXdpi = getResources().getDisplayMetrics().xdpi;
        }

        private float dist2(float dx, float dy) {
            return dx * dx + dy * dy;
        }

        @Override
        public void onSystemUiVisibilityChange(int visibility) {
            init();
            invalidate();
        }
    }

    private void setFullscreen(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void setOverscan(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
