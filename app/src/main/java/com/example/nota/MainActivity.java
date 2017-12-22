package com.example.nota;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteAdapter mNoteAdapter;
    private boolean apaSound;
    private int apaAnimation;
    private SharedPreferences sharedPreferences;
    private int manaEdit;
    Animation piscar, fadeIn;
    SoundPool soundPool;
    int sApagar = -1;
    int sEmpurrar = -1;
    int sImportante = -1;
    int sMudar = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            soundPool = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        try{
            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("delete.ogg");
            sApagar = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("select.ogg");
            sEmpurrar = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("important.ogg");
            sImportante = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("edit.ogg");
            sMudar = soundPool.load(descriptor, 0);
        } catch (IOException e){
            Toast.makeText(this, "Error euy ngapa yak", Toast.LENGTH_SHORT).show();
        }

        //Note adapter
        mNoteAdapter = new NoteAdapter();
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mNoteAdapter);
        listView.setEmptyView(findViewById(android.R.id.empty));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int whichItem, long id) {
                Nota sementaraCT = (Nota) mNoteAdapter.getItem(whichItem);
                DialogShowNote dialog = new DialogShowNote();
                dialog.sendNoteSelected(sementaraCT);
                dialog.show(getFragmentManager(), "");
                if (apaSound){soundPool.play(sEmpurrar, 1, 1, 0, 0, 1);}
            }
        });

        //I can't believe I figured these out on my own! lol I'm such a genius!
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int whichItem, long id) {
                Nota sementaraCT = (Nota) mNoteAdapter.getItem(whichItem);
                manaEdit = whichItem;
                DialogEditNote dialog = new DialogEditNote();
                dialog.sendNoteSelected(sementaraCT);
                dialog.show(getFragmentManager(), "");
                if (apaSound){soundPool.play(sMudar, 1, 1, 0, 0, 1);}
                return true;
            }
        });
    }

    public void floatAdd(View v){
        DialogNewNote dialogNewNote = new DialogNewNote();
        dialogNewNote.show(getFragmentManager(),"");
        if(apaSound){soundPool.play(sEmpurrar, 1, 1, 0, 0, 1);}
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNoteAdapter.simpanNote();
    }


    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferences = getSharedPreferences("Nota Si Bro", MODE_PRIVATE);
        apaSound = sharedPreferences.getBoolean("sound", true);
        apaAnimation = sharedPreferences.getInt("anim option", SettingsActivity.SLOW);
        piscar = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.kedip);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        if (apaAnimation == SettingsActivity.FAST){
            piscar.setDuration(100);
            fadeIn.setDuration(500);
        } else if (apaAnimation == SettingsActivity.SLOW){
            piscar.setDuration(1000);
            fadeIn.setDuration(1000);
        }
        mNoteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogNewNote dialogNewNote = new DialogNewNote();
        Intent pindahSetting = new Intent(this,SettingsActivity.class);

        switch(item.getItemId()){
            case R.id.action_add: dialogNewNote.show(getFragmentManager(),""); if(apaSound){soundPool.play(sEmpurrar, 1, 1, 0, 0, 1);} return true;
            case R.id.menuSetting: startActivity(pindahSetting); if(apaSound){soundPool.play(sEmpurrar, 1, 1, 0, 0, 1);} return true;

            default: return true;
        }
    }

    public void criarNovaNota(Nota nova){
        mNoteAdapter.tambahNote(nova);
    }

    public void mudarNota(Nota note){
        mNoteAdapter.ubahNote(manaEdit, note);
    }

    public void apagarNota(){
        mNoteAdapter.hapusNote(manaEdit);
    }

    public class NoteAdapter extends BaseAdapter{
        List<Nota> noteList = new ArrayList<Nota>();
        Nota tempNote = new Nota();
        LayoutInflater inflater;
        private JSONSerializer mSerializer;

        public NoteAdapter() {
            mSerializer = new JSONSerializer("Anotacao.json", MainActivity.this.getApplicationContext());
            try{
                noteList = mSerializer.load();
            }catch (Exception e){
                noteList = new ArrayList<Nota>();
                e.printStackTrace();
                Log.e("Error !", "", e);
            }
        }

        @Override
        public int getCount() {
            return noteList.size();
        }

        @Override
        public Object getItem(int whichItem) {
            return noteList.get(whichItem);
        }

        @Override
        public long getItemId(int whichItem) {
            return whichItem;
        }

        @Override
        public View getView(int whichItem, View view, ViewGroup viewGroup){
            if(view==null) {
                inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listitem, viewGroup, false);
            }
            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView txtDesc = (TextView) view.findViewById(R.id.txtDesc);
            ImageView imgTodo = (ImageView) view.findViewById(R.id.imageViewTodo);
            ImageView imgPenting = (ImageView) view.findViewById(R.id.imageViewImportant);
            ImageView imgIde = (ImageView) view.findViewById(R.id.imageViewIdea);
            tempNote = noteList.get(whichItem);
            CharSequence judul = tempNote.getTitulo();
            CharSequence desc = tempNote.getDescricao();
            txtDesc.setText(desc);
            txtTitle.setText(judul);
            if (!tempNote.isIniImportante()) {imgPenting.setVisibility(View.GONE);}
            if (!tempNote.isIniTodo()) {imgTodo.setVisibility(View.GONE);}
            if (!tempNote.isIniIdeia()) {imgIde.setVisibility(View.GONE);}

            if (tempNote.isIniImportante()) {
                imgPenting.setVisibility(View.VISIBLE);
                view.setAnimation(piscar);
            } else {
                view.setAnimation(fadeIn);
            }
            if (tempNote.isIniTodo()) {imgTodo.setVisibility(View.VISIBLE);}
            if (tempNote.isIniIdeia()) {imgIde.setVisibility(View.VISIBLE);}
            if (apaAnimation == SettingsActivity.NONE){view.clearAnimation();}
            return view;
        }

        public void tambahNote(Nota baru){
            if (baru.isIniImportante()) { //penting
                noteList.add(0, baru);
                if (apaSound) {soundPool.play(sImportante, 1, 1, 0, 0, 1);}
            } else { //biasa
                noteList.add(baru);
                if (apaSound) {soundPool.play(sEmpurrar, 1, 1, 0, 0, 1);}
            }
            notifyDataSetChanged();
        }

        public void simpanNote(){
            try{
                mSerializer.save(noteList);
            } catch (Exception e){
                e.printStackTrace();
                Log.e("Error gansss!", "", e);
            }
        }

        //watchout these are dope codes!
        public void hapusNote(int whichItem){
            noteList.remove(whichItem);
            notifyDataSetChanged();
            if (apaSound){soundPool.play(sApagar, 1, 1, 0, 0, 1);}
        }

        public void ubahNote(int whichItem, Nota baru){
            //simpan catatan
            if (baru.isIniImportante() && (noteList.indexOf(baru) != 0)) {
                noteList.add(0, baru);
                noteList.remove(whichItem + 1);
            } else {
                noteList.set(whichItem, baru);
            }
            //suara
            if (baru.isIniImportante()) {
                if (apaSound) {soundPool.play(sImportante, 1, 1, 0, 0, 1);}
            } else {
                if (apaSound) {soundPool.play(sEmpurrar, 1, 1, 0, 0, 1);}
            }
            notifyDataSetChanged();
        }


    }
}
