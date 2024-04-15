package com.example.socialnetworkfx.domain.dto;

import com.example.socialnetworkfx.domain.FriendshipRequest;
import com.example.socialnetworkfx.domain.User;

public class FriendshipRequestUserDTO {
    private User sender;
    private User receiver;
    private FriendshipRequest friendshipRequest;

    public FriendshipRequestUserDTO(FriendshipRequest friendshipRequest, User sender, User receiver){
        this.sender = sender;
        this.receiver = receiver;
        this.friendshipRequest = friendshipRequest;
    }

    public String getSenderName(){
        return sender.getFirstName() + " " + sender.getLastName();
    }

    public String getReceiverName(){
        return receiver.getFirstName() + " " + receiver.getLastName();
    }

    public String getStatus(){
        if (friendshipRequest.getStatus() == 0)
            return "Pending";
        else if (friendshipRequest.getStatus() == 1)
            return "Approved";
        else
            return "Rejected";
    }

    public FriendshipRequest getFriendshipRequest(){
        return friendshipRequest;
    }

    public User getSender(){
        return sender;
    }

    public User getReceiver(){
        return receiver;
    }
}
