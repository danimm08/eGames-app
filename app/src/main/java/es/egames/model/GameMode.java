package es.egames.model;

/**
 * Created by daniel on 4/02/17.
 */


public class GameMode extends BaseEntity {

    private String name;

    public GameMode() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
