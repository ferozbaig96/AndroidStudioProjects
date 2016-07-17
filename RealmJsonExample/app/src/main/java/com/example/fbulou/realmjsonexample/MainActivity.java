package com.example.fbulou.realmjsonexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.json.JSONException;
import org.json.JSONObject;

import Models.City;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.deleteRealm(realmConfiguration);  //Resetting realm
        Realm realm = Realm.getInstance(realmConfiguration);

        loadJsonFromString(realm);
        loadMultiJsonFromString(realm);
        loadJsonFromJsonObject(realm);

        realm.close();
    }

    private void loadJsonFromString(Realm realm) {
        final String myJSON = "{ name : \"Name 1\" , population : 500 }";

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObjectFromJson(City.class, myJSON);
            }
        });
    }

    private void loadMultiJsonFromString(Realm realm) {
        final String myJSON = "[ { name : \"Name 2\" , population : 700 } ," +
                " { name : \"Name 3\" , population : 900 } ]";

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createAllFromJson(City.class, myJSON);
            }
        });
    }

    private void loadJsonFromJsonObject(Realm realm) {
        final JSONObject json = new JSONObject();

        try {
            json.put("name", "Name 4");
            json.put("population", 150);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObjectFromJson(City.class, json);
            }
        });
    }
}