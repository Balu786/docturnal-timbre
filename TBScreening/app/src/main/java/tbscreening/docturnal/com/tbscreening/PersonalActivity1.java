package tbscreening.docturnal.com.tbscreening;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PersonalActivity1 extends AppCompatActivity implements View.OnClickListener {
    Spinner maritalView,heightSpnr,genderView;
    public static Bitmap productPic=null;
    public static ImageView imgview,category_focusSell,subCategory_focusSell;

    TextView nameEdit,mobileEdt,ageEdit,weightEdt,zipEdt,occupations;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal1);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        heightSpnr = (Spinner)findViewById(R.id.height);
        String[] items = new String[]{"4.5 - 5.0", "5.0 - 5.5", "5.5 - 6.0","6.0 - 6.5","6.5 +"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        heightSpnr.setAdapter(adapter);
        maritalView = (Spinner)findViewById(R.id.marital);
        String[] marital = new String[]{"Single", "Married", "divorced"};
        ArrayAdapter<String> maritalDro = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, marital);
        maritalView.setAdapter(maritalDro);
        genderView = (Spinner)findViewById(R.id.gender);
        String[] gender = new String[]{"Male", "Female", "Other"};
        ArrayAdapter<String> genderDro = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gender);
        genderView.setAdapter(genderDro);

        imgview=(ImageView)findViewById(R.id.loadImg);
        imgview.setOnClickListener(this);
        nameEdit=(TextView)findViewById(R.id.nameEdit);
        mobileEdt=(TextView)findViewById(R.id.mobileEdt);
        ageEdit=(TextView)findViewById(R.id.ageEdit);
        weightEdt=(TextView)findViewById(R.id.weightEdt);
        zipEdt=(TextView)findViewById(R.id.zipEdt);
        occupations=(TextView)findViewById(R.id.occupations);




    }
    private void selectImgages() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity1.this);
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

                } else if (options[item].equals("Choose from Gallery")) {
                    /*Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);*/
                   /* Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image*//*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            22);*/
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                    startActivityForResult(galleryIntent, 22);

                } else if (options[item].equals("Cancel")) {
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
                Bitmap bt=Bitmap.createScaledBitmap(thumbnail, 220, 280, false);
                imgview.setImageBitmap(bt);
                productPic=bt;
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
        BitmapFactory.decodeStream(PersonalActivity1.this.getContentResolver().openInputStream(selectedImage), null, o);

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
        return BitmapFactory.decodeStream(PersonalActivity1.this.getContentResolver().openInputStream(selectedImage), null, o2);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void savePersonalDeatial1(View v)
    {
        NetStatus netStatus=new NetStatus(PersonalActivity1.this);
        if(netStatus.isNetworkAvailable()) {
            if(doValidations()){
            final String name, mobile, age, weight, zip, gender, mstatus, heigt;
            name = nameEdit.getText().toString();
            mobile = mobileEdt.getText().toString();
            age = ageEdit.getText().toString();
            weight = weightEdt.getText().toString();
            zip = zipEdt.getText().toString();
            gender = genderView.getSelectedItem().toString();
            mstatus = maritalView.getSelectedItem().toString();
            heigt = heightSpnr.getSelectedItem().toString();



            JSONObject parent = new JSONObject();
            JSONObject child = new JSONObject();
            JSONArray jarray = new JSONArray();

            try {
                child.put("name", name);
                child.put("mobile", mobile);
                child.put("age", age);
                child.put("occupation", occupations.getText().toString());
                child.put("weight", weight);
                child.put("zip", zip);
                child.put("gender", gender);
                child.put("mstatus", mstatus);
                child.put("height", heigt);
                if (productPic != null) {
                    child.put("image", getStringImage(productPic));
                } else {
                    child.put("image", "null");
                }

                jarray.put(child);

                parent.put("personalDetails", jarray);
                System.out.print(parent);

           /* Toast.makeText(PersonalActivity1.this, parent.toString(), Toast.LENGTH_SHORT).show();*/
                ServiceUtil service = new ServiceUtil();
                String s = service.makeRequest("http://abhinav-training.com/api/webservices.php", parent.toString());
                System.out.print(parent);
                System.out.print(s);
                JSONObject obj = new JSONObject(s);
                // Toast.makeText(v.getContext(), obj.get("status").toString(), Toast.LENGTH_SHORT).show();
                if (obj.get("status").equals("1") || obj.get("status") == "1") {
                    Toast.makeText(PersonalActivity1.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, PersonalActivity2.class);
                    intent.putExtra("last_id", obj.get("lastId").toString());
                    startActivity(intent);

                } else {
                    Toast.makeText(PersonalActivity1.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        }
        else {
            Toast.makeText(PersonalActivity1.this, "Please Check Internet Connection Once", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View v) {
        try {
            selectImgages();
            Toast.makeText(PersonalActivity1.this, "Error", Toast.LENGTH_SHORT).show();

        }catch (Exception em){
            Toast.makeText(PersonalActivity1.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean doValidations(){

        if(getLen(nameEdit)){
            if(getLenMobile(mobileEdt)){
                if(getLen(occupations)) {
                if(getLen(ageEdit)){
                        if (getLen(weightEdt)) {
                            if (getLen(zipEdt)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }


        return  false;
    }
    private boolean getLen(TextView tt){
        if(tt.getText().length()>0){
            return true;
        }else {
            tt.setError("Field is required");
            return false;
        }
    }
    private boolean getLenMobile(TextView tt){
        if(tt.getText().length()>0){
            if(tt.getText().length()==10){
                char n=tt.getText().charAt(0);
                if((n=='9')  || (n=='8') || (n=='7')){
                    return true;
                }else {
                    tt.setError("Please give Valid Number");
                }

            }else {
                tt.setError("Enter 10 Digits");
                return false;
            }
            //return true;
        }else {
            tt.setError("Field is required");
            return false;
        }
        return false;
    }

}
