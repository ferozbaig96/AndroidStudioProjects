package Modules;

import Models.Cat;
import Models.Dog;
import io.realm.annotations.RealmModule;

@RealmModule(library = true, classes = {Cat.class, Dog.class})
public class DomesticAnimalsModule {
}
