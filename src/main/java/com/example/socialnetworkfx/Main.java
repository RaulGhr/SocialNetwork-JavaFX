package com.example.socialnetworkfx;


import com.example.socialnetworkfx.domain.Friendship;
import com.example.socialnetworkfx.domain.FriendshipRequest;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;
import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.repository.DBRepository.FriendshipDBRepository;
import com.example.socialnetworkfx.repository.DBRepository.FriendshipRequestDBRepository;
import com.example.socialnetworkfx.repository.abstractRepository.Repository;
import com.example.socialnetworkfx.repository.DBRepository.UserDBRepository;
import com.example.socialnetworkfx.service.FriendshipService;
import com.example.socialnetworkfx.service.NetworkService;
import com.example.socialnetworkfx.service.UserService;
import com.example.socialnetworkfx.ui.UI;
import com.example.socialnetworkfx.validators.FriendshipValidator;
import com.example.socialnetworkfx.validators.UserValidator;

public class Main {
    public static void main(String[] args) {
//        String url="jdbc:postgresql://localhost:5432/SocialNetwork";
        String url="jdbc:postgresql://192.168.1.137:5432/SocialNetwork";
        String username = "postgres";
        String password = "cevasafie";

//        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipFileRepository("java/org.example.data/friendships.txt");
//        Repository<Long, User> userRepository = new UserFileRepository("java/users.txt");


        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipDBRepository("friendships",url, username, password);
        Repository<Tuple<Long, Long>, FriendshipRequest> friendshipRequestRepository = new FriendshipRequestDBRepository("friendshipRequest",url, username, password);
        Repository<Long, User> userRepository = new UserDBRepository("users",url, username, password);

        UserService userService = new UserService(userRepository, friendshipRepository, new UserValidator());
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository,friendshipRequestRepository, new FriendshipValidator());
        NetworkService networkService = new NetworkService(friendshipRepository);


        UI ui = new UI(userService, friendshipService, networkService);
        ui.run();
    }
}