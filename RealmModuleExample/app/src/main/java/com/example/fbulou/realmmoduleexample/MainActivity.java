package com.example.fbulou.realmmoduleexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fbulou.library.Zoo;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import Models.Cat;
import Models.Cow;
import Models.Dog;
import Models.Elephant;
import Models.Lion;
import Models.Pig;
import Models.Snake;
import Models.Spider;
import Models.Zebra;
import Modules.CreepyAnimalsModule;
import Modules.DomesticAnimalsModule;
import Modules.ZooAnimalsModule;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmException;


/*
    Realm sample - moduleExample

    DOCs for what is RealmModule :
    https://realm.io/docs/java/latest/api/io/realm/annotations/RealmModule.html
*/

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private LinearLayout rootLayout = null;

    private void showStatus(String txt) {
        Log.e(TAG, txt);
        TextView tv = new TextView(this);
        tv.setText(txt);
        rootLayout.addView(tv);
    }

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

        rootLayout = ((LinearLayout) findViewById(R.id.container));
        rootLayout.removeAllViews();

        // The default Realm instance implicitly knows about all classes in the realmModuleAppExample Android Studio
        // module. This does not include the classes from the realmModuleLibraryExample AS module so a Realm using this
        // configuration would know about the following classes: { Cow, Pig, Snake, Spider }
        RealmConfiguration defaultConfig = new RealmConfiguration.Builder(this).build();

        // It is possible to extend the default schema by adding additional Realm modules using modules(). This can
        // also be Realm modules from libraries. The below Realm contains the following classes: { Cow, Pig, Snake,
        // Spider, Cat, Dog }
        RealmConfiguration farmAnimalsConfig = new RealmConfiguration.Builder(this)
                .name("farm.realm")
                .modules(Realm.getDefaultModule(), new DomesticAnimalsModule())
                .build();

        // Or you can completely replace the default schema.
        // This Realm contains the following classes: { Elephant, Lion, Zebra, Snake, Spider }
        RealmConfiguration exoticAnimalsConfig = new RealmConfiguration.Builder(this)
                .name("exotic.realm")
                .modules(new ZooAnimalsModule(), new CreepyAnimalsModule())
                .build();

        // Creating my own Realm
        // This Realm contains the following classes: { Snake, Spider }
        RealmConfiguration myConfig = new RealmConfiguration.Builder(this)
                .name("myRealm.realm")
                .modules(null, new CreepyAnimalsModule())
                .build();

        // Multiple Realms can be open at the same time
        showStatus("Opening multiple Realms");
        Realm defaultRealm = Realm.getInstance(defaultConfig);
        Realm farmRealm = Realm.getInstance(farmAnimalsConfig);
        Realm exoticRealm = Realm.getInstance(exoticAnimalsConfig);
        Realm myRealm = Realm.getInstance(myConfig);

        // Objects can be added to each Realm independantly
        showStatus("\nCreate objects in the default Realm");
        defaultRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObject(Cow.class);
                realm.createObject(Pig.class);
                realm.createObject(Snake.class);
                realm.createObject(Spider.class);
            }
        });

        showStatus("Create objects in the farm Realm");
        farmRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObject(Cow.class);
                realm.createObject(Pig.class);
                realm.createObject(Cat.class);
                realm.createObject(Dog.class);
            }
        });

        showStatus("Create objects in the exotic Realm");
        exoticRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObject(Elephant.class);
                realm.createObject(Lion.class);
                realm.createObject(Zebra.class);
                realm.createObject(Snake.class);
                realm.createObject(Spider.class);
            }
        });

        showStatus("Create objects in the myRealm");
        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObject(Snake.class);
                realm.createObject(Spider.class);
            }
        });

        // You can copy objects between Realms
        showStatus("\nCopy objects between Realms");
        showStatus("Number of pigs on the farm : " + farmRealm.where(Pig.class).count());
        showStatus("Copy pig from defaultRealm to farmRealm");
        final Pig defaultPig = defaultRealm.where(Pig.class).findFirst();
        farmRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(defaultPig);
            }
        });

        showStatus("Number of pigs on the farm : " + farmRealm.where(Pig.class).count());

        // Each Realm is restricted to only accept the classes in their schema.
        showStatus("\nTrying to add an unsupported class");
        defaultRealm.beginTransaction();
        try {
            defaultRealm.createObject(Elephant.class);
        } catch (RealmException expected) {
            showStatus("This throws a :" + expected.toString());
        } finally {
            defaultRealm.cancelTransaction();
        }

        // And Realms in library projects are independent from Realms in the app code
        showStatus("\nInteracting with library code that uses Realm internally");
        int animals = 5;
        Zoo libraryZoo = new Zoo(this);
        libraryZoo.open();
        showStatus("Adding animals: " + animals);
        libraryZoo.addAnimals(5);
        showStatus("Number of animals in the library Realm:" + libraryZoo.getNoOfAnimals());
        libraryZoo.close();


        // Remember to close all open Realms
        defaultRealm.close();
        farmRealm.close();
        exoticRealm.close();
        myRealm.close();

    }


}
