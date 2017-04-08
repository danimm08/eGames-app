package es.egames.model;

/**
 * Created by daniel on 4/02/17.
 */

public class Keyword extends BaseEntity {

    private String name;

    public Keyword() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
