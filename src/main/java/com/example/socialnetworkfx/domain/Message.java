package com.example.socialnetworkfx.domain;

import com.example.socialnetworkfx.domain.abstractDomain.Entity;

import java.time.LocalDateTime;

public class Message extends Entity<Long> {
    private Long idGroup;
    private Long idUser;
    private String text;
    private Long replyTo = null;
    LocalDateTime date;

    public Message(Long idGroup, Long idUser, String text, Long ReplyTo){
        this.idGroup = idGroup;
        this.idUser = idUser;
        this.text = text;
        this.replyTo = ReplyTo;
        this.date = LocalDateTime.now();
    }

    public Long getIdGroup(){
        return idGroup;
    }

    public Long getIdUser(){
        return idUser;
    }

    public Long getIdGroupChat() {
        return idGroup;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public String getText(){
        return text;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "idGroup=" + idGroup +
                ", idUser=" + idUser +
                ", text=" + text +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message that = (Message) o;
        return this.getId().equals(that.getId());
    }



}
