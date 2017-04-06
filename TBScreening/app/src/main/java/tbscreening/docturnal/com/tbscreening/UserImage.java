package tbscreening.docturnal.com.tbscreening;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserImage extends AppCompatActivity implements View.OnClickListener {
    public static Bitmap productPic=null;
    public static ImageView imgview;
    String paId="";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_image);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            paId = extras.getString("last_id");
            // and get whatever type user account id is
        }
        imgview=(ImageView)findViewById(R.id.loadImg);
        imgview.setOnClickListener(this);
        Button next=(Button)findViewById(R.id.next);
        next.setOnClickListener(this);

        Button uploadBtn=(Button)findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadImg:
                try {
                    selectImgages();
                } catch (Exception em) {
                    onBackPressed();
                }
                break;
            case R.id.uploadBtn:
                try {
                    selectImgages();
                } catch (Exception em) {
                    onBackPressed();
                }
                break;
            case R.id.next:
                NetStatus netStatus=new NetStatus(UserImage.this);
                if(netStatus.isNetworkAvailable()) {
                    JSONObject parent = new JSONObject();
                    JSONObject child = new JSONObject();
                    JSONArray jarray = new JSONArray();

                    try {

                        child.put("pid", paId.toString());

                        if (productPic != null) {
                            child.put("image", getStringImage(productPic));
                        } else {
                            child.put("image", "null");
                        }

                        jarray.put(child);
                        parent.put("UserImage", jarray);
                        System.out.print(parent);


                        ServiceUtil service = new ServiceUtil();
                        String s = service.makeRequest("http://abhinav-training.com/api/webservices.php", parent.toString());
                        System.out.print(parent);
                        System.out.print(s);
                        JSONObject obj = new JSONObject(s);
                        // Toast.makeText(v.getContext(), obj.get("status").toString(), Toast.LENGTH_SHORT).show();
                        if (obj.get("status").equals("1") || obj.get("status") == "1") {
                            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, HomeActivity.class);
                            intent.putExtra("last_id", paId.toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    } catch (JSONException ec) {

                    }
                }else {
                    Toast.makeText(UserImage.this, "Please Check the Internet Connection Once", Toast.LENGTH_SHORT).show();
                }

        }
    }


    private void selectImgages() {
        final CharSequence[] options = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserImage.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);*/
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    startActivityForResult(intent, 11);

                } /*else if (options[item].equals("Choose from Gallery")) {
                    *//*Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);*//*
                   *//* Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image*//**//*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            22);*//*
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                    startActivityForResult(galleryIntent, 22);

                }*/ else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 11) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
               /* Bitmap bt=Bitmap.createScaledBitmap(thumbnail, 220, 280, false);*/
                imgview.setImageBitmap(thumbnail);
                productPic=thumbnail;
            } else if (requestCode == 22) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                try {
                    Bitmap bitmapImage =decodeBitmap(selectedImage );
                    Bitmap bt=Bitmap.createScaledBitmap(bitmapImage, 220, 280, false);
                    imgview.setImageBitmap(bt);
                    productPic=bt;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public  Bitmap decodeBitmap(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(UserImage.this.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 120;
        final int REQUIRED_SIZE1 = 130;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE1) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(UserImage.this.getContentResolver().openInputStream(selectedImage), null, o2);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
