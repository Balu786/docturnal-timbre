package tbscreening.docturnal.com.tbscreening;//com.example.balu.videoupload;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Teja Anil Kumar on 1/13/2016.
 */
public class BackgroundTask extends AsyncTask<String,Void,String>
{
    AlertDialog alertDialog;
    Context ctx;
    SharedPreferences sharedPreferences;
    HttpURLConnection httpURLConnection =null;

    BackgroundTask(Context ctx)
    {
        this.ctx= ctx;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        //protected String doInBackGroun(String method,Student stud)
        String reg_url = "http://abhinav-training.com/api/api_index2.php?action=regUser";
      /*  StudentVO studVo = new StudentVO();
        JSONObject jsonObj = new JSONObject(studVo);*/
        String getName = params[0];
        String getsex = params[1];
        String getmarried_status = params[2];
        String gettravellingFrequncy = params[3];
        String getsmoke = params[4];
        String getioss = params[5];
        String getcough = params[6];
        String getbp = params[7];
        String getsuger = params[8];
        String getheight = params[9];
        String gettravellingMode = params[10];
        String getheadache = params[11];
        String getalcohol = params[12];
        String getage = params[13];
        String getsubstanceUsage = params[14];

        String gettemparature = params[15];
        String getnight = params[16];
        String getzipcode1 = params[17];

        String getpromiscuity = params[18];
        String getmobile= params[19];
        String getbronchitis=params[20];
        String getwiedth=params[21];




        try {
            URL url = new URL(reg_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches (false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
            String data  = URLEncoder.encode("getName","UTF-8") +"="+URLEncoder.encode(getName,"UTF-8")+ "&"+
                    URLEncoder.encode("getsex","UTF-8") +"="+URLEncoder.encode(getsex,"UTF-8")+ "&"+
                    URLEncoder.encode("getmarried_status","UTF-8") +"="+URLEncoder.encode(getmarried_status,"UTF-8")+ "&"+
                    URLEncoder.encode("gettravellingFrequncy","UTF-8") +"="+URLEncoder.encode(gettravellingFrequncy,"UTF-8")+ "&"+
                    URLEncoder.encode("getsmoke","UTF-8") +"="+URLEncoder.encode(getsmoke,"UTF-8")+ "&"+
                    URLEncoder.encode("getioss","UTF-8") +"="+URLEncoder.encode(getioss,"UTF-8")+ "&"+
                    URLEncoder.encode("getcough","UTF-8") +"="+URLEncoder.encode(getcough,"UTF-8")+ "&"+
                    URLEncoder.encode("getbp","UTF-8") +"="+URLEncoder.encode(getbp,"UTF-8")+ "&"+
                    URLEncoder.encode("getsuger","UTF-8") +"="+URLEncoder.encode(getsuger,"UTF-8")+ "&"+
                    URLEncoder.encode("getheight","UTF-8") +"="+URLEncoder.encode(getheight,"UTF-8")+ "&"+
                    URLEncoder.encode("getheadache","UTF-8") +"="+URLEncoder.encode(getheadache,"UTF-8")+ "&"+
                    URLEncoder.encode("getalcohol","UTF-8") +"="+URLEncoder.encode(getalcohol,"UTF-8")+ "&"+
                    URLEncoder.encode("gettravellingMode","UTF-8") +"="+URLEncoder.encode(gettravellingMode,"UTF-8")+ "&"+
                    URLEncoder.encode("getage","UTF-8") +"="+URLEncoder.encode(getage,"UTF-8")+ "&"+
                    URLEncoder.encode("gettemparature","UTF-8") +"="+URLEncoder.encode(gettemparature,"UTF-8")+ "&"+
                    URLEncoder.encode("getnight","UTF-8") +"="+URLEncoder.encode(getnight,"UTF-8")+ "&"+
                    URLEncoder.encode("getzipcode1","UTF-8") +"="+URLEncoder.encode(getzipcode1,"UTF-8")+ "&"+
                    URLEncoder.encode("getpromiscuity","UTF-8") +"="+URLEncoder.encode(getpromiscuity,"UTF-8")+ "&"+
                    URLEncoder.encode("getmobile","UTF-8") +"="+URLEncoder.encode(getmobile,"UTF-8")+ "&"+
                    URLEncoder.encode("getwiedth","UTF-8") +"="+URLEncoder.encode(getwiedth,"UTF-8")+ "&"+
                    URLEncoder.encode("getsubstanceUsage","UTF-8") +"="+URLEncoder.encode(getsubstanceUsage,"UTF-8")+ "&"+
                    URLEncoder.encode("getbronchitis","UTF-8") +"="+URLEncoder.encode(getbronchitis,"UTF-8");

            bufferedWriter.write(data);
            //System.out.println("_____________________________" + method + "_____++++++++" + first_name+"_________"+data);
            //bufferedWriter.write(new JsonObject(stud));
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();


            //Get Response
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {

                response.append('\r');
                response.append(line);

            }
            rd.close();
           // System.out.println("rs----"+response.toString());
            String serviceRs=response.toString();
            String[] result = serviceRs.split("/");

            return result[1];


        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login Information.......");

    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("rt------"+result);

       /* Toast.makeText( ctx , result, Toast.LENGTH_SHORT).show();*/
       /* if (result.equals("Inserted Successfully"))
        {
            Toast.makeText( ctx , result, Toast.LENGTH_SHORT).show();
        }
        else
        {
            alertDialog.setMessage(result);
            alertDialog.show();
        }*/

    }
}

