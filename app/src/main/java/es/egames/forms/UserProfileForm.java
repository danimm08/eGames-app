package es.egames.forms;

import es.egames.model.Address;

/**
 * Created by daniel on 30/03/17.
 */
public class UserProfileForm {

    private String name;
    private String surname;
    private Address address;

    public UserProfileForm() {
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
