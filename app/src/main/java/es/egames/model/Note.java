package es.egames.model;

import java.util.Date;

/**
 * Created by daniel on 4/02/17.
 */

public class Note extends BaseEntity {

    private String text;
    private Date date;

    private Exchange exchange;
    private User user;

    public Note() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
