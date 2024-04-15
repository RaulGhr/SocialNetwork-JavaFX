package com.example.socialnetworkfx.domain;

import com.example.socialnetworkfx.domain.abstractDomain.Entity;

public class GroupChat extends Entity<Long> {
    private String name;

    public GroupChat(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return "GroupChat{" +
                "id=" + super.getId() +
                ", name='" + name + '\'' +
                '}';
    }
}
