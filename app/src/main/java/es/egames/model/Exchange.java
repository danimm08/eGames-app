package es.egames.model;

import java.util.Date;

/**
 * Created by daniel on 4/02/17.
 */
public class Exchange extends BaseEntity {

    private Date creationDate;
    private Date lastUpdateDate;
    private Boolean status;
    private Date eventDate;
    private Integer numberOfAttemps;
    private Type type;
    private String wayExchange;

    private User user;
    private User lastModifier;

    public Exchange() {
        super();
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
}
