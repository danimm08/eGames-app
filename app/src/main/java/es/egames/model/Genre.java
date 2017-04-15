package es.egames.model;

/**
 * Created by daniel on 4/02/17.
 */

public class Genre extends BaseEntity {

    private String name;

    public Genre() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
