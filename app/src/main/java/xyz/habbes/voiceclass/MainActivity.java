package xyz.habbes.voiceclass;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        Recorder.startRecording();
        Toast.makeText(this, R.string.recording_started, Toast.LENGTH_SHORT);
    }

    private void stopRecording(){
        Recorder.stopRecording();
        setStartRecordingIcon();
        Toast.makeText(this, R.string.recording_started, Toast.LENGTH_SHORT);
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
}
