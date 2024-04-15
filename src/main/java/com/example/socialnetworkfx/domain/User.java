package com.example.socialnetworkfx.domain;

import com.example.socialnetworkfx.domain.abstractDomain.Entity;

import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email=email;
        this.password=password;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email){
    	this.email=email;
    }

    public String getEmail(){
    	return email;
    }

    public void setPassword(String password){
    	this.password=password;
    }

    public String getPassword(){
    	return password;
    }


    @Override
    public String toString() {
        return "Utilizator{" +
                "ID='" + super.getId() + '\'' +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}