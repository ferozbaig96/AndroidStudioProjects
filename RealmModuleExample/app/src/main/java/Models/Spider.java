package Models;

import io.realm.RealmObject;

public class Spider extends RealmObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}