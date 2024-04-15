package com.example.socialnetworkfx.domain.dto;

import com.example.socialnetworkfx.domain.Friendship;
import com.example.socialnetworkfx.domain.User;

public class FriendshipUserDTO {
    private Friendship friendship;
    private User user1;
    private User user2;
    public FriendshipUserDTO(Friendship friendship, User user1, User user2){
        this.friendship = friendship;
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getUser1Name(){
        return user1.getFirstName() + " " + user1.getLastName();
    }

    public String getUser2Name(){
        return user2.getFirstName() + " " + user2.getLastName();
    }
}
