package cn.lankton.anyshape;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by taofangxin on 16/3/21.
 */
public class AnyshapeImageView extends ImageView {

    Context context;
    Bitmap maskBitmap;
    Drawable srcDrawable;
    Bitmap srcBitmap;
    Path originMaskPath = null;
    Path realMaskPath = new Path();
    Paint paint = new Paint();
    int vWidth = 0;
    int vHeight = 0;
    public AnyshapeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AnyShapeImageView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            final int attr = a.getIndex(i);
            if (attr == R.styleable.AnyShapeImageView_anyshapeMask) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                maskBitmap = BitmapFactory.decodeResource(context.getResources(), a.getResourceId(attr, 0), options);
                originMaskPath = getPath(maskBitmap);
            }

        }
        a.recycle();
    }

    public AnyshapeImageView(Context context) {
        this(context, null);
    }

    public AnyshapeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        vHeight = getHeight();
        vWidth = getWidth();
        srcDrawable = this.getDrawable();
        if (null != srcDrawable) {
            srcBitmap = ((BitmapDrawable)srcDrawable).getBitmap();
        }
        if (originMaskPath != null) {
            Matrix matrix = new Matrix();
            matrix.setScale(vWidth * 1f / maskBitmap.getWidth(), vHeight * 1f / maskBitmap.getHeight());
            originMaskPath.transform(matrix, realMaskPath);
        }
    }

    private Path getPath(Bitmap mask) {
        Path path = new Path();
        int bWidth = mask.getWidth();
        int bHeight = mask.getHeight();
        Log.v("lanktondebug", "" + bWidth + "," + bHeight);
        int[] origin = new int[bWidth];
        int lastA = 0;
        for (int i = 0; i < bHeight; i++) {
            mask.getPixels(origin, 0, bWidth, 0, i, bWidth, 1);
            for (int j = 0; j < bWidth; j++) {
                int a = Color.alpha(origin[j]);
                if (j == 0) {
                    lastA = 0;
                    path.moveTo(0, i);
                    continue;
                }
                if (a != 0) {
                    if (lastA == 0) {
                        path.moveTo(j, i);
                    }
                }
                if (a != 0 && j == bWidth - 1) {
                    path.lineTo(j, i);
                } else if (a == 0 && lastA !=0 ) {
                    path.lineTo(j - 1, i);
                }
                lastA = a;
            }
        }
        return path;
    }




    @Override
    protected void onDraw(Canvas canvas) {
        Log.v("lanktondebug", "" + vWidth + "," + vHeight + "," +(realMaskPath == null));
        if (vWidth == 0 || vHeight == 0 || null == realMaskPath || null == maskBitmap) {
            Log.v("lanktondebug", "mask null");
            return;
        }
        Drawable srcDrawable = getDrawable();
        if (null == srcDrawable) {
            Log.v("lanktondebug", "src null");
            return;
        }
        Bitmap srcBitmap = ((BitmapDrawable)srcDrawable).getBitmap();
        Shader shader = new BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix shaderMatrix = new Matrix();
        float scaleX = vWidth * 1.0f / srcBitmap.getWidth();
        float scaleY = vHeight * 1.0f / srcBitmap.getHeight();
        shaderMatrix.setScale(scaleX, scaleY);
        shader.setLocalMatrix(shaderMatrix);
        paint.setShader(shader);
        paint.setStyle(Paint.Style.STROKE);
//        canvas.drawColor(Color.BLUE);
        canvas.drawPath(realMaskPath, paint);
//        canvas.drawBitmap(maskBitmap, 0, 0, null);
    }
}
