package tbscreening.docturnal.com.tbscreening;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    public  void iagree(View v)
    {
        NetStatus netStatus=new NetStatus(MainActivity.this);
        System.out.println(netStatus.isNetworkAvailable());
        if(netStatus.isNetworkAvailable()) {
            Toast.makeText(MainActivity.this, "Innert", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PersonalActivity1.class);
            startActivity(intent);
        }else {
            Toast.makeText(MainActivity.this, "Please Check Internet Connection Once", Toast.LENGTH_SHORT).show();
        }
    }
}
