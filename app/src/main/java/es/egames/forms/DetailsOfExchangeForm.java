package es.egames.forms;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import es.egames.model.Exchange;
import es.egames.model.Note;
import es.egames.model.PersonalGame;
import es.egames.model.Type;
import es.egames.model.User;


/**
 * Created by daniel on 31/03/17.
 */
public class DetailsOfExchangeForm implements Serializable {
    private Integer id;
    private Date creationDate;
    private Date lastUpdateDate;
    private Boolean status;
    private Date eventDate;
    private Integer numberOfAttemps;
    private Type type;
    private String wayExchange;

    private User user;
    private User lastModifier;
    private Set<PersonalGame> personalGameUser1;
    private Set<PersonalGame> personalGameUser2;
    private List<Note> notes;

    public DetailsOfExchangeForm() {
    }

    public DetailsOfExchangeForm(Exchange exchange, Set<PersonalGame> personalGameUser1, Set<PersonalGame> personalGameUser2, List<Note> notes) {
        this.id = exchange.getId();
        this.creationDate = exchange.getCreationDate();
        this.lastUpdateDate = exchange.getLastUpdateDate();
        this.status = exchange.getStatus();
        this.eventDate = exchange.getEventDate();
        this.numberOfAttemps = exchange.getNumberOfAttemps();
        this.type = exchange.getType();
        this.wayExchange = exchange.getWayExchange();
        this.user = exchange.getUser();
        this.lastModifier = exchange.getLastModifier();
        this.personalGameUser1 = personalGameUser1;
        this.personalGameUser2 = personalGameUser2;
        this.notes = notes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getNumberOfAttemps() {
        return numberOfAttemps;
    }

    public void setNumberOfAttemps(Integer numberOfAttemps) {
        this.numberOfAttemps = numberOfAttemps;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(User lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Set<PersonalGame> getPersonalGameUser1() {
        return personalGameUser1;
    }

    public void setPersonalGameUser1(Set<PersonalGame> personalGameUser1) {
        this.personalGameUser1 = personalGameUser1;
    }

    public Set<PersonalGame> getPersonalGameUser2() {
        return personalGameUser2;
    }

    public void setPersonalGameUser2(Set<PersonalGame> personalGameUser2) {
        this.personalGameUser2 = personalGameUser2;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
