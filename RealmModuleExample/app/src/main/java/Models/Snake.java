package Models;

import io.realm.RealmObject;

public class Snake extends RealmObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}