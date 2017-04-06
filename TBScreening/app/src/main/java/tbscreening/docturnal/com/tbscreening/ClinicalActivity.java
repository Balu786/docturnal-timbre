package tbscreening.docturnal.com.tbscreening;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClinicalActivity extends AppCompatActivity {
EditText promiscuity,travelling,bronchitis;
    RadioGroup alcohol,substance;
    SharedPreferences sharedPreferences;
    TextView textViewResponse;
    EditText coughFrequencyEdit,medicationEdit,hba1cEdit,bpEdit;//,immunityEdit;
    Spinner coughtView,appetitePattern,Diet,Calories,OtherCough,travellingMode,chestPaind,currentFever;
    private RadioButton alcoholButton,substanceButton;
    String paId,last_id;
    JSONObject child;
    CheckBox noneCheck,wCheck,bCheck,aCheck,pCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinical);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            paId = extras.getString("cdata");
            last_id = extras.getString("pid");
            // and get whatever type user account id is
        }
        try {
            child=new JSONObject(paId.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        noneCheck = (CheckBox)findViewById(R.id.noneCheck);
        wCheck = (CheckBox)findViewById(R.id.wCheck);
        bCheck = (CheckBox)findViewById(R.id.bCheck);
        aCheck = (CheckBox)findViewById(R.id.aCheck);
        pCheck = (CheckBox)findViewById(R.id.pCheck);
        noneCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    wCheck.setEnabled(false);
                    bCheck.setEnabled(false);
                    aCheck.setEnabled(false);
                    pCheck.setEnabled(false);
                    wCheck.setChecked(false);
                    bCheck.setChecked(false);
                    aCheck.setChecked(false);
                    pCheck.setChecked(false);
                }else{
                    wCheck.setEnabled(true);
                    bCheck.setEnabled(true);
                    aCheck.setEnabled(true);
                    pCheck.setEnabled(true);
                }
            }
        });

        alcohol=(RadioGroup)findViewById(R.id.alcoholGrp);
        textViewResponse = (TextView) findViewById(R.id.textViewResponse);
        sharedPreferences=getSharedPreferences("clinic_data", Context.MODE_APPEND);
        coughtView = (Spinner)findViewById(R.id.coughType);
        chestPaind=(Spinner)findViewById(R.id.chestPaind);
        appetitePattern=(Spinner)findViewById(R.id.appetiteType);
        travellingMode = (Spinner)findViewById(R.id.spinner1);
        currentFever = (Spinner)findViewById(R.id.currentFever);
        OtherCough=(Spinner)findViewById(R.id.othercough);
        String[] items = new String[]{"Low", "Medium", "High"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        travellingMode.setAdapter(adapter);
        String[] cought = new String[]{"continuous", "irregular", "rarely "};
        String[] AppetitePatternItems = new String[]{"Low", "Medium","High"};
        String[] otherCoughItems = new String[]{"Whooping","Bronchitis", "Asthma","Pneumonia"};
        String[] chestpaind = new String[]{"No", "Yes"};
        String[] feverArray = new String[]{"None","Morning", "Night","All Day"};


       ArrayAdapter<String> coughtDro = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cought);
        ArrayAdapter<String> appetitePatternadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, AppetitePatternItems);
        ArrayAdapter<String> OtherCoughadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, otherCoughItems);
        ArrayAdapter<String> chestadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, chestpaind);
        ArrayAdapter<String> feverAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, feverArray);

        coughtView.setAdapter(coughtDro);
        appetitePattern.setAdapter(appetitePatternadapter);
        OtherCough.setAdapter(OtherCoughadapter);
        chestPaind.setAdapter(chestadapter);
        currentFever.setAdapter(feverAdapter);


        coughFrequencyEdit=(EditText)findViewById(R.id.coughFrequencyEdit);
        medicationEdit=(EditText)findViewById(R.id.medicationEdit);
        hba1cEdit=(EditText)findViewById(R.id.hba1cEdit);
        bpEdit=(EditText)findViewById(R.id.bpEdit);
        //immunityEdit=(EditText)findViewById(R.id.immunityEdit);

    }
    public  void voiceRecoringSrc(View v)
    {

        NetStatus netStatus=new NetStatus(ClinicalActivity.this);
        if(netStatus.isNetworkAvailable()) {
            if(doValidations()) {
                JSONObject parent = new JSONObject();
                JSONArray jarray = new JSONArray();
                String supData="";
                if(wCheck.isChecked()){
                    supData=supData+wCheck.getText()+", ";
                }
                if(bCheck.isChecked()){
                    supData=supData+bCheck.getText()+", ";
                }
                if(aCheck.isChecked()){
                    supData=supData+aCheck.getText()+", ";
                }
                if(pCheck.isChecked()){
                    supData=supData+pCheck.getText()+", ";
                }


                try {
                    child.put("HBA", hba1cEdit.getText().toString());
                    child.put("BP", bpEdit.getText().toString());
                    child.put("AP", appetitePattern.getSelectedItem());
                    child.put("CFQ", coughFrequencyEdit.getText().toString());
                    child.put("CT", coughtView.getSelectedItem());
                    child.put("OC",supData.toString());

                    child.put("fever_pattern",currentFever.getSelectedItem());
                    child.put("chestPain",chestPaind.getSelectedItem());

                    child.put("TFQ", travellingMode.getSelectedItem());
                    child.put("CMS", medicationEdit.getText().toString());
                    //child.put("OSE", immunityEdit.getText().toString());

                    jarray.put(child);

                    parent.put("MoreClinicalDetails", jarray);
                    System.out.println(parent);

                    ServiceUtil service = new ServiceUtil();
                    String s = service.makeRequest("http://abhinav-training.com/api/webservices.php", parent.toString());
                    JSONObject obj = new JSONObject(s);
                    if (obj.get("status").equals("1") || obj.get("status") == "1") {
                        Toast.makeText(ClinicalActivity.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, UserImage.class);
                        intent.putExtra("last_id", last_id.toString());
                        startActivity(intent);

                    } else {
                        Toast.makeText(ClinicalActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }else{
            Toast.makeText(ClinicalActivity.this, "Check you internet connection once", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean doValidations(){
        if(getLen(hba1cEdit)){
           /* if(getLen(bpEdit)){*/
                if(getLen(coughFrequencyEdit)){
                    if(getLen(medicationEdit)){
                       /* if(getLen(immunityEdit)){*/
                            return true;
                       /* }*/
                    }
                }
          /*  }*/
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
}
