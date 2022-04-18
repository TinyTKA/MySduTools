package xyz.taouvw.mysdutools.MyView;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import xyz.taouvw.mysdutools.R;

public class CircleView extends SubsamplingScaleImageView {

    private int strokeWidth;

    private final PointF sCenter = new PointF();
    private final PointF vCenter = new PointF();
    private final Paint paint = new Paint();
    private final Bitmap pin = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow);
    private Bitmap RotatePin = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow);
    PointF sxy = new PointF();
    PointF Toxy = new PointF();
    float orientation = 0f;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        strokeWidth = (int) (density / 60f);
    }

    public void setMyOrientation(float orientation) {
        this.orientation = orientation;
        getRotatePin();
        invalidate();
    }

    private void getRotatePin() {
        Matrix m = new Matrix();
        m.setRotate(orientation);
        RotatePin = Bitmap.createBitmap(pin, 0, 0, 100, 100, m, false);
    }

    public void setXY(Float x, Float y) {
        this.Toxy.x = x;
        this.Toxy.y = y;
    }

    public void setXY(PointF p) {
        this.Toxy.x = p.x;
        this.Toxy.y = p.y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }
        PointF vxy;
        if (Toxy.x != 0 || Toxy.y != 0) {
            if (getScale() >= 0.5 && getScale() <= 1) {
                sxy.x = this.Toxy.x + 100 * getScale() - 100;
                sxy.y = this.Toxy.y + 100 * getScale() - 100;
                vxy = sourceToViewCoord(sxy);
                canvas.drawBitmap(RotatePin, vxy.x - 40 * getScale(), vxy.y - 40 * getScale(), paint);
            } else if (getScale() > 1 && getScale() <= 1.4) {
                sxy.x = this.Toxy.x + 50 * (getScale() - 1.01f);
                sxy.y = this.Toxy.y + 50 * (getScale() - 1.01f);
                vxy = sourceToViewCoord(sxy);
                canvas.drawBitmap(RotatePin, vxy.x - 40 * (getScale()), vxy.y - 40 * getScale(), paint);
            } else if (getScale() < 0.5) {
                sxy.x = this.Toxy.x + 600 * (getScale() - 1) + 235;
                sxy.y = this.Toxy.y + 600 * (getScale() - 1) + 235;
                vxy = sourceToViewCoord(sxy);
                canvas.drawBitmap(RotatePin, vxy.x - 30 * getScale(), vxy.y - 30 * getScale(), paint);

            }

        }
    }

}
