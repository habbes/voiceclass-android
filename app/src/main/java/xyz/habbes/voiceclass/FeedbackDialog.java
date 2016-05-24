package xyz.habbes.voiceclass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Habbes on 24/05/2016.
 */
public class FeedbackDialog extends DialogFragment {

    public interface FeedbackDialogListener {
        public void onFeedbackDialogPositiveClick(DialogFragment dialog, int selected);
        public void onFeedbackDialogNegativeClick(DialogFragment dialog, int selected);
    }

    FeedbackDialogListener dialogListener;
    int selectedIndex = -1;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            dialogListener = (FeedbackDialogListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                + " should implement FeedbackDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.feedback_correct_prompt)
                .setSingleChoiceItems(R.array.voice_classes, -1, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex = which;
                    }
                })
                .setPositiveButton(R.string.feedback_send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogListener.onFeedbackDialogPositiveClick(FeedbackDialog.this, selectedIndex);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogListener.onFeedbackDialogNegativeClick(FeedbackDialog.this, selectedIndex);
                    }
                });

        return builder.create();

    }

}