package xyz.habbes.voiceclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    String id;
    String classId;

    TextView txResult;
    Button btnYes;
    Button btnNo;
    ProgressDialog progressDialog;

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
        btnYes = (Button) findViewById(R.id.btnResultCorrect);
        btnNo = (Button) findViewById(R.id.btnResultIncorrect);

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

    }

    private void handleBtnNoClick(){

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
