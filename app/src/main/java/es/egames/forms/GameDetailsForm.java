package es.egames.forms;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import es.egames.model.Game;
import es.egames.model.Genre;
import es.egames.model.PersonalGame;
import es.egames.model.Platform;

/**
 * Created by daniel on 1/03/17.
 */
public class GameDetailsForm {

    private int id;
    private int version;
    private String title;
    private String coverUrl;
    private Date firstReleaseDate;
    private String publisher;
    private String storyLine;
    private String summary;
    private Integer timeToBeat;
    private Platform platform;
    private Collection<PersonalGame> personalGames;
    private Set<Genre> genres;


    public GameDetailsForm() {
        super();
    }

    public GameDetailsForm(Game game, List<PersonalGame> personalGameList) {
        this.id = game.getId();
        this.version = game.getVersion();
        this.title = game.getTitle();
        this.coverUrl = game.getCoverUrl();
        this.firstReleaseDate = game.getFirstReleaseDate();
        this.publisher = game.getPublisher();
        this.storyLine = game.getStoryLine();
        this.summary = game.getSummary();
        this.timeToBeat = game.getTimeToBeat();
        this.platform = game.getPlatform();
        this.genres = game.getGenres();
        this.personalGames = personalGameList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Date getFirstReleaseDate() {
        return firstReleaseDate;
    }

    public void setFirstReleaseDate(Date firstReleaseDate) {
        this.firstReleaseDate = firstReleaseDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getStoryLine() {
        return storyLine;
    }

    public void setStoryLine(String storyLine) {
        this.storyLine = storyLine;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getTimeToBeat() {
        return timeToBeat;
    }

    public void setTimeToBeat(Integer timeToBeat) {
        this.timeToBeat = timeToBeat;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Collection<PersonalGame> getPersonalGames() {
        return personalGames;
    }

    public void setPersonalGames(Collection<PersonalGame> personalGames) {
        this.personalGames = personalGames;
    }

    public Collection<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameDetailsForm that = (GameDetailsForm) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
