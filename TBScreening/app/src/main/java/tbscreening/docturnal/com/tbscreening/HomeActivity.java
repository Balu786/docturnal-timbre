package tbscreening.docturnal.com.tbscreening;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow;
import android.widget.LinearLayout;


import java.io.File;
import java.io.IOException;

public class HomeActivity extends Activity {

    MediaRecorder  recorder = new MediaRecorder();
    File audiofile = null;
    static final String TAG = "MediaRecording";
    Button startButton,stopButton;
    private TextView textView;
    private TextView textViewResponse;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    public PopupWindow pwindo;

    private View layout;

    String upLoadServerUri = "http://abhinav-training.com/api/upload.php";
    /**********  File Path *************/
    String uploadFilePath = "/mnt/sdcard/";
    private static final int SELECT_VIDEO = 2;

    public String selectedPath;
    Intent intent;
    String paId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            paId = extras.getString("last_id");
          //  Toast.makeText(HomeActivity.this, paId.toString(), Toast.LENGTH_SHORT).show();
            // and get whatever type user account id is
        }
       //// Toast.makeText(HomeActivity.this, paId.toString(), Toast.LENGTH_SHORT).show();
        startButton = (Button) findViewById(R.id.button1);
        stopButton = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);
        textViewResponse = (TextView) findViewById(R.id.textViewResponse);
        intent = getIntent();

       // Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
    }
    private void initiatePopupWindowForContat() {

        LayoutInflater inflaterpopup = (LayoutInflater) this
                .getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View layout = inflaterpopup.inflate(R.layout.loader,
                null);
        gif pGif = (gif) layout.findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.loder);

        pwindo = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,true);
        pwindo.setAnimationStyle(android.R.style.Animation_Dialog);
        pwindo.setFocusable(false);
        pwindo.setOutsideTouchable(false);
        pwindo.showAtLocation(layout, Gravity.TOP, 0, 0);
    }

    public void startRecording(View view) throws IOException {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        initiatePopupWindowForContat();
      /*  String id = intent.getStringExtra("clinic_id");
        String result = id.replace("\"", "");*/
        //Creating file
        File dir = Environment.getExternalStorageDirectory();
        try {
            audiofile = File.createTempFile(paId+"^sound", ".mp4", dir);
        } catch (IOException e) {
            Log.e(TAG, "external storage access error");
            return;
        }
        Toast.makeText(HomeActivity.this, "Start recording", Toast.LENGTH_SHORT).show();
        //Creating MediaRecorder and specifying audio source, output format, encoder & output format

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        recorder.prepare();
        recorder.start();

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                stopRecording();
            }
        };
        handler.postDelayed(runnable, 10000);//replace your time

    }



    public void stopRecording() {
        pwindo.dismiss();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        //stopping recorder
        recorder.stop();
  recorder.release();
        //after stopping the recorder, create the sound file and add it to media library.
        addRecordingToMediaLibrary();
    }

    protected void addRecordingToMediaLibrary() {
        //creating content values of size 4
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio___" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "vedio/mp4");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());

        //creating content resolver and storing it in the external content uri
        ContentResolver contentResolver = getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final Uri newUri = contentResolver.insert(base, values);



/*
        Bitmap bm = BitmapFactory.decodeStream(
                getContentResolver().openInputStream(uriFromPath));*/
        //selectedPath = getPath(newUri);
        System.out.println(selectedPath + "________________" + "audio___"+audiofile.getName()+"++++++++++++"+newUri);
        //sending broadcast message to scan the media file so that it can be available
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        // Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();

        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(HomeActivity.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                Intent i=new Intent(HomeActivity.this,SuccMessage.class);
                startActivity(i);

                textViewResponse.setText(Html.fromHtml("<b>yesssYour Voice <a href='" + s + "'>" + s + "</a></b>"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            protected String doInBackground(Void... params) {
                Upload u = new Upload();
                String msg = u.uploadVideo(uploadFilePath + "" + audiofile.getName(),paId.toString());
                //Intent i=new Intent(HomeActivity.this,SuccMessage.class);
               // startActivity(i);

                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++_______________________________"+msg);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }
}