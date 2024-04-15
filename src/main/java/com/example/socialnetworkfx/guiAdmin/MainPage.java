package com.example.socialnetworkfx.guiAdmin;

import com.example.socialnetworkfx.SocialNetworkAplication;
import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.guiAdmin.alerts.ActionAlert;
import com.example.socialnetworkfx.observer.ObsEvents;
import com.example.socialnetworkfx.observer.Observer;
import com.example.socialnetworkfx.repository.paging.PageableDBRepository;
import com.example.socialnetworkfx.service.FriendshipService;
import com.example.socialnetworkfx.service.GroupChatService;
import com.example.socialnetworkfx.service.NetworkService;
import com.example.socialnetworkfx.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

public class MainPage implements Observer {
    public Button buttonPrevPage;
    public Button buttonNextPage;
    public ComboBox comboBoxElems;
    UserService userService;
    FriendshipService friendshipService;
    NetworkService networkService;
    GroupChatService groupChatService;
    PageableDBRepository pageableDBRepositoryUser;

    @FXML
    public TableView<User> tableUsers;
    @FXML
    public TableColumn<User, Long> columnId;
    @FXML
    public TableColumn<User, String> columnFirstName;
    @FXML
    private TableColumn<User, String> columnSecondName;
    ObservableList<User> usersObsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        columnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        columnSecondName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableUsers.setItems(usersObsList);
    }
    public void initializeTableUsers() {
//        Iterable<User> allMovies = userService.getAll();
//        List<User> allMoviesList = StreamSupport.stream(allMovies.spliterator(), false).toList();
        List<User> allMoviesList = pageableDBRepositoryUser.findAll();
        usersObsList.setAll(allMoviesList);
    }
    public void setServicies(UserService userService, FriendshipService friendshipService, NetworkService networkService, GroupChatService groupChatService){
        this.userService=userService;
        userService.addObserver(this);
        this.friendshipService=friendshipService;
        this.networkService=networkService;
        this.groupChatService = groupChatService;
        this.pageableDBRepositoryUser = new PageableDBRepository<>(userService.getUserRepo(), buttonPrevPage, buttonNextPage, comboBoxElems, ObsEvents.UserEvent);
        this.pageableDBRepositoryUser.addObserver(this);
        this.initializeTableUsers();
    }

    @Override
    public void update(ObsEvents message) {
        if(message == ObsEvents.UserEvent){
            usersObsList.clear();
            usersObsList.setAll(pageableDBRepositoryUser.findAll());
        }
    }
    public void handleDeleteMovie(ActionEvent actionEvent) {
        User toBeDeleted = (User) tableUsers.getSelectionModel().getSelectedItem();
        if(toBeDeleted == null) {
            // means no movie selected for deletion
            ActionAlert.showErrorMessage(null, "Select a user before hitting delete");
        }
        else{

            userService.delete(toBeDeleted.getId());
//            ActionAlert.showMessage(null, Alert.AlertType.INFORMATION, "Success", "The selected user has been deleted");
            usersObsList.remove(toBeDeleted);

        }
    }
    public void handleAddUser(){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(SocialNetworkAplication.class.getResource("adminPages/addUserPage.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            dialogStage.setScene(scene);

            AddUserPage addUserPageController = fxmlLoader.getController();
            addUserPageController.setUp(userService, dialogStage, usersObsList);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleUpdateUser(){

        User toBeDeleted = (User) tableUsers.getSelectionModel().getSelectedItem();
        if(toBeDeleted == null) {
            // means no movie selected for deletion
            ActionAlert.showErrorMessage(null, "Select a user before hitting delete");
            return;
        }
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(SocialNetworkAplication.class.getResource("adminPages/updateUserPage.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            dialogStage.setScene(scene);

            UpdateUserPage updateUserPageController = fxmlLoader.getController();
            updateUserPageController.setUp(userService, dialogStage, usersObsList, toBeDeleted);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleGroupChat(){

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(SocialNetworkAplication.class.getResource("adminPages/groupChat.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Group Chat");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            dialogStage.setScene(scene);

            GroupChatPage groupChatPageController = fxmlLoader.getController();
            groupChatPageController.setUp(groupChatService, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleFriendship(){

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(SocialNetworkAplication.class.getResource("adminPages/friendshipPage.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Friendship");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            dialogStage.setScene(scene);

            FriendshipPage friendshipPageController = fxmlLoader.getController();
            friendshipPageController.setUp(friendshipService, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleGroupChatOfUser(){

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(SocialNetworkAplication.class.getResource("adminPages/groupChatUserView.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            User userChat = (User) tableUsers.getSelectionModel().getSelectedItem();
            if(userChat == null) {
                ActionAlert.showErrorMessage(null, "Select a user before open a chat");
                return;
            }

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("chat user: "+ userChat.getFirstName()+" "+userChat.getLastName());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(scene);

            GroupChatUserView groupChatUserViewController = fxmlLoader.getController();
            groupChatUserViewController.setUp(userChat, groupChatService, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
