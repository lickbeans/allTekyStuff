package com.example.aaroncampbell.elevennote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesActivity extends AppCompatActivity {

    private ListView notesList;
//    String[] notes = new String[]{"Note1", "Note2", "Note3"};
    private NotesArrayAdapter notesArrayAdapter;
    private ArrayList<Note> notesArray;
    private SharedPreferences notesPrefs;
    private List<Locale.Category> categoryList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        notesPrefs = getPreferences(Context.MODE_PRIVATE);
        setupNotes();

        Collections.sort(notesArray);
        //Hook up our UI elements to our Java code
        //This was our simple adapter using a simple string array as data and a simple resource
        notesList = (ListView)findViewById(R.id.listView);

        // way more complex adapter
        notesArrayAdapter = new NotesArrayAdapter(this, R.layout.notes_list_item, notesArray);
        notesList.setAdapter(notesArrayAdapter);

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = notesArray.get(position);

                Intent intent = new Intent(NotesActivity.this, NoteDetailActivity.class);
                intent.putExtra("Title", note.getTitle());
                intent.putExtra("Text", note.getText());
                intent.putExtra("Index", position);

                startActivityForResult(intent, 1);

            }
        });
        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NotesActivity.this);
                alertBuilder.setTitle("Delete");
                alertBuilder.setMessage("You sure?");
                alertBuilder.setNegativeButton("Cancel", null);
                alertBuilder.setPositiveButton("Delete",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Note note = notesArray.get(position);
                        deleteFile("##" + note.getTitle());
                        notesArray.remove(position);
                        notesArrayAdapter.updateAdapter(notesArray);
                    }

                });
                alertBuilder.create().show();
                return true;
            }
        });

        //create adapter and wire it to the listView
//        notesList.setAdapter(new ArrayAdapter<>(this, R.layout.notes_textview_list_item, notes));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int index = data.getIntExtra("Index", -1);
            Note note = new Note(data.getStringExtra("Title"),
                    data.getStringExtra("Text"), new Date());

            if (index == -1) {
                notesArray.add(note);
            }
            else {
                notesArray.set(index, note);
            }
            Collections.sort(notesArray);
            notesArrayAdapter.updateAdapter(notesArray);
        }
    }

    private void setupNotes() {
        notesArray = new ArrayList<>();
        if (notesPrefs.getBoolean("firstRun", true)) {
            SharedPreferences.Editor editor = notesPrefs.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

            Note note1 = new Note("Note1", "This is a note", new Date());
            notesArray.add(note1);
            notesArray.add(new Note("Note2", "This is another note", new Date()));
            notesArray.add(new Note("Note3", "This is another note", new Date()));

            for (Note note : notesArray) {
                writeFile(note);
            }
        }
        else {
            readFile();
        }
    }

    private void readFile() {
        File[] filesDir = this.getFilesDir().listFiles();
        for (File file : filesDir) {
            FileInputStream inputStream = null;
            String title = file.getName();
            if (!title.startsWith("##")) {
                continue;
            }
            else {
                title = title.substring(2,title.length());
            }
            Date date = new Date (file.lastModified());
            String text = "";
            try {
                inputStream = openFileInput(title);
                byte[] input = new byte[inputStream.available()];
                while (inputStream.read(input) != -1) {}
                text += new String(input);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    inputStream.close();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
                catch (NullPointerException ie) {
                    ie.printStackTrace();
                }
            }
            notesArray.add(new Note(title, text, date));
        }
    }
    private void writeFile(Note note) {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput("##" + note.getTitle(), Context.MODE_PRIVATE);
            outputStream.write(note.getText().getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException ioe) {
            } catch (NullPointerException npe) {
            } catch (Exception e) {
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(NotesActivity.this, NoteDetailActivity.class);
            intent.putExtra("Title", "");
            intent.putExtra("Text", "");
                    startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
