package com.example.socialnetworkfx.guiAdmin;

import com.example.socialnetworkfx.domain.User;
import com.example.socialnetworkfx.guiAdmin.alerts.ActionAlert;
import com.example.socialnetworkfx.service.UserService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserPage {
    public TextField emailField;
    public TextField passwordField;
    UserService userService;
    Stage stage;
    ObservableList<User> usersObsList;

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField lastNameField;


    public void setUp(UserService userService, Stage stage,ObservableList<User> usersObsList){
        this.userService=userService;
        this.stage=stage;
        this.usersObsList=usersObsList;
    }



    public void addUser(){
        String firstName=firstNameField.getText();
        String lastName=lastNameField.getText();
        String email=emailField.getText();
        String password=passwordField.getText();
        if(firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("")){
            ActionAlert.showErrorMessage(stage,"You must complete all fields!");
        }
        else{
            userService.create(firstName,lastName,email,password);
            stage.close();
        }
    }



}
