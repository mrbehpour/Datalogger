package ir.saa.android.datalogger;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.google.gson.Gson;

import org.ndeftools.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import database.structs.dtoItemValues;
import database.structs.dtoMaxItemVal;
import nfc.NfcReaderActivity;

public class ActivityMultiMedia extends AppCompatActivity {

    File videoFile;
    LinearLayout llVideo;
    LinearLayout llCamera;
    LinearLayout llVoice;
    String mCurrentPhotoPath;
    VideoView videoView;
    private  static int VIDEO_REQUEST=101;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri videoUri=null;
    ImageView imgTakePhoto;
    dtoItemValues ob;
    String ItemInfoId="";
    File Video;
    File Image;
    LinearLayout btnRecord;
    LinearLayout btnStop;
    LinearLayout llPlay;
    Chronometer chrTime;
    boolean isRecord=false;
    private String voiceFullName ;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    ImageView imgSoundWaves;

    String SAA_ID="";
    String fileCatName="";



    private File createVideoFile() throws IOException {
        File wmiDirectory=null;
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File folder = new File( Environment.getExternalStorageDirectory() +
                File.separator+"dataLogger/Video/");
        boolean success = true;
        String ViFileName = ItemInfoId + "-"+ Tarikh.getCurrentMiladidatetime().replace("/","").replace(":","").trim();
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
            File storageDir = folder;
////                Environment.getExternalStoragePublicDirectory(
////                Environment.DIRECTORY_PICTURES);
            Video = File.createTempFile(
                    ViFileName,  // prefix
                    ".mp4",         // suffix
                    storageDir      // directory

            );

        } else {
            // Do something else on failure
        }




//        if(wmiDirectory.mkdir()) {
//
//        }

        if(Video!=null) {
            mCurrentPhotoPath = Video.getAbsolutePath();
        }
        //mCurrentPhotoPath = image.getAbsolutePath();


        // Save a file: path for use with ACTION_VIEW intents

