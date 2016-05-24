package xyz.habbes.voiceclass;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
    implements SendAudioDialog.SendAudioDialogListener {

    FloatingActionButton fab;
    TextView txRecording;
    TextView txRecordToStart;
    boolean hasRecording = false;

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

    private void startSubmitActivity(){
        Intent intent = new Intent(getApplicationContext(), SubmitAudioActivity.class);
        startActivity(intent);
    }

    private void showSendDialog(){
        DialogFragment dialog = new SendAudioDialog();
        dialog.show(getSupportFragmentManager(), "SendAudioDialog");
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

    }

    @Override
    public void onSendAudioDialogNegativeClick(DialogFragment dialog) {

    }
}
