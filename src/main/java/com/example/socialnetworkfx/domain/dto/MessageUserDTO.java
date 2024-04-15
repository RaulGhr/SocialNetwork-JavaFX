package com.example.socialnetworkfx.domain.dto;

import com.example.socialnetworkfx.domain.Message;
import com.example.socialnetworkfx.domain.User;

public class MessageUserDTO {
    private User user;
    private Message message;

    public MessageUserDTO(Message message, User user){
        this.user = user;
        this.message = message;
    }

    public String getUserName(){
        return user.getFirstName() + " " + user.getLastName();
    }

    public String getMessageText(){
        return message.getText();
    }

    public String getMessageDate(){
        return message.getDate().toString();
    }

    public Long getMessageId(){
        return message.getId();
    }

    public Long getMessageReplyId(){
        return message.getReplyTo();
    }
}
