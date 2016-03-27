package cn.lankton.anyshape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.List;
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

    /**
     * Asynchronous method to init paths by resourceids
     * @param context
     * @param resList
     */
    public void createPaths(Context context, List<Integer> resList) {
        for (Integer resId : resList) {
            if (resId > 0) {
                PathAsyncTask task = new PathAsyncTask(context);
                task.execute(resId);
            }
        }
    }

    class PathAsyncTask extends AsyncTask <Integer, Void, Path> {
        private Context context;
        public PathAsyncTask(Context context){
            super();
            this.context = context;
        }

        @Override
        protected Path doInBackground(Integer... params) {
            int resId = params[0];
            PathInfo pi = new PathInfo();
            Bitmap maskBitmap = BitmapFactory.decodeResource(context.getResources(), resId);
            pi.path = PathManager.getInstance().getPathFromBitmap(maskBitmap);
            pi.width = maskBitmap.getWidth();
            pi.height = maskBitmap.getHeight();
            //creating is done, add the path info into the cache
            PathManager.getInstance().addPathInfo(resId, pi);
            maskBitmap.recycle();
            return null;
        }

    }
}
