package com.example.nota;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sena on 4/19/2017.
 */

public class DialogEditNote extends DialogFragment {
    private Nota novo = new Nota();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_nota_novo,null);

        final EditText editTitle = (EditText) dialogView.findViewById(R.id.editTitle);
        final EditText editDesc = (EditText) dialogView.findViewById(R.id.editDescription);
        final CheckBox cbIde = (CheckBox) dialogView.findViewById(R.id.cbIdea);
        final CheckBox cbTodo = (CheckBox) dialogView.findViewById(R.id.cbTodo);
        final CheckBox cbPenting = (CheckBox) dialogView.findViewById(R.id.cbImportant);
        Button btnOK = (Button) dialogView.findViewById(R.id.btnOK);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);
        btnCancel.setText("Apagar");
        if(novo != null) {
            editTitle.setText(novo.getTitulo());
            editDesc.setText(novo.getDescricao());
            cbIde.setChecked(novo.isIniIdeia());
            cbPenting.setChecked(novo.isIniImportante());
            cbTodo.setChecked(novo.isIniTodo());
        }

        builder.setView(dialogView).setMessage("Mudar anotações");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogConfirmacao dialog = new DialogConfirmacao();
                dialog.show(getFragmentManager(),"");
                dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novo.setTitulo(editTitle.getText().toString());
                novo.setDescricao(editDesc.getText().toString());
                novo.setIniIdeia(cbIde.isChecked());
                novo.setIniTodo(cbTodo.isChecked());
                novo.setIniImportante(cbPenting.isChecked());

                MainActivity panggilaktivitas = (MainActivity) getActivity();
                if (!novo.getTitulo().isEmpty()) {
                    panggilaktivitas.mudarNota(novo);
                    dismiss();
                } else {
                    Toast.makeText(panggilaktivitas, "Error: ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return builder.create();
    }

    public void sendNoteSelected(Nota noteSelected){
        novo = noteSelected;
    }
}
