package cn.lankton.anyshape;

import android.graphics.Path;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by taofangxin on 16/3/24.
 */
public class PathManager {
    Map<Integer,PathInfo> pathMap = new HashMap<>();
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
}
