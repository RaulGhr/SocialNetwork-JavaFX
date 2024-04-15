package com.example.socialnetworkfx.domain;

import com.example.socialnetworkfx.domain.abstractDomain.Entity;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;

import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<Long,Long>> {

    Long id1;
    Long id2;
    LocalDateTime date;


    public Friendship(Long id1, Long id2){
        this.setId(new Tuple<>(id1,id2));
        this.id1 = id1;
        this.id2 = id2;
        this.date = LocalDateTime.now();

    }

    public Tuple<Long,Long> getId(){
        return super.getId();
    }

    public boolean isPartOf(Long id){
        return id == this.getId().getLeft() || id == this.getId().getRight();
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public Long getId1(){
        return id1;
    }

    public Long getId2(){
        return id2;
    }


    @Override
    public String toString() {
        return "Friendship{" +
                "id1=" + this.getId().getLeft() +
                ", id2=" + this.getId().getRight() +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship)) return false;
        Friendship that = (Friendship) o;
        return this.getId().equals(that.getId());
    }
}
