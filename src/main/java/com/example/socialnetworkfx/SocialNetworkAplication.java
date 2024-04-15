package com.example.socialnetworkfx;

import com.example.socialnetworkfx.domain.*;
import com.example.socialnetworkfx.domain.abstractDomain.Tuple;
import com.example.socialnetworkfx.guiAdmin.MainPage;
import com.example.socialnetworkfx.repository.DBRepository.*;
import com.example.socialnetworkfx.repository.abstractRepository.Repository;
import com.example.socialnetworkfx.service.FriendshipService;
import com.example.socialnetworkfx.service.GroupChatService;
import com.example.socialnetworkfx.service.NetworkService;
import com.example.socialnetworkfx.service.UserService;
import com.example.socialnetworkfx.validators.FriendshipValidator;
import com.example.socialnetworkfx.validators.UserValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SocialNetworkAplication extends Application {
    String url;
    String username;
    String password;

    Repository<Tuple<Long, Long>, Friendship> friendshipRepository;
    Repository<Long, User> userRepository;
    Repository<Long, GroupChat> groupChatRepository;
    Repository<Tuple<Long, Long>, GroupChatMember> groupChatMemberRepository;
    Repository<Long, Message> messageRepository;
    Repository<Tuple<Long, Long>, FriendshipRequest> friendshipRequestRepository;


    UserService userService;
    FriendshipService friendshipService;
    NetworkService networkService;
    GroupChatService groupChatService;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        initLogic();
        initView(primaryStage);
        primaryStage.setTitle("Social Network");
        primaryStage.show();

    }
    public void initView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SocialNetworkAplication.class.getResource("adminPages/mainPage.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        MainPage mainPageControler = fxmlLoader.getController();
        mainPageControler.setServicies(userService, friendshipService, networkService, groupChatService);
    }

    public void initLogic(){
//        this.url="jdbc:postgresql://192.168.1.137:5432/SocialNetwork";
        this.url="jdbc:postgresql://localhost:5432/SocialNetwork";
        this.username = "postgres";
        this.password = "cevasafie";

        this.friendshipRepository = new FriendshipDBRepository("friendships",url, username, password);
        this.userRepository = new UserDBRepository("users",url, username, password);
        this.groupChatRepository = new GroupChatDBRepository("\"groupChat\"",url, username, password);
        this.groupChatMemberRepository = new GroupChatMemberDBRepository("\"groupChatMember\"",url, username, password);
        this.messageRepository = new MessageDBRepository("\"message\"",url, username, password);
        this.friendshipRequestRepository = new FriendshipRequestDBRepository("\"friendshipRequest\"",url, username, password);

        this.userService = new UserService(userRepository, friendshipRepository, new UserValidator());
        this.friendshipService = new FriendshipService(friendshipRepository, userRepository,friendshipRequestRepository, new FriendshipValidator());
        this.networkService = new NetworkService(friendshipRepository);
        this.groupChatService = new GroupChatService(groupChatRepository, groupChatMemberRepository, messageRepository,userRepository);
    }
}
