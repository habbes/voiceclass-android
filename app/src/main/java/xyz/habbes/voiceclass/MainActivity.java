package xyz.habbes.voiceclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
    implements SendAudioDialog.SendAudioDialogListener {

    FloatingActionButton fab;
    TextView txRecording;
    TextView txRecordToStart;
    boolean hasRecording = false;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    public static final String BASE_URL = "http://habbes.xyz:9050/api";
    public static final String TRAIN_URL = BASE_URL + "/train";
    public static final String CLASSIFY_URL = BASE_URL + "/classify";
    public static final String FEEDBACK_URL = BASE_URL + "/feedback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txRecording = (TextView) findViewById(R.id.txRecording);
        txRecordToStart = (TextView) findViewById(R.id.txRecordToStart);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFabClick();
            }
        });

    }

    private void addRequest(Request r){
        r.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        getRequestQueue().add(r);
    }

    private RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }

    private void startRecording(){
        setStopRecordingIcon();
        hideRecordToStartText();
        showRecordingText();
        Recorder.startRecording();
        Toast.makeText(this, R.string.recording_started, Toast.LENGTH_SHORT).show();
        hasRecording = false;
    }

    private void stopRecording() {
        Recorder.stopRecording();
        hideRecordingText();
        setStartRecordingIcon();
        Toast.makeText(this, R.string.recording_stopped, Toast.LENGTH_SHORT).show();
        hasRecording = true;
        showSendDialog();
        showRecordToStartText();
    }

    public void sendAudio(){
        JSONObject body = new JSONObject();
        JSONObject audio = new JSONObject();

        try {
            audio.put("type", "pcm");
            audio.put("sampleRate", Recorder.SAMPLE_RATE);
            audio.put("sampleWidth", Recorder.SAMPLE_WIDTH);
            audio.put("channelCount", Recorder.CHANNEL_COUNT);
            try {
                audio.put("data", Base64.encodeToString(
                        Recorder.readRecordedData(), Base64.DEFAULT
                ));
            } catch (IOException e) {
                e.printStackTrace();
                errorToast(getResources().getString(R.string.error_reading_recording));
                return;
            }
            body.put("audio", audio);
        } catch(JSONException e){
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, CLASSIFY_URL, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressDialog();
                try {
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("id", response.getString("id"));
                    intent.putExtra("class", response.getString("class"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    errorToast(R.string.error_response);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Log.e("VoiceClass:SENDAUDIO", error.toString());
                errorToast(R.string.error_try_again);
            }
        }
        );

        addRequest(request);
        showProgressDialog(getResources().getString(R.string.analyzing_audio_title),
                getResources().getString(R.string.analyzing_audio_message));
    }

    private void errorToast(int res){
        errorToast(getResources().getString(res));
    }

    private void errorToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void hideRecordToStartText(){
        txRecordToStart.setVisibility(View.GONE);
    }

    private void showRecordToStartText(){
        txRecordToStart.setVisibility(View.VISIBLE);
    }

    private void showRecordingText(){
        txRecording.setVisibility(View.VISIBLE);
    }

    private void hideRecordingText(){
        txRecording.setVisibility(View.GONE);
    }

    private void showSendDialog(){
        DialogFragment dialog = new SendAudioDialog();
        dialog.show(getSupportFragmentManager(), "SendAudioDialog");
    }

    private void showProgressDialog(String title, String message){
        hideProgressDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setTitle(title);
        progressDialog.show();
    }

    private void hideProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void handleFabClick(){
        if(Recorder.isRecording()){
            stopRecording();
        } else {
            startRecording();
        }
    }

    private void setStartRecordingIcon(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_btn_speak_now, getApplicationContext().getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_btn_speak_now));
        }
    }

    private void setStopRecordingIcon(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause, getApplicationContext().getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSendAudioDialogPositiveClick(DialogFragment dialog) {
        sendAudio();
    }

    @Override
    public void onSendAudioDialogNegativeClick(DialogFragment dialog) {

    }
}
