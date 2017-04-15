package es.egames.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by daniel on 4/02/17.
 */
public class Image extends BaseEntity {

    private String pathUrl;

    private PersonalGame personalGame;

    public Image() {
        super();
    }

    public Image(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

//    @JsonIgnore
//    public PersonalGame getPersonalGame() {
//        return personalGame;
//    }
//
//    public void setPersonalGame(PersonalGame personalGame) {
//        this.personalGame = personalGame;
//    }
}
