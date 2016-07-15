package Modules;

import Models.Elephant;
import Models.Lion;
import Models.Zebra;
import io.realm.annotations.RealmModule;

@RealmModule(library = true, classes = {Elephant.class, Lion.class, Zebra.class})
public class ZooAnimalsModule {
}
