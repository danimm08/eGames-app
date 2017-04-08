package es.egames.model;

/**
 * Created by daniel on 4/02/17.
 */
public class Qualification extends BaseEntity {

    private String text;
    private Double mark;

    private Exchange exchange;
    private User user;

    public Qualification() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
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
