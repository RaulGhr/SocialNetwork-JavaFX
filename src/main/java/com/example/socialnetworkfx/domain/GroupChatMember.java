package com.example.socialnetworkfx.domain;

import com.example.socialnetworkfx.domain.abstractDomain.Entity;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;

public class GroupChatMember extends Entity<Tuple<Long,Long>> {
    private Long idGroup;
    private Long idUser;

    public GroupChatMember(Long idUser, Long idGroup){
        this.setId(new Tuple<>(idUser,idGroup));
        this.idGroup = idGroup;
        this.idUser = idUser;
    }

    public Long getIdGroup(){
        return idGroup;
    }

    public Long getIdUser(){
        return idUser;
    }

    @Override
    public String toString() {
        return "GroupMember{" +
                "idGroup=" + idGroup +
                ", idUser=" + idUser +
                '}';
    }
}
