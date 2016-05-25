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
public class SendAudioDialog extends DialogFragment {

    public interface SendAudioDialogListener {
        public void onSendAudioDialogPositiveClick(DialogFragment dialog);
        public void onSendAudioDialogNegativeClick(DialogFragment dialog);
    }

    SendAudioDialogListener dialogListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            dialogListener = (SendAudioDialogListener) activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                + " must implement SendAudioDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.submit_dialog_title)
                .setMessage(R.string.submit_dialog_message)
                .setPositiveButton(R.string.btn_send_audio, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogListener.onSendAudioDialogPositiveClick(SendAudioDialog.this);
                    }
                })
                .setNegativeButton(R.string.btn_discard, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogListener.onSendAudioDialogNegativeClick(SendAudioDialog.this);
                    }
                });
        return builder.create();


    }
}
