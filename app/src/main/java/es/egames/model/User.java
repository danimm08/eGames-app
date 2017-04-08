package es.egames.model;

import java.util.Collection;

/**
 * Created by daniel on 4/02/17.
 */

public class User extends BaseEntity {

    private String name;
    private String surname;
    private Double reputation;
    private String profilePicture;
    private Address address;
    private Integer nExchanges;

    private Collection<User> followers;
    private Collection<User> followees;
    private UserAccount userAccount;

    public User() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Double getReputation() {
        return reputation;
    }

    public void setReputation(Double reputation) {
        this.reputation = reputation;
    }


    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getnExchanges() {
        return nExchanges;
    }

    public void setnExchanges(Integer nExchanges) {
        this.nExchanges = nExchanges;
    }

    public Collection<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Collection<User> followers) {
        this.followers = followers;
    }

    public Collection<User> getFollowees() {
        return followees;
    }

    public void setFollowees(Collection<User> followees) {
        this.followees = followees;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}
