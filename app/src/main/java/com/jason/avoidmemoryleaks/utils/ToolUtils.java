package com.jason.avoidmemoryleaks.utils;

import android.content.Context;

/**
 * @author: Jason
 * @date: 15/5/18.
 * @time: 上午11:13.
 */


/**
 * 说明：
 *      当我们在Activity中或者其他地方使用 ToolUtils.getInstance()时,我们总是会将activity中的context传递给该工具类，
 *      当前我们所用的ToolUtils是单例,意味着被初始化后会一直存在于内存中，已方便我们以后调用的时候不会再次创建ToolUtils对象，
 *      但是ToolUtils中的context变量一直都会持有Acitivity种的context，会导致Activity即使执行了onDestroy方法，也不能够
 *      将自己销毁,但applicationContext就不同了,它一直伴随着我们应用存在(中途也可能会被销毁,但也会自动onCreate),所以就不用
 *      担心ToolUtils中的mContext会持有某Activity的引用,让其无法销毁。
 */
public class ToolUtils {

    public static ToolUtils toolUtils = null;
    private Context mContext;
    private ToolUtils(Context context){
        this.mContext = context;
    }


    public synchronized static ToolUtils getInstance(Context context) {
        if (toolUtils == null) {
            toolUtils = new ToolUtils(context.getApplicationContext());
        }

        return toolUtils;
    }


}
