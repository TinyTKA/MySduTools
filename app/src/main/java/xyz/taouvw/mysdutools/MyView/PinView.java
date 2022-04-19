package xyz.taouvw.mysdutools.MyView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import xyz.taouvw.mysdutools.R;

public class PinView extends SubsamplingScaleImageView {

    private final Paint paint = new Paint();
    private final PointF vPin = new PointF();
    private PointF sPin;
    private Bitmap pin;
    private final Bitmap rawPin = BitmapFactory.decodeResource(this.getResources(), R.mipmap.arrow);
    private float ori;
    private PointF Toxy = new PointF();

    public PinView(Context context) {
        this(context, null);
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public void setPin(PointF sPin) {
        this.sPin = sPin;
        initialise();
        invalidate();
    }

    public void setOri(float ori) {
        this.ori = ori;
        initialise();
        invalidate();
    }

    public void setToxy(PointF toxy) {
        this.Toxy = toxy;
        initialise();
        invalidate();
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        pin = rawPin;
        float w = (density / 420f) * pin.getWidth();
        float h = (density / 420f) * pin.getHeight();
        Matrix m = new Matrix();
        m.postRotate(ori);
        pin = Bitmap.createScaledBitmap(pin, (int) w, (int) h, true);
        pin = Bitmap.createBitmap(pin, 0, 0, (int) w, (int) h, m, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }
        paint.setAntiAlias(true);
        if (sPin != null && pin != null) {
            sourceToViewCoord(sPin, vPin);
            float vX = vPin.x - (pin.getWidth() / 2);
            float vY = vPin.y - pin.getHeight() / 2;
            canvas.drawBitmap(pin, vX, vY, paint);
        }
    }

}
