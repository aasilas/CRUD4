package com.example.nota;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sena on 3/21/2017.
 */

public class Nota {
    private String titulo;
    private String descricao;
    private boolean iniIdeia;
    private boolean iniTodo;
    private boolean iniImportante;

    private static final String JSON_TITLE = "titulo";
    private static final String JSON_DESCRIPTION = "descrição";
    private static final String JSON_IDEA = "ideia";
    private static final String JSON_TODO = "todo";
    private static final String JSON_IMPORTANT = "importante";

    public Nota(){}

    public Nota(JSONObject jsonObject) throws JSONException{
        titulo = jsonObject.getString(JSON_TITLE);
        descricao = jsonObject.getString(JSON_DESCRIPTION);
        iniIdeia = jsonObject.getBoolean(JSON_IDEA);
        iniTodo = jsonObject.getBoolean(JSON_TODO);
        iniImportante = jsonObject.getBoolean(JSON_IMPORTANT);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isIniIdeia() {
        return iniIdeia;
    }

    public void setIniIdeia(boolean iniIdeia) {
        this.iniIdeia = iniIdeia;
    }

    public boolean isIniTodo() {
        return iniTodo;
    }

    public void setIniTodo(boolean iniTodo) {
        this.iniTodo = iniTodo;
    }

    public boolean isIniImportante() {
        return iniImportante;
    }

    public void setIniImportante(boolean iniImportante) {
        this.iniImportante = iniImportante;
    }

    public JSONObject convertToJSON() throws JSONException{
        JSONObject jo = new JSONObject();

        jo.put(JSON_TITLE, titulo);
        jo.put(JSON_DESCRIPTION, descricao);
        jo.put(JSON_IDEA, iniIdeia);
        jo.put(JSON_TODO, iniTodo);
        jo.put(JSON_IMPORTANT, iniImportante);

        return jo;
    }
}
