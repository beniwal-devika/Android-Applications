package mobile_application_development.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devikabeniwal on 05/05/17.
 */

public class MyService extends Service {

    private static final String TAG = "MyService";
    private boolean running = true;

    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    private ServiceReceiver serviceReceiver;
    static final String SERVICE_DATA = "SERVICE_DATA";


    public List<ArticleList> articleList = new ArrayList<>();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        serviceReceiver = new ServiceReceiver();
        IntentFilter filter2 = new IntentFilter(ACTION_MSG_TO_SERVICE);
        registerReceiver(serviceReceiver, filter2);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(running) {
                    if (articleList.isEmpty()) {
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Intent intent1 = new Intent();
                        intent1.setAction(MainActivity.ACTION_NEWS_STORY);

                        int count = articleList.size();
                        for (int i = 0; i < count; i++) {
                            String title = articleList.get(i).getTitle();
                            String author = articleList.get(i).getAuthor();
                            String desc = articleList.get(i).getDescription();
                            String imageurl = articleList.get(i).getimageUrl();
                            String time = articleList.get(i).getPublishedAt();
                            String url = articleList.get(i).getUrl();
                            intent1.putExtra("title",title);
                            intent1.putExtra("author",author);
                            intent1.putExtra("desc",desc);
                            intent1.putExtra("imageurl",imageurl);
                            intent1.putExtra("time",time);
                            intent1.putExtra("url",url);
                            sendBroadcast(intent1);
                        }
                        articleList.clear();
                    }

                   }
            }
        }).start();


        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }

    public void updateDescriptionData(ArrayList<ArticleList> cList) {
        if(cList == null) return;
        Intent intent = new Intent();
        intent.setAction(MainActivity.ACTION_NEWS_STORY);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.SERVICE_DATA, cList);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }


    class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACTION_MSG_TO_SERVICE)) {
                if(intent.hasExtra(MainActivity.SOURCE_ID)) {
                    String source = intent.getStringExtra(MainActivity.SOURCE_ID);
                    new AsyncTask2(MyService.this).executeOnExecutor(AsyncTask1.THREAD_POOL_EXECUTOR,source);
                }
            }
        }
    }

}
