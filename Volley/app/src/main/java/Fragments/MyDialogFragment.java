package Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fbulou.volley.R;

public class MyDialogFragment extends DialogFragment {

    public interface RetryButtonListener {
        void onClickRetryButton();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Hey buddy, it seems you're not connected to the internet")
                .setIcon(R.mipmap.no_net2)
                .setMessage("Would you like to retry ?")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RetryButtonListener mActivity = (RetryButtonListener) getActivity();
                        mActivity.onClickRetryButton();
                        dismiss(); //dismiss the dialog fragment
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        getActivity().finish();
                    }
                });

        return mAlertDialogBuilder.create();
    }
}
