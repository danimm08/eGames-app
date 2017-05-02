package es.egames.forms;

import android.graphics.Bitmap;

import es.egames.model.Game;
import es.egames.model.PersonalGame;
import es.egames.model.User;

/**
 * Created by daniel on 18/04/17.
 */

public class SoughtItem {

    private Bitmap image;
    private String filedOne;
    private String filedTwo;
    private String filedThree;
    private String filedFour;
    private Object object;

    public SoughtItem() {
    }

    private SoughtItem(String filedOne, String filedTwo, String filedThree, String filedFour, Object object) {
        this.filedOne = filedOne;
        this.filedTwo = filedTwo;
        this.filedThree = filedThree;
        this.filedFour = filedFour;
        this.object = object;
    }

    public static SoughtItem createFromUser(User user) {
        SoughtItem soughtItem = new SoughtItem(user.getName(), user.getSurname(), user.getUserAccount().getUsername(), null, user);
        return soughtItem;
    }

    public static SoughtItem createFromPersonalGame(PersonalGame personalGame) {
        SoughtItem soughtItem = new SoughtItem(personalGame.getDescription(), null, personalGame.getUser().getUserAccount().getUsername(), personalGame.getType().toString(), personalGame);
        return soughtItem;
    }

    public static SoughtItem createFromGame(Game game) {
        SoughtItem soughtItem = new SoughtItem(game.getTitle(), null, game.getPlatform().getName(), null, game);
        return soughtItem;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getFiledOne() {
        return filedOne;
    }

    public void setFiledOne(String filedOne) {
        this.filedOne = filedOne;
    }

    public String getFiledTwo() {
        return filedTwo;
    }

    public void setFiledTwo(String filedTwo) {
        this.filedTwo = filedTwo;
    }

    public String getFiledThree() {
        return filedThree;
    }

    public void setFiledThree(String filedThree) {
        this.filedThree = filedThree;
    }

    public String getFiledFour() {
        return filedFour;
    }

    public void setFiledFour(String filedFour) {
        this.filedFour = filedFour;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoughtItem that = (SoughtItem) o;

        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        if (filedOne != null ? !filedOne.equals(that.filedOne) : that.filedOne != null)
            return false;
        if (filedTwo != null ? !filedTwo.equals(that.filedTwo) : that.filedTwo != null)
            return false;
        if (filedThree != null ? !filedThree.equals(that.filedThree) : that.filedThree != null)
            return false;
        if (filedFour != null ? !filedFour.equals(that.filedFour) : that.filedFour != null)
            return false;
        return object != null ? object.equals(that.object) : that.object == null;

    }

    @Override
    public int hashCode() {
        int result = image != null ? image.hashCode() : 0;
        result = 31 * result + (filedOne != null ? filedOne.hashCode() : 0);
        result = 31 * result + (filedTwo != null ? filedTwo.hashCode() : 0);
        result = 31 * result + (filedThree != null ? filedThree.hashCode() : 0);
        result = 31 * result + (filedFour != null ? filedFour.hashCode() : 0);
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }
}
