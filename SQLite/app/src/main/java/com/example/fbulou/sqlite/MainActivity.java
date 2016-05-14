package com.example.fbulou.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase contactsDB = null;
    Button mCreateDB, mAddContact, mDeleteContact, mGetContacts, mDeleteDB;
    EditText mNameEdittext, mEmailEdittext, mIDEdittext;
    TextView mResultTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCreateDB = (Button) findViewById(R.id.mCreateDB);
        mAddContact = (Button) findViewById(R.id.mAddContact);
        mDeleteContact = (Button) findViewById(R.id.mDeleteContact);
        mGetContacts = (Button) findViewById(R.id.mGetContacts);
        mDeleteDB = (Button) findViewById(R.id.mDeleteDB);

        mNameEdittext = (EditText) findViewById(R.id.mNameEdittext);
        mEmailEdittext = (EditText) findViewById(R.id.mEmailEdittext);
        mIDEdittext = (EditText) findViewById(R.id.mIDEdittext);

        mResultTextview = (TextView) findViewById(R.id.mResultTextview);
    }

    public void createDB(View view) {
        try {
            contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);

            contactsDB.execSQL("CREATE TABLE IF NOT EXISTS contacts " +
                    "(id INTEGER PRIMARY KEY, name VARCHAR, email VARCHAR);");

            File database = getApplicationContext().getDatabasePath("MyContacts.db");

            if (!database.exists())
                Toast.makeText(this, "Database Created", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Database Missing", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Contacts Error", "Error Creating Database");
        }

        mAddContact.setEnabled(true);
        mDeleteContact.setEnabled(true);
        mGetContacts.setEnabled(true);
        mDeleteDB.setEnabled(true);
    }

    public void addContact(View view) {
        String contactName = mNameEdittext.getText().toString(),
                contactEmail = mEmailEdittext.getText().toString();

        if (contactName.length() != 0 && contactEmail.length() != 0) {

            contactsDB.execSQL("INSERT INTO contacts(name,email) VALUES ('" +
                    contactName + " ',' " + contactEmail + " ');");

            Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Please enter the details", Toast.LENGTH_SHORT).show();
    }

    public void getContacts(View view) {
        Cursor cursor = contactsDB.rawQuery("SELECT * FROM contacts", null);

        int idColumn = cursor.getColumnIndex("id"),
                nameColumn = cursor.getColumnIndex("name"),
                emailColumn = cursor.getColumnIndex("email");

        cursor.moveToFirst();

        String result = "";

        if (cursor!=null && (cursor.getCount() > 0)) {
            do {
                String id = cursor.getString(idColumn),
                        name = cursor.getString(nameColumn),
                        email = cursor.getString(emailColumn);

                result = result + id + " : " + name + " : " + email + " \n";

            } while (cursor.moveToNext());

            mResultTextview.setText(result);
	cursor.close();

        } else {
            Toast.makeText(this, "No Results to show", Toast.LENGTH_SHORT).show();
            mResultTextview.setText("");
        }
    }

    public void deleteContact(View view) {

        String id = mIDEdittext.getText().toString();

        if (id.length() != 0) {
            contactsDB.execSQL("DELETE FROM contacts WHERE id = " + id + " ;");
            Toast.makeText(this, "Contact Deleted", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Enter the ID", Toast.LENGTH_SHORT).show();
    }

    public void deleteDB(View view) {
        this.deleteDatabase("MyContacts");
	mResultTextview.setText("");

        mAddContact.setEnabled(false);
        mDeleteContact.setEnabled(false);
        mGetContacts.setEnabled(false);
        mDeleteDB.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        if (contactsDB != null)
            contactsDB.close();
        super.onDestroy();
    }
}
