package es.egames.forms;

import es.egames.model.Address;

/**
 * Created by daniel on 11/02/17.
 */
public class RegistrationForm {

    private String name;
    private String surname;
    private Address address;
    private String username;
    private String password;
    private String email;

    public RegistrationForm() {
        super();
    }

    public RegistrationForm(String name, String surname, Address address, String username, String password, String email) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.username = username;
        this.password = password;
        this.email = email;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
