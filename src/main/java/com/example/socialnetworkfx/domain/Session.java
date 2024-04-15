package com.example.socialnetworkfx.domain;

import com.example.socialnetworkfx.service.FriendshipService;
import com.example.socialnetworkfx.service.GroupChatService;
import com.example.socialnetworkfx.service.NetworkService;
import com.example.socialnetworkfx.service.UserService;

public class Session {
    private User user;
    UserService userService;
    FriendshipService friendshipService;
    NetworkService networkService;
    GroupChatService groupChatService;

    public Session(User user, UserService userService, FriendshipService friendshipService, NetworkService networkService, GroupChatService groupChatService) {
        this.user = user;
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.networkService = networkService;
        this.groupChatService = groupChatService;
    }

    public User getUser() {
        return user;
    }
}
