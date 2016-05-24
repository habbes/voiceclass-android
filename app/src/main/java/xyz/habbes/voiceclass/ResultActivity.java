package xyz.habbes.voiceclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {

    String id;
    String classId;

    TextView txResult;
    TextView txFeedbackTitle;
    TextView txFeedbackText;
    Button btnYes;
    Button btnNo;
    LinearLayout layoutBtns;
    ProgressDialog progressDialog;

    RequestQueue requestQueue;

    public final static String MALE_ADULT = "MaleAdult";
    public final static String MALE_CHILD = "MaleChild";
    public final static String MALE_TEEN = "MaleTeen";
    public final static String MALE_SENIOR = "MaleSenior";
    public final static String FEMALE_ADULT = "FemaleAdult";
    public final static String FEMALE_CHILD = "FemaleChild";
    public final static String FEMALE_TEEN = "FemaleTeen";
    public final static String FEMALE_SENIOR = "FemaleSenior";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        classId = intent.getStringExtra("class");

        txResult = (TextView) findViewById(R.id.txResult);
        txFeedbackText = (TextView) findViewById(R.id.txFeedbackText);
        txFeedbackTitle = (TextView) findViewById(R.id.txFeedbackTitle);
        btnYes = (Button) findViewById(R.id.btnResultCorrect);
        btnNo = (Button) findViewById(R.id.btnResultIncorrect);
        layoutBtns = (LinearLayout) findViewById(R.id.layoutResultBtnGroup);

        txResult.setText(className(classId));

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBtnYesClick();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBtnNoClick();
            }
        });
    }

    private void handleBtnYesClick(){
        sendFeedback(classId);
    }

    private void handleBtnNoClick(){

    }

    private void sendFeedback(final String clsId){

        JSONObject body = new JSONObject();
        try {
            body.put("id", id);
            body.put("class", clsId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, MainActivity.FEEDBACK_URL, body,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgressDialog();
                        txResult.setText(className(clsId));
                        txFeedbackText.setText(R.string.feedback_thank_you);
                        txFeedbackTitle.setText(R.string.feedback_machine_learns);
                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                    }
                }
        );
        addRequest(request);
        showProgressDialog("Sending Feedback", "Sending your feedback. Please wait...");
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

    private void addRequest(Request r){
        getRequestQueue().add(r);
    }

    private RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(this);
        }
        return requestQueue;
    }

    private void hideButtons(){
        layoutBtns.setVisibility(View.GONE);
    }

    private void showButtons(){
        layoutBtns.setVisibility(View.VISIBLE);
    }


    private int className(String clsId){
        switch(clsId){
            case MALE_ADULT:
                return R.string.class_male_adult;
            case MALE_CHILD:
                return R.string.class_male_child;
            case MALE_TEEN:
                return R.string.class_male_teen;
            case MALE_SENIOR:
                return R.string.class_male_senior;
            case FEMALE_ADULT:
                return R.string.class_female_adult;
            case FEMALE_CHILD:
                return R.string.class_female_child;
            case FEMALE_SENIOR:
                return R.string.class_female_senior;
            case FEMALE_TEEN:
                return R.string.class_female_teen;
        }
        return R.string.class_unknown;
    }
}
