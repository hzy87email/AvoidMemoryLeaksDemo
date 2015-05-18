package com.jason.avoidmemoryleaks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * 参照部分网络上的搜集的知识点编写
 * 错误的示例
 *
 * 原因：
 *      当我们执行MainActivity的finish方法,被延迟的消息会在被处理之前存在于主线程消息队列中10分钟,而这个消息又包含了Handler的引用,而Handler是一个匿名
 *      内部类的实例,其持有外面的MainActivity的引用,所以这导致了MainActivity无法回收,从而导致MainActivity持有的很多资源都无法回收,可能产生内存泄露。
 *      (里面的new Runnable这里也是匿名内部类实现的,同样也会持有MainActivity的引用,也会阻止MainActivity被回收)
 *
 *      解决办法: 一个静态的匿名内部类实例不会持有外部类的引用。(参照DemoActivity)
 *
 */
public class MainActivity extends AppCompatActivity {

    private TextView textView;

//    private Handler mHandler = new Handler(){
//
//        @Override
//        public void handleMessage(Message msg) {
//
//        }
//
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textview);
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText("ok");
//            }
//        }, 1000 * 60 * 10);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
