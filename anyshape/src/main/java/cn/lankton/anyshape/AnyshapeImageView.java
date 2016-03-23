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
import android.graphics.drawable.ColorDrawable;
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
    Path originMaskPath = null;
    Path realMaskPath = new Path();
    Paint paint = new Paint();
    int backColor;
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
                originMaskPath = getPathFromBitmap(maskBitmap);
            } else if (attr == R.styleable.AnyShapeImageView_anyshapeBackColor) {
                backColor = a.getColor(attr, Color.TRANSPARENT);
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
        if (originMaskPath != null) {
            Matrix matrix = new Matrix();
            matrix.setScale(vWidth * 1f / maskBitmap.getWidth(), vHeight * 1f / maskBitmap.getHeight());
            originMaskPath.transform(matrix, realMaskPath);
        }
    }

    /**
     * get the path from a mask bitmap
     * @param mask
     * @return
     */
    private Path getPathFromBitmap(Bitmap mask) {
        Path path = new Path();
        int bWidth = mask.getWidth();
        int bHeight = mask.getHeight();
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
        if (null == maskBitmap) {
            // if the mask is null, the view will work as a normal ImageView
            super.onDraw(canvas);
            return;
        }
        if (vWidth == 0 || vHeight == 0) {
            return;
        }

        paint.reset();
        //get the drawable to show. if not set the src, will use  backColor
        Drawable showDrawable = getDrawable();
        if (null != showDrawable) {
            Bitmap showBitmap = ((BitmapDrawable) showDrawable).getBitmap();
            Shader shader = new BitmapShader(showBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix shaderMatrix = new Matrix();
            float scaleX = vWidth * 1.0f / showBitmap.getWidth();
            float scaleY = vHeight * 1.0f / showBitmap.getHeight();
            shaderMatrix.setScale(scaleX, scaleY);
            shader.setLocalMatrix(shaderMatrix);
            paint.setShader(shader);
            paint.setStyle(Paint.Style.STROKE);
        } else {
            //no src , use the backColor to fill the path
            paint.setColor(backColor);
            paint.setStyle(Paint.Style.STROKE);
        }
        canvas.drawPath(realMaskPath, paint);

    }

    /**
     * allow coder to set the backColor
     * @param color
     */
    public void setBackColor(int color) {
        backColor = color;
    }
}
