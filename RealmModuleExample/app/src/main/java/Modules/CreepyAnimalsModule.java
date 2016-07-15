package Modules;

import Models.Snake;
import Models.Spider;
import io.realm.annotations.RealmModule;

@RealmModule(classes = {Snake.class, Spider.class})
public class CreepyAnimalsModule {
}
