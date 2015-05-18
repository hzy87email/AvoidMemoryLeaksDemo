package com.jason.avoidmemoryleaks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.lang.ref.WeakReference;


/**
 * @author: Jason
 * @date: 15/5/18.
 * @time: 上午11:35.
 *
 *
 * 解决可能由handler造成的内存泄露
 *
 *  开发中需要注意的点以免内存泄漏：

        1，不要让生命周期长于Activity的对象持有到Activity的引用

        2，尽量使用Application的Context而不是Activity的Context

        3，尽量不要在Activity中使用非静态内部类，因为非静态内部类会隐式持有外部类实例的引用（具体可以查看细话Java：”失效”的private修饰符了解）。如果使用静态内部类，将外部实例引用作为弱引用持有。

        4，垃圾回收不能解决内存泄露，了解Android中垃圾回收机制
 *
 *
 *
 *
 *  获取context的方法，以及使用上context和applicationContext的区别：

        1，View.getContext,返回当前View对象的Context对象，通常是当前正在展示的Activity对象。

        2，Activity.getApplicationContext,获取当前Activity所在的(应用)进程的Context对象，通常我们使用Context对象时，要优先考虑这个全局的进程Context。

        3，ContextWrapper.getBaseContext():用来获取一个ContextWrapper进行装饰之前的Context，可以使用这个方法，这个方法在实际开发中使用并不多，也不建议使用。

        4，Activity.this 返回当前的Activity实例，如果是UI控件需要使用Activity作为Context对象，但是默认的Toast实际上使用ApplicationContext也可以。
 *
 *
 */
public class DemoActivity extends AppCompatActivity {

    private  TextView textView;


    private static class MyHandler extends Handler {

        private final WeakReference<DemoActivity> demoActivityWeakReference;

        private MyHandler(DemoActivity activity) {
            this.demoActivityWeakReference = new WeakReference<DemoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {

            DemoActivity activity = demoActivityWeakReference.get();
            if (activity != null) {
                // ...需要处理的逻辑


            }


        }
    }

    private MyHandler mHandler = new MyHandler(this);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        textView = (TextView) findViewById(R.id.textview);

        mHandler.postDelayed(new MyRunnable(textView), 1000 * 60 * 10);


    }



    private static class MyRunnable implements Runnable {

        private TextView textView;

        private WeakReference<TextView> textViewWeakReference;

        public MyRunnable(TextView textView) {
            this.textViewWeakReference = new WeakReference<TextView>(textView);
        }

        @Override
        public void run() {

            textView = textViewWeakReference.get();
            if (textView != null) {
                textView.setText("OK");

            }
        }
    }


    /**
     * 在使用Handler后,一定要在onDestroy里面 remove
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 当参数为null的时候可以清除掉所有跟该页面handler相关的Runnable和Message,在onDestroy中调用此方法也就不会发生内存泄露了
        mHandler.removeCallbacksAndMessages(null);

    }
}