        return Video;
    }

    private File createImageFile() throws IOException {
        File wmiDirectory=null;
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File folder = new File( Environment.getExternalStorageDirectory() +
                File.separator+"dataLogger/Picture/");
        boolean success = false;
        String ViFileName = ItemInfoId + "-";
        if (!folder.exists()) {
            success = folder.mkdirs();
        }else {
            success=true;
        }
        if (success) {
            // Do something on success
            File storageDir = folder;
////                Environment.getExternalStoragePublicDirectory(
////                Environment.DIRECTORY_PICTURES);
            Image = File.createTempFile(
                    ViFileName,  // prefix
                    ".jpg",         // suffix
                    storageDir      // directory

            );

        } else {
            // Do something else on failure
        }




//        if(wmiDirectory.mkdir()) {
//
//        }

        if(Image!=null) {
            mCurrentPhotoPath = Image.getAbsolutePath().substring(0,ItemInfoId.length()-1);
        }
        //mCurrentPhotoPath = image.getAbsolutePath();


        // Save a file: path for use with ACTION_VIEW intents

        return Image;
    }
    private String  setfileName(String dir){
        String voiceName = "";
        if(fileCatName.equals("")==true) {
            voiceName=dir +"/"+ ItemInfoId + "-"+ Tarikh.getCurrentMiladidatetime()
                    .replace("/","").replace(":","").trim() + ".3gp";
        }
        else{
            voiceName=dir +"/"+ ItemInfoId + "-"+ Tarikh.getCurrentMiladidatetime()
                    .replace("/","").replace(":","").trim() + ".3gp";

        }
        return voiceName;
    }
    private void startRecord(){


        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File folder = new File( Environment.getExternalStorageDirectory() +
                File.separator+"dataLogger/Voice/");
        boolean success = true;
        String ViFileName = ItemInfoId + "-";
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            voiceFullName= setfileName(folder.getAbsolutePath());
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(voiceFullName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mRecorder.prepare();
                mRecorder.start();
            } catch (IOException e) {

            }


        }
    }
    private void stopRecording(){
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        if(ob!=null) {
            TabFragmentItem.OldVoicePath= ob.VoicePath;
        }
        TabFragmentItem.dicItemValues.get(Integer.valueOf(ItemInfoId)).VoicePath=voiceFullName;

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);

        Gson gson=new Gson();
        if(getIntent().getStringExtra("ItemInfoId").equals("null")==false){
            ItemInfoId=getIntent().getStringExtra("ItemInfoId");
        }
        if(getIntent().getStringExtra("ItemInfoValues").equals("null")==false) {
            ob = gson.fromJson(getIntent().getStringExtra("ItemInfoValues"), dtoItemValues.class);
        }
        llPlay=(LinearLayout)findViewById(R.id.llPlay);
        btnRecord=(LinearLayout)findViewById(R.id.btnRecord);
        btnStop=(LinearLayout)findViewById(R.id.btnStop);
        btnStop.setEnabled(false);
        chrTime = (Chronometer) findViewById(R.id.recordSound_chrTime);
        imgSoundWaves=(ImageView)findViewById(R.id.imgSoundWaves);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRecord){
                    btnStop.setEnabled(true);
                    btnRecord.setEnabled(false);
                    startRecord();
                    chrTime.setBase(SystemClock.elapsedRealtime());
                    chrTime.start();
                    isRecord=true;
                    //startRecord.setTextColor(Color.DKGRAY);
                    //btnStop.setBackgroundColor(Color.RED);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imgSoundWaves.setImageTintList(ContextCompat.getColorStateList(G.context, R.color.red_500));
                    }

                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRecord){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    stopRecording();
                    chrTime.stop();
                    isRecord=false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imgSoundWaves.setImageTintList(ContextCompat.getColorStateList(G.context, R.color.green_500));
                        RingtoneManager.getRingtone(G.context, Uri.parse(voiceFullName)).play();

                        isRecord=false;
                        btnStop.setEnabled(false);
                        btnRecord.setEnabled(true);

                    }

                }
            }
        });



        llVideo=(LinearLayout)findViewById(R.id.llVideo);
        llCamera=(LinearLayout)findViewById(R.id.llCamera);
        //llVoice=(LinearLayout)findViewById(R.id.llVoice);
        videoView=(VideoView)findViewById(R.id.videoView);

        llVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoCap=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                try {
                    videoFile=createVideoFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                if(videoCap.resolveActivity(getPackageManager())!=null){
                    videoCap.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
                    startActivityForResult(videoCap,VIDEO_REQUEST);
                }
            }
        });
        llPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    videoView.start();

            }
        });
        imgTakePhoto=(ImageView)findViewById(R.id.imgTakePhoto);
        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();

                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.i("111", "IOException");
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {

                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        Intent pictureIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE
                        );
                        if(pictureIntent.resolveActivity(getPackageManager()) != null) {
                            Uri outputFileUri = Uri.fromFile(photoFile);
                            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                            try {
                                startActivityForResult(pictureIntent,
                                        REQUEST_IMAGE_CAPTURE);
                            }catch (Exception ex){
                                String s=ex.getMessage();
                            }
                        }
                    }
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK ){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            videoUri=data.getData();

            videoView.setVideoURI(videoUri);

            new File(videoUri.getPath());
            if(ob!=null) {
                TabFragmentItem.OldVideoPath= ob.VideoPath;
            }
            TabFragmentItem.dicItemValues.get(Integer.valueOf(ItemInfoId)).VideoPath=videoUri.getPath();

        }
        Bitmap mImageBitmap;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
            try {
                FileInputStream fileInputStream=new FileInputStream(String.valueOf(mCurrentPhotoPath).replace("file:","     ").trim());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=10;
                mImageBitmap=BitmapFactory.decodeStream(fileInputStream,null,options);
                if(ob!=null) {
                    TabFragmentItem.OldImagePath= ob.ImagePath;
                }
                TabFragmentItem.dicItemValues.get(Integer.valueOf(ItemInfoId)).ImagePath=mCurrentPhotoPath;
                imgTakePhoto.setImageURI(Uri.parse(mCurrentPhotoPath));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
