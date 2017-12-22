package com.example.nota;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sena on 3/22/2017.
 */

public class DialogShowNote extends DialogFragment {
    private Nota baruCT;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_abre_nota,null);

        TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        TextView txtDesc = (TextView) view.findViewById(R.id.txtDescription);
        txtTitle.setText(baruCT.getTitulo());
        txtDesc.setText(baruCT.getDescricao());
        ImageView gambarPenting = (ImageView) view.findViewById(R.id.imageViewImportant);
        ImageView gambarIde = (ImageView) view.findViewById(R.id.imageViewIdea);
        ImageView gambarTodo = (ImageView) view.findViewById(R.id.imageViewTodo);
        Button btnOK = (Button) view.findViewById(R.id.btnOK);

        if (!baruCT.isIniImportante()){
            gambarPenting.setVisibility(View.GONE);
        }
        if (!baruCT.isIniIdeia()){
            gambarIde.setVisibility(View.GONE);
        }
        if (!baruCT.isIniTodo()){
            gambarTodo.setVisibility(View.GONE);
        }

        builder.setView(view).setMessage("Suas anotações");

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }

    public void sendNoteSelected(Nota noteSelected){
        baruCT = noteSelected;
    }
}
