package com.example.fbulou.library;

import android.content.Context;

import Models.Cat;
import Modules.AllAnimalsModule;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Library projects can also use Realms, but some configuration options are mandatory to avoid clashing with Realms used
 * in the app code.
 */

public class Zoo {

    private final RealmConfiguration realmConfig;
    private Realm realm;

    public Zoo(Context context) {
        realmConfig = new RealmConfiguration.Builder(context) // Beware this is the app context
                .name("library.zoo.realm")                    // So always use a unique name
                .modules(new AllAnimalsModule())           // Always use explicit modules in library projects
                .build();

        // Reset Realm
        Realm.deleteRealm(realmConfig);
    }

    public void open() {
        // Don't use Realm.setDefaultInstance() in library projects. It is unsafe as app developers can override the
        // default configuration. So always use explicit configurations in library projects.
        realm = Realm.getInstance(realmConfig);
    }

    public long getNoOfAnimals() {
        return realm.where(Cat.class).count();
    }

    public void addAnimals(final int count) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < count; i++) {
                    Cat cat = realm.createObject(Cat.class);
                    cat.setName("Cat " + i);
                }
            }
        });
    }

    public void close() {
        realm.close();
    }
}
