package live.sockets.mediaplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    TextView textView;;
    String filePath = "NULL";
    String fileName = "NULL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        int filePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (filePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },73);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 200:
                if (resultCode == RESULT_OK) {

                    filePath = data.getData().getLastPathSegment();
                    filePath = filePath.substring(filePath.lastIndexOf(':')+1);
                    fileName = filePath.substring(filePath.lastIndexOf('/')+1);

                    textView.setText(fileName);
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 73:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    textView.setText("Select Audio/Video File");

                }else {
                    textView.setText("Storage Permission Denied");
                }
        }
    }

    // browseButtonOnClickListener
    public void browseMedia(View view){
        int filePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (filePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },73);
        }
        else {
            Intent filePicker = new Intent(Intent.ACTION_GET_CONTENT);
            filePicker.setType("*/*");
            startActivityForResult(filePicker, 200);
        }
    }

    // playButtonOnClickListener
    public void play(View view){
        if (!filePath.equalsIgnoreCase("NULL")) {
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("FILE_PATH", Environment.getExternalStorageDirectory().getPath()+"/"+filePath);
            startActivity(intent);
        } else{
            Toast.makeText(this,"No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

}