package es.egames.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

/**
 * Created by daniel on 4/02/17.
 */

public class PersonalGame extends BaseEntity {

    private String description;
    private Type type;
    private Integer numberOfViews;
    private Double distance;


    private Exchange exchange;

    private Game game;
    private User user;
    private Set<Image> images;

    public PersonalGame() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(Integer numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //Auxiliar relationship
    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }
}
