package com.example.socialnetworkfx.guiUser;

import com.example.socialnetworkfx.SocialNetworkAplication;
import com.example.socialnetworkfx.domain.Session;
import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.guiAdmin.AddUserPage;
import com.example.socialnetworkfx.guiAdmin.GroupChatUserView;
import com.example.socialnetworkfx.guiAdmin.MainPage;
import com.example.socialnetworkfx.guiAdmin.alerts.ActionAlert;
import com.example.socialnetworkfx.service.FriendshipService;
import com.example.socialnetworkfx.service.GroupChatService;
import com.example.socialnetworkfx.service.NetworkService;
import com.example.socialnetworkfx.service.UserService;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInPage {

    public StackPane stackPaneMain;
    public TextField tableRegisterFieldFirstName;
    public TextField tableRegisterFieldLastName;
    public TextField tableRegisterFieldEmail;
    public TextField tableRegisterFieldPassword;
    public TextField tableLogInFieldEmail;
    public PasswordField tableLogInFieldPassword;
    public VBox hidePannel;
    UserService userService;
    FriendshipService friendshipService;
    NetworkService networkService;

    GroupChatService groupChatService;

    Stage stage;

    User user;



    public void setUp(UserService userService, FriendshipService friendshipService, NetworkService networkService, GroupChatService groupChatService,Stage stage){
        this.userService=userService;
        this.friendshipService=friendshipService;
        this.networkService=networkService;
        this.groupChatService = groupChatService;
        this.stage=stage;
    }

    public void switchWindowParts() {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(javafx.util.Duration.seconds(0.5));
        translateTransition.setNode(hidePannel);
        if(hidePannel.getTranslateX() == 0){
            translateTransition.setToX(hidePannel.getWidth());
        }
        else{
            translateTransition.setToX(0);
        }
        translateTransition.play();

    }

    public void registerHandler(){
        String firstName=tableRegisterFieldFirstName.getText();
        String lastName=tableRegisterFieldLastName.getText();
        String email=tableRegisterFieldEmail.getText();
        String password=tableRegisterFieldPassword.getText();
        if(firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("")){
            ActionAlert.showErrorMessage(this.stage,"You must complete all fields!");
        }
        else{
            this.user = userService.create(firstName,lastName,email,password);
            tableRegisterFieldFirstName.setText("");
            tableRegisterFieldLastName.setText("");
            tableRegisterFieldEmail.setText("");
            tableRegisterFieldPassword.setText("");
//            ActionAlert.showErrorMessage(this.stage,"Account created!");
        }
    }

    public void logInHandler(){
        String email=tableLogInFieldEmail.getText();
        String password=tableLogInFieldPassword.getText();
        if(email.equals("") || password.equals("")){
            ActionAlert.showErrorMessage(this.stage,"You must complete all fields!");
        }
        else if(email.equals("admin") && password.equals("admin")) {
            tableLogInFieldEmail.setText("");
            tableLogInFieldPassword.setText("");
            startAdminSession();
        }
        else{
            this.user = userService.logIn(email,password);
            if(this.user!=null){
//                ActionAlert.showErrorMessage(this.stage,"Log in successful!");
                tableLogInFieldEmail.setText("");
                tableLogInFieldPassword.setText("");
                startSession();
            }
            else{
                ActionAlert.showErrorMessage(this.stage,"Wrong email or password!");
            }
        }
    }

    public void startSession(){
        Session session = new Session(user,userService,friendshipService,networkService,groupChatService);
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(SocialNetworkAplication.class.getResource("adminPages/groupChatUserView.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);


            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("chat user: "+ user.getFirstName()+" "+user.getLastName());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(scene);

            GroupChatUserView groupChatUserViewController = fxmlLoader.getController();
            groupChatUserViewController.setUp(user, groupChatService, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startAdminSession(){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(SocialNetworkAplication.class.getResource("adminPages/mainPage.fxml"));
            Stage newStage = new Stage();
            Scene newScene = new Scene(fxmlLoader.load());
            newStage.setScene(newScene);

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("ADMIN PAGE");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(newScene);

            MainPage mainPageController = fxmlLoader.getController();
            mainPageController.setServicies(userService, friendshipService, networkService, groupChatService);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
