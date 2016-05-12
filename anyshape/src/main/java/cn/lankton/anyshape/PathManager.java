package cn.lankton.anyshape;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by taofangxin on 16/3/24.
 */
public class PathManager {
    private Map<Integer,PathInfo> pathMap = new HashMap<>();
    private static PathManager instance = null;


    public static synchronized PathManager getInstance() {
        if (null == instance) {
            instance = new PathManager();
        }
        return instance;
    }

    public void addPathInfo(int resId, PathInfo pi) {
        pathMap.put(resId, pi);
    }

    public PathInfo getPathInfo(int resId) {
        return pathMap.get(resId);
    }

    /**
     * get the path from a mask bitmap
     * @param mask
     * @return
     */
    public Path getPathFromBitmap(Bitmap mask) {
        Path path = new Path();
        int bWidth = mask.getWidth();
        int bHeight = mask.getHeight();
        int[] origin = new int[bWidth];
        int lastA;
        for (int i = 0; i < bHeight; i++) {
            mask.getPixels(origin, 0, bWidth, 0, i, bWidth, 1);
            lastA = 0;
            for (int j = 0; j < bWidth; j++) {
                int a = Color.alpha(origin[j]);
                if (a != 0 && lastA == 0) {
                    path.moveTo(j, i);
                } else if (a == 0 && lastA !=0 ) {
                    path.lineTo(j - 1, i);
                } else if (a != 0 && j == bWidth - 1) {
                    path.lineTo(j, i);
                }
                lastA = a;
            }
        }
        return path;
    }

}
