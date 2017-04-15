package es.egames.forms;

import java.io.Serializable;
import java.util.List;

import es.egames.model.Note;
import es.egames.model.PersonalGame;
import es.egames.model.Type;

/**
 * Created by daniel on 9/03/17.
 */
public class ExchangeForm  implements Serializable {

    private List<PersonalGame> personalGamesUser1;
    private List<PersonalGame> personalGamesUser2;
    private Type type;
    private String wayExchange;
    private List<Note> notes;

    public ExchangeForm() {
        super();
    }

    public List<PersonalGame> getPersonalGamesUser1() {
        return personalGamesUser1;
    }

    public void setPersonalGamesUser1(List<PersonalGame> personalGamesUser1) {
        this.personalGamesUser1 = personalGamesUser1;
    }

    public List<PersonalGame> getPersonalGamesUser2() {
        return personalGamesUser2;
    }

    public void setPersonalGamesUser2(List<PersonalGame> personalGamesUser2) {
        this.personalGamesUser2 = personalGamesUser2;
    }

    public Type getType() {
        return type;
    }


    public void setType(Type type) {
        this.type = type;
    }

    public String getWayExchange() {
        return wayExchange;
    }

    public void setWayExchange(String wayExchange) {
        this.wayExchange = wayExchange;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
