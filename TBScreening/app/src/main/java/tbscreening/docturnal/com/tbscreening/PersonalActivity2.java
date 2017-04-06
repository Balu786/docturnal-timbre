package tbscreening.docturnal.com.tbscreening;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonalActivity2 extends AppCompatActivity {
EditText sugerEdit,bpEdit,headacheEdit,travellingEdit,coughtEdit;
    RadioGroup smokingRg,nightSweatRg,iossRg,alcoholGrp,tbGrp,tbProxiGrp,CoughINdicatorGrp,hivGrp,exerciseGrp;
    SharedPreferences sharedPreferences;
    Spinner diabetesView,SupplementsVieew;
    private RadioButton smokeButton,nightButton,iossButton;
    String paId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal2);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            paId = extras.getString("last_id");
            // and get whatever type user account id is
        }
        //Toast.makeText(PersonalActivity2.this, paId.toString(), Toast.LENGTH_SHORT).show();


        smokingRg=(RadioGroup)findViewById(R.id.smokingGrp);
        alcoholGrp=(RadioGroup)findViewById(R.id.alcoholGrp);

        tbGrp=(RadioGroup)findViewById(R.id.tbGrp);

        tbProxiGrp=(RadioGroup)findViewById(R.id.tbProxiGrp);
        CoughINdicatorGrp=(RadioGroup)findViewById(R.id.CoughINdicatorGrp);
        hivGrp=(RadioGroup)findViewById(R.id.hivGrp);
        exerciseGrp=(RadioGroup)findViewById(R.id.exerciseGrp);
        nightSweatRg=(RadioGroup)findViewById(R.id.nightSweatGrp);
        iossRg=(RadioGroup)findViewById(R.id.iossGrp);

        sharedPreferences=getSharedPreferences("clinic_data", Context.MODE_APPEND);
        diabetesView = (Spinner)findViewById(R.id.diabetes);
        String[] diabetes = new String[]{"None","Type1", "Type2", "Type3"};
        ArrayAdapter<String> genderDro = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, diabetes);
        diabetesView.setAdapter(genderDro);
        SupplementsVieew = (Spinner)findViewById(R.id.SupplementsDrop);
        String[] supplements = new String[]{"Vitamin", "sea cod", "Ayurveda","unani"};
        ArrayAdapter<String> sDro = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, supplements);
        SupplementsVieew.setAdapter(sDro);

    }
    public  String getSup(){


        CheckBox hcheck = (CheckBox)findViewById(R.id.hcheck);
       // CheckBox ccheck = (CheckBox)findViewById(R.id.ccheck);
        CheckBox vcheck = (CheckBox)findViewById(R.id.vcheck);
        CheckBox scheck = (CheckBox)findViewById(R.id.scheck);
        CheckBox acheck = (CheckBox)findViewById(R.id.acheck);
        CheckBox ucheck = (CheckBox)findViewById(R.id.ucheck);

        String supData="";
        if(hcheck.isChecked()){
            supData=supData+hcheck.getText()+", ";
        }
       /* if(ccheck.isChecked()){
            supData=supData+ccheck.getText()+", ";
        }*/
        if(vcheck.isChecked()){
            supData=supData+vcheck.getText()+", ";
        }
        if(scheck.isChecked()){
            supData=supData+scheck.getText()+", ";
        }
        if(acheck.isChecked()){
            supData=supData+acheck.getText()+", ";
        }
        if(ucheck.isChecked()){
            supData=supData+ucheck.getText()+", ";
        }

        return supData;
    }


    public void voiceRecoringSrc(View v){
        String smoking,alcol,fhtb,lwtb,pci,hiv,dibs,sup,ns,loa,ex;
        smoking=getRadioText(smokingRg.getCheckedRadioButtonId());
        alcol=getRadioText(alcoholGrp.getCheckedRadioButtonId());
        fhtb=getRadioText(tbGrp.getCheckedRadioButtonId());
        lwtb=getRadioText(tbProxiGrp.getCheckedRadioButtonId());
        pci=getRadioText(CoughINdicatorGrp.getCheckedRadioButtonId());
        hiv=getRadioText(hivGrp.getCheckedRadioButtonId());
        ns=getRadioText(nightSweatRg.getCheckedRadioButtonId());
        loa=getRadioText(iossRg.getCheckedRadioButtonId());
        ex=getRadioText(exerciseGrp.getCheckedRadioButtonId());
        dibs=diabetesView.getSelectedItem().toString();
        sup=SupplementsVieew.getSelectedItem().toString();

        NetStatus netStatus=new NetStatus(PersonalActivity2.this);
        if(netStatus.isNetworkAvailable()) {
            JSONObject parent = new JSONObject();
            JSONObject child = new JSONObject();
            JSONArray jarray = new JSONArray();

            try {
                child.put("pid", paId);
                child.put("Smoking", smoking);
                child.put("Alcohol", alcol);
                child.put("FHTB", fhtb);
                child.put("LWTB", lwtb);
                child.put("PCi", pci);
                child.put("HIV", hiv);
                child.put("Diabetes", dibs);
                child.put("Supplement", getSup());
                child.put("NSW", ns);
                child.put("LOA", loa);
                child.put("Exe", ex);
                jarray.put(child);

                Intent intent = new Intent(this, ClinicalActivity.class);
                intent.putExtra("cdata", child.toString());
                intent.putExtra("pid", paId.toString());
                startActivity(intent);

           /* parent.put("ClinicalDetails", jarray);
            System.out.print(parent);

           // Toast.makeText(PersonalActivity2.this, parent.toString(), Toast.LENGTH_SHORT).show();
            ServiceUtil service=new  ServiceUtil();
            String s= service.makeRequest("http://alabs.in/proddy/insertfile.php",parent.toString());
            Toast.makeText(v.getContext(), s.toString(), Toast.LENGTH_SHORT).show();
            if(s.equals("success")){
                Toast.makeText(PersonalActivity2.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,ClinicalActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(PersonalActivity2.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }*/


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(PersonalActivity2.this, "Please Check Internet Connection Once", Toast.LENGTH_SHORT).show();
        }



    }
    public String getRadioText(int id){
        String text;
        RadioButton rd=(RadioButton)findViewById(id);
        text=rd.getText().toString();
        return text;
    }

}
