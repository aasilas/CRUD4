package com.example.nota;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sena on 4/18/2017.
 */

public class JSONSerializer {
    private String nomeFile;
    private Context contexto;

    public JSONSerializer(String nf, Context con) {
        nomeFile = nf;
        contexto = con;
    }

    public void save(List<Nota> nota) throws JSONException, IOException{
        JSONArray jsonArray = new JSONArray();

        for (Nota c : nota){
            jsonArray.put(c.convertToJSON());
        }

        Writer autor = null;
        try {
            OutputStream out = contexto.openFileOutput(nomeFile, contexto.MODE_PRIVATE);
            autor = new OutputStreamWriter(out);
            autor.write(jsonArray.toString());
        } finally {
            if(autor != null){
                autor.close();
            }
        }
    }

    public ArrayList<Nota> load() throws IOException, JSONException{
        ArrayList<Nota> listaNota = new ArrayList<Nota>();
        BufferedReader leitores = null;

        try{
            InputStream in = contexto.openFileInput(nomeFile);
            leitores = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String baris = null;

            while ((baris = leitores.readLine()) != null){
                jsonString.append(baris);
            }

            JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for(int i=0;i<jsonArray.length();i++){
                listaNota.add(new Nota(jsonArray.getJSONObject(i)));
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if(leitores!=null){
                leitores.close();
            }
        }

        return listaNota;
    }
}
