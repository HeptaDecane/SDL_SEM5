package live.sockets.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.PointerIcon;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.HashSet;
import java.util.Set;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private ImageView background;
    private ImageView icon;
    private Intent intent;
    Set<String> videoFormats = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_video);

        videoFormats.add("3gp");
        videoFormats.add("mp4");
        videoFormats.add("mkv");
        videoFormats.add("ts");
        videoFormats.add("webm");
        intent = getIntent();
        String filePath = intent.getStringExtra("FILE_PATH");
        String extension = filePath.substring(filePath.lastIndexOf('.')+1);
        videoView = findViewById(R.id.videoView);
        icon = findViewById(R.id.icon);
        background = findViewById(R.id.background);

        if (videoFormats.contains(extension)) {
            icon.setTranslationY(-10000);
            background.setTranslationY(-10000);
        }

        videoView.setVideoPath(filePath);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();

    }

}