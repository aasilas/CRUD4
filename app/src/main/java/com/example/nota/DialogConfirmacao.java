package com.example.nota;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Sena on 5/27/2017.
 */

public class DialogConfirmacao extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder((MainActivity) getActivity());
        builder.setMessage("Apagar esta com?");
        builder.setPositiveButton(getResources().getText(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity panggilaktivitas = (MainActivity) getActivity();
                panggilaktivitas.apagarNota();
            }
        });
        builder.setNegativeButton(getResources().getText(R.string.cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }
}
