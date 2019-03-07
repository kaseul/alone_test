package staclogintest.kr.hs.mirim.alone_test.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import staclogintest.kr.hs.mirim.alone_test.R;
import staclogintest.kr.hs.mirim.alone_test.StopActivity;
import staclogintest.kr.hs.mirim.alone_test.model.PlaylistContentItem;

public class AudioService extends Service {
    MediaPlayer mp = new MediaPlayer();
    private ArrayList<PlaylistContentItem> playlist;
    int count;

    public AudioService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playlist = (ArrayList<PlaylistContentItem>)intent.getSerializableExtra("playlist");
        count = intent.getIntExtra("position", 0);

        if(playlist.size() == 0) {
            stopSelf();
        }

        try {
            mp.setDataSource(playlist.get(count).url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mp.setLooping(false);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(++count < playlist.size()) {
                    Log.d("again", "again"+count+ playlist.get(count).url);
                    try {
                        mp.reset();
                        mp.setDataSource(playlist.get(count).url);
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.d("stop", "stop");
                    stopSelf();
                }
            }
        });

        mp.start();

        PendingIntent pendingIntent = PendingIntent.getActivity(AudioService.this, 0, new Intent(this, StopActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("나 혼자 살까?")
                .setContentText("터치 시 재생 중지")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyManager.notify(001, notifyBuilder.build());

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
